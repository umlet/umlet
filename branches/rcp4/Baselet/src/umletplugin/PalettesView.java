package umletplugin;

import javax.inject.Inject;
import javax.inject.Named;

import org.apache.log4j.Logger;
import org.eclipse.e4.ui.di.Focus;
import org.eclipse.e4.ui.services.IServiceConstants;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Shell;

import com.baselet.control.Main;
import com.baselet.control.Utils;
import com.baselet.gui.listener.PaletteComboBoxListener;

public class PalettesView {

	private final static Logger log = Logger.getLogger(Utils.getClassName());
	
	@Inject
	public PalettesView(Composite parent, @Named(IServiceConstants.ACTIVE_SHELL) Shell shell) {
		log.debug("Call PalettesView");
		
		Composite composite = new Composite(parent, SWT.EMBEDDED);
		FillLayout layout = new FillLayout(SWT.VERTICAL);
		composite.setLayout(layout);
		
		Combo paletteList = new Combo(composite, SWT.READ_ONLY);	
		paletteList.setItems(Main.getInstance().getPaletteNames().toArray(new String[]{}));
		paletteList.setVisibleItemCount(15);
		paletteList.select(0);
		
		paletteList.setFocus();
		PaletteComboBoxListener pl = new PaletteComboBoxListener();
		paletteList.addSelectionListener(pl);
		paletteList.addMouseWheelListener(pl);
		
		composite.layout();
	}
	
	@Focus
	public void focus() {
		log.debug("Called focus()");
	}
}
