package com.baselet.element.relation.facet;

import java.util.Arrays;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.baselet.control.SharedUtils;
import com.baselet.control.basics.geom.Line;
import com.baselet.control.enums.LineType;
import com.baselet.control.enums.RegexValueHolder;
import com.baselet.diagram.draw.DrawHandler;
import com.baselet.diagram.draw.helper.ColorOwn;
import com.baselet.diagram.draw.helper.ColorOwn.Transparency;
import com.baselet.diagram.draw.helper.StyleException;
import com.baselet.element.facet.FirstRunKeyValueFacet;
import com.baselet.element.facet.PropertiesParserState;
import com.baselet.element.relation.helper.RelationPointHandler;
import com.baselet.element.relation.helper.ResizableObject;
import com.baselet.element.sticking.PointDoubleIndexed;

/**
 * must be in first-run because it manipulates the relation position and size (in case of [text]-[text] line which can possibly exceed the old relation size)
 * must handle values in parsingFinished when drawer-setup is finished
 */
public class RelationLineTypeFacet extends FirstRunKeyValueFacet {

	static final String KEY = "lt";

	private static class Match<T extends RegexValueHolder> {
		private final String text;
		private final T type;

		public Match(String matchedText, T matchedObject) {
			super();
			this.text = matchedText;
			this.type = matchedObject;
		}

	}

	public static final RelationLineTypeFacet INSTANCE = new RelationLineTypeFacet();

	private RelationLineTypeFacet() {}

	private final Logger log = LoggerFactory.getLogger(RelationLineTypeFacet.class);

	/**
	 * all arrowtypes and linetypes to expect (order is important because eg << must be before < to be recognized correctly, therefore there are 2 shared lists. also linetype .. must be before .)
	 */
	private static final List<ArrowEnd> SHARED_ARROW_STRINGS_BEFORE = Arrays.asList(ArrowEnd.CIRCLE_CROSS, ArrowEnd.CIRCLE, ArrowEnd.DIAGONAL_CROSS);
	private static final List<ArrowEnd> SHARED_ARROW_STRINGS_AFTER = Arrays.asList(ArrowEnd.DOUBLE_PIPE, ArrowEnd.SINGLE_PIPE, ArrowEnd.SMALL_CIRCLE, ArrowEnd.BOX);
	private static final List<ArrowEnd> LEFT_ARROW_STRINGS = SharedUtils.mergeLists(SHARED_ARROW_STRINGS_BEFORE, Arrays.asList(ArrowEnd.LEFT_BOX, ArrowEnd.LEFT_FILLED_DIAMOND, ArrowEnd.LEFT_DIAMOND, ArrowEnd.LEFT_FILLED_CLOSED, ArrowEnd.LEFT_CLOSED, ArrowEnd.LEFT_NORMAL, ArrowEnd.LEFT_ZERO_TO_MANY, ArrowEnd.LEFT_ONE_TO_MANY, ArrowEnd.LEFT_INVERTED, ArrowEnd.LEFT_INTERFACE_OPEN, ArrowEnd.LEFT_MEASURE_NORMAL), SHARED_ARROW_STRINGS_AFTER);
	private static final List<ArrowEnd> RIGHT_ARROW_STRINGS = SharedUtils.mergeLists(SHARED_ARROW_STRINGS_BEFORE, Arrays.asList(ArrowEnd.RIGHT_BOX, ArrowEnd.RIGHT_FILLED_DIAMOND, ArrowEnd.RIGHT_DIAMOND, ArrowEnd.RIGHT_FILLED_CLOSED, ArrowEnd.RIGHT_CLOSED, ArrowEnd.RIGHT_MEASURE_NORMAL, ArrowEnd.RIGHT_NORMAL, ArrowEnd.RIGHT_ZERO_TO_MANY, ArrowEnd.RIGHT_ONE_TO_MANY, ArrowEnd.RIGHT_INVERTED, ArrowEnd.RIGHT_INTERFACE_OPEN), SHARED_ARROW_STRINGS_AFTER);
	private static final List<LineType> LINE_TYPES = Arrays.asList(LineType.SOLID, LineType.DOTTED, LineType.DASHED);

