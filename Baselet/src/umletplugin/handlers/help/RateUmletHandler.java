 
package umletplugin.handlers.help;

import org.eclipse.e4.core.di.annotations.Execute;

import umletplugin.utils.BrowserLauncher;

import com.baselet.control.Constants.Program;
import com.baselet.control.Constants.ProgramName;

public class RateUmletHandler {
	
	@Execute
	public void execute() {
		if (Program.PROGRAM_NAME == ProgramName.UMLET) BrowserLauncher.openURL("http://marketplace.eclipse.org/content/umlet-uml-tool-fast-uml-diagrams");
		else if (Program.PROGRAM_NAME == ProgramName.PLOTLET) BrowserLauncher.openURL("http://marketplace.eclipse.org/content/plotlet");
	}	
}