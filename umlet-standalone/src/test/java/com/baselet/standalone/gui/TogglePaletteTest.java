package com.baselet.standalone.gui;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

import java.awt.Component;
import java.awt.Container;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import javax.swing.JComponent;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JSplitPane;
import javax.swing.JToggleButton;
import javax.swing.KeyStroke;
import javax.swing.SwingUtilities;
import javax.swing.plaf.basic.BasicSplitPaneDivider;
import javax.swing.plaf.basic.BasicSplitPaneUI;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

import com.baselet.control.Main;
import com.baselet.control.config.Config;
import com.baselet.control.constants.MenuConstants;
import com.baselet.control.constants.SystemInfo;
import com.baselet.control.enums.Program;
import com.baselet.control.enums.RuntimeType;
import com.baselet.control.util.Path;
import com.baselet.diagram.CurrentDiagram;
import com.baselet.diagram.DiagramHandler;
import com.baselet.diagram.PaletteHandler;
import com.baselet.gui.BaseGUI;
import com.baselet.gui.BaseGUIBuilder;
import com.baselet.gui.CurrentGui;

public class TogglePaletteTest {

	@Rule
	public TemporaryFolder temporary = new TemporaryFolder();

	private Program oldProgram;
	private String oldUiManager;
	private String oldHome;
	private int oldRightSplit;
	private BaseGUI oldGui;
	private DiagramHandler oldCurrentDiagram;
	private List<DiagramHandler> oldDiagrams;
	private Map<String, PaletteHandler> oldPalettes;
	private BaseGUI gui;
	private TestBuilder builder;
	private JSplitPane root;
	private JMenuBar menu;
	private JMenuItem toggle;
	private DiagramHandler diagram;

	private static class TestBuilder extends BaseGUIBuilder {
		JSplitPane create(Component diagram) {
			return initBase(diagram, 600);
		}
	}

	@Before
	public void setUp() throws Exception {
		temporary.newFolder("palettes");
		temporary.newFolder("custom_elements");
		SwingUtilities.invokeAndWait(() -> {
			oldProgram = Program.isInitialized() ? Program.getInstance() : null;
			Program.init("test", RuntimeType.BATCH);
			oldUiManager = Config.getInstance().getUiManager();
			oldRightSplit = Config.getInstance().getRight_split_position();
			Config.getInstance().setUiManager("javax.swing.plaf.metal.MetalLookAndFeel");
			Config.getInstance().setRight_split_position(300);
			oldHome = Path.homeProgram();
			Path.setHomeProgram(temporary.getRoot().getAbsolutePath() + File.separator);
			oldGui = CurrentGui.getInstance().getGui();
			gui = mock(BaseGUI.class);
			CurrentGui.getInstance().setGui(gui);
			oldCurrentDiagram = CurrentDiagram.getInstance().getDiagramHandler();
			Main main = Main.getInstance();
			oldDiagrams = new ArrayList<DiagramHandler>(main.getDiagrams());
			oldPalettes = new TreeMap<String, PaletteHandler>(main.getPalettes());
			main.getDiagrams().clear();
			main.getPalettes().clear();
			diagram = new DiagramHandler(null);
			main.getDiagrams().add(diagram);
			main.getPalettes().put("test", new PaletteHandler(null));
			CurrentDiagram.getInstance().setCurrentDiagramHandler(diagram);
			builder = new TestBuilder();
			root = builder.create(diagram.getDrawPanel().getScrollPane());
			root.setSize(1000, 800);
			layout(root);
			doAnswer(invocation -> {
				assertTrue(SwingUtilities.isEventDispatchThread());
				builder.togglePalette();
				layout(root);
				return null;
			}).when(gui).togglePalette();
			menu = new MenuBuilder().createMenu(new JPanel(), new JPanel(), new JToggleButton());
			toggle = findItem(menu, MenuConstants.TOGGLE_PALETTE);
			assertNotNull(toggle);
		});
	}

	@After
	public void tearDown() throws Exception {
		SwingUtilities.invokeAndWait(() -> {
			Main.getInstance().getDiagrams().clear();
			Main.getInstance().getDiagrams().addAll(oldDiagrams);
			Main.getInstance().getPalettes().clear();
			Main.getInstance().getPalettes().putAll(oldPalettes);
			CurrentDiagram.getInstance().setCurrentDiagramHandler(oldCurrentDiagram);
			CurrentGui.getInstance().setGui(oldGui);
			Path.setHomeProgram(oldHome);
			Config.getInstance().setUiManager(oldUiManager);
			Config.getInstance().setRight_split_position(oldRightSplit);
			if (oldProgram != null) {
				Program.init(oldProgram.getVersion(), oldProgram.getRuntimeType());
			}
		});
	}

