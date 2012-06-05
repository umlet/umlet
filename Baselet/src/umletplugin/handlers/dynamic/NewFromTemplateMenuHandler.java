 
package umletplugin.handlers.dynamic;

import java.io.IOException;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import com.baselet.control.Main;

public class NewFromTemplateMenuHandler extends DynamicMenuHandler {
	
	private List<String> menuItemLabels = Main.getInstance().getTemplateNames();
	private String menuElementId = "umletplugin.newfromtemplate.menu";
	private String handlingClassURI = "bundleclass://UmletPlugin/umletplugin.handlers.dynamic.NewFromTemplateHandler";
	
	@PostConstruct
	public void init() {
		super.init(menuItemLabels, menuElementId, handlingClassURI);
	}
	
	@PreDestroy
	public void cleanup() throws IOException {
		super.cleanup(menuElementId);
	}
}