	public RelationPointHandler getRelationPoints(PropertiesParserState state) {
		return ((SettingsRelation) state.getSettings()).getRelationPoints();
	}

	private static class Remaining {
		String value;

		public Remaining(String value) {
			this.value = value;
		}

		@Override
		public String toString() { // is used in error message
			return value;
		}

	}

	@Override
	public void handleValue(String value, PropertiesParserState state) {}

	private <T extends RegexValueHolder> String listToString(List<T> valueHolderList) {
		StringBuilder sb = new StringBuilder();
		for (RegexValueHolder r : valueHolderList) {
			String simpleRegex = r.getRegexValue().replace(ArrowEnd.BOX_REGEX, "[text]").replaceAll("\\\\", "");
			sb.append(simpleRegex).append(',');
		}
		sb.setLength(sb.length() - 1);
		return sb.toString();
	}

	/**
	 * if no explicit linetype is set, draw a solid line without arrows
	 */
	public static void drawDefaultLineAndArrows(DrawHandler drawer, RelationPointHandler relationPoints) {
		drawLineAndArrows(drawer, relationPoints, new Match<LineType>("", LineType.SOLID), new Match<ArrowEnd>("", null), new Match<ArrowEnd>("", null));
	}

	private static void drawLineAndArrows(DrawHandler drawer, RelationPointHandler relationPoints, Match<LineType> lineType, Match<ArrowEnd> leftArrow, Match<ArrowEnd> rightArrow) {
		drawLineBetweenPoints(drawer, relationPoints, lineType.type, leftArrow.type != null, rightArrow.type != null);
		drawArrowEnds(drawer, relationPoints, leftArrow, rightArrow);
		relationPoints.resizeRectAndReposPoints(); // line description and relation-endings can change the relation size, therefore recalc it now
	}

	private static void drawArrowEnds(DrawHandler drawer, RelationPointHandler relationPoints, Match<ArrowEnd> leftArrow, Match<ArrowEnd> rightArrow) {
		ColorOwn oldBgColor = drawer.getBackgroundColor();
		drawer.setBackgroundColor(oldBgColor.transparency(Transparency.FOREGROUND)); // arrow background is not transparent
		print(drawer, relationPoints, leftArrow, relationPoints.getFirstLine(), true);
		print(drawer, relationPoints, rightArrow, relationPoints.getLastLine(), false);
		drawer.setBackgroundColor(oldBgColor); // reset background
	}

	private static void print(DrawHandler drawer, ResizableObject relationPoints, Match<ArrowEnd> match, Line line, boolean drawOnLineStart) {
		relationPoints.resetPointMinSize(((PointDoubleIndexed) line.getPoint(drawOnLineStart)).getIndex());
		if (match.type != null) {
			match.type.print(drawer, line, drawOnLineStart, match.text, relationPoints);
		}
	}

	private static void drawLineBetweenPoints(DrawHandler drawer, RelationPointHandler relationPoints, LineType lineType, boolean shortLeftLine, boolean shortRightLine) {
		LineType oldLt = drawer.getLineType();
		drawer.setLineType(lineType);
		relationPoints.drawLinesBetweenPoints(drawer, shortLeftLine, shortRightLine);
		drawer.setLineType(oldLt);
	}

	private <T extends RegexValueHolder> Match<T> extractPart(List<T> valueHolderList, Remaining remaining) {
		for (T valueHolder : valueHolderList) {
			String regex = "^" + valueHolder.getRegexValue(); // only match from start of the line (left to right)
			String newRemainingValue = remaining.value.replaceFirst(regex, "");
			if (!remaining.value.equals(newRemainingValue)) {
				String removedPart = remaining.value.substring(0, remaining.value.length() - newRemainingValue.length());
				remaining.value = newRemainingValue;
				return new Match<T>(removedPart, valueHolder);
			}
		}
		return new Match<T>("", null);
	}

	private String getValueNotNull(Match<? extends RegexValueHolder> valueHolder) {
		if (valueHolder.type == null) {
			return "";
		}
		else {
			return valueHolder.type.getRegexValue();
		}
	}

