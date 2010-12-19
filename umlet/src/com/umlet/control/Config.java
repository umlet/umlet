package com.umlet.control;

import java.awt.Dimension;
import java.awt.Frame;
import java.awt.Point;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;

import com.umlet.constants.Constants;
import com.umlet.gui.base.UmletGUI;

public class Config {

	private static String configfile;

	public static void loadConfig(String configfile) {
		Config.configfile = configfile;
		File umletcfg = new File(Umlet.getInstance().getHomePath() + configfile);
		if (umletcfg.exists()) {
			try {
				BufferedReader reader = new BufferedReader(new FileReader(umletcfg));
				String line;
				while ((line = reader.readLine()) != null) {
					try {
						if (line.startsWith("default_fonsize = ")) {
							Constants.defaultFontsize = Integer.parseInt(line.substring("default_fonsize = ".length()));
						}
						else if (line.startsWith("start_maximized = ")) {
							Constants.start_maximized = Boolean.parseBoolean(line.substring("start_maximized = ".length()));
						}
						else if (line.startsWith("show_stickingpolygon = ")) {
							Constants.show_stickingpolygon = Boolean.parseBoolean(line.substring("show_stickingpolygon = ".length()));
						}
						else if (line.startsWith("show_grid = ")) {
							Constants.show_grid = Boolean.parseBoolean(line.substring("show_grid = ".length()));
						}
						else if (line.startsWith("ui_manager = ")) {
							Constants.ui_manager = line.substring("ui_manager = ".length());
						}
						else if (line.startsWith("main_split_position = ")) {
							Constants.main_split_position = Integer.parseInt(line.substring("main_split_position = ".length()));
						}
						else if (line.startsWith("right_split_position = ")) {
							Constants.right_split_position = Integer.parseInt(line.substring("right_split_position = ".length()));
						}
						else if (line.startsWith("mail_split_position = ")) {
							Constants.mail_split_position = Integer.parseInt(line.substring("mail_split_position = ".length()));
						}
						// In case of start_maximized=true we don't store any size or location information
						else if (line.startsWith("umlet_size = ") && !Constants.start_maximized) {
							int x = Integer.parseInt(line.substring("umlet_size = ".length(), line.indexOf(",")));
							int y = Integer.parseInt(line.substring(line.indexOf(",") + 1));
							Constants.umlet_size = new Dimension(x, y);
						}
						else if (line.startsWith("umlet_location = ") && !Constants.start_maximized) {
							int x = Integer.parseInt(line.substring("umlet_location = ".length(), line.indexOf(",")));
							int y = Integer.parseInt(line.substring(line.indexOf(",") + 1));
							Constants.umlet_location = new Point(x, y);
						}

						/** MAIL **/
						else if (line.startsWith("mail_smtp = ")) {
							Constants.mail_smtp = line.substring("mail_smtp = ".length());
						}
						else if (line.startsWith("mail_smtp_auth = ")) {
							Constants.mail_smtp_auth = Boolean.parseBoolean(line.substring("mail_smtp_auth = ".length()));
						}
						else if (line.startsWith("mail_smtp_user = ")) {
							Constants.mail_smtp_user = line.substring("mail_smtp_user = ".length());
						}
						else if (line.startsWith("mail_smtp_pw_store = ")) {
							Constants.mail_smtp_pw_store = Boolean.parseBoolean(line.substring("mail_smtp_pw_store = ".length()));
						}
						else if (line.startsWith("mail_smtp_pw = ")) {
							Constants.mail_smtp_pw = line.substring("mail_smtp_pw = ".length());
						}
						else if (line.startsWith("mail_from = ")) {
							Constants.mail_from = line.substring("mail_from = ".length());
						}
						else if (line.startsWith("mail_to = ")) {
							Constants.mail_to = line.substring("mail_to = ".length());
						}
						else if (line.startsWith("mail_cc = ")) {
							Constants.mail_cc = line.substring("mail_cc = ".length());
						}
						else if (line.startsWith("mail_bcc = ")) {
							Constants.mail_bcc = line.substring("mail_bcc = ".length());
						}
						else if (line.startsWith("mail_uxf = ")) {
							Constants.mail_uxf = Boolean.parseBoolean(line.substring("mail_uxf = ".length()));
						}
						else if (line.startsWith("mail_gif = ")) {
							Constants.mail_gif = Boolean.parseBoolean(line.substring("mail_gif = ".length()));
						}
						else if (line.startsWith("mail_pdf = ")) {
							Constants.mail_pdf = Boolean.parseBoolean(line.substring("mail_pdf = ".length()));
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
			if (configfile != null) // only save config after config has been loaded from somewhere
			{
				File umletcfg = new File(Umlet.getInstance().getHomePath() + configfile);
				umletcfg.delete();
				umletcfg.createNewFile();
				FileWriter writer = new FileWriter(umletcfg);
				writer.write("default_fonsize = " + Constants.defaultFontsize + "\n");
				writer.write("show_stickingpolygon = " + Constants.show_stickingpolygon + "\n");
				writer.write("show_grid = " + Constants.show_grid + "\n");
				writer.write("ui_manager = " + Constants.ui_manager + "\n");
				UmletGUI gui = Umlet.getInstance().getGUI();
				writer.write("main_split_position = " + gui.getMainSplitPosition() + "\n");
				writer.write("right_split_position = " + gui.getRightSplitPosition() + "\n");
				if (gui.getMailSplitPosition() > Constants.MIN_MAIL_SPLIT_POSITION) writer.write("mail_split_position = " + gui.getMailSplitPosition() + "\n");
				if (gui.getTopContainer() != null) {
					// If the window is maximized in any direction this fact is written in the cfg
					if (((gui.getTopContainer().getExtendedState() & Frame.MAXIMIZED_BOTH) == Frame.MAXIMIZED_BOTH)) {
						writer.write("start_maximized = true\n");
					}
					// Otherwise the size and the location is written in the cfg
					else {
						writer.write("start_maximized = false\n");
						writer.write("umlet_size = " + gui.getTopContainer().getSize().width + "," + gui.getTopContainer().getSize().height + "\n");
						writer.write("umlet_location = " + gui.getTopContainer().getLocation().x + "," + gui.getTopContainer().getLocation().y + "\n");
					}
				}

				/** MAIL **/
				if (!Constants.mail_smtp.isEmpty()) writer.write("mail_smtp = " + Constants.mail_smtp + "\n");
				writer.write("mail_smtp_auth = " + Constants.mail_smtp_auth + "\n");
				if (!Constants.mail_smtp_user.isEmpty()) writer.write("mail_smtp_user = " + Constants.mail_smtp_user + "\n");
				writer.write("mail_smtp_pw_store = " + Constants.mail_smtp_pw_store + "\n");
				if (!Constants.mail_smtp_pw.isEmpty()) writer.write("mail_smtp_pw = " + Constants.mail_smtp_pw + "\n");
				if (!Constants.mail_from.isEmpty()) writer.write("mail_from = " + Constants.mail_from + "\n");
				if (!Constants.mail_to.isEmpty()) writer.write("mail_to = " + Constants.mail_to + "\n");
				if (!Constants.mail_cc.isEmpty()) writer.write("mail_cc = " + Constants.mail_cc + "\n");
				if (!Constants.mail_bcc.isEmpty()) writer.write("mail_bcc = " + Constants.mail_bcc + "\n");
				writer.write("mail_uxf = " + Constants.mail_uxf + "\n");
				writer.write("mail_gif = " + Constants.mail_gif + "\n");
				writer.write("mail_pdf = " + Constants.mail_pdf + "\n");

				writer.close();
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}

	}
}
