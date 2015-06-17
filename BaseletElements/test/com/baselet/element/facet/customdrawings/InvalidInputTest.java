package com.baselet.element.facet.customdrawings;

import org.junit.Test;

public class InvalidInputTest {

	@Test(expected = CustomDrawingParserException.class)
	public void unkownFunction1() throws CustomDrawingParserException {
		new CustomDrawingParserImpl("a", 0, 0, null).parseCheckedEx();
	}

	@Test(expected = CustomDrawingParserException.class)
	public void unkownFunction2() throws CustomDrawingParserException {
		new CustomDrawingParserImpl("draw", 0, 0, null).parseCheckedEx();
	}

	@Test(expected = CustomDrawingParserRuntimeException.class)
	public void unkownFunction3() {
		new CustomDrawingParserImpl("aäöü", 0, 0, null).parse();
	}

	@Test(expected = CustomDrawingParserRuntimeException.class)
	public void unkownFunction4() {
		new CustomDrawingParserImpl("drawRect(10,10,10,10)", 0, 0, null).parse();
	}

	@Test(expected = CustomDrawingParserRuntimeException.class)
	public void drawArcWrongParameters1() {
		new CustomDrawingParserImpl("drawArc()", 0, 0, null).parse();
	}

	@Test(expected = CustomDrawingParserRuntimeException.class)
	public void drawArcWrongParameters2() {
		new CustomDrawingParserImpl("drawArc(1,2,3,4,5,6,false,8)", 0, 0, null).parse();
	}

	@Test(expected = CustomDrawingParserRuntimeException.class)
	public void drawArcWrongParameters3() {
		new CustomDrawingParserImpl("drawArc(1,2,3,4,5,6)", 0, 0, null).parse();
	}

	@Test(expected = CustomDrawingParserRuntimeException.class)
	public void drawArcWrongParameters4() {
		new CustomDrawingParserImpl("drawArc(1,2,3,4,5,6,0)", 0, 0, null).parse();
	}

	@Test(expected = CustomDrawingParserRuntimeException.class)
	public void drawArcWrongParameters5() {
		new CustomDrawingParserImpl("drawArc(1,\"4.5\",3,4,5,6,false)", 0, 0, null).parse();
	}

	@Test(expected = CustomDrawingParserRuntimeException.class)
	public void drawArcOpenCaseSensitiveTokenFalse() {
		new CustomDrawingParserImpl("drawArc(1,2,3,4,5,6,FALSE)", 0, 0, null).parse();
	}

	@Test(expected = CustomDrawingParserRuntimeException.class)
	public void drawArcOpenCaseSensitiveTokenTrue() {
		new CustomDrawingParserImpl("drawArc(1,2,3,4,5,6,TRUE)", 0, 0, null).parse();
	}

	@Test(expected = CustomDrawingParserRuntimeException.class)
	public void drawCircleWrongParameters1() {
		new CustomDrawingParserImpl("drawCircle()", 0, 0, null).parse();
	}

	@Test(expected = CustomDrawingParserRuntimeException.class)
	public void drawCircleWrongParameters2() {
		new CustomDrawingParserImpl("drawCircle(1,2,3,4)", 0, 0, null).parse();
	}

	@Test(expected = CustomDrawingParserRuntimeException.class)
	public void drawCircleWrongParameters3() {
		new CustomDrawingParserImpl("drawCircle(1,2)", 0, 0, null).parse();
	}

	@Test(expected = CustomDrawingParserRuntimeException.class)
	public void drawCircleWrongParameters4() {
		new CustomDrawingParserImpl("drawCircle(1,true,3)", 0, 0, null).parse();
	}

	@Test(expected = CustomDrawingParserRuntimeException.class)
	public void drawCircleWrongParameters5() {
		new CustomDrawingParserImpl("drawCircle(1,\"4.5\",3)", 0, 0, null).parse();
	}

	@Test(expected = CustomDrawingParserRuntimeException.class)
	public void drawEllipseWrongParameters1() {
		new CustomDrawingParserImpl("drawEllipse()", 0, 0, null).parse();
	}

	@Test(expected = CustomDrawingParserRuntimeException.class)
	public void drawEllipseWrongParameters2() {
		new CustomDrawingParserImpl("drawEllipse(1,2,3,4,5)", 0, 0, null).parse();
	}

	@Test(expected = CustomDrawingParserRuntimeException.class)
	public void drawEllipseWrongParameters3() {
		new CustomDrawingParserImpl("drawEllipse(1,2,3)", 0, 0, null).parse();
	}

	@Test(expected = CustomDrawingParserRuntimeException.class)
	public void drawEllipseWrongParameters4() {
		new CustomDrawingParserImpl("drawEllipse(1,true,3,4)", 0, 0, null).parse();
	}

	@Test(expected = CustomDrawingParserRuntimeException.class)
	public void drawEllipseWrongParameters5() {
		new CustomDrawingParserImpl("drawEllipse(1,\"4.5\",3,4)", 0, 0, null).parse();
	}

	@Test(expected = CustomDrawingParserRuntimeException.class)
	public void drawLineWrongParameters1() {
		new CustomDrawingParserImpl("drawLine()", 0, 0, null).parse();
	}

	@Test(expected = CustomDrawingParserRuntimeException.class)
	public void drawLineWrongParameters2() {
		new CustomDrawingParserImpl("drawLine(1,2,3,4,5)", 0, 0, null).parse();
	}

	@Test(expected = CustomDrawingParserRuntimeException.class)
	public void drawLineWrongParameters3() {
		new CustomDrawingParserImpl("drawLine(1,2,3)", 0, 0, null).parse();
	}

