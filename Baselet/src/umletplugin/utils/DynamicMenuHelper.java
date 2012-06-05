package umletplugin.utils;

import org.eclipse.e4.ui.model.application.MApplication;
import org.eclipse.e4.ui.model.application.ui.menu.MDirectMenuItem;
import org.eclipse.e4.ui.model.application.ui.menu.MMenu;
import org.eclipse.e4.ui.model.application.ui.menu.MMenuElement;
import org.eclipse.e4.ui.model.application.ui.menu.MMenuFactory;

/**
 * Convenience methods for adding/removing menu items to menus. 
 * 
 * @author Lisi Bluemelhuber
 *
 */
public class DynamicMenuHelper {

	public static void addMenuItemToMenu(MApplication application, String menuElementId, String menuItemLabel, String handlingClassURI) {
		MMenu target = (MMenu) findMenuElement(application, menuElementId);
		MDirectMenuItem newItem = MMenuFactory.INSTANCE.createDirectMenuItem();
		newItem.setContributionURI(handlingClassURI);
		newItem.setLabel(menuItemLabel);
		target.getChildren().add(newItem);
	}
	
	public static void clearMenu(MApplication application, String menuElementId) {
		MMenu target = (MMenu) findMenuElement(application, menuElementId);
		target.getChildren().clear();
	}
	
	private static MMenuElement findMenuElement(MApplication application, String menuElementId) {
		MMenu mainMenu = application.getChildren().get(0).getMainMenu();
		return findMenuElement(mainMenu, menuElementId);
	}

	private static MMenuElement findMenuElement(MMenu mainMenu, String menuElementId) {
		for (MMenuElement child : mainMenu.getChildren()) {
			if (child.getElementId() != null && child.getElementId().equals(menuElementId))
				return child;
			if (child instanceof MMenu) {
				MMenuElement found = findMenuElement((MMenu)child, menuElementId);
				if (found != null)
					return found;
			}
		}
		return null;
	}
}
