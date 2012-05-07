package com.umlet.custom;

import java.awt.Rectangle;
import java.util.Timer;
import java.util.TimerTask;
import java.util.Vector;

import com.baselet.control.Constants;
import com.baselet.control.Constants.Program;
import com.baselet.control.Main;
import com.baselet.diagram.CustomPreviewHandler;
import com.baselet.diagram.DiagramHandler;
import com.baselet.diagram.DrawPanel;
import com.baselet.element.GridElement;
import com.umlet.gui.CustomCodeSyntaxPane;
import com.umlet.gui.CustomElementPanel;


public class CustomElementHandler {

	private Timer timer;
	private CustomCodeSyntaxPane codepane;
	private CustomPreviewHandler preview;
	private GridElement editedEntity;
	private GridElement originalElement;
	private TimerTask compiletask;
	private boolean changed;
	private ErrorHandler errorhandler;
	private HelpHandler helphandler;
	private boolean compilation_running;
	private CustomElementPanel panel;
	boolean keypressed;
	private String old_text;

	public CustomElementHandler() {
		this.codepane = new CustomCodeSyntaxPane(null);
		this.errorhandler = new ErrorHandler(this.codepane);
		this.helphandler = new HelpHandler(this.codepane, this);
		this.codepane.addMouseMotionListener(this.errorhandler);
		this.codepane.addKeyListener(this.helphandler);
		this.preview = new CustomPreviewHandler();
		this.timer = new Timer(true);
		this.changed = false;
		this.compilation_running = false;
		this.old_text = null;
		this.panel = new CustomElementPanel(this);

		this.codepane.initJSyntaxPane();
		
		//for undo/redo 
		this.codepane.initCodePanelListener();
		
		//TODO CUSTOM ELMENTS REFACTORING
//		StyledDocument doc = codepane.getStyledDocument();
//		Style def = StyleContext.getDefaultStyleContext().getStyle(StyleContext.DEFAULT_STYLE);
//		doc.addStyle("default", def);
//		Style err = doc.addStyle("error", def);
//		StyleConstants.setForeground(err, Color.red);
//		StyleConstants.setBold(err, true);
//		StyleConstants.setItalic(err, true);
	}

	public CustomElementPanel getPanel() {
		return this.panel;
	}

	public void newEntity() {
		this.newEntity("Default");
	}

	public void newEntity(String template) {
		this.preview.closePreview();
		this.originalElement = null;
		this.editedEntity = CustomElementCompiler.getInstance().genEntityFromTemplate(template, this.errorhandler);
		if (editedEntity instanceof CustomElement) this.codepane.setText(((CustomElement) this.editedEntity).getCode());
		else {
			this.codepane.setText("");
		}
		this.editedEntity.setPanelAttributes("// Modify the text below and" +
				Constants.NEWLINE +
				"// observe the element preview." +
				Constants.NEWLINE + Constants.NEWLINE +
				"Hello, World! " +
				Constants.NEWLINE +
				"Enjoy " + Program.PROGRAM_NAME + "!");
		this.editedEntity.setBounds(20, 20, 200, 200);
		this.updatePreview(editedEntity);
		this.getPreviewHandler().getDrawPanel().getSelector().select(editedEntity);
		this.setChanged(false);
		this.start();
	}

	public void editEntity(CustomElement e) {
		this.preview.closePreview();
		this.originalElement = e;
		this.editedEntity = e.CloneFromMe();
		this.editedEntity.setLocation(20, 20);
		this.codepane.setText(e.getCode());
		this.updatePreview(this.editedEntity);
		this.getPreviewHandler().getDrawPanel().getSelector().select(editedEntity);
		this.setChanged(false);

		this.start();
	}

	public void saveEntity() {
		GridElement e = CustomElementCompiler.getInstance().genEntity(this.codepane.getText(), errorhandler);
		this.editedEntity = e;
		this.updatePreview(e); // update preview panel to set the entities bounds...
		this.updateElement(e);
		this.setChanged(false);
	}

	public boolean closeEntity() {
		if (this.changed) {
			/*
			 * int ch=JOptionPane.showOptionDialog(Main.getInstance().getGUI(),"Save changes?","CustomElement",JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.WARNING_MESSAGE,null, null, null);
			 * if (ch==JOptionPane.YES_OPTION)
			 * this.saveEntity();
			 * else if (ch==JOptionPane.CANCEL_OPTION)
			 * return false;
			 * else
			 * this.setChanged(false);
			 */
			this.setChanged(false);
		}
		this.stop();
		this.preview.closePreview();
		this.preview.getDrawPanel().getSelector().deselectAll();

		//clear controller before editing new custom element
		Main.getInstance().getDiagramHandler().getController().clear();
		
		DrawPanel dia = Main.getInstance().getGUI().getCurrentDiagram();
		if (dia != null) dia.getSelector().updateSelectorInformation();
		else Main.getInstance().setPropertyPanelToGridElement(null);
		return true;
	}

