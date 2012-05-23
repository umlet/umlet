package umletplugin.handlers.file;

import java.io.File;
import java.lang.reflect.InvocationTargetException;
import java.util.List;
import java.util.Map;

import javax.inject.Named;

import org.eclipse.e4.core.contexts.IEclipseContext;
import org.eclipse.e4.core.di.annotations.Execute;
import org.eclipse.e4.ui.model.application.MApplication;
import org.eclipse.e4.ui.model.application.commands.MBindingContext;
import org.eclipse.e4.ui.model.application.ui.basic.MBasicFactory;
import org.eclipse.e4.ui.model.application.ui.basic.MPart;
import org.eclipse.e4.ui.model.application.ui.basic.MPartStack;
import org.eclipse.e4.ui.services.IServiceConstants;
import org.eclipse.e4.ui.workbench.modeling.EModelService;
import org.eclipse.e4.ui.workbench.modeling.EPartService;
import org.eclipse.e4.ui.workbench.modeling.EPartService.PartState;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Shell;

import umletplugin.NewEditor;

import com.baselet.diagram.DiagramHandler;

public class OpenHandler {

	@Execute
	public void execute(EPartService service, MApplication application, EModelService modelService, 
			@Named(IServiceConstants.ACTIVE_SHELL) Shell shell, IEclipseContext context) throws InvocationTargetException, InterruptedException {
		FileDialog dialog = new FileDialog(shell);
		String filename = dialog.open();
		File file = new File(filename);
		DiagramHandler diagram = new DiagramHandler(file);
		
		MPartStack stack = (MPartStack) modelService.find("umletplugin.newpartstack", application);
        
        MPart part = MBasicFactory.INSTANCE.createPart();
        part.setCloseable(true);
        part.setLabel(file.getName());
        part.setContributionURI("bundleclass://UmletPlugin/umletplugin.NewEditor");
        context.getParent().set("file", file);
      
        System.out.println("Part: "+part);
        //((NewEditor)part.getObject()).setHandler(diagram);
        
        stack.getChildren().add(part);
        service.showPart(part, PartState.ACTIVATE);
		
//		MenuFactoryEclipse.getInstance().doAction(MenuFactory.OPEN, null);
	}
}
