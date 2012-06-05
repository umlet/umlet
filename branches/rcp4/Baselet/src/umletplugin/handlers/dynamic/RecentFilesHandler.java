package umletplugin.handlers.dynamic;

import java.lang.reflect.InvocationTargetException;

import javax.inject.Named;

import org.apache.log4j.Logger;
import org.eclipse.e4.core.contexts.IEclipseContext;
import org.eclipse.e4.core.di.annotations.Execute;
import org.eclipse.e4.core.di.annotations.Optional;
import org.eclipse.e4.ui.model.application.MApplication;
import org.eclipse.e4.ui.model.application.ui.menu.MMenuItem;
import org.eclipse.e4.ui.services.IServiceConstants;
import org.eclipse.e4.ui.workbench.modeling.EModelService;
import org.eclipse.e4.ui.workbench.modeling.EPartService;
import org.eclipse.swt.widgets.Shell;

import com.baselet.control.Utils;

import umletplugin.handlers.file.OpenHandler;

public class RecentFilesHandler {

	private final static Logger log = Logger.getLogger(Utils.getClassName());
	
	@Execute
	public void execute(MApplication application, MMenuItem item, OpenHandler handler, EPartService service, EModelService modelService, 
			@Named(IServiceConstants.ACTIVE_SHELL) Shell shell, IEclipseContext context, @Optional String filename) 
					throws InvocationTargetException, InterruptedException {
		log.debug("execute called");
		handler.execute(service, application, modelService, shell, context, item.getLabel());
	}
}
