package com.baselet.control;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;

import org.apache.log4j.Logger;
import org.eclipse.core.runtime.FileLocator;

import com.baselet.control.Constants.Program;
import com.baselet.control.Constants.RuntimeType;
import com.baselet.plugin.MainPlugin;

public class Path {

	private final static Logger log = Logger.getLogger(Utils.getClassName());

	private static String tempDir;
	private static String homeProgramDir;

	public static String config() {
		return Path.homeProgram() + Program.CONFIG_NAME;
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
		String path;
		try {
			// We convert to an URI to avoid HTML problems with special characters like space,ä,ö,ü,...
			path = Main.class.getProtectionDomain().getCodeSource().getLocation().toURI().getPath();
		} catch (URISyntaxException e) {
			// If the conversion fails, we don't convert to an URI but we have to manually replace those special characters later
			// WARNING: Only space will be replaced here, other special characters like ä,ö,ü,... won't (add them if needed)
			path = Main.class.getProtectionDomain().getCodeSource().getLocation().getPath().replace("%20", " ");
		}
		return path;
	}

}
