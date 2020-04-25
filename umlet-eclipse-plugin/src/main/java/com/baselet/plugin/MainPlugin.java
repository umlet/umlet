package com.baselet.plugin;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.net.URL;

import org.eclipse.core.runtime.FileLocator;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.jface.dialogs.ErrorDialog;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.osgi.framework.BundleContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.baselet.control.Main;
import com.baselet.control.config.handler.ConfigHandler;
import com.baselet.control.enums.Program;
import com.baselet.control.enums.RuntimeType;
import com.baselet.control.util.Path;
import com.baselet.gui.CurrentGui;
import com.baselet.plugin.gui.EclipseGUI;

/**
 * The activator class controls the plug-in life cycle
 */
public class MainPlugin extends AbstractUIPlugin {

	Logger log = LoggerFactory.getLogger(MainPlugin.class);

	// The plug-in ID
	private static String pluginId;

	public static String getPluginId() {
		return pluginId;
	}

	// The shared instance
	private static MainPlugin plugin;

	/**
	 * The constructor
	 */
	public MainPlugin() {
		plugin = this;
	}

	// see https://wiki.eclipse.org/Eclipse_Plug-in_Development_FAQ#How_do_I_show_a_message_dialogue_for_exceptions_and_log_them.3F
	public void showAndLogError(Throwable ex, final String title) {
		StringWriter writer = new StringWriter();
		ex.printStackTrace(new PrintWriter(writer));

		final String message = ex.getMessage();
		final String formattedMessage = pluginId + " : " + message; //$NON-NLS-1$
		final Status status = new Status(IStatus.ERROR, pluginId, formattedMessage, new Throwable(writer.toString()));

		getLog().log(status);
		Display.getDefault().asyncExec(new Runnable() {
			@Override
			public void run() {
				ErrorDialog.openError(Display.getDefault().getActiveShell(),
						title, message, status);
			}
		});
	}

	/* (non-Javadoc)
	 * @see org.eclipse.ui.plugin.AbstractUIPlugin#start(org.osgi.framework.BundleContext) */
	@Override
	public void start(BundleContext context) throws Exception {
		Thread.setDefaultUncaughtExceptionHandler(new Thread.UncaughtExceptionHandler() {
			@Override
			public void uncaughtException(Thread t, Throwable e) {
				showAndLogError(e, "Unexpected Exception in UMLet plugin");
			}
		});
		log.info("Initializing Plugin ...");
		super.start(context);
		initHomeProgramPath();
		readBundleManifestInfo();
		ConfigHandler.loadConfig();
		Main.getInstance().init(new EclipseGUI(Main.getInstance()));
		log.info("Plugin initialized");
	}

	private void initHomeProgramPath() throws IOException {
		String path = null;
		URL homeURL = MainPlugin.getURL();
		path = FileLocator.toFileURL(homeURL).toString().substring("file:/".length());
		if (File.separator.equals("/")) {
			path = "/" + path;
		}
		Path.setHomeProgram(path);
	}

	// Issue 83: Use OSGI Bundle to read Manifest information
	private void readBundleManifestInfo() {
		pluginId = MainPlugin.getDefault().getBundle().getSymbolicName();
		Program.init(MainPlugin.getDefault().getBundle().getVersion().toString(), RuntimeType.ECLIPSE_PLUGIN);

	}

	/* (non-Javadoc)
	 * @see org.eclipse.ui.plugin.AbstractUIPlugin#stop(org.osgi.framework.BundleContext) */
	@Override
	public void stop(BundleContext context) throws Exception {
		CurrentGui.getInstance().getGui().closeWindow();
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
		return imageDescriptorFromPlugin(pluginId, path);
	}

	public static URL getURL() {
		return FileLocator.find(MainPlugin.getDefault().getBundle(), new org.eclipse.core.runtime.Path("/"), null);
	}

	public static void logError(String message, Throwable t) {
		getDefault().getLog().log(new Status(IStatus.ERROR, pluginId, message, t));
	}
}
