package com.baselet.control;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.ObjectInputStream.GetField;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.jar.Manifest;

import org.apache.log4j.Logger;
import org.eclipse.core.runtime.FileLocator;

import com.baselet.control.Constants.Program;
import com.baselet.control.Constants.RuntimeType;
import com.baselet.plugin.MainPlugin;
import com.umlet.element.experimental.Id;

public class Path {

	private final static Logger log = Logger.getLogger(Utils.getClassName());

	private static String tempDir;
	private static String homeProgramDir;

	public static String config() {
		return userHome() + File.separator + Program.CONFIG_NAME;
	}

	private static String userHome() {
		String homeDir = System.getProperty("user.home");
		if (!homeDir.endsWith(File.separator)) homeDir += File.separator;
		File homeDirFile = new File(homeDir + Program.PROGRAM_NAME);
		if (!homeDirFile.exists()) homeDirFile.mkdir();
		return homeDirFile.getAbsolutePath();
	}

	public static String customElements() {
		return homeProgram() + "custom_elements/";
	}

	public static String temp() {
		if (tempDir == null) {
			String tmp = System.getProperty("java.io.tmpdir");
			if (!(tmp.endsWith(File.separator))) tmp = tmp + File.separator;
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
		if (homeProgramDir == null) {
			if (Program.RUNTIME_TYPE == RuntimeType.ECLIPSE_PLUGIN) {
				String path = null;
				try {
					URL homeURL = MainPlugin.getURL();
					path = FileLocator.toFileURL(homeURL).toString().substring(new String("file:/").length());
					if (File.separator.equals("/")) path = "/" + path;
				} catch (IOException e) {
					log.error("Cannot find location of Eclipse Plugin jar", e);
				}
				homeProgramDir = path;
			}
			else {
				String tempPath, realPath;
				tempPath = Path.executable();
				tempPath = tempPath.substring(0, tempPath.length() - 1);
				tempPath = tempPath.substring(0, tempPath.lastIndexOf('/') + 1);
				realPath = new File(tempPath).getAbsolutePath() + "/";
				homeProgramDir = realPath;
			}
		}
		return homeProgramDir;
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
		try { //Convert URL to URI to avoid HTML problems with special characters like space,ä,ö,ü,...
			path = codeSourceUrl.toURI().getPath();
		} catch (URISyntaxException e) {/*path stays null*/}

		if (path == null) { // URI2URL Conversion failed, because URI.getPath() returned null OR because of an URISyntaxException
			// In this case use the URL and replace special characters manually (for now only space)
			path = codeSourceUrl.getPath().replace("%20", " ");
		}

		return path;
	}

	public static List<Class<?>> getAllClassesInPackage(String filterString) throws IOException, ClassNotFoundException {
		List<Class<?>> list = new ArrayList<Class<?>>();
		String path = Path.executable();
		if (!Path.executable().endsWith(".jar")) list = getNewGridElementList(path, filterString);
		else {
			Enumeration<JarEntry> jarEntries = new JarFile(Path.executable()).entries();
			while(jarEntries.hasMoreElements()) {
				JarEntry jarEntry = jarEntries.nextElement();
				add(list, jarEntry.getName(), filterString);
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
		for(File file : files) {
			list.add(file);
			if(file.isDirectory()) getFiles(file, list);
		}
	}

	public static Manifest manifest() throws FileNotFoundException, IOException {
		Manifest manifest;
		if (Path.executable().endsWith(".jar")) manifest = new JarFile(Path.executable()).getManifest();
		else manifest = new Manifest(new FileInputStream(Path.homeProgram() + "META-INF" + File.separator + "MANIFEST.MF"));
		return manifest;
	}

}