	public CustomPreviewHandler getPreviewHandler() {
		return this.preview;
	}

	public CustomCodeSyntaxPane getCodePane() {
		return this.codepane;
	}

	private void updatePreview(GridElement e) {
		if (e != null) {
			Vector<GridElement> entities = this.preview.getDrawPanel().getAllEntities();
			if (entities.size() > 0) {
				e.setBounds(entities.get(0).getBounds());
				e.setPanelAttributes(entities.get(0).getPanelAttributes());
				if (this.preview.getDrawPanel().getSelector().getSelectedEntities().size() > 0) this.preview.getDrawPanel().getSelector().singleSelectWithoutUpdatePropertyPanel(e);
				this.preview.getDrawPanel().remove(entities.get(0));
			}

			e.setHandler(this.preview);
			this.preview.getDrawPanel().add(e);
			e.repaint();
		}
	}

	// starts the task
	private void start() {
		this.compiletask = new CustomElementCompileTask(this);
		this.timer.schedule(compiletask, Constants.CUSTOM_ELEMENT_COMPILE_INTERVAL,
				Constants.CUSTOM_ELEMENT_COMPILE_INTERVAL);
	}

	// stops the task
	private void stop() {
		if (this.compiletask != null) this.compiletask.cancel();
	}

	// runs compilation every 1 seconds and updates gui/errors...
	protected void runCompilation() {
		if (!this.compilation_running && !keypressed) // prevent 2 compilations to run at the same time (if compilation takes more then 1sec)
		{
			this.compilation_running = true;
			String txt = this.codepane.getText();
			if (!txt.equals(this.old_text)) {
				this.setChanged(true);
				this.errorhandler.clearErrors();
				this.old_text = txt;
				GridElement e = CustomElementCompiler.getInstance().genEntity(txt, errorhandler);
				if (e != null) {
					this.editedEntity = e;
					this.panel.setCustomElementSaveable(true);
					this.updatePreview(e);
				}
				else {
					this.panel.setCustomElementSaveable(false);
				}
			}
			this.compilation_running = false;
		}
		keypressed = false;
	}

	private void setChanged(boolean changed) {
		this.changed = changed;
		Main.getInstance().getGUI().setCustomElementChanged(this, changed);
	}

	// reloads the element on all open panels and adds it to the custom element panel if not already there.
	private void updateElement(GridElement element) {

		// if a new element has been created add it to current diagram
		if (this.originalElement == null) {
			DiagramHandler current = null;
			DrawPanel c = Main.getInstance().getGUI().getCurrentDiagram();
			if (c == null) {
				Main.getInstance().doNew();
				current = Main.getInstance().getGUI().getCurrentDiagram().getHandler();
			}
			else current = c.getHandler();

			Vector<GridElement> ents = current.getDrawPanel().getAllEntities();
			// set location for element
			int x = 10, y = 10;
			for (GridElement e : ents) {
				if (e.getY() + e.getHeight() + 10 > y) y = e.getY() + e.getHeight() + 10;
			}

			Rectangle bounds = new Rectangle(x, y, element.getWidth(), element.getHeight());
			this.addElementToDiagram(element, current, true, bounds, element.getPanelAttributes());
		}
		else { // replace edited element (and ONLY edited element)
			this.originalElement.getHandler().getDrawPanel().remove(this.originalElement);
			this.addElementToDiagram(element, this.originalElement.getHandler(), true,
					this.originalElement.getBounds(), this.originalElement.getPanelAttributes());
		}
	}

	private void addElementToDiagram(GridElement e, DiagramHandler d, boolean setchanged, Rectangle bounds, String state) {

		// TODO bug if custom elements get inserted in zoomed drawpanel
		// the zoom to 100% bugfix works for inserting new elements but editing old elements with zoom will not work anymore
		// We must zoom to the defaultGridsize before execution
		// int oldZoom = d.getGridSize();
		// d.setGridAndZoom(Constants.DEFAULTGRIDSIZE, false);

		GridElement e2 = e.CloneFromMe();
		e2.setHandler(d);
		e2.setPanelAttributes(state);
		e2.setBounds(bounds);
		d.getDrawPanel().add(e2);
		if (setchanged) d.setChanged(true);

		// And zoom back to the oldGridsize after execution
		// d.setGridAndZoom(oldZoom, false);
	}
}
