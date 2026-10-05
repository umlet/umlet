package com.baselet.standalone.gui;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.mockStatic;

import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import javax.swing.JCheckBoxMenuItem;
import javax.swing.JComponent;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JToggleButton;
import javax.swing.KeyStroke;
import javax.swing.RepaintManager;
import javax.swing.SwingUtilities;

import org.fife.ui.rsyntaxtextarea.RSyntaxTextArea;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;
import org.mockito.MockedStatic;

import com.baselet.control.Main;
import com.baselet.control.basics.geom.Rectangle;
import com.baselet.control.config.Config;
import com.baselet.control.config.handler.ConfigHandler;
import com.baselet.control.constants.MenuConstants;
import com.baselet.control.constants.SystemInfo;
import com.baselet.control.enums.ElementId;
import com.baselet.control.enums.Program;
import com.baselet.control.enums.RuntimeType;
import com.baselet.control.util.Path;
import com.baselet.diagram.DiagramHandler;
import com.baselet.diagram.PaletteHandler;
import com.baselet.diagram.io.DiagramFileHandler;
import com.baselet.diagram.io.OutputHandler;
import com.baselet.element.ElementFactorySwing;
import com.baselet.element.interfaces.GridElement;
import com.baselet.gui.BaseGUI;
import com.baselet.gui.CurrentGui;
import com.baselet.gui.command.Command;

public class ShowGridTest {

	@Rule
	public TemporaryFolder temporary = new TemporaryFolder();

	private boolean oldShowGrid;
	private String oldUiManager;
	private String oldHome;
	private BaseGUI oldGui;
	private Program oldProgram;
	private List<DiagramHandler> oldDiagrams;
	private Map<String, PaletteHandler> oldPalettes;
	private RepaintManager oldRepaintManager;
	private final Set<JComponent> repainted = new HashSet<JComponent>();
	private JMenuBar menu;
	private JMenuItem showGrid;
	private DiagramHandler diagram;
	private GridElement element;

	@Before
	public void setUp() throws Exception {
		temporary.newFolder("palettes");
		temporary.newFolder("custom_elements");
		SwingUtilities.invokeAndWait(() -> {
			oldProgram = Program.isInitialized() ? Program.getInstance() : null;
			Program.init("test", RuntimeType.BATCH); // real Swing components, without windows or file-drop support
			oldShowGrid = Config.getInstance().isShow_grid();
			oldUiManager = Config.getInstance().getUiManager();
			Config.getInstance().setUiManager("javax.swing.plaf.metal.MetalLookAndFeel");
			Config.getInstance().setShow_grid(false);
			oldHome = Path.homeProgram();
			Path.setHomeProgram(temporary.getRoot().getAbsolutePath() + File.separator);
			oldGui = CurrentGui.getInstance().getGui();
			CurrentGui.getInstance().setGui(mock(BaseGUI.class));
			Main main = Main.getInstance();
			oldDiagrams = new ArrayList<DiagramHandler>(main.getDiagrams());
			oldPalettes = new TreeMap<String, PaletteHandler>(main.getPalettes());
			main.getDiagrams().clear();
			main.getPalettes().clear();
			diagram = new DiagramHandler(null);
			element = ElementFactorySwing.create(ElementId.UMLClass, new Rectangle(20, 30, 100, 80), "Example", "", diagram);
			diagram.getDrawPanel().addElement(element);
			main.getDiagrams().add(diagram);
			main.getDiagrams().add(new DiagramHandler(null));
			main.getPalettes().put("test", new PaletteHandler(null));
			menu = new MenuBuilder().createMenu(new JPanel(), new JPanel(), new JToggleButton());
			showGrid = findItem(menu, MenuConstants.SHOW_GRID);
			assertNotNull(showGrid);
			oldRepaintManager = RepaintManager.currentManager(menu);
			RepaintManager.setCurrentManager(new RepaintManager() {
				@Override
				public void addDirtyRegion(JComponent component, int x, int y, int width, int height) {
					repainted.add(component);
				}
			});
		});
	}

	@After
	public void tearDown() throws Exception {
		SwingUtilities.invokeAndWait(() -> {
			RepaintManager.setCurrentManager(oldRepaintManager);
			Main.getInstance().getDiagrams().clear();
			Main.getInstance().getDiagrams().addAll(oldDiagrams);
			Main.getInstance().getPalettes().clear();
			Main.getInstance().getPalettes().putAll(oldPalettes);
			CurrentGui.getInstance().setGui(oldGui);
			Path.setHomeProgram(oldHome);
			Config.getInstance().setShow_grid(oldShowGrid);
			Config.getInstance().setUiManager(oldUiManager);
			if (oldProgram != null) {
				Program.init(oldProgram.getVersion(), oldProgram.getRuntimeType());
			}
		});
	}

	@Test
	public void commandRepaintsAllDiagramsAndPalettesWithoutEditing() throws Exception {
		SwingUtilities.invokeAndWait(() -> repainted.clear());
		clickShowGrid();
		SwingUtilities.invokeAndWait(() -> {
			assertTrue(Config.getInstance().isShow_grid());
			assertTrue(showGrid.isSelected());
			for (DiagramHandler handler : Main.getInstance().getDiagramsAndPalettes()) {
				assertTrue(repainted.contains(handler.getDrawPanel()));
				assertFalse(handler.isChanged());
				assertTrue(handler.getController().isEmpty());
			}
			assertEquals(new Rectangle(20, 30, 100, 80), element.getRectangle());
			assertEquals("Example", element.getPanelAttributes());
			assertEquals(10, diagram.getGridSize());
		});
		clickShowGrid();
		SwingUtilities.invokeAndWait(() -> {
			assertFalse(Config.getInstance().isShow_grid());
			assertFalse(showGrid.isSelected());
		});
	}

