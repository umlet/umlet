package com.baselet.element.facet.customdrawings;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;

import com.baselet.control.enums.AlignHorizontal;

public class ValidInputTest {
	private DummyDrawHandler drawHandler;

	@Before
	public void before() {
		drawHandler = new DummyDrawHandler();
	}

	@Test
	public void drawArcParameters1() {
		new CustomDrawingParserImpl("drawArc(1,2,3,4,5,6,true)", 0, 0, drawHandler).parse();
		assertEquals(DummyDrawHandler.drawArcToString(1, 2, 3, 4, 5, 6, true, null, null, null, null), drawHandler.getLastDrawCall());
	}

	@Test
	public void drawArcParameters2() {
		new CustomDrawingParserImpl("drawArc(1,2,3,4,5,6,false)", 0, 0, drawHandler).parse();
		assertEquals(DummyDrawHandler.drawArcToString(1, 2, 3, 4, 5, 6, false, null, null, null, null), drawHandler.getLastDrawCall());
	}

	@Test
	public void drawArcParameters3() {
		new CustomDrawingParserImpl("drawArc(width  , height  , 3  , 4  , 5  , 6  , true  )", 111, 222, drawHandler).parse();
		assertEquals(DummyDrawHandler.drawArcToString(111, 222, 3, 4, 5, 6, true, null, null, null, null), drawHandler.getLastDrawCall());
	}

	@Test
	public void drawArcParameters4() {
		new CustomDrawingParserImpl("drawArc(width  , height  , width  , height  , width  , height  , true  )", 111, 222, drawHandler).parse();
		assertEquals(DummyDrawHandler.drawArcToString(111, 222, 111, 222, 111, 222, true, null, null, null, null), drawHandler.getLastDrawCall());
	}

	@Test
	public void drawArcParameters5() {
		new CustomDrawingParserImpl("drawArc(width / 2 , height * 0.5  , 3 + 2 , 4 *3 , 5 - 1 , 1 + 6 * 2  , false  )", 100, 200, drawHandler).parse();
		assertEquals(DummyDrawHandler.drawArcToString(50, 100, 5, 12, 4, 13, false, null, null, null, null), drawHandler.getLastDrawCall());
	}

	@Test
	public void drawCircleParameters1() {
		new CustomDrawingParserImpl("drawCircle(10,10,10)", 0, 0, drawHandler).parse();
		assertEquals(DummyDrawHandler.drawCircleToString(10, 10, 10, null, null, null, null), drawHandler.getLastDrawCall());
	}

	@Test
	public void drawCircleParameters2() {
		new CustomDrawingParserImpl("drawCircle(1,2,3)", 0, 0, drawHandler).parse();
		assertEquals(DummyDrawHandler.drawCircleToString(1, 2, 3, null, null, null, null), drawHandler.getLastDrawCall());
	}

	@Test
	public void drawCircleParameters3() {
		new CustomDrawingParserImpl("drawCircle(width/2, height * 0.5, 10)", 100, 200, drawHandler).parse();
		assertEquals(DummyDrawHandler.drawCircleToString(50, 100, 10, null, null, null, null), drawHandler.getLastDrawCall());
	}

	@Test
	public void drawEllipseParameters1() {
		new CustomDrawingParserImpl("drawEllipse(1,2,3,4)", 0, 0, drawHandler).parse();
		assertEquals(DummyDrawHandler.drawEllipseToString(1, 2, 3, 4, null, null, null, null), drawHandler.getLastDrawCall());
	}

	@Test
	public void drawEllipseParameters2() {
		new CustomDrawingParserImpl("drawEllipse(10,10,width-20,height-20)", 100, 200, drawHandler).parse();
		assertEquals(DummyDrawHandler.drawEllipseToString(10, 10, 80, 180, null, null, null, null), drawHandler.getLastDrawCall());
	}

	@Test
	public void drawLineParameters1() {
		new CustomDrawingParserImpl("drawLine(0,0,width,height)", 100, 200, drawHandler).parse();
		assertEquals(DummyDrawHandler.drawLineToString(0, 0, 100, 200, null, null, null), drawHandler.getLastDrawCall());
	}

	@Test
	public void drawLineParameters2() {
		new CustomDrawingParserImpl("drawLine(1,2,3,4)", 0, 0, drawHandler).parse();
		assertEquals(DummyDrawHandler.drawLineToString(1, 2, 3, 4, null, null, null), drawHandler.getLastDrawCall());
	}

