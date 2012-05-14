package umletplugin.utils;

import org.apache.log4j.Logger;
import org.eclipse.swt.program.Program;

import com.baselet.control.Utils;

// TODO incorporate into com.baselet.control after more extensive testing
public class BrowserLauncher {
	
	private final static Logger log = Logger.getLogger(Utils.getClassName());

	public static void openURL(String url) {
		log.debug("Opening URL: "+url);
		
		Program.launch(url);
	}
}
