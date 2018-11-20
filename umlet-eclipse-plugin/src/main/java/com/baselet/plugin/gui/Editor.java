package com.baselet.plugin.gui;

import java.awt.BorderLayout;
import java.awt.Cursor;
import java.awt.Frame;
import java.awt.Panel;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.util.UUID;

import javax.swing.JApplet;
import javax.swing.SwingUtilities;
import javax.swing.text.JTextComponent;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.swt.SWT;
import org.eclipse.swt.awt.SWT_AWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IEditorSite;
import org.eclipse.ui.IFileEditorInput;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.part.EditorPart;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.baselet.control.Main;
import com.baselet.control.enums.Program;
import com.baselet.diagram.DiagramHandler;
import com.baselet.diagram.DrawPanel;
import com.baselet.diagram.PaletteHandler;
import com.baselet.element.old.custom.CustomElementHandler;
import com.baselet.gui.CurrentGui;
import com.baselet.gui.listener.UmletWindowFocusListener;
import com.baselet.gui.pane.OwnSyntaxPane;

public class Editor extends EditorPart {

	private static final Logger log = LoggerFactory.getLogger(Editor.class);

	private DiagramHandler handler;
	private Panel embeddedPanel;

	private final EclipseGUIBuilder guiComponents = new EclipseGUIBuilder();

	private final UUID uuid = UUID.randomUUID();

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
				handler.doSaveAs(Program.getInstance().getExtension());
			}
		});
	}

	@Override
	public boolean isSaveAsAllowed() {
		return true;
	}

	File diagramFile;

	private Frame mainFrame;

	private Composite mainComposite;

	@Override
	public void init(IEditorSite site, IEditorInput input) throws PartInitException {
		log.info("Call editor.init() " + uuid.toString());
		setSite(site);
		setInput(input);
		setPartName(input.getName());
		diagramFile = getFile(input);
		try { // use invokeAndWait to make sure the initialization is finished before SWT proceeds
			SwingUtilities.invokeAndWait(new Runnable() {
				@Override
				public void run() { // initialize embedded panel here (and not in createPartControl) to avoid ugly scrollbars
					embeddedPanel = guiComponents.initEclipseGui();
				}
			});
		} catch (Exception e) {
			throw new PartInitException("Create DiagramHandler interrupted", e);
		}
	}

	private File getFile(IEditorInput input) throws PartInitException {
		if (input instanceof IFileEditorInput) { // Files opened from workspace
			return ((IFileEditorInput) input).getFile().getLocation().toFile();
		}
		else if (input instanceof org.eclipse.ui.ide.FileStoreEditorInput) { // Files from outside of the workspace (eg: edit current palette)
			return new File(((org.eclipse.ui.ide.FileStoreEditorInput) input).getURI());
		}
		else {
			throw new PartInitException("Editor input not supported.");
		}
	}

	@Override
	public boolean isDirty() {
		return handler.isChanged();
	}

	@Override
	public void createPartControl(Composite parent) {
		getGui().setCurrentEditor(Editor.this); // must be done before initalization of DiagramHandler (eg: to set propertypanel text)
		handler = new DiagramHandler(diagramFile);
		getGui().registerEditorForDiagramHandler(Editor.this, handler);
		getGui().setCurrentDiagramHandler(handler); // must be also set here because onFocus is not always called (eg: tab is opened during eclipse startup)
		getGui().open(handler);

		log.info("Call editor.createPartControl() " + uuid.toString());
		mainComposite = new Composite(parent, SWT.EMBEDDED);
		mainFrame = SWT_AWT.new_Frame(mainComposite);

		// Bug 228221 - SWT no longer receives key events in KeyAdapter when using SWT_AWT.new_Frame AWT frame
		// Use a RootPaneContainer e.g. JApplet to embed the swing panel in the SWT part
		// https://bugs.eclipse.org/bugs/show_bug.cgi?id=228221
		// http://www.eclipse.org/articles/article.php?file=Article-Swing-SWT-Integration/index.html
		// The proposal to use JApplet instead of JPanel no longer works (eclipse photon java 8 and 11),
		// key events are only partially propagated to the underlying SWT event queue.
		JApplet applet = new JApplet();
		applet.setLayout(new BorderLayout());
		applet.setFocusCycleRoot(false);
		applet.add(embeddedPanel, BorderLayout.CENTER);
		mainFrame.add(applet);
		mainFrame.addWindowFocusListener(new UmletWindowFocusListener());

		// Leaving the swing context is not sufficient to return the processing of the key events to the
		// SWT event queue. Therefore install a WindowFocusistener, that will force the SWT shell to
		// receive the focus again.
		// This is a workaround, that should be integrated in the SWT_AWT class. Even better would be
		// to fix the bug in the event processing.
		// Only tested on linux gtk, avoid to add an untestet change to windows or macos
		if ("gtk".equals(SWT.getPlatform())) {
			mainFrame.addWindowFocusListener(new WindowAdapter() {
				@Override
				public void windowLostFocus(WindowEvent e) {
					mainComposite.getDisplay().asyncExec(new Runnable() {
						@Override
						public void run() {
							mainComposite.getShell().forceActive();
						}
					});
				}
			});
		}
	}

	private EclipseGUI getGui() {
		return (EclipseGUI) CurrentGui.getInstance().getGui();
	}

	@Override
	public void setFocus() {
		log.info("Call editor.setFocus() " + uuid.toString());

		getGui().setCurrentEditor(this);
		getGui().setCurrentDiagramHandler(handler);
		if (handler != null) {
			handler.getDrawPanel().getSelector().updateSelectorInformation();
		}

		Display.getDefault().syncExec(new Runnable() {
			@Override
			public void run() {
				/**
				 * usually the palettes get lost  after switching the editor (for unknown reasons but perhaps because the Main class is build for exactly one palette (like in standalone umlet) but here every tab has its own palette)
				 * Therefore recreate them and also reselect the current palette and repaint every element with scrollbars (otherwise they have a visual error)
				 */
				if (guiComponents.getPalettePanel().getComponentCount() == 0) {
					for (PaletteHandler palette : Main.getInstance().getPalettes().values()) {
						guiComponents.getPalettePanel().add(palette.getDrawPanel().getScrollPane(), palette.getName());
						palette.getDrawPanel().getScrollPane().invalidate();
					}
				}
				showPalette(getSelectedPaletteName());
				getGui().setValueOfZoomDisplay(handler.getGridSize());
				guiComponents.getPropertyTextPane().invalidate();
			}
		});
	}

	public DrawPanel getDiagram() {
		if (handler == null) {
			return null;
		}
		return handler.getDrawPanel();
	}

	@Override
	public void dispose() {
		super.dispose();
		log.info("Call editor.dispose( )" + uuid.toString());
		// AB: The eclipse plugin might hang sometimes if this section is not placed into an event queue, since swing or swt is not thread safe!
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				if (guiComponents.getMailPanel().isVisible()) {
					guiComponents.getMailPanel().closePanel();
				}
				getGui().editorRemoved(Editor.this);
			}
		});
	}

	public void setCursor(Cursor cursor) {
		embeddedPanel.setCursor(cursor);
	}

	public OwnSyntaxPane getPropertyPane() {
		return guiComponents.getPropertyTextPane();
	}

	public JTextComponent getCustomPane() {
		return guiComponents.getCustomPanel().getTextPane();
	}

	public void requestFocus() {
		embeddedPanel.requestFocus();
	}

	public Frame getMainFrame() {
		return mainFrame;
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
		return guiComponents.getPaletteList().getSelectedItem().toString();
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
		guiComponents.setPaletteActive(paletteName);
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
