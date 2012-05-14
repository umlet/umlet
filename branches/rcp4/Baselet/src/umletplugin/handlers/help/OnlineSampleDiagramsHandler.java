 
package umletplugin.handlers.help;

import org.eclipse.e4.core.di.annotations.Execute;

import com.baselet.control.Constants.Program;
import com.baselet.control.Constants.ProgramName;

import umletplugin.utils.BrowserLauncher;

public class OnlineSampleDiagramsHandler {
	
	@Execute
	public void execute() {
		if (Program.PROGRAM_NAME == ProgramName.UMLET) BrowserLauncher.openURL("http://www.itmeyer.at/umlet/uml2/");
		else if (Program.PROGRAM_NAME == ProgramName.PLOTLET) BrowserLauncher.openURL("http://www.itmeyer.at/umlet/uml2/");
	}	
}