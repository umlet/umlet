 
package umletplugin.handlers.file;

import org.eclipse.e4.core.di.annotations.CanExecute;
import org.eclipse.e4.core.di.annotations.Execute;

import umletplugin.utils.CanExecuteHelper;

import com.baselet.gui.MenuFactory;
import com.baselet.gui.eclipse.MenuFactoryEclipse;

public class MailToHandler {

	@CanExecute
	public boolean enabled() {
		return !CanExecuteHelper.diagramEmpty();
	}
	
	@Execute
	public void execute() {
		MenuFactoryEclipse.getInstance().doAction(MenuFactory.MAIL_TO, null);
	}	
}