package com.umlet.control;

import java.awt.Dimension;
import java.awt.Point;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;

import javax.swing.JFrame;

import com.umlet.constants.Constants;
import com.umlet.gui.UmletGUI;
import com.umlet.gui.eclipse.EclipseGUI;

public class Config {
	
	private static String configfile;
	
	public static void loadConfig(UmletGUI gui)
	{
		String configfile = gui.getConfigFile();
		Config.configfile = configfile;
		File umletcfg = new File(Umlet.getInstance().getHomePath() + configfile);
		if(umletcfg.exists())
		{
			try
			{
				BufferedReader reader = new BufferedReader(new FileReader(umletcfg));
				String line;
				while((line = reader.readLine()) != null)
				{
					try
					{ 
						if(line.startsWith("default_fonsize = "))
							Constants.default_fontsize = Integer.parseInt(line.substring("default_fonsize = ".length()));
						else if(line.startsWith("start_maximized = ")) {
							Constants.start_maximized = Boolean.parseBoolean(line.substring("start_maximized = ".length()));
							if(Constants.start_maximized)
							{
								Constants.umlet_size = new Dimension(960,750);
								Constants.umlet_location = new Point(5,5);
							}
						}
						else if(line.startsWith("show_stickingpolygon = "))
							Constants.show_stickingpolygon = Boolean.parseBoolean(line.substring("show_stickingpolygon = ".length()));
						else if(line.startsWith("ui_manager = "))
							Constants.ui_manager = line.substring("ui_manager = ".length());
						else if(line.startsWith("main_split_position = "))
							Constants.main_split_position = Integer.parseInt(line.substring("main_split_position = ".length()));
						else if(line.startsWith("right_split_position = "))
							Constants.right_split_position = Integer.parseInt(line.substring("right_split_position = ".length()));
						else if(line.startsWith("umlet_size = ") && !Constants.start_maximized) {
							int x = Integer.parseInt(line.substring("umlet_size = ".length(),line.indexOf(",")));
							int y = Integer.parseInt(line.substring(line.indexOf(",")+1));
							Constants.umlet_size = new Dimension(x,y);
						}
						else if(line.startsWith("umlet_location = ") && !Constants.start_maximized) {
							int x = Integer.parseInt(line.substring("umlet_location = ".length(),line.indexOf(",")));
							int y = Integer.parseInt(line.substring(line.indexOf(",")+1));
							Constants.umlet_location = new Point(x,y);
						}
					}
					catch(Exception ex)
					{
						ex.printStackTrace();
					}
				}
				
				reader.close();
			}
			catch(Exception ex)
			{
				ex.printStackTrace();
			}
		}
		else if (gui instanceof EclipseGUI) {
			//If there is no configfile we set the WindowsLookAndFeel or otherwise the Plugin will crash under Linux
			Constants.ui_manager = "com.sun.java.swing.plaf.windows.WindowsLookAndFeel";
		}
	}
	
	public static void saveConfig()
	{
		try
		{
			if(configfile != null) //only save config after config has been loaded from somewhere
			{
				File umletcfg = new File(Umlet.getInstance().getHomePath() + configfile);
				umletcfg.delete();
				umletcfg.createNewFile();
				FileWriter writer = new FileWriter(umletcfg);
				writer.write("default_fonsize = " + Constants.default_fontsize + "\n");
				writer.write("show_stickingpolygon = " + Constants.show_stickingpolygon + "\n");
				writer.write("ui_manager = " + Constants.ui_manager + "\n");
				UmletGUI gui = Umlet.getInstance().getGUI();
				if(gui.getMainSplitPosition() > 0)
					writer.write("main_split_position = " + gui.getMainSplitPosition() + "\n");
				if(gui.getRightSplitPosition() > 0)
					writer.write("right_split_position = " + gui.getRightSplitPosition() + "\n");
				if(gui.getTopContainer() != null)
				{
					writer.write("start_maximized = " + ((gui.getTopContainer().getExtendedState() & JFrame.MAXIMIZED_BOTH) != 0) + "\n");
					if(!((gui.getTopContainer().getExtendedState() & JFrame.MAXIMIZED_BOTH) != 0))
					{
						writer.write("umlet_size = " + gui.getTopContainer().getSize().width + "," 
							+ gui.getTopContainer().getSize().height + "\n");
						writer.write("umlet_location = " + gui.getTopContainer().getLocation().x + "," + 
							gui.getTopContainer().getLocation().y);
					}
				}
				writer.close();
			}
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
		}
		
	}
}
