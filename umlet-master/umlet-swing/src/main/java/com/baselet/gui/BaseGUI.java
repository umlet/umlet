package com.baselet.gui;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Frame;
import java.util.Collection;

import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import javax.swing.UIManager;
import javax.swing.plaf.InsetsUIResource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.baselet.control.CanCloseProgram;
import com.baselet.control.HandlerElementMap;
import com.baselet.control.config.Config;
import com.baselet.diagram.CustomPreviewHandler;
import com.baselet.diagram.DiagramHandler;
import com.baselet.diagram.DrawPanel;
import com.baselet.element.interfaces.GridElement;
import com.baselet.element.old.custom.CustomElement;
import com.baselet.element.old.custom.CustomElementHandler;
import com.baselet.gui.menu.MenuFactorySwing;
import com.baselet.gui.pane.OwnSyntaxPane;

public abstract class BaseGUI {

	private static final Logger log = LoggerFactory.getLogger(BaseGUI.class);

	protected CanCloseProgram main;
	protected Collection<GridElement> selected_elements;
	protected boolean paletteEdited = false;

	public BaseGUI(CanCloseProgram main) {
		this.main = main;
	}

	public final void initGUI() {
		try {
			UIManager.setLookAndFeel(Config.getInstance().getUiManager());
		} catch (Exception e) { // If the LookAndFeel cannot be set, it gets logged (without stacktrace) and the default style is used
			log.error(e.getMessage());
		}

		initGUIParameters();
		// this.setLayout(new BorderLayout());

		init();
		// this.requestFocus();
	}

	public abstract void focusPropertyPane();

	public JPopupMenu getContextMenu(GridElement e) {
		MenuFactorySwing menuFactory = MenuFactorySwing.getInstance();

		JPopupMenu contextMenu = new JPopupMenu();
		if (e instanceof CustomElement) {
			contextMenu.add(menuFactory.createEditSelected());
		}

		if (!(HandlerElementMap.getHandlerForElement(e) instanceof CustomPreviewHandler)) {
			contextMenu.add(menuFactory.createDelete());
			contextMenu.add(menuFactory.createCopy());
			contextMenu.add(menuFactory.createCut());
		}
		JMenuItem group = menuFactory.createGroup();
		contextMenu.add(group);
		if (selected_elements.size() < 2) {
			group.setEnabled(false);
		}

		JMenuItem ungroup = menuFactory.createUngroup();
		contextMenu.add(ungroup);
		if (e.getGroup() == null) {
			ungroup.setEnabled(false);
		}

		contextMenu.add(menuFactory.createSetColor(true));
		contextMenu.add(menuFactory.createSetColor(false));

		contextMenu.add(createAlignmentMenu(menuFactory));
		contextMenu.add(createLayerMenu(menuFactory));

		return contextMenu;
	}

	private JMenu createLayerMenu(MenuFactorySwing menuFactory) {
		JMenu layerMenu = menuFactory.createLayerUp();
		layerMenu.setEnabled(!selected_elements.isEmpty());
		return layerMenu;
	}

	private JMenu createAlignmentMenu(MenuFactorySwing menuFactory) {
		JMenu alignmentMenu = menuFactory.createAlign();
		alignmentMenu.setEnabled(selected_elements.size() > 1); // only enable when at least 2 elements are selected
		return alignmentMenu;
	}

	public void elementsSelected(Collection<GridElement> selectedElements) {
		selected_elements = selectedElements;
	}

	protected void initGUIParameters() {
		UIManager.put("TabbedPane.selected", Color.white);
		UIManager.put("TabbedPane.tabInsets", new InsetsUIResource(0, 4, 1, 0));
		UIManager.put("TabbedPane.contentBorderInsets", new InsetsUIResource(0, 0, 0, 0));
	}

	public void setPaletteEdited(boolean isEdited) {
		paletteEdited = isEdited;
	}

	public boolean getPaletteEdited() {
		return paletteEdited;
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
		Config.getInstance().setLastUsedPalette(palette);
	}

	public abstract void open(DiagramHandler diagram);

	public abstract void jumpTo(DiagramHandler diagram);

	public abstract void close(DiagramHandler diagram);

	public abstract DrawPanel getCurrentDiagram();

	public abstract void enablePasteMenuEntry();

	public abstract void setCustomElementSelected(boolean selected);

	public abstract void diagramSelected(DiagramHandler handler);

	public void enableSearch(@SuppressWarnings("unused") boolean enable) {
		/* do nothing */
	}

	public abstract int getMainSplitPosition();

	public abstract int getMailSplitPosition();

	public abstract int getRightSplitPosition();

	public abstract OwnSyntaxPane getPropertyPane();

	public abstract void setValueOfZoomDisplay(int i);

	public abstract boolean hasExtendedContextMenu();

	public abstract boolean saveWindowSizeInConfig();

	public void afterSaving() {
		/* do nothing */
	}

	public abstract void setCursor(Cursor cursor);

	public abstract void requestFocus();

	public void updateGrayedOutMenuItems(@SuppressWarnings("unused") DiagramHandler handler) {
		/* do nothing */
	}

	public abstract Frame getMainFrame();

	public void setLookAndFeel(String newui, JFrame optionframe) {
		/* do nothing */
	}

}
