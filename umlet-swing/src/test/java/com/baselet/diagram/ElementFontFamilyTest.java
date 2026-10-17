package com.baselet.diagram;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import javax.imageio.ImageIO;
import javax.swing.SwingUtilities;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.baselet.control.basics.geom.Rectangle;
import com.baselet.control.config.Config;
import com.baselet.control.enums.ElementId;
import com.baselet.control.enums.Program;
import com.baselet.control.enums.RuntimeType;
import com.baselet.diagram.draw.DrawHandler;
import com.baselet.diagram.io.OutputHandler;
import com.baselet.element.ElementFactorySwing;
import com.baselet.element.NewGridElement;
import com.baselet.element.interfaces.GridElement;
import com.itextpdf.text.pdf.PdfDictionary;
import com.itextpdf.text.pdf.PdfName;
import com.itextpdf.text.pdf.PdfReader;

public class ElementFontFamilyTest {

	private static final String SAMPLE = "Hamburgefonts iii WWW";
	private static final String[] FAMILIES = {"SansSerif", "Serif", "Monospaced"};
	private String oldUiManager;
	private String oldFamily;
	private int oldSize;

	@Before
	public void setUp() throws Exception {
		SwingUtilities.invokeAndWait(() -> {
			if (!Program.isInitialized()) {
				Program.init("test", RuntimeType.BATCH);
			}
			Config config = Config.getInstance();
			oldUiManager = config.getUiManager();
			oldFamily = config.getDefaultFontFamily();
			oldSize = config.getDefaultFontsize();
			config.setUiManager("javax.swing.plaf.metal.MetalLookAndFeel");
			config.setDefaultFontFamily("SansSerif");
			config.setDefaultFontsize(14);
		});
	}

	@After
	public void tearDown() throws Exception {
		SwingUtilities.invokeAndWait(() -> {
			Config.getInstance().setUiManager(oldUiManager);
			Config.getInstance().setDefaultFontFamily(oldFamily);
			Config.getInstance().setDefaultFontsize(oldSize);
		});
	}

	@Test
	public void logicalFamiliesMatchDiagramFontsForMeasurementPaintingAndStyles() throws Exception {
		onEdt(() -> {
			for (ElementId id : Arrays.asList(ElementId.Text, ElementId.UMLClass, ElementId.UMLUseCase, ElementId.UMLNote)) {
				for (String family : FAMILIES) {
					for (String text : Arrays.asList(SAMPLE, "*_/" + SAMPLE + "/_*")) {
						String properties = text + "\nfontsize=18.5";
						NewGridElement actual = element(diagram("Serif"), id, properties + "\nfontfamily=" + family);
						NewGridElement expected = element(diagram(family), id, properties);
						assertEquals(drawer(expected).textWidth(text), drawer(actual).textWidth(text), 0.0001);
						assertImagesEqual(render(expected), render(actual));
						assertEquals(drawer(expected).textWidth(text), drawer(actual).textWidth(text), 0.0001);
						assertEquals(18.5, drawer(actual).getFontSize(), 0);
					}
				}
			}
		});
	}

	@Test
	public void overrideRemovalAndNeighboringElementsKeepInheritance() throws Exception {
		onEdt(() -> {
			DiagramHandler diagram = diagram("Serif");
			NewGridElement inherited = element(diagram, ElementId.Text, SAMPLE);
			BufferedImage original = render(inherited);
			NewGridElement changed = element(diagram, ElementId.Text, SAMPLE + "\nfontfamily=Monospaced");
			assertFalse(Arrays.equals(pixels(original), pixels(render(changed))));
			for (String family : FAMILIES) {
				changed.setPanelAttributes(SAMPLE + "\nfontfamily=" + family);
				drawer(changed).textWidth(SAMPLE);
				render(changed);
				assertImagesEqual(original, render(inherited));
				assertEquals("Serif", diagram.getFontHandler().getFont(false).getFamily());
			}
			changed.setPanelAttributes(SAMPLE);
			assertImagesEqual(original, render(changed));
			diagram.getFontHandler().setDiagramDefaultFontFamily("Monospaced");
			changed.updateModelFromText();
			assertImagesEqual(render(element(diagram("Monospaced"), ElementId.Text, SAMPLE)), render(changed));
			diagram.getFontHandler().resetDiagramDefaultFontFamily();
			changed.updateModelFromText();
			assertImagesEqual(render(element(diagram("SansSerif"), ElementId.Text, SAMPLE)), render(changed));
		});
	}

	@Test
	public void wrappingAutoresizeAndModernCustomDrawingUseSelectedFamily() throws Exception {
		onEdt(() -> {
			for (String family : FAMILIES) {
				for (String properties : Arrays.asList(
						SAMPLE + " " + SAMPLE + "\nstyle=wordwrap\nfontsize=19",
						SAMPLE + " " + SAMPLE + "\nstyle=autoresize\nfontsize=19",
						"fontsize=19\ncustomelement=\ndrawText(\"" + SAMPLE + "\",10,30,left)")) {
					NewGridElement actual = element(diagram("SansSerif"), ElementId.Text, "fontfamily=" + family + "\n" + properties);
					NewGridElement expected = element(diagram(family), ElementId.Text, properties);
					assertEquals(expected.getRectangle(), actual.getRectangle());
					try {
						assertImagesEqual(render(expected), render(actual));
					} catch (AssertionError e) {
						throw new AssertionError(family + ": " + properties, e);
					}
					// Painting updates FontHandler's render context; compare copies in that same context.
					GridElement clone = ElementFactorySwing.createCopy(actual);
					GridElement expectedClone = ElementFactorySwing.createCopy(expected);
					assertEquals(actual.getPanelAttributes(), clone.getPanelAttributes());
					assertImagesEqual(render(expectedClone), render(clone));
				}
			}
		});
	}

