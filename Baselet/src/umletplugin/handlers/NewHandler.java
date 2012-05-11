package umletplugin.handlers;

import org.apache.log4j.Logger;
import org.eclipse.e4.core.di.annotations.Execute;
import org.eclipse.e4.ui.model.application.MApplication;
import org.eclipse.e4.ui.model.application.ui.basic.MBasicFactory;
import org.eclipse.e4.ui.model.application.ui.basic.MPart;
import org.eclipse.e4.ui.model.application.ui.basic.MPartStack;
import org.eclipse.e4.ui.workbench.modeling.EModelService;
import org.eclipse.e4.ui.workbench.modeling.EPartService;
import org.eclipse.e4.ui.workbench.modeling.EPartService.PartState;

import com.baselet.control.Utils;

public class NewHandler {
	
	private final static Logger log = Logger.getLogger(Utils.getClassName());

	@Execute
	public void execute(EPartService service, MApplication application, EModelService modelService) {
		log.debug("Called execute()");

		MPartStack stack = (MPartStack) modelService.find("umletplugin.newPartStack", application);
		
		MPart part = MBasicFactory.INSTANCE.createPart();
		part.setCloseable(true);
		part.setLabel("new");
		stack.getChildren().add(part);
		service.showPart(part, PartState.ACTIVATE);
	}
}