	@Test(expected = CustomDrawingParserRuntimeException.class)
	public void drawLineWrongParameters4() {
		new CustomDrawingParserImpl("drawLine(1,true,3,4)", 0, 0, null).parse();
	}

	@Test(expected = CustomDrawingParserRuntimeException.class)
	public void drawLineWrongParameters5() {
		new CustomDrawingParserImpl("drawLine(1,\"4.5\",3,4)", 0, 0, null).parse();
	}

	@Test(expected = CustomDrawingParserRuntimeException.class)
	public void drawRectangleWrongParameters1() {
		new CustomDrawingParserImpl("drawRectangle()", 0, 0, null).parse();
	}

	@Test(expected = CustomDrawingParserRuntimeException.class)
	public void drawRectangleWrongParameters2() {
		new CustomDrawingParserImpl("drawRectangle(1,2,3,4,5)", 0, 0, null).parse();
	}

	@Test(expected = CustomDrawingParserRuntimeException.class)
	public void drawRectangleWrongParameters3() {
		new CustomDrawingParserImpl("drawRectangle(1,2,3)", 0, 0, null).parse();
	}

	@Test(expected = CustomDrawingParserRuntimeException.class)
	public void drawRectangleWrongParameters4() {
		new CustomDrawingParserImpl("drawRectangle(1,true,3,4)", 0, 0, null).parse();
	}

	@Test(expected = CustomDrawingParserRuntimeException.class)
	public void drawRectangleWrongParameters5() {
		new CustomDrawingParserImpl("drawRectangle(1,\"4.5\",3,4)", 0, 0, null).parse();
	}

	@Test(expected = CustomDrawingParserRuntimeException.class)
	public void drawRectangleRoundWrongParameters1() {
		new CustomDrawingParserImpl("drawRectangleRound()", 0, 0, null).parse();
	}

	@Test(expected = CustomDrawingParserRuntimeException.class)
	public void drawRectangleRoundWrongParameters2() {
		new CustomDrawingParserImpl("drawRectangleRound(1,2,3,4,5,6)", 0, 0, null).parse();
	}

	@Test(expected = CustomDrawingParserRuntimeException.class)
	public void drawRectangleRoundWrongParameters3() {
		new CustomDrawingParserImpl("drawRectangleRound(1,2,3,4)", 0, 0, null).parse();
	}

	@Test(expected = CustomDrawingParserRuntimeException.class)
	public void drawRectangleRoundWrongParameters4() {
		new CustomDrawingParserImpl("drawRectangleRound(1,true,3,4,5)", 0, 0, null).parse();
	}

	@Test(expected = CustomDrawingParserRuntimeException.class)
	public void drawRectangleRoundWrongParameters5() {
		new CustomDrawingParserImpl("drawRectangleRound(1,\"4.5\",3,4,5)", 0, 0, null).parse();
	}

	@Test(expected = CustomDrawingParserRuntimeException.class)
	public void drawTextWrongParameters1() {
		new CustomDrawingParserImpl("drawText()", 0, 0, null).parse();
	}

	@Test(expected = CustomDrawingParserRuntimeException.class)
	public void drawTextWrongParameters2() {
		new CustomDrawingParserImpl("drawText(\"Test\",2,3,center,5)", 0, 0, null).parse();
	}

	@Test(expected = CustomDrawingParserRuntimeException.class)
	public void drawTextWrongParameters3() {
		new CustomDrawingParserImpl("drawText(\"Test\",2,3)", 0, 0, null).parse();
	}

	@Test(expected = CustomDrawingParserRuntimeException.class)
	public void drawTextWrongParameters4() {
		new CustomDrawingParserImpl("drawText(\"Test\",true,3,right)", 0, 0, null).parse();
	}

	@Test(expected = CustomDrawingParserRuntimeException.class)
	public void drawTextWrongParameters5() {
		new CustomDrawingParserImpl("drawText(\"Test\",2,3,\"left\")", 0, 0, null).parse();
	}

	@Test(expected = CustomDrawingParserRuntimeException.class)
	public void drawTextAlignmentCaseSensitiveLeft() {
		new CustomDrawingParserImpl("drawText(\"Test\",2,3, LEFT)", 0, 0, null).parse();
	}

	@Test(expected = CustomDrawingParserRuntimeException.class)
	public void drawTextAlignmentCaseSensitiveRight() {
		new CustomDrawingParserImpl("drawText(\"Test\",2,3, RIGHT)", 0, 0, null).parse();
	}

	@Test(expected = CustomDrawingParserRuntimeException.class)
	public void drawTextAlignmentCaseSensitiveCenter() {
		new CustomDrawingParserImpl("drawText(\"Test\",2,3, CENTER)", 0, 0, null).parse();
	}

	@Test(expected = CustomDrawingParserRuntimeException.class)
	public void drawTextBackslashOnly() {
		new CustomDrawingParserImpl("drawText(\"Test\\ \",2,3, right)", 0, 0, null).parse();
	}

	@Test(expected = CustomDrawingParserRuntimeException.class)
	public void drawTextBackslashOdd() {
		new CustomDrawingParserImpl("drawText(\"Test\\\\\\ \",2,3, right)", 0, 0, null).parse();
	}

	@Test(expected = CustomDrawingParserRuntimeException.class)
	public void widthCaseSensitive() {
		new CustomDrawingParserImpl("drawCircle(1,WIDTH,3)", 0, 0, null).parse();
	}

	@Test(expected = CustomDrawingParserRuntimeException.class)
	public void heightCaseSensitive() {
		new CustomDrawingParserImpl("drawCircle(1,HEIGHT,3)", 0, 0, null).parse();
	}
}