	@Override
	public void parsingFinished(PropertiesParserState state, List<String> handledLines) {
		if (handledLines.isEmpty()) {
			drawDefaultLineAndArrows(state.getDrawer(), getRelationPoints(state));
		}
		else if (handledLines.size() == 1) {
			RelationPointHandler relationPoints = getRelationPoints(state);
			String value = extractValue(handledLines.get(0));
			Remaining remaining = new Remaining(value);

			Match<ArrowEnd> leftArrow = extractPart(LEFT_ARROW_STRINGS, remaining);
			Match<LineType> lineType = extractPart(LINE_TYPES, remaining);
			if (leftArrow.type == null && lineType.type == null) {
				throw new StyleException("left arrow must be one of the following or empty:\n" + listToString(LEFT_ARROW_STRINGS));
			}
			if (lineType.type == null) {
				throw new StyleException("lineType must be specified. One of: " + listToString(LINE_TYPES));
			}
			Match<ArrowEnd> rightArrow = extractPart(RIGHT_ARROW_STRINGS, remaining);
			if (rightArrow.type == null && !remaining.value.isEmpty()) {
				throw new StyleException("right arrow must be one of the following or empty:\n" + listToString(RIGHT_ARROW_STRINGS));
			}
			if (!remaining.value.isEmpty()) {
				throw new StyleException("Unknown part after rightArrow: " + remaining);

			}
			log.debug("Split Relation " + value + " into following parts: " + getValueNotNull(leftArrow) + " | " + getValueNotNull(lineType) + " | " + getValueNotNull(rightArrow));

			drawLineAndArrows(state.getDrawer(), relationPoints, lineType, leftArrow, rightArrow);
			relationPoints.resizeRectAndReposPoints(); // apply the (possible) changes now to make sure the following facets use correct coordinates
		}
		else {
			throw new StyleException("Only one lineType allowed");
		}
	}

