package com.baselet.diagram;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Scanner;
import java.util.TimerTask;

import com.baselet.control.enums.Program;
import com.baselet.gui.BrowserLauncher;

public class UpdateCheckTimerTask extends TimerTask {

	private static final UpdateCheckTimerTask instance = new UpdateCheckTimerTask();

	private UpdateCheckTimerTask() {}

	public static UpdateCheckTimerTask getInstance() {
		return instance;
	}

	private String filename;

	public String getFilename() {
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
			StartUpHelpText.log.error("Error at checking for new " + Program.getInstance().getProgramName() + " version", e);
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
		StringBuilder sb = new StringBuilder("");
		Scanner sc = null;
		try {
			sc = new Scanner(StartUpHelpText.getStartUpFileName());
			while (sc.hasNextLine()) {
				String line = sc.nextLine();
				if (line.contains("<body>")) {
					break;
				}
				sb.append(line).append("\n");
			}
			sb.append(textFromURL).append("</body></html>");
		} finally {
			if (sc != null) {
				sc.close();
			}
		}
		return sb.toString();
	}

	private static String getNewVersionTextFromURL() throws IOException {
		String versionText = BrowserLauncher.readURL(Program.getInstance().getWebsite() + "/current_umlet_version_changes.txt");
		versionText = versionText.replace("<", "&lt;").replace(">", "&gt;").replace("&", "&amp;").replace("\"", "&quot;"); // escape html characters for safety
		String[] splitString = versionText.split("\n");
		String actualVersion = splitString[0];
		if (Program.getInstance().getVersion().compareTo(actualVersion) >= 0) {
			return null; // no newer version found
		}

		StringBuilder sb = new StringBuilder("");
		sb.append("<p><b>A new version of ").append(Program.getInstance().getProgramName()).append(" (").append(actualVersion).append(") is available at <a href=\"").append(Program.getInstance().getWebsite()).append("\">").append(Program.getInstance().getWebsite().substring("http://".length())).append("</a></b></p>");
		// Every line after the first one describes a feature of the new version and will be listed
		for (int i = 1; i < splitString.length; i++) {
			sb.append("<p>").append(splitString[i]).append("</p>");
		}
		return sb.toString();
	}
}