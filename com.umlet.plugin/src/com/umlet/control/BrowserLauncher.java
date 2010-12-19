package com.umlet.control;

import java.awt.Desktop;
import java.lang.reflect.Method;
import java.net.URI;

import com.umlet.constants.Constants;
import com.umlet.constants.Constants.Os;

public class BrowserLauncher {

	@SuppressWarnings("unchecked")
	public static void openURL(String url) {

		try {
			// Since Java6 this is a much easier method to open the browser
			if (Desktop.isDesktopSupported()) {
				Desktop desktop = Desktop.getDesktop();
				desktop.browse(new URI(url));
			}

			// Only if desktop is not supported we try the old umlet specific code
			else {
				if (Constants.OS == Os.MAC) {
					Class fileMgr = Class.forName("com.apple.eio.FileManager");
					Method openURL = fileMgr.getDeclaredMethod("openURL", new Class[] { String.class });
					openURL.invoke(null, new Object[] { url });
				}
				else if (Constants.OS == Os.WINDOWS) Runtime.getRuntime().exec("rundll32 url.dll,FileProtocolHandler " + url);
				else { // assume Unix or Linux
					String[] browsers = {
							"firefox", "opera", "konqueror", "epiphany", "mozilla", "netscape" };
					String browser = null;
					for (int count = 0; (count < browsers.length) && (browser == null); count++)
						if (Runtime.getRuntime().exec(
								new String[] { "which", browsers[count] }).waitFor() == 0) browser = browsers[count];
					if (browser == null) throw new Exception("Could not find web browser");
					else Runtime.getRuntime().exec(new String[] { browser, url });
				}
			}
		} catch (Exception e) {
			System.err.println("Error in openURL:" + e.getMessage());
			e.printStackTrace();
		}
	}
}
