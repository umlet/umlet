package com.baselet.control;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.jar.Manifest;

public class Path {

	private static String tempDir;
	private static String homeProgramDir;

	public static String config() {
		return userHome() + File.separator + Program.CONFIG_NAME;
	}

	private static String userHome() {
		String homeDir = userHomeBase();
		if (!homeDir.endsWith(File.separator)) {
			homeDir += File.separator;
		}
		File homeDirFile = new File(homeDir + Program.NAME);
		if (!homeDirFile.exists()) {
			homeDirFile.mkdir();
		}
		return homeDirFile.getAbsolutePath();
	}

	private static String userHomeBase() {
		try {
			String xdgConfigHome = System.getenv("XDG_CONFIG_HOME"); // use env variable $XDG_CONFIG_HOME if it's set
			if (xdgConfigHome != null) {
				return xdgConfigHome;
			}
		} catch (Exception e) { /* if env variable cannot be read, ignore it */}

		return System.getProperty("user.home");
	}

	public static String customElements() {
		return homeProgram() + "custom_elements/";
	}

	public static String temp() {
		if (tempDir == null) {
			String tmp = System.getProperty("java.io.tmpdir");
			if (!tmp.endsWith(File.separator)) {
				tmp = tmp + File.separator;
			}
			tempDir = tmp;
		}
		return tempDir;
	}

	/**
	 * STANDALONE NOJAR: <programpath>
	 * STANDALONE JAR: <programpath>
	 * ECLIPSE NOJAR: <programpath>
	 * ECLIPSE JAR: <eclipsepath>/<configuration>/<dirToStoreCustomStuff>
	 */
	public static String homeProgram() {
		return homeProgramDir;
	}

	public static void setHomeProgram(String homeProgramDir) {
		Path.homeProgramDir = homeProgramDir;
	}

	/**
	 * STANDALONE NOJAR: <programpath>/bin/
	 * STANDALONE JAR: <programpath>/<progname>.jar
	 * ECLIPSE NOJAR: <programpath>
	 * ECLIPSE JAR: <eclipsepath>/<pluginname>.jar
	 */
	public static String executable() {
		String path = null;
		URL codeSourceUrl = Main.class.getProtectionDomain().getCodeSource().getLocation();
		try { // Convert URL to URI to avoid HTML problems with special characters like space,ä,ö,ü,...
			path = codeSourceUrl.toURI().getPath();
		} catch (URISyntaxException e) {/* path stays null */}

		if (path == null) { // URI2URL Conversion failed, because URI.getPath() returned null OR because of an URISyntaxException
			// In this case use the URL and replace special characters manually (for now only space)
			path = codeSourceUrl.getPath().replace("%20", " ");
		}

		return path;
	}

	public static List<Class<?>> getAllClassesInPackage(String filterString) throws IOException, ClassNotFoundException {
		List<Class<?>> list = new ArrayList<Class<?>>();
		String path = Path.executable();
		if (!Path.executable().endsWith(".jar")) {
			list = getNewGridElementList(path, filterString);
		}
		else {
			JarFile jarFile = null;
			try {
				jarFile = new JarFile(Path.executable());
				Enumeration<JarEntry> jarEntries = jarFile.entries();
				while (jarEntries.hasMoreElements()) {
					JarEntry jarEntry = jarEntries.nextElement();
					add(list, jarEntry.getName(), filterString);
				}
			} finally {
				if (jarFile != null) {
					jarFile.close();
				}
			}
		}
		return list;
	}

	private static List<Class<?>> getNewGridElementList(String path, String filterString) throws ClassNotFoundException {
		List<File> fileList = new ArrayList<File>();
		getFiles(new File(path), fileList);
		List<Class<?>> returnList = new ArrayList<Class<?>>();
		for (File file : fileList) {
			add(returnList, file.getAbsolutePath(), filterString);
		}
		return returnList;
	}

	private static void add(List<Class<?>> fileList, String className, String filterString) throws ClassNotFoundException {
		if (className.endsWith(".class")) {
			className = className.toString().replace("/", ".").replace("\\", ".");
			if (className.contains(filterString)) {
				className = className.substring(className.indexOf(filterString));
				className = className.substring(0, className.length() - ".class".length());
				fileList.add(Class.forName(className));
			}
		}
	}

	private static void getFiles(File folder, List<File> list) {
		folder.setReadOnly();
		File[] files = folder.listFiles();
		for (File file : files) {
			list.add(file);
			if (file.isDirectory()) {
				getFiles(file, list);
			}
		}
	}

	public static Manifest manifest() throws IOException {
		Manifest manifest;
		if (Path.executable().endsWith(".jar")) {
			JarFile jarFile = null;
			try {
				jarFile = new JarFile(Path.executable());
				manifest = jarFile.getManifest();
			} finally {
				if (jarFile != null) {
					jarFile.close();
				}
			}
		}
		else {
			FileInputStream is = new FileInputStream(Path.homeProgram() + "META-INF" + File.separator + "MANIFEST.MF");
			manifest = new Manifest(is);
			is.close();
		}
		return manifest;
	}

}
