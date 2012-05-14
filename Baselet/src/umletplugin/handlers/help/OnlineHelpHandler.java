 
package umletplugin.handlers.help;

import org.eclipse.e4.core.di.annotations.Execute;

import umletplugin.utils.BrowserLauncher;

import com.baselet.control.Constants.Program;

public class OnlineHelpHandler {
	
	@Execute
	public void execute() {
		BrowserLauncher.openURL(Program.WEBSITE + "/faq.htm");
	}		
}