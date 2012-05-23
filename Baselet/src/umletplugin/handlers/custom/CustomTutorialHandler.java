 
package umletplugin.handlers.custom;

import org.eclipse.e4.core.di.annotations.Execute;

import com.baselet.gui.MenuFactory;
import com.baselet.gui.eclipse.MenuFactoryEclipse;

public class CustomTutorialHandler {
	@Execute
	public void execute() {
		MenuFactoryEclipse.getInstance().doAction(MenuFactory.CUSTOM_ELEMENTS_TUTORIAL, null);
	}
}