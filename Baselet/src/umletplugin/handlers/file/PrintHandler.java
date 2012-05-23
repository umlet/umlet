 
package umletplugin.handlers.file;

import org.eclipse.e4.core.di.annotations.CanExecute;
import org.eclipse.e4.core.di.annotations.Execute;

import com.baselet.control.Main;
import com.baselet.diagram.DiagramHandler;
import com.baselet.gui.MenuFactory;
import com.baselet.gui.eclipse.MenuFactoryEclipse;

public class PrintHandler {
	
	@CanExecute
	public boolean isSelectable() {
		DiagramHandler diagramHandler = Main.getInstance().getGUI().getCurrentDiagram().getHandler();
		if (diagramHandler.getDrawPanel().getAllEntities().isEmpty()) {
			return false;
		}
		return true;
	}
	
	@Execute
	public void execute() {
		MenuFactoryEclipse.getInstance().doAction(MenuFactory.PRINT, null);
	}
}