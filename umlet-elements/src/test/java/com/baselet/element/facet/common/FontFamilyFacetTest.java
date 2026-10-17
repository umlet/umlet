package com.baselet.element.facet.common;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.Test;

import com.baselet.control.StringStyle;
import com.baselet.control.basics.geom.DimensionDouble;
import com.baselet.control.basics.geom.PointDouble;
import com.baselet.control.basics.geom.Rectangle;
import com.baselet.control.enums.AlignHorizontal;
import com.baselet.diagram.draw.helper.Style;
import com.baselet.diagram.draw.helper.StyleException;
import com.baselet.element.elementnew.Text;
import com.baselet.element.facet.PropertiesParserState;
import com.baselet.element.facet.customdrawings.DummyDrawHandler;
import com.baselet.element.interfaces.Component;
import com.baselet.element.interfaces.DrawHandlerInterface;
import com.baselet.element.settings.SettingsText;

public class FontFamilyFacetTest {

	@Test
	public void unsupportedRendererKeepsPropertyAsTextAndDoesNotOfferIt() {
		List<String> printed = new ArrayList<String>();
		DummyDrawHandler drawer = new DummyDrawHandler() {
			@Override
			protected DimensionDouble textDimensionHelper(StringStyle text) {
				return new DimensionDouble(text.getStringWithoutMarkup().length() * 7, 12);
			}

			@Override
			public void printHelper(StringStyle[] lines, PointDouble point, AlignHorizontal align) {
				for (StringStyle line : lines) {
					printed.add(line.getStringWithoutMarkup());
				}
			}
		};
		assertFalse(drawer.supportsFontFamily());
		Component component = mock(Component.class);
		when(component.getDrawHandler()).thenReturn(drawer);
		when(component.getMetaDrawHandler()).thenReturn(drawer);
		when(component.getBoundsRect()).thenReturn(new Rectangle(0, 0, 400, 200));
		DrawHandlerInterface handler = mock(DrawHandlerInterface.class);
		when(handler.getGridSize()).thenReturn(10);
		Text element = new Text();
		element.init(new Rectangle(0, 0, 400, 200), "Sample\nfontfamily=Serif\nfontsize=18", "", component, handler);
		element.updateModelFromText();
		assertTrue(printed.contains("fontfamily=Serif"));
		assertFalse(printed.contains("fontsize=18"));
		assertEquals(18, drawer.getFontSize(), 0);
		assertFalse(element.getAutocompletionList().stream().anyMatch(t -> t.getText().startsWith("fontfamily=")));
		for (String family : Arrays.asList("SansSerif", "Monospaced", "Consolas")) {
			printed.clear();
			element.setPanelAttributes("Sample\nfontfamily=" + family);
			assertTrue(printed.contains("fontfamily=" + family));
		}
	}

	@Test(expected = StyleException.class)
	public void physicalFontsAreNotAccepted() {
		FontFamilyFacet.INSTANCE.handleValue("Consolas", new PropertiesParserState(new SettingsText(), new DummyDrawHandler()));
	}

	@Test
	public void familyIsPartOfCapturedStyleAndEquality() {
		Style inherited = new Style();
		for (String family : Arrays.asList("SansSerif", "Serif", "Monospaced")) {
			Style selected = new Style();
			selected.setFontFamily(family);
			Style captured = selected.cloneFromMe();
			assertEquals(selected, captured);
			assertEquals(selected.hashCode(), captured.hashCode());
			assertNotEquals(inherited, captured);
			selected.setFontFamily(null);
			assertEquals(family, captured.getFontFamily());
			assertEquals(inherited, selected);
		}
	}
}
