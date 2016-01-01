package com.baselet.element.old.custom;

import java.util.Iterator;
import java.util.Timer;
import java.util.TimerTask;

import com.baselet.control.HandlerElementMap;
import com.baselet.control.Main;
import com.baselet.control.basics.geom.Rectangle;
import com.baselet.control.constants.Constants;
import com.baselet.control.enums.Program;
import com.baselet.custom.CustomCodeSyntaxPane;
import com.baselet.custom.CustomElementPanel;
import com.baselet.diagram.CurrentDiagram;
import com.baselet.diagram.CustomPreviewHandler;
import com.baselet.diagram.DiagramHandler;
import com.baselet.diagram.DrawPanel;
import com.baselet.element.ElementFactorySwing;
import com.baselet.element.interfaces.GridElement;
import com.baselet.gui.CurrentGui;

public class CustomElementHandler {

	private final Timer timer;
	private final CustomCodeSyntaxPane codepane;
	private final CustomPreviewHandler preview;
	private GridElement editedEntity;
	private GridElement originalElement;
	private TimerTask compiletask;
	private boolean changed;
	private final ErrorHandler errorhandler;
	private boolean compilation_running;
	private final CustomElementPanel panel;
	boolean keypressed;
	private String old_text;

	public CustomElementHandler() {
		codepane = new CustomCodeSyntaxPane();
		errorhandler = new ErrorHandler(codepane);
		codepane.getTextComponent().addMouseMotionListener(errorhandler);
		preview = new CustomPreviewHandler();
		timer = new Timer("customElementTimer", true);
		changed = false;
		compilation_running = false;
		old_text = null;
		panel = new CustomElementPanel(this);
	}

	public CustomElementPanel getPanel() {
		return panel;
	}

	public void newEntity() {
		this.newEntity("Default");
	}

	public void newEntity(String template) {
		preview.closePreview();
		originalElement = null;
		editedEntity = CustomElementCompiler.getInstance().genEntityFromTemplate(template, errorhandler);
		if (editedEntity instanceof CustomElement) {
			codepane.setCode(((CustomElement) editedEntity).getCode());
		}
		else {
			codepane.setCode("");
		}
		editedEntity.setPanelAttributes("// Modify the text below and" +
										Constants.NEWLINE +
										"// observe the element preview." +
										Constants.NEWLINE + Constants.NEWLINE +
										"Hello, World! " +
										Constants.NEWLINE +
										"Enjoy " + Program.getInstance().getProgramName() + "!");
		editedEntity.setRectangle(new Rectangle(20, 20, 200, 200));
		updatePreview(editedEntity);
		getPreviewHandler().getDrawPanel().getSelector().select(editedEntity);
		setChanged(false);
		start();
	}

	public void editEntity(CustomElement e) {
		preview.closePreview();
		originalElement = e;
		editedEntity = e.cloneFromMe();
		editedEntity.setLocation(20, 20);
		codepane.setCode(e.getCode());
		updatePreview(editedEntity);
		getPreviewHandler().getDrawPanel().getSelector().select(editedEntity);
		setChanged(false);

		start();
	}

	public void saveEntity() {
		GridElement e = CustomElementCompiler.getInstance().genEntity(codepane.getText(), errorhandler);
		editedEntity = e;
		updatePreview(e); // update preview panel to set the entities bounds...
		updateElement(e);
		setChanged(false);
	}

	public boolean closeEntity() {
		if (changed) {
			setChanged(false);
		}
		stop();
		preview.closePreview();
		preview.getDrawPanel().getSelector().deselectAll();

		// clear controller before editing new custom element
		CurrentDiagram.getInstance().getDiagramHandler().getController().clear();

		DrawPanel dia = CurrentGui.getInstance().getGui().getCurrentDiagram();
		if (dia != null) {
			dia.getSelector().updateSelectorInformation();
		}
		else {
			Main.getInstance().setPropertyPanelToGridElement(null);
		}
		return true;
	}

	public CustomPreviewHandler getPreviewHandler() {
		return preview;
	}

