package umletplugin.handlers.dynamic;

import org.apache.log4j.Logger;
import org.eclipse.e4.core.di.annotations.Execute;
import org.eclipse.e4.ui.model.application.ui.menu.MMenuItem;

import com.baselet.control.Utils;
import com.baselet.gui.MenuFactory;
import com.baselet.gui.eclipse.MenuFactoryEclipse;

public class ExportAsHandler {

	private final static Logger log = Logger.getLogger(Utils.getClassName());
	
	@Execute
	public void execute(MMenuItem item) {
		log.debug("execute called");
		// extension string must be lower case to successfully select jpg in the save dialog
		MenuFactoryEclipse.getInstance().doAction(MenuFactory.EXPORT_AS, item.getLabel().toLowerCase());
	}
}