	@Test
	public void mouseAndMenuToggleTheSameSidebarAndRestoreTheStartupDivider() throws Exception {
		SwingUtilities.invokeAndWait(() -> {
			builder.getMainSplit().setDividerLocation(700);
			builder.getRightSplit().setDividerLocation(240);
			layout(root);
			doubleClickMainDivider();
			assertSidebarVisible(false);
		});
		clickToggle();
		SwingUtilities.invokeAndWait(() -> {
			assertSidebarVisible(true);
			assertEquals(600, builder.getMainSplit().getDividerLocation());
			assertEquals(240, builder.getRightSplit().getDividerLocation());
			builder.getMainSplit().setDividerLocation(750);
			layout(root);
		});
		clickToggle();
		SwingUtilities.invokeAndWait(() -> {
			assertSidebarVisible(false);
			doubleClickMainDivider();
			assertSidebarVisible(true);
			assertEquals(600, builder.getMainSplit().getDividerLocation());
			assertEquals(240, builder.getRightSplit().getDividerLocation());
		});
	}

	@Test
	public void menuRoutesOnEdtWithoutNeedingOrChangingADiagram() throws Exception {
		clickToggle();
		SwingUtilities.invokeAndWait(() -> {
			assertSidebarVisible(false);
			verify(gui).togglePalette();
			verify(gui, never()).getCurrentDiagram();
			assertSame(diagram, CurrentDiagram.getInstance().getDiagramHandler());
			assertFalse(diagram.isChanged());
			assertTrue(diagram.getController().isEmpty());
		});
		clickToggle();
		SwingUtilities.invokeAndWait(() -> assertSidebarVisible(true));
	}

	@Test
	public void shortcutIsUniqueAndDoesNotReplacePrintOrPropertyEditorBindings() throws Exception {
		SwingUtilities.invokeAndWait(() -> {
			KeyStroke shortcut = KeyStroke.getKeyStroke(KeyEvent.VK_P, SystemInfo.META_KEY.getMaskDown() | InputEvent.SHIFT_DOWN_MASK);
			assertEquals(shortcut, toggle.getAccelerator());
			assertEquals(1, countAccelerator(menu, shortcut));
			assertEquals(KeyStroke.getKeyStroke(KeyEvent.VK_P, SystemInfo.META_KEY.getMaskDown()), findItem(menu, MenuConstants.PRINT).getAccelerator());
			assertNull(builder.getPropertyTextPane().getTextComponent().getInputMap().get(shortcut));
			JMenu view = (JMenu) findItem(menu, MenuConstants.VIEW);
			assertSame(toggle, view.getItem(0));
			assertTrue(toggle.isEnabled());
		});
	}

	private void assertSidebarVisible(boolean visible) {
		assertEquals(visible, builder.getMainSplit().getRightComponent().isVisible());
		assertEquals(visible, isVisibleInHierarchy(builder.getPalettePanel()));
		assertEquals(visible, isVisibleInHierarchy(builder.getPaletteList()));
		assertEquals(visible, isVisibleInHierarchy(builder.getPropertyTextPane().getPanel()));
		assertTrue(isVisibleInHierarchy(builder.getMainSplit().getLeftComponent()));
	}

	private static boolean isVisibleInHierarchy(Component component) {
		for (; component != null; component = component.getParent()) {
			if (!component.isVisible()) {
				return false;
			}
		}
		return true;
	}

	private void clickToggle() throws Exception {
		SwingUtilities.invokeAndWait(() -> toggle.doClick(0));
		SwingUtilities.invokeAndWait(() -> {}); // drain MenuFactory's asynchronous action
	}

	private void doubleClickMainDivider() {
		BasicSplitPaneDivider divider = ((BasicSplitPaneUI) builder.getMainSplit().getUI()).getDivider();
		divider.dispatchEvent(new MouseEvent(divider, MouseEvent.MOUSE_CLICKED, System.currentTimeMillis(), 0, 1, 1, 2, false, MouseEvent.BUTTON1));
		layout(root);
	}

	private static void layout(Container container) {
		container.doLayout();
		for (Component child : container.getComponents()) {
			if (child instanceof Container && child.isVisible()) {
				layout((Container) child);
			}
		}
	}

	private static JMenuItem findItem(JComponent parent, String text) {
		for (Component child : parent instanceof JMenu ? ((JMenu) parent).getMenuComponents() : parent.getComponents()) {
			if (child instanceof JMenuItem && text.equals(((JMenuItem) child).getText())) {
				return (JMenuItem) child;
			}
			if (child instanceof JMenu) {
				JMenuItem found = findItem((JMenu) child, text);
				if (found != null) {
					return found;
				}
			}
		}
		return null;
	}

	private static int countAccelerator(JComponent parent, KeyStroke shortcut) {
		int count = 0;
		for (Component child : parent instanceof JMenu ? ((JMenu) parent).getMenuComponents() : parent.getComponents()) {
			if (child instanceof JMenu) {
				count += countAccelerator((JMenu) child, shortcut);
			}
			else if (child instanceof JMenuItem && shortcut.equals(((JMenuItem) child).getAccelerator())) {
				count++;
			}
		}
		return count;
	}
}