	public CustomCodeSyntaxPane getCodePane() {
		return codepane;
	}

	private void updatePreview(GridElement e) {
		if (e != null) {
			Iterator<GridElement> iter = preview.getDrawPanel().getGridElements().iterator();
			if (iter.hasNext()) {
				GridElement element = iter.next();
				e.setRectangle(element.getRectangle());
				e.setPanelAttributes(element.getPanelAttributes());
				preview.getDrawPanel().removeElement(element);
			}

			preview.setHandlerAndInitListeners(e);
			preview.getDrawPanel().addElement(e);
			e.repaint();
		}
	}

	// starts the task
	private void start() {
		compiletask = new CustomElementCompileTask(this);
		timer.schedule(compiletask, Constants.CUSTOM_ELEMENT_COMPILE_INTERVAL,
				Constants.CUSTOM_ELEMENT_COMPILE_INTERVAL);
	}

	// stops the task
	private void stop() {
		if (compiletask != null) {
			compiletask.cancel();
		}
	}

	// runs compilation every 1 seconds and updates gui/errors...
	protected void runCompilation() {
		if (!compilation_running && !keypressed) // prevent 2 compilations to run at the same time (if compilation takes more then 1sec)
		{
			compilation_running = true;
			String txt = codepane.getText();
			if (!txt.equals(old_text)) {
				setChanged(true);
				errorhandler.clearErrors();
				old_text = txt;
				editedEntity = CustomElementCompiler.getInstance().genEntity(txt, errorhandler);
				panel.setCustomElementSaveable(true);
				updatePreview(editedEntity);
			}
			compilation_running = false;
		}
		keypressed = false;
	}

	private void setChanged(boolean changed) {
		this.changed = changed;
		CurrentGui.getInstance().getGui().setCustomElementChanged(this, changed);
	}

	// reloads the element on all open panels and adds it to the custom element panel if not already there.
	private void updateElement(GridElement element) {

		// if a new element has been created add it to current diagram
		if (originalElement == null) {
			DiagramHandler current = null;
			DrawPanel c = CurrentGui.getInstance().getGui().getCurrentDiagram();
			if (c == null) {
				Main.getInstance().doNew();
				current = CurrentGui.getInstance().getGui().getCurrentDiagram().getHandler();
			}
			else {
				current = c.getHandler();
			}

			// set location for element
			int x = 10, y = 10;
			for (GridElement e : current.getDrawPanel().getGridElements()) {
				if (e.getRectangle().y + e.getRectangle().height + 10 > y) {
					y = e.getRectangle().y + e.getRectangle().height + 10;
				}
			}

			Rectangle bounds = new Rectangle(x, y, element.getRectangle().width, element.getRectangle().height);
			addElementToDiagram(element, current, true, bounds, element.getPanelAttributes());
		}
		else { // replace edited element (and ONLY edited element)
			HandlerElementMap.getHandlerForElement(originalElement).getDrawPanel().removeElement(originalElement);
			addElementToDiagram(element, HandlerElementMap.getHandlerForElement(originalElement), true,
					originalElement.getRectangle(), originalElement.getPanelAttributes());
		}
	}

	private void addElementToDiagram(GridElement e, DiagramHandler d, boolean setchanged, Rectangle bounds, String state) {

		// TODO bug if custom elements get inserted in zoomed drawpanel
		// the zoom to 100% bugfix works for inserting new elements but editing old elements with zoom will not work anymore
		// We must zoom to the defaultGridsize before execution
		// int oldZoom = d.getGridSize();
		// d.setGridAndZoom(Constants.DEFAULTGRIDSIZE, false);

		GridElement e2 = ElementFactorySwing.createCopy(e, d);
		e2.setPanelAttributes(state);
		e2.setRectangle(bounds);
		d.getDrawPanel().addElement(e2);
		if (setchanged) {
			d.setChanged(true);
		}

		// And zoom back to the oldGridsize after execution
		// d.setGridAndZoom(oldZoom, false);
	}
}
