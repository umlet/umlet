package umletplugin.handlers.extra;

import javax.inject.Inject;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;

import com.baselet.gui.standalone.SearchListener;

public class SearchHandler {

	@Inject
	public SearchHandler(Composite parent) {
		Label label = new Label(parent, SWT.EMBEDDED);
		label.setText("Search: ");
		Text text = new Text(parent, SWT.LEFT);
		
		SearchListener listener = new SearchListener();
		// TODO rework to fit swt
//		text.addMouseMotionListener(listener);
//		text.addKeyListener(listener);
//		text.addFocusListener(new TextPaneFocusListener());
	}
}