	@Test
	public void drawRectangleParameters1() {
		new CustomDrawingParserImpl("drawRectangle(0,0,width,height)", 30, 40, drawHandler).parse();
		assertEquals(DummyDrawHandler.drawRectangleToString(0, 0, 30, 40, null, null, null, null), drawHandler.getLastDrawCall());
	}

	@Test
	public void drawRectangleParameters2() {
		new CustomDrawingParserImpl("drawRectangle(1,2,3,4)", 0, 0, drawHandler).parse();
		assertEquals(DummyDrawHandler.drawRectangleToString(1, 2, 3, 4, null, null, null, null), drawHandler.getLastDrawCall());
	}

	@Test
	public void drawRectangleRoundParameters1() {
		new CustomDrawingParserImpl("drawRectangleRound(0,0,width,height,2)", 30, 40, drawHandler).parse();
		assertEquals(DummyDrawHandler.drawRectangleRoundToString(0, 0, 30, 40, 2, null, null, null, null), drawHandler.getLastDrawCall());
	}

	@Test
	public void drawRectangleRoundParameters2() {
		new CustomDrawingParserImpl("drawRectangleRound(1,2,30,40,3)", 0, 0, drawHandler).parse();
		assertEquals(DummyDrawHandler.drawRectangleRoundToString(1, 2, 30, 40, 3, null, null, null, null), drawHandler.getLastDrawCall());
	}

	@Test
	public void drawTextParameters1() {
		new CustomDrawingParserImpl("drawText(\"Das ist \\\" dfs \", 10, 20, left )", 0, 0, drawHandler).parse();
		assertEquals(DummyDrawHandler.drawTextToString("Das ist \" dfs ", 10, 20, AlignHorizontal.LEFT, null), drawHandler.getLastDrawCall());
	}

	@Test
	public void drawTextParameters2() {
		new CustomDrawingParserImpl("drawText(\"Test\",2,3,center)", 0, 0, drawHandler).parse();
		assertEquals(DummyDrawHandler.drawTextToString("Test", 2, 3, AlignHorizontal.CENTER, null), drawHandler.getLastDrawCall());
	}

	@Test
	public void drawTextParameters3() {
		new CustomDrawingParserImpl("drawText(\"Test\",width / 2, height/2, right)", 100, 200, drawHandler).parse();
		assertEquals(DummyDrawHandler.drawTextToString("Test", 50, 100, AlignHorizontal.RIGHT, null), drawHandler.getLastDrawCall());
	}

	@Test
	public void drawTextParameters4() {
		new CustomDrawingParserImpl("drawText(\" äöüß ÄÖÜ ,. #+? \",50,100,right)", 0, 0, drawHandler).parse();
		assertEquals(DummyDrawHandler.drawTextToString(" äöüß ÄÖÜ ,. #+? ", 50, 100, AlignHorizontal.RIGHT, null), drawHandler.getLastDrawCall());
	}

	@Test
	public void drawTextParameters5() {
		new CustomDrawingParserImpl("drawText(\"Test\", 2.5, 3.5, left)", 0, 0, drawHandler).parse();
		assertEquals(DummyDrawHandler.drawTextToString("Test", 2.5, 3.5, AlignHorizontal.LEFT, null), drawHandler.getLastDrawCall());
	}

	@Test
	public void drawTextParametersEscaping1() {
		new CustomDrawingParserImpl("drawText(\"Test \\\" \", 2.5, 3.5, left)", 0, 0, drawHandler).parse();
		assertEquals(DummyDrawHandler.drawTextToString("Test \" ", 2.5, 3.5, AlignHorizontal.LEFT, null), drawHandler.getLastDrawCall());
	}

	@Test
	public void drawTextParametersEscaping2() {
		new CustomDrawingParserImpl("drawText(\"Test \\\\ \", 2.5, 3.5, left)", 0, 0, drawHandler).parse();
		assertEquals(DummyDrawHandler.drawTextToString("Test \\ ", 2.5, 3.5, AlignHorizontal.LEFT, null), drawHandler.getLastDrawCall());
	}

	@Test
	public void drawTextParametersEscaping3() {
		new CustomDrawingParserImpl("drawText(\"Test \\\" \\\\\\\\ \", 2.5, 3.5, left)", 0, 0, drawHandler).parse();
		assertEquals(DummyDrawHandler.drawTextToString("Test \" \\\\ ", 2.5, 3.5, AlignHorizontal.LEFT, null), drawHandler.getLastDrawCall());
	}
}
