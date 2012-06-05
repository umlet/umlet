package umletplugin.handlers.dynamic;

import org.apache.log4j.Logger;
import org.eclipse.e4.core.di.annotations.Execute;
import org.eclipse.e4.ui.model.application.ui.menu.MMenuItem;

import com.baselet.control.Utils;

public class NewFromTemplateHandler {

	private final static Logger log = Logger.getLogger(Utils.getClassName());
	
	@Execute
	public void execute(MMenuItem item) {
		log.debug("execute called");
		// TODO implement this
	}
}