	@Test
	public void commandPreservesExistingRedoHistory() throws Exception {
		SwingUtilities.invokeAndWait(() -> {
			diagram.getController().executeCommand(mock(Command.class));
			diagram.getController().undo();
			assertTrue(diagram.getController().isRedoable());
		});
		clickShowGrid();
		SwingUtilities.invokeAndWait(() -> {
			assertFalse(diagram.getController().isUndoable());
			assertTrue(diagram.getController().isRedoable());
			assertFalse(diagram.isChanged());
		});
	}

	@Test
	public void menuReflectsPreferenceChangesAndUsesAnUnclaimedAccelerator() throws Exception {
		SwingUtilities.invokeAndWait(() -> {
			assertTrue(showGrid instanceof JCheckBoxMenuItem);
			assertTrue(showGrid.isEnabled());
			assertFalse(showGrid.isSelected());
			Config.getInstance().setShow_grid(true); // same setter as Options and configuration loading
			assertTrue(showGrid.isSelected());
			Config.getInstance().setShow_grid(false);
			assertFalse(showGrid.isSelected());
			KeyStroke shortcut = KeyStroke.getKeyStroke(KeyEvent.VK_G, SystemInfo.META_KEY.getMaskDown() | InputEvent.SHIFT_DOWN_MASK);
			assertEquals(shortcut, showGrid.getAccelerator());
			assertEquals(KeyStroke.getKeyStroke(KeyEvent.VK_G, SystemInfo.META_KEY.getMaskDown()), findItem(menu, MenuConstants.GROUP).getAccelerator());
			assertEquals(1, countAccelerator(menu, shortcut));
			assertNull(new RSyntaxTextArea().getInputMap().get(shortcut));
		});
	}

	@Test
	public void commandPreferenceRoundTripsThroughExistingConfigFile() throws Exception {
		File configFile = temporary.newFile("umlet.cfg");
		File legacyFile = new File(temporary.getRoot(), "legacy.cfg");
		String oldConfigPath = System.getProperty(ConfigHandler.UMLET_CFG_PATH);
		try {
			System.setProperty(ConfigHandler.UMLET_CFG_PATH, configFile.getAbsolutePath());
			for (boolean expected : new boolean[] { true, false }) {
				clickShowGrid();
				SwingUtilities.invokeAndWait(() -> {
					// Redirect paths before any real path method can create a user configuration directory.
					try (MockedStatic<Path> paths = mockStatic(Path.class, invocation -> {
						if (invocation.getMethod().getName().equals("osConformConfig")) {
							return configFile.getAbsolutePath();
						}
						if (invocation.getMethod().getName().equals("legacyConfig")) {
							return legacyFile.getAbsolutePath();
						}
						return invocation.callRealMethod();
					})) {
						ConfigHandler.saveConfig(CurrentGui.getInstance().getGui());
					}
					Config.getInstance().setShow_grid(!expected);
					ConfigHandler.loadConfig();
					assertEquals(expected, Config.getInstance().isShow_grid());
					assertEquals(expected, showGrid.isSelected());
				});
			}
		} finally {
			if (oldConfigPath == null) {
				System.clearProperty(ConfigHandler.UMLET_CFG_PATH);
			}
			else {
				System.setProperty(ConfigHandler.UMLET_CFG_PATH, oldConfigPath);
			}
		}
	}

	@Test
	public void commandDoesNotChangeUxfOrExportedPngAndSvg() throws Exception {
		String uxfBefore = serializeDiagram();
		byte[] pngBefore = export("png");
		byte[] svgBefore = export("svg");
		clickShowGrid();
		assertEquals(uxfBefore, serializeDiagram());
		assertArrayEquals(pngBefore, export("png"));
		assertArrayEquals(svgBefore, export("svg"));
	}

	private String serializeDiagram() throws Exception {
		String[] result = new String[1];
		SwingUtilities.invokeAndWait(() -> result[0] = new DiagramSnapshot(diagram).serialize());
		return result[0];
	}

	private static class DiagramSnapshot extends DiagramFileHandler {
		DiagramSnapshot(DiagramHandler handler) {
			super(handler, null);
		}

		String serialize() {
			return createStringToBeSaved();
		}
	}

	private byte[] export(String format) throws Exception {
		ByteArrayOutputStream output = new ByteArrayOutputStream();
		SwingUtilities.invokeAndWait(() -> {
			try {
				OutputHandler.createToStream(format, output, diagram);
			} catch (Exception e) {
				throw new RuntimeException(e);
			}
		});
		return output.toByteArray();
	}

	private void clickShowGrid() throws Exception {
		SwingUtilities.invokeAndWait(() -> showGrid.doClick(0));
		SwingUtilities.invokeAndWait(() -> {}); // drain the normal asynchronous MenuFactory action
	}

	private static JMenuItem findItem(JComponent parent, String text) {
		for (java.awt.Component child : parent instanceof JMenu ? ((JMenu) parent).getMenuComponents() : parent.getComponents()) {
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
		for (java.awt.Component child : parent instanceof JMenu ? ((JMenu) parent).getMenuComponents() : parent.getComponents()) {
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
