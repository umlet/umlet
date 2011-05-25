package com.baselet.control;

import java.awt.Dimension;
import java.awt.Frame;
import java.awt.Point;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.Arrays;

import com.baselet.control.Constants.Program;
import com.baselet.gui.BaseGUI;


public class Config {

	private static final String PROGRAM_VERSION = "program_version = ";
	private static final String DEFAULT_FONTSIZE = "default_fontsize = ";
	private static final String DEFAULT_FONTFAMILY = "default_fontfamily = ";
	private static final String SHOW_STICKINGPOLYGON = "show_stickingpolygon = ";
	private static final String SHOW_GRID = "show_grid = ";
	private static final String ENABLE_CUSTOM_ELEMENTS = "enable_custom_elements = ";
	private static final String UI_MANAGER = "ui_manager = ";
	private static final String MAIN_SPLIT_POSITION = "main_split_position = ";
	private static final String RIGHT_SPLIT_POSITION = "right_split_position = ";
	private static final String START_MAXIMIZED = "start_maximized = ";
	private static final String MAIL_SPLIT_POSITION = "mail_split_position = ";
	private static final String PROGRAM_SIZE = "program_size = ";
	private static final String PROGRAM_LOCATION = "program_location = ";
	private static final String RECENT_FILES = "recent_files = ";
	private static final String MAIL_SMTP = "mail_smtp = ";
	private static final String MAIL_SMTP_AUTH = "mail_smtp_auth = ";
	private static final String MAIL_SMTP_USER = "mail_smtp_user = ";
	private static final String MAIL_SMTP_PW_STORE = "mail_smtp_pw_store = ";
	private static final String MAIL_SMTP_PW = "mail_smtp_pw = ";
	private static final String MAIL_FROM = "mail_from = ";
	private static final String MAIL_TO = "mail_to = ";
	private static final String MAIL_CC = "mail_cc = ";
	private static final String MAIL_BCC = "mail_bcc = ";
	private static final String MAIL_XML = "mail_xml = ";
	private static final String MAIL_GIF = "mail_gif = ";
	private static final String MAIL_PDF = "mail_pdf = ";
	private static File configfile;

