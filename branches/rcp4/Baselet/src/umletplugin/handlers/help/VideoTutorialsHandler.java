package umletplugin.handlers.help;

import org.eclipse.e4.core.di.annotations.Execute;

import umletplugin.utils.BrowserLauncher;

public class VideoTutorialsHandler {

	@Execute
	public void execute() {
		BrowserLauncher.openURL("http://www.youtube.com/watch?v=3UHZedDtr28");
	}
}
