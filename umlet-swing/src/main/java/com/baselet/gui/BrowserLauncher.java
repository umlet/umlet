package com.baselet.gui;

import java.awt.Desktop;
import java.io.IOException;
import java.lang.reflect.Method;
import java.net.URI;
import java.net.URL;
import java.util.Scanner;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.baselet.control.constants.SystemInfo;
import com.baselet.control.enums.Os;

public class BrowserLauncher {

	private static final Logger log = LoggerFactory.getLogger(BrowserLauncher.class);

	public static void openURL(String url) {

		try {
			// Since Java6 this is a much easier method to open the browser
			if (Desktop.isDesktopSupported()) {
				Desktop desktop = Desktop.getDesktop();
				desktop.browse(new URI(url));
			}

			// Only if desktop is not supported we try the old main specific code
			else {
				if (SystemInfo.OS == Os.MAC) {
					Class<?> fileMgr = Class.forName("com.apple.eio.FileManager");
					Method openURL = fileMgr.getDeclaredMethod("openURL", new Class[] { String.class });
					openURL.invoke(null, new Object[] { url });
				}
				else if (SystemInfo.OS == Os.WINDOWS) {
					Runtime.getRuntime().exec("rundll32 url.dll,FileProtocolHandler " + url);
				}
				else { // assume Unix or Linux
					String[] browsers = { "firefox", "opera", "konqueror", "epiphany", "mozilla", "netscape" };
					String browser = null;
					for (int count = 0; count < browsers.length && browser == null; count++) {
						if (Runtime.getRuntime().exec(new String[] { "which", browsers[count] }).waitFor() == 0) {
							browser = browsers[count];
						}
					}
					if (browser == null) {
						throw new Exception("Could not find web browser");
					}
					else {
						Runtime.getRuntime().exec(new String[] { browser, url });
					}
				}
			}
		} catch (Exception e) {
			log.error("Error at opening the URL.", e);
		}
	}

	public static String readURL(String url) throws IOException {
		StringBuilder sb = new StringBuilder("");
		Scanner sc = null;
		try {
			sc = new Scanner(new URL(url).openStream());
			while (sc.hasNextLine()) {
				sb.append(sc.nextLine()).append("\n");
			}
		} finally {
			if (sc != null) {
				sc.close();
			}
		}
		return sb.toString();
	}
}
