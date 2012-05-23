 
package umletplugin.handlers.file;

import org.eclipse.e4.core.di.annotations.Execute;

import com.baselet.gui.MenuFactory;
import com.baselet.gui.eclipse.MenuFactoryEclipse;

public class SaveAsHandler {
	@Execute
	public void execute() {
		MenuFactoryEclipse.getInstance().doAction(MenuFactory.SAVE, null);
	}	
}