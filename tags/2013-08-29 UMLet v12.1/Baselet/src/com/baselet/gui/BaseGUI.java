package com.baselet.gui;

import java.awt.Color;
import java.awt.Cursor;

import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import javax.swing.UIManager;
import javax.swing.plaf.InsetsUIResource;

import org.apache.log4j.Logger;

import com.baselet.control.Constants;
import com.baselet.control.Constants.Program;
import com.baselet.control.Constants.ProgramName;
import com.baselet.control.Main;
import com.baselet.diagram.CustomPreviewHandler;
import com.baselet.diagram.DiagramHandler;
import com.baselet.diagram.DrawPanel;
import com.baselet.element.GridElement;
import com.baselet.element.Group;
import com.umlet.custom.CustomElement;
import com.umlet.custom.CustomElementHandler;


public abstract class BaseGUI {
	
	private static final Logger log = Logger.getLogger(BaseGUI.class);

	protected Main main;
	protected int selected_elements;
	protected boolean paletteEdited = false;

	public BaseGUI(Main main) {
		this.main = main;
	}

	public final void initGUI() {
		try {
			UIManager.setLookAndFeel(Constants.ui_manager);
		} catch (Exception e) { // If the LookAndFeel cannot be set, it gets logged (without stacktrace) and the default style is used
			log.error(e.getMessage());
		}

		this.initGUIParameters();
//		this.setLayout(new BorderLayout());

		this.init();
//		this.requestFocus();
	}

	public abstract void focusPropertyPane();

	public JPopupMenu getContextMenu(GridElement entity) {
		MenuFactorySwing menuFactory = MenuFactorySwing.getInstance();

		JPopupMenu contextMenu = new JPopupMenu();
		if (entity instanceof CustomElement) {
			contextMenu.add(menuFactory.createEditSelected());
		}

		if (!(Main.getHandlerForElement(entity) instanceof CustomPreviewHandler)) {
			contextMenu.add(menuFactory.createDelete());
		}
		JMenuItem group = menuFactory.createGroup();
		contextMenu.add(group);
		if (this.selected_elements < 2) group.setEnabled(false);

		JMenuItem ungroup = menuFactory.createUngroup();
		contextMenu.add(ungroup);
		if (!(entity instanceof Group)) ungroup.setEnabled(false);

		if (Program.PROGRAM_NAME == ProgramName.UMLET) {
			contextMenu.add(menuFactory.createSetColor(true));
			contextMenu.add(menuFactory.createSetColor(false));
		}

		// insert alignment menu
		JMenu alignmentMenu = menuFactory.createAlign();
		alignmentMenu.setEnabled(this.selected_elements > 1); // only enable when at least 2 elements are selected
		contextMenu.add(alignmentMenu);

		return contextMenu;
	}

	public void elementsSelected(int count) {
		this.selected_elements = count;
	}

	protected void initGUIParameters() {
		UIManager.put("TabbedPane.selected", Color.white);
		UIManager.put("TabbedPane.tabInsets", new InsetsUIResource(0, 4, 1, 0));
		UIManager.put("TabbedPane.contentBorderInsets", new InsetsUIResource(0, 0, 0, 0));
	}

	public void setPaletteEdited(boolean isEdited) {
		this.paletteEdited = isEdited;
	}

	public boolean getPaletteEdited() {
		return this.paletteEdited;
	}

	public abstract CustomElementHandler getCurrentCustomHandler();

	public abstract void setCustomPanelEnabled(boolean enable);

	public abstract void setMailPanelEnabled(boolean enable);

	public abstract boolean isMailPanelVisible();

	public abstract void updateDiagramName(DiagramHandler diagram, String name);

	public abstract void setDiagramChanged(DiagramHandler diagram, boolean changed);

	public abstract void setCustomElementChanged(CustomElementHandler handler, boolean changed);

	public abstract void closeWindow();

	protected abstract void init();

	public abstract String getSelectedPalette();

	public void showPalette(String palette) {
		Constants.lastUsedPalette = palette;
	}

	public abstract void open(DiagramHandler diagram);

	public abstract void jumpTo(DiagramHandler diagram);

	public abstract void close(DiagramHandler diagram);

	public abstract DrawPanel getCurrentDiagram();

	public abstract void enablePasteMenuEntry();

	public abstract void setUngroupEnabled(boolean enabled);

	public abstract void setCustomElementSelected(boolean selected);

	public abstract void diagramSelected(DiagramHandler handler);

	public void enableSearch(@SuppressWarnings("unused") boolean enable) {
		/* do nothing*/
		}

	public abstract int getMainSplitPosition();

	public abstract int getMailSplitPosition();

	public abstract int getRightSplitPosition();

	public abstract OwnSyntaxPane getPropertyPane();

	public abstract void setValueOfZoomDisplay(int i);

	public void afterSaving() {
		/* do nothing*/
	}

	public abstract void setCursor(Cursor cursor);

	public abstract void requestFocus();

	public void updateGrayedOutMenuItems(@SuppressWarnings("unused") DiagramHandler handler) {
		/* do nothing*/
	}
}
