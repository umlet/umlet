 
package umletplugin.handlers.edit;

import org.eclipse.e4.core.di.annotations.CanExecute;
import org.eclipse.e4.core.di.annotations.Execute;

import umletplugin.utils.CanExecuteHelper;

import com.baselet.gui.MenuFactory;
import com.baselet.gui.eclipse.MenuFactoryEclipse;

public class RedoHandler {

	@CanExecute
	public boolean enabled() {
		return CanExecuteHelper.redoable();
	}
	
	@Execute
	public void execute() {
		MenuFactoryEclipse.getInstance().doAction(MenuFactory.REDO, null);
	}	
}