package com.baselet.plugin;

import java.net.URL;

import org.apache.log4j.Logger;
import org.eclipse.core.runtime.FileLocator;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.osgi.framework.BundleContext;

import com.baselet.control.Constants.Program;
import com.baselet.control.Constants.RuntimeType;
import com.baselet.control.Main;
import com.baselet.control.Utils;
import com.baselet.gui.eclipse.EclipseGUI;

/**
 * The activator class controls the plug-in life cycle
 */
public class MainPlugin extends AbstractUIPlugin {

	private final static Logger log = Logger.getLogger(Utils.getClassName());

	// The plug-in ID
	public static String PLUGIN_ID;

	// The shared instance
	private static MainPlugin plugin;

	private static EclipseGUI gui;

	public static EclipseGUI getGUI() {
		return gui;
	}

	/**
	 * The constructor
	 */
	public MainPlugin() {
		Program.RUNTIME_TYPE = RuntimeType.ECLIPSE_PLUGIN;
		plugin = this;
	}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.ui.plugin.AbstractUIPlugin#start(org.osgi.framework.BundleContext)
	 */
	@Override
	public void start(BundleContext context) throws Exception {
		super.start(context);
		
		try {
			Main.getInstance().initOverallSettings();
			gui = new EclipseGUI(Main.getInstance());
			Main.getInstance().init(gui);
		} catch (Exception e) {
			log.error("Initialization or uncaught outer Exception", e);
		}
	}


	/*
	 * (non-Javadoc)
	 * @see org.eclipse.ui.plugin.AbstractUIPlugin#stop(org.osgi.framework.BundleContext)
	 */
	@Override
	public void stop(BundleContext context) throws Exception {
		Main.getInstance().getGUI().closeWindow();
		plugin = null;
		super.stop(context);
	}

	/**
	 * Returns the shared instance
	 * 
	 * @return the shared instance
	 */
	public static MainPlugin getDefault() {
		return plugin;
	}

	/**
	 * Returns an image descriptor for the image file at the given
	 * plug-in relative path
	 * 
	 * @param path
	 *            the path
	 * @return the image descriptor
	 */
	public static ImageDescriptor getImageDescriptor(String path) {
		return imageDescriptorFromPlugin(PLUGIN_ID, path);
	}

	public static URL getURL() {
		return FileLocator.find(MainPlugin.getDefault().getBundle(), new org.eclipse.core.runtime.Path("/"), null);
	}
}