	@Test
	public void desktopExportsUseElementFamiliesAndRetainDefaultNeighbor() throws Exception {
		onEdt(() -> {
			DiagramHandler mixed = diagram("SansSerif");
			int y = 0;
			for (String family : FAMILIES) {
				DiagramHandler actual = diagram("Serif");
				actual.getDrawPanel().addElement(element(actual, ElementId.Text, SAMPLE + "\nfontfamily=" + family));
				DiagramHandler expected = diagram(family);
				expected.getDrawPanel().addElement(element(expected, ElementId.Text, SAMPLE));
				assertImagesEqual(ImageIO.read(new ByteArrayInputStream(export(expected, "png"))), ImageIO.read(new ByteArrayInputStream(export(actual, "png"))));
				String svg = new String(export(actual, "svg"), java.nio.charset.StandardCharsets.UTF_8);
				assertFalse(svg.contains("fontfamily="));
				String svgFamily = family.equals("SansSerif") ? "sans-serif" : family.equals("Serif") ? "serif" : "monospace";
				assertTrue(svg, svg.contains("font-family=\"" + svgFamily + "\""));
				NewGridElement entry = element(mixed, ElementId.Text, SAMPLE + "\nfontfamily=" + family);
				entry.setLocation(0, y);
				y += 100;
				mixed.getDrawPanel().addElement(entry);
			}
			NewGridElement inherited = element(mixed, ElementId.Text, "Inherited");
			inherited.setLocation(0, y);
			mixed.getDrawPanel().addElement(inherited);
			PdfReader reader = new PdfReader(export(mixed, "pdf"));
			try {
				PdfDictionary fonts = reader.getPageN(1).getAsDict(PdfName.RESOURCES).getAsDict(PdfName.FONT);
				Set<String> names = new HashSet<String>();
				for (PdfName key : fonts.getKeys()) {
					PdfDictionary font = (PdfDictionary) PdfReader.getPdfObject(fonts.get(key));
					names.add(font.getAsName(PdfName.BASEFONT).toString());
				}
				assertEquals(new HashSet<String>(Arrays.asList("/Helvetica", "/Times-Roman", "/Courier")), names);
			} finally {
				reader.close();
			}
			assertEquals("SansSerif", mixed.getFontHandler().getFont(false).getFamily());
		});
	}

	@Test
	public void documentedCommentAndAutocompleteUseCanonicalFamilies() throws Exception {
		onEdt(() -> {
			NewGridElement actual = element(diagram("Serif"), ElementId.Text, SAMPLE + "\nfontfamily=SansSerif //possible: SansSerif,Serif,Monospaced");
			assertImagesEqual(render(element(diagram("SansSerif"), ElementId.Text, SAMPLE)), render(actual));
			for (String family : FAMILIES) {
				assertTrue(actual.getAutocompletionList().stream().anyMatch(t -> t.getText().equals("fontfamily=" + family)));
			}
			// Delayed painting must use the style captured when the text was parsed.
			drawer(actual).setFontFamily("Monospaced");
			assertImagesEqual(render(element(diagram("SansSerif"), ElementId.Text, SAMPLE)), render(actual));
			actual.setPanelAttributes(SAMPLE + "\n// fontfamily=Monospaced");
			assertImagesEqual(render(element(diagram("Serif"), ElementId.Text, SAMPLE)), render(actual));
		});
	}

	private static DiagramHandler diagram(String family) {
		DiagramHandler diagram = new DiagramHandler(null);
		diagram.getFontHandler().setDiagramDefaultFontFamily(family);
		return diagram;
	}

	private static NewGridElement element(DiagramHandler diagram, ElementId id, String properties) {
		return ElementFactorySwing.create(id, new Rectangle(0, 0, 230, 90), properties, "", diagram);
	}

	private static DrawHandler drawer(GridElement element) {
		return element.getComponent().getDrawHandler();
	}

	private static BufferedImage render(GridElement element) {
		BufferedImage image = new BufferedImage(element.getRectangle().width, element.getRectangle().height, BufferedImage.TYPE_INT_RGB);
		Graphics2D graphics = image.createGraphics();
		try {
			graphics.setColor(Color.WHITE);
			graphics.fillRect(0, 0, image.getWidth(), image.getHeight());
			((Component) element.getComponent()).paint(graphics);
		} finally {
			graphics.dispose();
		}
		return image;
	}

	private static int[] pixels(BufferedImage image) {
		return image.getRGB(0, 0, image.getWidth(), image.getHeight(), null, 0, image.getWidth());
	}

	private static void assertImagesEqual(BufferedImage expected, BufferedImage actual) {
		assertEquals(expected.getWidth(), actual.getWidth());
		assertEquals(expected.getHeight(), actual.getHeight());
		assertArrayEquals(pixels(expected), pixels(actual));
	}

	private static byte[] export(DiagramHandler diagram, String format) throws Exception {
		ByteArrayOutputStream output = new ByteArrayOutputStream();
		OutputHandler.createToStream(format, output, diagram);
		return output.toByteArray();
	}

	private interface CheckedRunnable {
		void run() throws Exception;
	}

	private static void onEdt(CheckedRunnable task) throws Exception {
		SwingUtilities.invokeAndWait(() -> {
			try {
				task.run();
			} catch (Exception e) {
				throw new RuntimeException(e);
			}
		});
	}
}
