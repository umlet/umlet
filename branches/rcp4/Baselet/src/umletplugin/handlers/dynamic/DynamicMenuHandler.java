package umletplugin.handlers.dynamic;

import java.io.IOException;
import java.util.List;

import javax.inject.Inject;

import org.apache.log4j.Logger;
import org.eclipse.e4.ui.model.application.MApplication;
import org.eclipse.emf.ecore.EObject;

import com.baselet.control.Utils;

import umletplugin.utils.DynamicMenuHelper;

/**
 * Extend this class with a handler to construct a dynamically fillable menu.
 * In the extending class, init() must be preceded by @PostConstruct,
 * cleanup() must be preceded by @PreDestroy
 * 
 * @author Lisi Bluemelhuber
 *
 */
public class DynamicMenuHandler {
	
	private final static Logger log = Logger.getLogger(Utils.getClassName());
	
	@Inject
	protected MApplication application;
	
	public void init(List<String> menuItemLabels, String menuElementId, String handlingClassURI) {
		log.debug("initializing menu \""+menuElementId+"\"");
		for (final String filename : menuItemLabels) {
			DynamicMenuHelper.addMenuItemToMenu(application, menuElementId, 
					filename, handlingClassURI);
		}	
	}
	
	public void cleanup(String menuElementId) throws IOException {
		log.debug("clearing menu \""+menuElementId+"\"");
		DynamicMenuHelper.clearMenu(application, menuElementId);
		// explicit save needed to make sure changed model is acknowledged
		// see http://www.eclipse.org/forums/index.php/t/358388/
		((EObject)application).eResource().save(null);
	}
}