	public static void loadConfig() {
		configfile = new File(Path.config());
		if (configfile.exists()) {
			try {
				BufferedReader reader = new BufferedReader(new FileReader(configfile));
				String line;
				while ((line = reader.readLine()) != null) {
					try {
						if (line.startsWith(DEFAULT_FONTSIZE)) {
							Constants.defaultFontsize = Integer.parseInt(line.substring(DEFAULT_FONTSIZE.length()));
						}
						else if (line.startsWith(DEFAULT_FONTFAMILY)) {
							Constants.defaultFontFamily = line.substring(DEFAULT_FONTFAMILY.length());
						}
						else if (line.startsWith(SHOW_STICKINGPOLYGON)) {
							Constants.show_stickingpolygon = Boolean.parseBoolean(line.substring(SHOW_STICKINGPOLYGON.length()));
						}
						else if (line.startsWith(SHOW_GRID)) {
							Constants.show_grid = Boolean.parseBoolean(line.substring(SHOW_GRID.length()));
						}
						else if (line.startsWith(ENABLE_CUSTOM_ELEMENTS)) {
							Constants.enable_custom_elements = Boolean.parseBoolean(line.substring(ENABLE_CUSTOM_ELEMENTS.length()));
						}
						else if (line.startsWith(UI_MANAGER)) {
							Constants.ui_manager = line.substring(UI_MANAGER.length());
						}
						else if (line.startsWith(MAIN_SPLIT_POSITION)) {
							Constants.main_split_position = Integer.parseInt(line.substring(MAIN_SPLIT_POSITION.length()));
						}
						else if (line.startsWith(RIGHT_SPLIT_POSITION)) {
							Constants.right_split_position = Integer.parseInt(line.substring(RIGHT_SPLIT_POSITION.length()));
						}
						else if (line.startsWith(MAIL_SPLIT_POSITION)) {
							Constants.mail_split_position = Integer.parseInt(line.substring(MAIL_SPLIT_POSITION.length()));
						}
						else if (line.startsWith(START_MAXIMIZED)) {
							Constants.start_maximized = Boolean.parseBoolean(line.substring(START_MAXIMIZED.length()));
						}
						// In case of start_maximized=true we don't store any size or location information
						else if (line.startsWith(PROGRAM_SIZE) && !Constants.start_maximized) {
							int x = Integer.parseInt(line.substring(PROGRAM_SIZE.length(), line.indexOf(",")));
							int y = Integer.parseInt(line.substring(line.indexOf(",") + 1));
							Constants.program_size = new Dimension(x, y);
						}
						else if (line.startsWith(PROGRAM_LOCATION) && !Constants.start_maximized) {
							int x = Integer.parseInt(line.substring(PROGRAM_LOCATION.length(), line.indexOf(",")));
							int y = Integer.parseInt(line.substring(line.indexOf(",") + 1));
							Constants.program_location = new Point(x, y);
						}
						else if (line.startsWith(RECENT_FILES)) {
							Main.getInstance().getRecentFiles().addAll(Arrays.asList(line.substring(RECENT_FILES.length()).split("\\|")));
						}

						/** MAIL **/
						else if (line.startsWith(MAIL_SMTP)) {
							Constants.mail_smtp = line.substring(MAIL_SMTP.length());
						}
						else if (line.startsWith(MAIL_SMTP_AUTH)) {
							Constants.mail_smtp_auth = Boolean.parseBoolean(line.substring(MAIL_SMTP_AUTH.length()));
						}
						else if (line.startsWith(MAIL_SMTP_USER)) {
							Constants.mail_smtp_user = line.substring(MAIL_SMTP_USER.length());
						}
						else if (line.startsWith(MAIL_SMTP_PW_STORE)) {
							Constants.mail_smtp_pw_store = Boolean.parseBoolean(line.substring(MAIL_SMTP_PW_STORE.length()));
						}
						else if (line.startsWith(MAIL_SMTP_PW)) {
							Constants.mail_smtp_pw = line.substring(MAIL_SMTP_PW.length());
						}
						else if (line.startsWith(MAIL_FROM)) {
							Constants.mail_from = line.substring(MAIL_FROM.length());
						}
						else if (line.startsWith(MAIL_TO)) {
							Constants.mail_to = line.substring(MAIL_TO.length());
						}
						else if (line.startsWith(MAIL_CC)) {
							Constants.mail_cc = line.substring(MAIL_CC.length());
						}
						else if (line.startsWith(MAIL_BCC)) {
							Constants.mail_bcc = line.substring(MAIL_BCC.length());
						}
						else if (line.startsWith(MAIL_XML)) {
							Constants.mail_xml = Boolean.parseBoolean(line.substring(MAIL_XML.length()));
						}
						else if (line.startsWith(MAIL_GIF)) {
							Constants.mail_gif = Boolean.parseBoolean(line.substring(MAIL_GIF.length()));
						}
						else if (line.startsWith(MAIL_PDF)) {
							Constants.mail_pdf = Boolean.parseBoolean(line.substring(MAIL_PDF.length()));
						}

					} catch (Exception ex) {
						ex.printStackTrace();
					}
				}
				reader.close();
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}
	}

	public static void saveConfig() {
		try {
			if (configfile != null) {
				configfile.delete();
				configfile.createNewFile();
				FileWriter writer = new FileWriter(configfile);
				writer.write(PROGRAM_VERSION + Program.VERSION + "\n");
				writer.write(DEFAULT_FONTSIZE + Constants.defaultFontsize + "\n");
				writer.write(DEFAULT_FONTFAMILY + Constants.defaultFontFamily + "\n");
				writer.write(SHOW_STICKINGPOLYGON + Constants.show_stickingpolygon + "\n");
				writer.write(SHOW_GRID + Constants.show_grid + "\n");
				writer.write(ENABLE_CUSTOM_ELEMENTS + Constants.enable_custom_elements + "\n");
				writer.write(UI_MANAGER + Constants.ui_manager + "\n");
				BaseGUI gui = Main.getInstance().getGUI();
				writer.write(MAIN_SPLIT_POSITION + gui.getMainSplitPosition() + "\n");
				writer.write(RIGHT_SPLIT_POSITION + gui.getRightSplitPosition() + "\n");
				if (gui.getMailSplitPosition() > Constants.MIN_MAIL_SPLIT_POSITION) writer.write(MAIL_SPLIT_POSITION + gui.getMailSplitPosition() + "\n");
				if (gui.getTopContainer() != null) {
					// If the window is maximized in any direction this fact is written in the cfg
					if (((gui.getTopContainer().getExtendedState() & Frame.MAXIMIZED_BOTH) == Frame.MAXIMIZED_BOTH)) {
						writer.write(START_MAXIMIZED + "true\n");
					}
					// Otherwise the size and the location is written in the cfg
					else {
						writer.write(START_MAXIMIZED + "false\n");
						writer.write(PROGRAM_SIZE + gui.getTopContainer().getSize().width + "," + gui.getTopContainer().getSize().height + "\n");
						writer.write(PROGRAM_LOCATION + gui.getTopContainer().getLocation().x + "," + gui.getTopContainer().getLocation().y + "\n");
					}
				}
				if (!Main.getInstance().getRecentFiles().isEmpty()) {
					String recentFileString =RECENT_FILES;
					for (String recentFile : Main.getInstance().getRecentFiles()) {
						recentFileString += recentFile + "|";
					}
					writer.write(recentFileString.substring(0,recentFileString.length()-1) + "\n");
				}

				/** MAIL **/
				if (!Constants.mail_smtp.isEmpty()) writer.write(MAIL_SMTP + Constants.mail_smtp + "\n");
				writer.write(MAIL_SMTP_AUTH + Constants.mail_smtp_auth + "\n");
				if (!Constants.mail_smtp_user.isEmpty()) writer.write(MAIL_SMTP_USER + Constants.mail_smtp_user + "\n");
				writer.write(MAIL_SMTP_PW_STORE + Constants.mail_smtp_pw_store + "\n");
				if (!Constants.mail_smtp_pw.isEmpty()) writer.write(MAIL_SMTP_PW + Constants.mail_smtp_pw + "\n");
				if (!Constants.mail_from.isEmpty()) writer.write(MAIL_FROM + Constants.mail_from + "\n");
				if (!Constants.mail_to.isEmpty()) writer.write(MAIL_TO + Constants.mail_to + "\n");
				if (!Constants.mail_cc.isEmpty()) writer.write(MAIL_CC + Constants.mail_cc + "\n");
				if (!Constants.mail_bcc.isEmpty()) writer.write(MAIL_BCC + Constants.mail_bcc + "\n");
				writer.write(MAIL_XML + Constants.mail_xml + "\n");
				writer.write(MAIL_GIF + Constants.mail_gif + "\n");
				writer.write(MAIL_PDF + Constants.mail_pdf + "\n");

				writer.close();
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}

	}
}
