package com.baselet.element.facet.customdrawings;

import static org.junit.Assert.assertEquals;

import com.baselet.diagram.draw.helper.ColorOwn;
import com.baselet.diagram.draw.helper.theme.Theme;
import com.baselet.diagram.draw.helper.theme.ThemeFactory;
import org.junit.Before;
import org.junit.Test;

import com.baselet.control.enums.AlignHorizontal;
import com.baselet.control.enums.LineType;
import com.baselet.diagram.draw.helper.ColorOwn.Transparency;

public class StyleOptionsTest {
	private DummyDrawHandler drawHandler;
	private final Theme theme = ThemeFactory.getCurrentTheme();

	@Before
	public void before() {
		drawHandler = new DummyDrawHandler();
	}

	@Test
	public void drawArcParameters() {
		new CustomDrawingParserImpl("drawArc(width / 2 , height * 0.5  , 3 + 2 , 4 *3 , 5 - 1 , 1 + 6 * 2  , false  ) lt=- lw=25 bg=black fg=pink", 100, 200, drawHandler).parse();
		assertEquals(DummyDrawHandler.drawArcToString(50, 100, 5, 12, 4, 13, false,
				theme.forString("pink", Transparency.FOREGROUND),
				theme.forString("black", Transparency.BACKGROUND),
				LineType.SOLID, 25.0), drawHandler.getLastDrawCall());
		checkDefaultSettingsRestored();
	}

	@Test
	public void drawCircleParameters() {
		new CustomDrawingParserImpl("drawCircle(width/2, height * 0.5, 10) lt=- lw=25 bg=black fg=pink", 100, 200, drawHandler).parse();
		assertEquals(DummyDrawHandler.drawCircleToString(50, 100, 10,
				theme.forString("pink", Transparency.FOREGROUND),
				theme.forString("black", Transparency.BACKGROUND),
				LineType.SOLID, 25.0), drawHandler.getLastDrawCall());
		checkDefaultSettingsRestored();
	}

	@Test
	public void drawEllipseParametersLtOrder() {
		new CustomDrawingParserImpl("drawEllipse(1,2,3,4) lt=: bg=black lt=- lw=25 lt=. fg=pink lt=..", 30, 40, drawHandler).parse();
		assertEquals(DummyDrawHandler.drawEllipseToString(1, 2, 3, 4,
				theme.forString("pink", Transparency.FOREGROUND),
				theme.forString("black", Transparency.BACKGROUND),
				LineType.DOUBLE_DASHED, 25.0), drawHandler.getLastDrawCall());
		checkDefaultSettingsRestored();
	}

	@Test
	public void drawLineParametersLwOrder() {
		new CustomDrawingParserImpl("drawLine(1,2,3,4) lw=25 lw=35 lt=- lw=5 fg=pink lw=1", 30, 40, drawHandler).parse();
		assertEquals(DummyDrawHandler.drawLineToString(1, 2, 3, 4,
				theme.forString("pink", Transparency.FOREGROUND),
				LineType.SOLID, 25.0), drawHandler.getLastDrawCall());
		checkDefaultSettingsRestored();
	}

	@Test(expected = CustomDrawingParserRuntimeException.class)
	public void invalidDrawLineParameterBg() {
		new CustomDrawingParserImpl("drawLine(1,2,3,4) bg=red", 0, 0, drawHandler).parse();
	}

	@Test
	public void drawRectangleParameters() {
		new CustomDrawingParserImpl("drawRectangle(0,0,width,height) lt=- lw=25 bg=black fg=pink", 30, 40, drawHandler).parse();
		assertEquals(DummyDrawHandler.drawRectangleToString(0, 0, 30, 40,
				theme.forString("pink", Transparency.FOREGROUND),
				theme.forString("black", Transparency.BACKGROUND),
				LineType.SOLID, 25.0), drawHandler.getLastDrawCall());
		checkDefaultSettingsRestored();
	}

	/**
	 * draw Method doesn't override settings, but should use the previous set options
	 */
	@Test
	public void drawRectangleParametersNoDefaultUsed() {
		ColorOwn fg = theme.getColor(Theme.PredefinedColors.ORANGE);
		ColorOwn bg = theme.getColor(Theme.PredefinedColors.DARK_GRAY).transparency(Transparency.BACKGROUND);
		LineType lt = LineType.DOUBLE_DOTTED;
		double lw = 10;
		drawHandler.setForegroundColor(fg);
		drawHandler.setBackgroundColor(bg);
		drawHandler.setLineType(lt);
		drawHandler.setLineWidth(lw);

		new CustomDrawingParserImpl("drawRectangle(0,0,width,height)", 30, 40, drawHandler).parse();
		assertEquals(DummyDrawHandler.drawRectangleToString(0, 0, 30, 40,
				fg,
				bg,
				lt, lw), drawHandler.getLastDrawCall());

		assertEquals(fg, drawHandler.getForegroundColor());
		assertEquals(bg, drawHandler.getBackgroundColor());
		assertEquals(lt, drawHandler.getLineType());
		assertEquals(lw, drawHandler.getLineWidth(), 0.01);
	}

