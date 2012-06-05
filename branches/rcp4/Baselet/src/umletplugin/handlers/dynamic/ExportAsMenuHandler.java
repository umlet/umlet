 
package umletplugin.handlers.dynamic;

import java.io.IOException;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import com.baselet.control.Constants;

/**
 * This handler fills the "export as"-menu dynamically at runtime.
 * 
 * @author Lisi Bluemelhuber
 */
public class ExportAsMenuHandler extends DynamicMenuHandler {
	
	private List<String> menuItemLabels = Constants.exportFormatList;
	private String menuElementId = "umletplugin.exportas.menu";
	private String handlingClassURI = "bundleclass://UmletPlugin/umletplugin.handlers.dynamic.ExportAsHandler";
	
	@PostConstruct
	public void init() {
		super.init(menuItemLabels, menuElementId, handlingClassURI);
	}	
	
	@PreDestroy
	public void cleanup() throws IOException {
		super.cleanup(menuElementId);
	}
}