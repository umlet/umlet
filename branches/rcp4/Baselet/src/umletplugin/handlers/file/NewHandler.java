package umletplugin.handlers.file;

import org.eclipse.e4.core.contexts.IEclipseContext;
import org.eclipse.e4.core.di.annotations.Execute;
import org.eclipse.e4.ui.model.application.MApplication;
import org.eclipse.e4.ui.model.application.ui.basic.MBasicFactory;
import org.eclipse.e4.ui.model.application.ui.basic.MPart;
import org.eclipse.e4.ui.model.application.ui.basic.MPartStack;
import org.eclipse.e4.ui.workbench.modeling.EModelService;
import org.eclipse.e4.ui.workbench.modeling.EPartService;
import org.eclipse.e4.ui.workbench.modeling.EPartService.PartState;

public class NewHandler {

	@Execute
	public void execute(EPartService service, MApplication application, EModelService modelService, IEclipseContext context) {		
		MPartStack stack = (MPartStack) modelService.find("umletplugin.newpartstack", application);
        
        MPart part = MBasicFactory.INSTANCE.createPart();
        part.setCloseable(true);
        part.setLabel("new");
        part.setContributionURI("bundleclass://UmletPlugin/umletplugin.NewEditor");
        context.getParent().remove("file");
        
        stack.getChildren().add(part);
        service.showPart(part, PartState.ACTIVATE);
	}
}
