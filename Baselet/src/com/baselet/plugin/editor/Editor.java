package com.baselet.plugin.editor;

import java.awt.CardLayout;
import java.awt.Cursor;
import java.awt.Frame;
import java.awt.Panel;
import java.io.File;
import java.lang.reflect.InvocationTargetException;
import java.util.UUID;

import javax.swing.SwingUtilities;
import javax.swing.text.JTextComponent;

import org.apache.log4j.Logger;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.swt.SWT;
import org.eclipse.swt.awt.SWT_AWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IEditorSite;
import org.eclipse.ui.IFileEditorInput;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.part.EditorPart;

import com.baselet.control.Constants.Program;
import com.baselet.control.Main;
import com.baselet.diagram.DiagramHandler;
import com.baselet.diagram.DrawPanel;
import com.baselet.diagram.PaletteHandler;
import com.baselet.gui.OwnSyntaxPane;
import com.baselet.gui.eclipse.EclipseGUIBuilder;
import com.baselet.plugin.MainPlugin;
import com.umlet.custom.CustomElementHandler;

public class Editor extends EditorPart {
	
	private static final Logger log = Logger.getLogger(Editor.class);

	private DiagramHandler handler;
	private Panel embedded_panel;

	private EclipseGUIBuilder guiComponents = new EclipseGUIBuilder();

	private UUID uuid = UUID.randomUUID();

	@Override
	public void doSave(IProgressMonitor monitor) {
		handler.doSave();
		monitor.done();
	}

