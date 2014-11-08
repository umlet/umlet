package com.baselet.diagram;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Scanner;
import java.util.TimerTask;

import com.baselet.control.enums.Program;
import com.baselet.gui.BrowserLauncher;

public class UpdateCheckTimerTask extends TimerTask {

	private static String filename;

	public static String getFilename() {
		return filename;
	}

	@Override
	public void run() {
		try {
			String newVersionText = getNewVersionTextWithStartupHtmlFormat();
			if (newVersionText != null) { // The text is != null if a new version exists
				filename = StartUpHelpText.createTempFileWithText(newVersionText);
			}
		} catch (Exception e) {
			StartUpHelpText.log.error("Error at checking for new " + Program.NAME + " version", e);
		}
	}

	private static String getNewVersionTextWithStartupHtmlFormat() throws IOException {
		String textFromURL = getNewVersionTextFromURL();
		if (textFromURL == null) {
			return null;
		}
		else {
			return wrapUpdateTextIntoStartupFileHtmlStyle(textFromURL);
		}
	}

	private static String wrapUpdateTextIntoStartupFileHtmlStyle(String textFromURL) throws FileNotFoundException {
		String returnText = "";
		Scanner sc = null;
		try {
			sc = new Scanner(new File(StartUpHelpText.getStartUpFileName()));
			while (sc.hasNextLine()) {
				String line = sc.nextLine();
				if (line.contains("<body>")) {
					break;
				}
				returnText += line + "\n";
			}
			returnText += textFromURL + "</body></html>";
		} finally {
			if (sc != null) {
				sc.close();
			}
		}
		return returnText;
	}

	private static String getNewVersionTextFromURL() throws IOException {
		String versionText = BrowserLauncher.readURL(Program.WEBSITE + "/current_umlet_version_changes.txt");
		versionText = versionText.replace("<", "&lt;").replace(">", "&gt;").replace("&", "&amp;").replace("\"", "&quot;"); // escape html characters for safety
		String[] splitString = versionText.split("\n");
		String actualVersion = splitString[0];
		if (Program.VERSION.compareTo(actualVersion) >= 0) {
			return null; // no newer version found
		}

		String returnText = "<p><b>A new version of " + Program.NAME + " (" + actualVersion + ") is available at <a href=\"" + Program.WEBSITE + "\">" + Program.WEBSITE.substring("http://".length()) + "</a></b></p>";
		// Every line after the first one describes a feature of the new version and will be listed
		for (int i = 1; i < splitString.length; i++) {
			returnText += "<p>" + splitString[i] + "</p>";
		}
		return returnText;
	}
}