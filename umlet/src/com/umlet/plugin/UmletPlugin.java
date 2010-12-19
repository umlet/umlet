package com.umlet.plugin;

import java.io.IOException;
import java.net.URL;

import org.apache.log4j.Logger;
import org.eclipse.core.runtime.FileLocator;
import org.eclipse.core.runtime.Path;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.osgi.framework.BundleContext;

import com.umlet.control.Umlet;
import com.umlet.gui.eclipse.EclipseGUI;

/**
 * The activator class controls the plug-in life cycle
 */
public class UmletPlugin extends AbstractUIPlugin {

	private static Logger log = Logger.getLogger(UmletPlugin.class);

	// The plug-in ID
	public static final String PLUGIN_ID = "com.umlet.plugin";

	// The shared instance
	private static UmletPlugin plugin;

	private static EclipseGUI gui;

	public static EclipseGUI getGUI() {
		return gui;
	}

	/**
	 * The constructor
	 */
	public UmletPlugin() {
		plugin = this;
	}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.ui.plugin.AbstractUIPlugin#start(org.osgi.framework.BundleContext)
	 */
	@Override
	public void start(BundleContext context) throws Exception {
		super.start(context);

		// get the homepath
		String thePath = null;
		try {
			URL homeURL = FileLocator.find(UmletPlugin.getDefault().getBundle(), new Path("/"), null);
			thePath = FileLocator.toFileURL(homeURL).toString().substring(new String("file:/").length());
			if (System.getProperty("file.separator").equals("/")) // [UB]: if UNIX
			thePath = "/" + thePath;
		} catch (IOException e) {
			log.error("Umlet->init()", e);
		}

		Umlet umlet = Umlet.getInstance();
		umlet.setHomePath(thePath);
		umlet.initLoggerConfiguration();

		try {
			gui = new EclipseGUI(umlet);
			Umlet.getInstance().init(gui);
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
		Umlet.getInstance().getGUI().closeWindow();
		plugin = null;
		super.stop(context);
	}

	/**
	 * Returns the shared instance
	 * 
	 * @return the shared instance
	 */
	public static UmletPlugin getDefault() {
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
}