	@Override
	public KeyValue getKeyValue() {
		return new KeyValue(
				KEY,
				new ValueInfo("<-", "left arrow", "iVBORw0KGgoAAAANSUhEUgAAAEkAAAAQCAAAAAB/mQ0/AAAACXBIWXMAAAsTAAALEwEAmpwYAAAABGdBTUEAALGOfPtRkwAAACBjSFJNAAB6JQAAgIMAAPn/AACA6QAAdTAAAOpgAAA6mAAAF2+SX8VGAAAAq0lEQVR42mL8z4AAX3gYyAdMCOabUsUf1DDpR4fqld0cFJjE8P////////+eL2Oy/z9FAGLSdgOV5b//U27SaQeR7u///1Ns0u0Inor3/ykHDLcZqAP+M/x/HuJw/T813PT////1Ks2/KTeJiYGBIeD8S8MTVPDf//////8/rpHzmQpuYmBgsLgsbLiFKm76////bZuQ55SncQiYrvCaApMYkUuVDwIUeA4wABeKjdP2NRIjAAAAAElFTkSuQmCC")
				, new ValueInfo("|<-", "left measure-arrow", "iVBORw0KGgoAAAANSUhEUgAAAEkAAAAQCAAAAAB/mQ0/AAAAAmJLR0QA/4ePzL8AAAAJcEhZcwAACxMAAAsTAQCanBgAAAAHdElNRQffBxQPHSyED19AAAAAp0lEQVQ4y2P8z/ifAQa+8DCQD5gQzDelij+oYdKPDtUruzkoMInhP8P//////54vY7L/P0UAYtJ2A5Xlv/9TbtJpB5Hu7///U25SBE/F+/+UA4bbDNQB/xn/M4a8ma5BFbMY/q9Xaf5Nue+YGBgYAs6/NDxBFTf9////uEbOZyq4iYGBweKysOEWqrjp////t21CnlOexiFgusJrCkxiRC5VPghQ4DkAWvhD8uO+gNEAAAAA")
				, new ValueInfo("<.", "left arrow with dashed line", "iVBORw0KGgoAAAANSUhEUgAAAEkAAAAQCAAAAAB/mQ0/AAAACXBIWXMAAAsTAAALEwEAmpwYAAAABGdBTUEAALGOfPtRkwAAACBjSFJNAAB6JQAAgIMAAPn/AACA6QAAdTAAAOpgAAA6mAAAF2+SX8VGAAAAsUlEQVR42mL8z4AAX3gYyAdMCOabUsUf1DDpR4fqld0cFJjE8P////////+eL2Oy/z9FAGLSdgOV5b//U27SaQeR7u///1Ns0u0Inor3/ykHDLdhAVb///////Xkcur/M/x/HuJw/T813PT////1Ks2/KTeJiYGBIeD8S8MTDBQDiIHHNXI+U8FNDAwMFpeFDbdQxU3///+/bRPynPI0DgHTFV5TYBIjcqnyQYACzwEGAD/RpzQKsM6sAAAAAElFTkSuQmCC")
				, new ValueInfo("<..", "left arrow with dotted line", "iVBORw0KGgoAAAANSUhEUgAAAEkAAAAQCAAAAAB/mQ0/AAAACXBIWXMAAAsTAAALEwEAmpwYAAAABGdBTUEAALGOfPtRkwAAACBjSFJNAAB6JQAAgIMAAPn/AACA6QAAdTAAAOpgAAA6mAAAF2+SX8VGAAAArElEQVR42mL8z4AAX3gYyAdMCOabUsUf1DDpR4fqld0cFJjE8P////////+eL2Oy/z9FAGLSdgOV5b//U27SaQeR7u///1Ns0u0Inor3/ykHDLcZGB7X1/+nHDH8fx7icP0/Ndz0////9SrNvyk3iYmBgSHg/EvDEwwUA4iBxzVyPlPBTQwMDBaXhQ23UMVN////v20T8pzyNA4B0xVeU2ASI3Kp8kGAAs8BBgAP7rtyPzvx8QAAAABJRU5ErkJggg==")
				, new ValueInfo("<<-", "closed left arrow", "iVBORw0KGgoAAAANSUhEUgAAAEkAAAAQCAAAAAB/mQ0/AAAACXBIWXMAAAsTAAALEwEAmpwYAAAABGdBTUEAALGOfPtRkwAAACBjSFJNAAB6JQAAgIMAAPn/AACA6QAAdTAAAOpgAAA6mAAAF2+SX8VGAAAArElEQVR42mL8z4AAd1QYyAdMCOabUlUGapj0o0P1CgNF4P////////89X8Zk/38ohzwA0bzdQGX57/+Um3TaQaT7O8JYsk26HcFT8R7ZgeSadJuB4TGcQ0l4M/x/HuJw/T813PT////1Ks2/KTeJiYGBIeD8S8MTDBQDiIHHNXI+U8FNDAwMFpeFDbdQ5iRGeITdSZRY858qOVjlsDPDG6q4iYGB4YMABSYBBgB/rAWtC0QJWQAAAABJRU5ErkJggg==")
				, new ValueInfo("<<<-", "filled closed left arrow", "iVBORw0KGgoAAAANSUhEUgAAAEkAAAAQCAAAAAB/mQ0/AAAACXBIWXMAAAsTAAALEwEAmpwYAAAABGdBTUEAALGOfPtRkwAAACBjSFJNAAB6JQAAgIMAAPn/AACA6QAAdTAAAOpgAAA6mAAAF2+SX8VGAAAAmklEQVR42mL8z4AAd1QYyAdMCOabCFUGSsB/KPhewsYI55ADoJp/z+JjQjKWfJO2y7KgOJBck04bs6J5lTyTbgeyMDFQw6TbDNQB/xn+P/cUZaaKm/7//79eVpJKJv3/nCUpRrFJjJDcciKB9cYfBob/lOcWiyvB0taUBTkj3Bl3Ev8d+0+NfPf////pDK8pDycI+CBAgZMAAwDOwWE1BFRLXwAAAABJRU5ErkJggg==")
				, new ValueInfo("<<<<-", "diamond left arrow", "iVBORw0KGgoAAAANSUhEUgAAAEkAAAAQCAAAAAB/mQ0/AAAACXBIWXMAAAsTAAALEwEAmpwYAAAABGdBTUEAALGOfPtRkwAAACBjSFJNAAB6JQAAgIMAAPn/AACA6QAAdTAAAOpgAAA6mAAAF2+SX8VGAAAA3UlEQVR42mL8z4AAPzgYyAdMCOabUs7EO1Qw6UeH6pXVDLoUmPX/////////ni9jsv////+3EzgSbv8nC0BM2m6gsvw3RIBssxj+//9/2kGk+ztCiEyzGP7fjuCpeI8qSJZZDLcZGB5jCl8nPbwZ/j8PcbiObtBhg5jH5ITTepXm38hi92MsDpMZd59zdI7DRb43K8z+TXYq+H9cI+czhLVao+Q9Benp///f9Sqb////f93F5fr//xSZ9P//bZuQ6zkam///p9ik//+nMxR8J9ug/4zIpcobEQpKFcAAdWYW1hzT7fsAAAAASUVORK5CYII=")
				, new ValueInfo("<<<<<-", "filled diamond left arrow", "iVBORw0KGgoAAAANSUhEUgAAAEkAAAAQCAAAAAB/mQ0/AAAACXBIWXMAAAsTAAALEwEAmpwYAAAABGdBTUEAALGOfPtRkwAAACBjSFJNAAB6JQAAgIMAAPn/AACA6QAAdTAAAOpgAAA6mAAAF2+SX8VGAAAAyUlEQVR42mL8z4AAPzgYyAdMCOabCE6NSxQY9R8KvpewMTIwMJrc+E8mgJr0exYf1HVM3rcpMWm7LAuShwNvk2vSaWNWFA+zskTdJsek24EsTOiBx8GaQLJZDLcZqAP+M/x/7inKjC7MLx39mJxwWi8riWIOs7bJYTLj7nOWpBjCIHWp2b/JT0/H1XWg6UBCruQ9JSnz/+96eWsGBgZBVafrZKZxRngOvpP47wX7734fivPd////pzMUfP9PNmBELlXeiFCQoAADAGzWnc70WHTSAAAAAElFTkSuQmCC")
				, new ValueInfo("()-", "circle left arrow", "iVBORw0KGgoAAAANSUhEUgAAAEoAAAAXCAAAAACJq4aEAAAACXBIWXMAAAsTAAALEwEAmpwYAAAABGdBTUEAALGOfPtRkwAAACBjSFJNAAB6JQAAgIMAAPn/AACA6QAAdTAAAOpgAAA6mAAAF2+SX8VGAAAA/0lEQVR42mL8z0AtwARjXKi15WRkNS09QalRNzwD2Zvf//88WTzV9Qq5Zv3//////90S/b//Q8Fkic3/yQIM/////79b5TiS0GWFzWQbdV3iOIrYZYnL5Brl0Y8mONmFTKPOK/xGF9U5ToZRTAwMa5NZ0OMifi15ieGADYaozQEyjGL8z8D5ngNd9Afvb7KMYsSSdxjJSKAsDAwcPzBdxfKbrLAywcx2FwzIC3aHIxiiRxzIy4NUTFcGGlPQjJ8iYUFmyUDFPIheMqiQXzL8/79bhFrl1f//1z0Umvd////9eLeOy2XyTPoPT+oX1h4484PFwCHYgtzymJF6NQ5gAGG56ureP9K3AAAAAElFTkSuQmCC")
				, new ValueInfo("(+)-", "circle left arrow with plus symbol", "iVBORw0KGgoAAAANSUhEUgAAAEoAAAAXCAAAAACJq4aEAAAACXBIWXMAAAsTAAALEwEAmpwYAAAABGdBTUEAALGOfPtRkwAAACBjSFJNAAB6JQAAgIMAAPn/AACA6QAAdTAAAOpgAAA6mAAAF2+SX8VGAAABD0lEQVR42mL8z0AtwARjXKi15WRkNS09QalRNzwD2Zvf//88WTzV9Qq5Zv3//////90S/b//Q8Fkic3/yQIM/////79b5TiS0GWFzWQbdV3iOIrYZYnL5Brl0Y/sxP///092IdOo8wq/0Yz6r3OcDKOYGBjWJrOgx0X8WvISwwEbDFGbA2QYxfifgfM9B4IDAT94f5NlFON/BgYGRqSUhsolOoGyMDBw/OCA6EdyFctvssLKBDPbXTAgL9gdjmCIHnEgLw9SMV0ZaExBM36KhAWZJQMV8yB6yaBCfsnw//9uEWqVV///X/dQaN7//f/34906LpfJM+k/PFVeWHvgzA8WA4dgC3LLY0bq1TiAAQAcj+vskkcbaQAAAABJRU5ErkJggg==")
				, new ValueInfo("x-", "diagonally crossed left arrow", "iVBORw0KGgoAAAANSUhEUgAAAEoAAAAXCAAAAACJq4aEAAAACXBIWXMAAAsTAAALEwEAmpwYAAAABGdBTUEAALGOfPtRkwAAACBjSFJNAAB6JQAAgIMAAPn/AACA6QAAdTAAAOpgAAA6mAAAF2+SX8VGAAAAf0lEQVR42uzUwQ3AIAgFUHQCjq7gnB3J3uhOsgG9WErTS4vcWk6GxBejX5JAVGX4CsWmyVMU11V7W/VZMoqw3VbvSikVvJKhhuGWLCWEbUK6UEK4+CWBqI8jkiHoVCYMoXcV94JxuYpLey/nfirdQ6UjDIxmMqAnDukfyI9rHwBCzBVSiqTGJwAAAABJRU5ErkJggg==")
				, new ValueInfo("[txt]-", "box end with text", "iVBORw0KGgoAAAANSUhEUgAAAEoAAAAXCAAAAACJq4aEAAAACXBIWXMAAAsTAAALEwEAmpwYAAAABGdBTUEAALGOfPtRkwAAACBjSFJNAAB6JQAAgIMAAPn/AACA6QAAdTAAAOpgAAA6mAAAF2+SX8VGAAAAzUlEQVR42mL8z0AtwMLAiF3iPxlGYdfESLqrmKjmv1GjyDDqOQM6g2TA+J/xPwMDAwPPF6gAlMFIVrpiYGBguPB1jwibFsONX/++7nFiosiDhxhWfrN5+drx+yGGlf8gziIVwD3I+J+h/uoXtyKYz8jwIJJR//T4jzJQYBRSuNx6++AGRanhP8P//////2f4/Vd3xSrd3/8ZfkP4/0kGMKNsOFNC//8PLP1vw3mdTKNgYcXwB5osoAwKgh1TfJhkZxYcZS8jOUZRrcoBDABbMrpBXVzS8wAAAABJRU5ErkJggg==")
				, new ValueInfo("[txt]<-", "box end with text and arrow pointing to it", "iVBORw0KGgoAAAANSUhEUgAAAEoAAAAXCAAAAACJq4aEAAAACXBIWXMAAAsTAAALEwEAmpwYAAAABGdBTUEAALGOfPtRkwAAACBjSFJNAAB6JQAAgIMAAPn/AACA6QAAdTAAAOpgAAA6mAAAF2+SX8VGAAABPklEQVR42qyVv0sDMRTHX6SDpQ4KDg4OCicUpKDgJDc4OStU8A+40UkouHVwEZw6dXRUXBwcHG5wEgsOh2Cp2KVT/VVRsGAhHB8HSe/AGy7HvS0v5JNvXr55UUheURCVPEEGVPIiZa9qIrfzpUEN80INaoujXFCjo6UHfzKlLISkEAB9Mr92TdowqL5J9CPU1YpzqrFGlUyiZPJ3G7PHP2CNCsQP2tC5D8QPAWR36uATMqAa4t3MvLzNtRriaaBrbysUChERhdTbw819+RuKqg6aZVuYmDKHy+vm6kC4cA611QFjvnr66D3G9tgKXldbmVTpsHJ2XtGIjnx1W977tjeDW/R2YLuGW+yMUei6c2mNYlyXmCqg61afbVFJDweA5sJ7OpQxw7/WF2W/ptNVPQXKoiGrnBpyQXL7cn4HACsYkiEdOFqWAAAAAElFTkSuQmCC"));
	}
}