	@Override
	public void doSaveAs() {
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				handler.doSaveAs(Program.EXTENSION);
			}
		});
	}

	@Override
	public boolean isSaveAsAllowed() {
		return true;
	}
	
	File diagramFile;

	@Override
	public void init(IEditorSite site, IEditorInput input) throws PartInitException {
		log.info("Call editor.init() " + uuid.toString());
		this.setSite(site);
		this.setInput(input);
		this.setPartName(input.getName());
		diagramFile = getFile(input);
		try { //use invokeAndWait to make sure the initialization is finished before SWT proceeds
			SwingUtilities.invokeAndWait(new Runnable() {
				@Override
				public void run() { // initialize embedded panel here (and not in createPartControl) to avoid ugly scrollbars
					embedded_panel = guiComponents.initEclipseGui();
				}
			});
		} catch (InterruptedException e) {
			log.error("Create DiagramHandler interrupted");
		} catch (InvocationTargetException e) {
			log.error("Create DiagramHandler invocation exception");
		}
	}

	private File getFile(IEditorInput input) throws PartInitException {
		if (input instanceof IFileEditorInput) { // Files opened from workspace
			return ((IFileEditorInput) input).getFile().getLocation().toFile();
		}
		else if (input instanceof org.eclipse.ui.ide.FileStoreEditorInput) { // Files from outside of the workspace (eg: edit current palette)
			return new File(((org.eclipse.ui.ide.FileStoreEditorInput) input).getURI());
		}
		else throw new PartInitException("Editor input not supported.");
	}

	@Override
	public boolean isDirty() {
		return this.handler.isChanged();
	}

	@Override
	public void createPartControl(Composite parent) {
		MainPlugin.getGUI().setCurrentEditor(Editor.this); // must be done before initalization of DiagramHandler (eg: to set propertypanel text)
		handler = new DiagramHandler(diagramFile);
		MainPlugin.getGUI().registerEditorForDiagramHandler(Editor.this, handler);
		MainPlugin.getGUI().setCurrentDiagramHandler(handler); // must be also set here because onFocus is not always called (eg: tab is opened during eclipse startup)
		MainPlugin.getGUI().open(handler);

		log.info("Call editor.createPartControl() " + uuid.toString());
		final Frame frame = SWT_AWT.new_Frame(new Composite(parent, SWT.EMBEDDED));
		frame.add(embedded_panel);
	}

	@Override
	public void setFocus() {
		log.info("Call editor.setFocus() " + uuid.toString());

		MainPlugin.getGUI().setCurrentEditor(this);
		MainPlugin.getGUI().setCurrentDiagramHandler(handler);
		if (handler != null) handler.getDrawPanel().getSelector().updateSelectorInformation();

		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {	
				/**
				 * usually the palettes get lost (for unknown reasons) after switching the editor, therefore recreate them.
				 * also reselect the current palette and repaint every element with scrollbars (otherwise they have a visual error)
				 */
				if (guiComponents.getPalettePanel().getComponentCount()==0) {
					for (PaletteHandler palette : Main.getInstance().getPalettes().values()) {
						guiComponents.getPalettePanel().add(palette.getDrawPanel().getScrollPane(), palette.getName());
					}
				}
				showPalette(getSelectedPaletteName());
				Main.getInstance().getGUI().setValueOfZoomDisplay(handler.getGridSize());
				guiComponents.getPropertyTextPane().revalidate();
			}
		});
	}

	public DrawPanel getDiagram() {
		if (handler == null) return null;
		return this.handler.getDrawPanel();
	}

	@Override
	public void dispose() {
		super.dispose();
		log.info("Call editor.dispose( )" + uuid.toString());
		//AB: The eclipse plugin might hang sometimes if this section is not placed into an event queue, since swing or swt is not thread safe!
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				if (guiComponents.getMailPanel().isVisible()) guiComponents.getMailPanel().closePanel();
				MainPlugin.getGUI().editorRemoved(Editor.this);
			}
		});
	}

	public void setCursor(Cursor cursor) {
		this.embedded_panel.setCursor(cursor);
	}

	public OwnSyntaxPane getPropertyPane() {
		return guiComponents.getPropertyTextPane();
	}

	public JTextComponent getCustomPane() {
		return guiComponents.getCustomPanel().getTextPane();
	}

	public void requestFocus() {
		this.embedded_panel.requestFocus();
	}

	public void dirtyChanged() {
		org.eclipse.swt.widgets.Display.getDefault().asyncExec(new Runnable() {
			@Override
			public void run() {
				firePropertyChange(IEditorPart.PROP_DIRTY);
			}
		});
	}

	public void diagramNameChanged() {
		org.eclipse.swt.widgets.Display.getDefault().asyncExec(new Runnable() {
			@Override
			public void run() {
				firePropertyChange(IWorkbenchPart.PROP_TITLE);
			}
		});
	}

	public CustomElementHandler getCustomElementHandler() {
		return guiComponents.getCustomHandler();
	}

	public void setMailPanelEnabled(boolean enable) {
		guiComponents.setMailPanelEnabled(enable);
	}

	public boolean isMailPanelVisible() {
		return guiComponents.getMailPanel().isVisible();
	}

	public String getSelectedPaletteName() {
		return this.guiComponents.getPaletteList().getSelectedItem().toString();
	}

	public int getMainSplitLocation() {
		return guiComponents.getMainSplit().getDividerLocation();
	}

	public int getRightSplitLocation() {
		return guiComponents.getRightSplit().getDividerLocation();
	}

	public int getMailSplitLocation() {
		return guiComponents.getMailSplit().getDividerLocation();
	}

	public void showPalette(final String paletteName) {
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				CardLayout palettePanelLayout = (CardLayout) guiComponents.getPalettePanel().getLayout();
				palettePanelLayout.show(guiComponents.getPalettePanel(), paletteName);
				Main.getInstance().getPalettePanel().getScrollPane().revalidate();
			}
		});
	}

	public void setCustomPanelEnabled(boolean enable) {
		guiComponents.setCustomPanelEnabled(enable);
		setDrawPanelEnabled(!enable);
	}

	private void setDrawPanelEnabled(boolean enable) {
		handler.getDrawPanel().getScrollPane().setEnabled(enable);
	}

	public void focusPropertyPane() {
		guiComponents.getPropertyTextPane().getTextComponent().requestFocus();
	}

	public void open(final DiagramHandler handler) {
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				guiComponents.setContent(handler.getDrawPanel().getScrollPane());
			}
		});
	}

}