	/**
	 * check if the values are reset to the previous set values
	 */
	@Test
	public void drawRectangleParametersNoDefaultOverrideReset() {
		ColorOwn fg = theme.getColor(Theme.PredefinedColors.ORANGE);
		ColorOwn bg = theme.getColor(Theme.PredefinedColors.DARK_GRAY).transparency(Transparency.BACKGROUND);
		LineType lt = LineType.DOUBLE_DOTTED;
		double lw = 10;
		drawHandler.setForegroundColor(fg);
		drawHandler.setBackgroundColor(bg);
		drawHandler.setLineType(lt);
		drawHandler.setLineWidth(lw);

		new CustomDrawingParserImpl("drawRectangle(0,0,width,height) lt=- lw=25 bg=black fg=pink", 30, 40, drawHandler).parse();
		assertEquals(DummyDrawHandler.drawRectangleToString(0, 0, 30, 40,
				theme.forString("pink", Transparency.FOREGROUND),
				theme.forString("black", Transparency.BACKGROUND),
				LineType.SOLID, 25.0), drawHandler.getLastDrawCall());

		assertEquals(fg, drawHandler.getForegroundColor());
		assertEquals(bg, drawHandler.getBackgroundColor());
		assertEquals(lt, drawHandler.getLineType());
		assertEquals(lw, drawHandler.getLineWidth(), 0.01);
	}

	@Test
	public void drawRectangleParametersBgOrder() {
		new CustomDrawingParserImpl("drawRectangle(0,0,width,height) bg=black lt=- bg=red lw=25 bg=blue fg=pink bg=#AAFFBB", 30, 40, drawHandler).parse();
		assertEquals(DummyDrawHandler.drawRectangleToString(0, 0, 30, 40,
				theme.forString("pink", Transparency.FOREGROUND),
				theme.forString("black", Transparency.BACKGROUND),
				LineType.SOLID, 25.0), drawHandler.getLastDrawCall());
		checkDefaultSettingsRestored();
	}

	@Test
	public void drawRectangleRoundParameters() {
		new CustomDrawingParserImpl("drawRectangleRound(0,0,width,height,2) bg=#FF10A0 fg=#040506 lt=. lw=2.5", 30, 40, drawHandler).parse();
		assertEquals(DummyDrawHandler.drawRectangleRoundToString(0, 0, 30, 40, 2,
				theme.forString("#040506", Transparency.FOREGROUND),
				theme.forString("#FF10A0", Transparency.BACKGROUND),
				LineType.DASHED, 2.5), drawHandler.getLastDrawCall());
		checkDefaultSettingsRestored();
	}

	@Test
	public void drawTextParameterFg() {
		new CustomDrawingParserImpl("drawText(\"Das ist \\\" dfs \", 10, 20, left ) fg=red", 0, 0, drawHandler).parse();
		ColorOwn red = theme.getColor(Theme.PredefinedColors.RED);
		assertEquals(DummyDrawHandler.drawTextToString("Das ist \" dfs ", 10, 20, AlignHorizontal.LEFT, red), drawHandler.getLastDrawCall());
		checkDefaultSettingsRestored();
	}

	@Test
	public void drawTextParameterFgFg() {
		ColorOwn pink = theme.getColor(Theme.PredefinedColors.PINK);
		new CustomDrawingParserImpl("drawText(\"Das ist \\\" dfs \", 10, 20, left ) fg=pink fg=blue", 0, 0, drawHandler).parse();
		assertEquals(DummyDrawHandler.drawTextToString("Das ist \" dfs ", 10, 20, AlignHorizontal.LEFT, pink), drawHandler.getLastDrawCall());
		checkDefaultSettingsRestored();
	}

	@Test(expected = CustomDrawingParserRuntimeException.class)
	public void invalidDrawTextParameterBg() {
		new CustomDrawingParserImpl("drawText(\"Test\",2,3,center) bg=red", 0, 0, drawHandler).parse();
	}

	@Test(expected = CustomDrawingParserRuntimeException.class)
	public void invalidDrawTextParameterLt() {
		new CustomDrawingParserImpl("drawText(\"Test\",width / 2, height/2, right) lt=.", 100, 200, drawHandler).parse();
	}

	@Test(expected = CustomDrawingParserRuntimeException.class)
	public void invalidDrawTextParameterLw() {
		new CustomDrawingParserImpl("drawText(\" äöüß ÄÖÜ ,. #+? \",50,100,right) lw=12", 0, 0, drawHandler).parse();
	}

	private void checkDefaultSettingsRestored() {
		assertEquals(DummyDrawHandler.defaultFg, drawHandler.getForegroundColor());
		assertEquals(DummyDrawHandler.defaultBg, drawHandler.getBackgroundColor());
		assertEquals(DummyDrawHandler.defaultLineType, drawHandler.getLineType());
		assertEquals(DummyDrawHandler.defaultLineWidth, drawHandler.getLineWidth(), 0.01);

	}
}
