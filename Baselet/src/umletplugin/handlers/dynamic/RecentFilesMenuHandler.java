 
package umletplugin.handlers.dynamic;

import java.io.IOException;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import com.baselet.control.Constants;

/**
 * This handler fills the "recent files"-menu dynamically at runtime.
 * 
 * @author Lisi Bluemelhuber
 */
public class RecentFilesMenuHandler extends DynamicMenuHandler {
	
	private List<String> menuItemLabels = Constants.recentlyUsedFilesList.getList();
	private String menuElementId = "umletplugin.recentfiles.menu";
	private String handlingClassURI = "bundleclass://UmletPlugin/umletplugin.handlers.dynamic.RecentFilesHandler";
	
	
	@PostConstruct
	public void init() {
		super.init(menuItemLabels, menuElementId, handlingClassURI);	
	}
	
	@PreDestroy
	public void cleanup() throws IOException {
		super.cleanup(menuElementId);
	}
}