package umletplugin.utils;

import com.baselet.control.Main;
import com.baselet.diagram.Controller;
import com.baselet.diagram.DrawPanel;

/**
 * Convenience methods to be used by handlers in their @CanExecute method.
 * 
 * @author Lisi Bluemelhuber
 *
 */
public class CanExecuteHelper {

	private static Controller controller = Main.getInstance().getDiagramHandler().getController();
	private static DrawPanel drawPanel = Main.getInstance().getDiagramHandler().getDrawPanel();
	
	public static boolean diagramEmpty() {
		return drawPanel.getAllEntities().isEmpty();
	}
	
	public static boolean undoable() {
		return controller.isUndoable();
	}
	
	public static boolean redoable() {
		return controller.isRedoable();
	}
	
	public static boolean empty() {
		return controller.isEmpty();
	}
}
