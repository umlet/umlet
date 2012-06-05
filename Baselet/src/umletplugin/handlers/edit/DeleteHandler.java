 
package umletplugin.handlers.edit;

import org.eclipse.e4.core.di.annotations.CanExecute;
import org.eclipse.e4.core.di.annotations.Execute;

import umletplugin.utils.CanExecuteHelper;

import com.baselet.gui.MenuFactory;
import com.baselet.gui.eclipse.MenuFactoryEclipse;

public class DeleteHandler {
	
	@CanExecute
	public boolean enabled() {
		return !CanExecuteHelper.empty();
	}
	
	@Execute
	public void execute() {
		MenuFactoryEclipse.getInstance().doAction(MenuFactory.DELETE, null);
	}
}