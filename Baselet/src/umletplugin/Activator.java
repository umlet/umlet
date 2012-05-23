package umletplugin;

import java.net.URL;

import org.eclipse.core.runtime.FileLocator;
import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;

import com.baselet.control.Main;
import com.baselet.control.Constants.Program;
import com.baselet.control.Constants.RuntimeType;
import com.baselet.gui.eclipse.EclipseGUI;
import com.baselet.plugin.MainPlugin;

public class Activator implements BundleActivator {

	private static BundleContext context;

	static BundleContext getContext() {
		return context;
	}

	/*
	 * (non-Javadoc)
	 * @see org.osgi.framework.BundleActivator#start(org.osgi.framework.BundleContext)
	 */
	public void start(BundleContext bundleContext) throws Exception {
		Activator.context = bundleContext;
		Program.RUNTIME_TYPE = RuntimeType.ECLIPSE_PLUGIN;
		try {
			Main.getInstance().initOverallSettings();
			Main.getInstance().init(new EclipseGUI(Main.getInstance()));
		} catch (Exception e) {
//			log.error("Initialization or uncaught outer Exception", e);
		}
	}

	/*
	 * (non-Javadoc)
	 * @see org.osgi.framework.BundleActivator#stop(org.osgi.framework.BundleContext)
	 */
	public void stop(BundleContext bundleContext) throws Exception {
		Activator.context = null;
		Main.getInstance().getGUI().closeWindow();
	}

	public static URL getURL() {
		return FileLocator.find(context.getBundle(), new org.eclipse.core.runtime.Path("/"), null);
	}

}
