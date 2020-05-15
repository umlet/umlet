package com.baselet.element;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Vector;

import com.baselet.diagram.draw.helper.ColorOwn;
import com.baselet.diagram.draw.helper.Theme;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.baselet.control.SharedUtils;
import com.baselet.control.basics.geom.Dimension;
import com.baselet.control.basics.geom.DimensionDouble;
import com.baselet.control.basics.geom.Line;
import com.baselet.control.basics.geom.Point;
import com.baselet.control.basics.geom.PointDouble;
import com.baselet.control.basics.geom.Rectangle;
import com.baselet.control.config.SharedConfig;
import com.baselet.control.constants.SharedConstants;
import com.baselet.control.enums.AlignHorizontal;
import com.baselet.control.enums.Direction;
import com.baselet.control.enums.ElementStyle;
import com.baselet.control.enums.LineType;
import com.baselet.diagram.draw.DrawHandler;
import com.baselet.diagram.draw.helper.ColorOwn.Transparency;
import com.baselet.element.facet.Facet;
import com.baselet.element.facet.KeyValueFacet;
import com.baselet.element.facet.PropertiesParserState;
import com.baselet.element.facet.Settings;
import com.baselet.element.facet.common.GroupFacet;
import com.baselet.element.facet.common.LayerFacet;
import com.baselet.element.interfaces.Component;
import com.baselet.element.interfaces.CursorOwn;
import com.baselet.element.interfaces.DrawHandlerInterface;
import com.baselet.element.interfaces.GridElement;
import com.baselet.element.interfaces.GridElementDeprecatedAddons;
import com.baselet.element.sticking.PointChange;
import com.baselet.element.sticking.Stickable;
import com.baselet.element.sticking.StickableMap;
import com.baselet.element.sticking.Stickables;
import com.baselet.element.sticking.StickingPolygon;
import com.baselet.gui.AutocompletionText;

public abstract class NewGridElement implements GridElement {

	private final Logger log = LoggerFactory.getLogger(NewGridElement.class);

	private DrawHandler drawer; // this is the drawer for element specific stuff
	private DrawHandler metaDrawer; // this is a separate drawer to draw stickingborder, selection-background etc.

	private Component component;

	private DrawHandlerInterface handler;

	private List<String> panelAttributes;

	protected PropertiesParserState state;

	protected final UndoHistory undoStack = new UndoHistory();

	public void init(Rectangle bounds, String panelAttributes, String additionalAttributes, Component component, DrawHandlerInterface handler) {
		this.component = component;
		drawer = component.getDrawHandler();
		metaDrawer = component.getMetaDrawHandler();
		setPanelAttributesHelper(panelAttributes);
		setRectangle(bounds);
		this.handler = handler;
		state = new PropertiesParserState(createSettings(), drawer);
		setAdditionalAttributes(additionalAttributes);
	}

	@Override
	public String getPanelAttributes() {
		return SharedUtils.listToString("\n", panelAttributes);
	}

	@Override
	public List<String> getPanelAttributesAsList() {
		return panelAttributes;
	}

	@Override
	public void setPanelAttributes(String panelAttributes) {
		setPanelAttributesHelper(panelAttributes);
		updateModelFromText();
	}

	public void setPanelAttributesHelper(String panelAttributes) {
		this.panelAttributes = Arrays.asList(panelAttributes.split("\n", -1)); // split with -1 to retain empty lines at the end
	}

	/**
	 * ugly workaround to avoid that the Resize().execute() call which calls setSize() on this model updates the model during the
	 * calculated model update from autoresize. Otherwise the drawer cache would get messed up (it gets cleaned up 2 times in a row and afterwards everything gets drawn 2 times).
	 * Best testcase is an autoresize element with a background. Write some text and everytime autresize triggers, the background is drawn twice.
	 */
	private boolean autoresizePossiblyInProgress = false;

	@Override
	public void updateModelFromText() {
		autoresizePossiblyInProgress = true;
		drawer.clearCache();
		drawer.resetStyle(); // must be set before actions which depend on the fontsize (otherwise a changed fontsize would be recognized too late)
		try {
			PropertiesParser.parsePropertiesAndHandleFacets(this, state);
		} catch (Exception e) {
			log.info("Cannot parse Properties Text", e);
			drawer.resetStyle();
			String localizedMessage = e.getLocalizedMessage();
			if (localizedMessage == null) {
				localizedMessage = e.toString();
			}
			drawError(drawer, localizedMessage);
		}
		autoresizePossiblyInProgress = false;

		component.afterModelUpdate();
	}

	protected void drawError(DrawHandler drawer, String errorText) {
		ColorOwn red = Theme.getCurrentThemeColor().getColorMap().get(ColorOwn.PredefinedColors.RED);

		drawer.setEnableDrawing(true);
		drawer.setForegroundColor(red);
		drawer.setBackgroundColor(red.transparency(Transparency.SELECTION_BACKGROUND));
		drawer.setLineWidth(0.2);
		drawer.drawRectangle(0, 0, getRealSize().width, getRealSize().height); // draw dotted rect (to enforce background color even if element has no border)
		resetAndDrawMetaDrawerContent(metaDrawer);
		// y coordinate specifies the bottom line of the first text line therefore
		// count the new line chars and calculate offset to place the message in the center
		String[] errorLines = errorText.split("\n");
		double y = getRealSize().height * 0.5 - errorLines.length * drawer.textHeightMaxWithSpace() / 2.0 + drawer.textHeightMax();
		for (int i = 0; i < errorLines.length; i++, y += drawer.textHeightMaxWithSpace()) {
			drawer.print(errorLines[i], 3, y, AlignHorizontal.LEFT);
		}
	}

	void resetMetaDrawerAndDrawCommonContent(PropertiesParserState state, boolean resetMetaDrawer) {
		drawCommonContent(state);
		if (resetMetaDrawer) {
			resetAndDrawMetaDrawerContent(metaDrawer);
		}
	}

	protected abstract void drawCommonContent(PropertiesParserState state);

	protected void resetAndDrawMetaDrawerContent(DrawHandler drawer) {
		ColorOwn currentColor = Theme.getCurrentThemeColor();

		drawer.clearCache();
		drawer.setForegroundColor(currentColor.getStyleColorMap().get(ColorOwn.ColorStyle.SELECTION_FG));
		drawer.setBackgroundColor(currentColor.getStyleColorMap().get(ColorOwn.ColorStyle.SELECTION_BG));
		drawer.drawRectangle(0, 0, getRealSize().width, getRealSize().height);
		if (SharedConfig.getInstance().isDev_mode()) {
			drawer.setForegroundColor(currentColor.getColorMap().get(ColorOwn.PredefinedColors.BLACK));
			drawer.setFontSize(10.5);
			drawer.print(getId().toString(), new PointDouble(getRealSize().width - 3, getRealSize().height - 2), AlignHorizontal.RIGHT);
		}
		drawer.resetColorSettings();
		if (SharedConfig.getInstance().isShow_stickingpolygon()) {
			drawStickingPolygon(drawer);
		}
	}

	@Override
	public void setProperty(String key, Object newValue) {
		String keyWithSep = key + KeyValueFacet.SEP;
		StringBuilder sb = new StringBuilder("");
		for (String line : getPanelAttributesAsList()) {
			if (!line.startsWith(keyWithSep)) {
				sb.append(line).append("\n");
			}
		}
		if (sb.length() > 0) { // remove last linebreak
			sb.setLength(sb.length() - 1);
		}
		if (newValue != null) {
			sb.append("\n").append(keyWithSep).append(newValue.toString()); // null will not be added as a value
		}
		setPanelAttributes(sb.toString());
	}

	@Override
	public String getSetting(String key) {
		for (String line : getPanelAttributesAsList()) {
			if (line.startsWith(key + KeyValueFacet.SEP)) {
				String[] split = line.split(KeyValueFacet.SEP, 2);
				if (split.length > 1) {
					return split[1];
				}
			}
		}
		return null;
	}

	@Override
	public String getAdditionalAttributes() {
		return ""; // usually GridElements have no additional attributes
	}

	@Override
	public void setAdditionalAttributes(String additionalAttributes) {
		// usually GridElements have no additional attributes
	}

	@Override
	public boolean isInRange(Rectangle rect1) {
		return rect1.contains(getRectangle());
	}

	@Override
	public Set<Direction> getResizeArea(int x, int y) {
		Set<Direction> returnSet = new HashSet<Direction>();
		if (state.getElementStyle() == ElementStyle.NORESIZE || state.getElementStyle() == ElementStyle.AUTORESIZE) {
			return returnSet;
		}

		if (x <= 5 && x >= 0) {
			returnSet.add(Direction.LEFT);
		}
		else if (x <= getRectangle().width && x >= getRectangle().width - 5) {
			returnSet.add(Direction.RIGHT);
		}

		if (y <= 5 && y >= 0) {
			returnSet.add(Direction.UP);
		}
		else if (y <= getRectangle().height && y >= getRectangle().height - 5) {
			returnSet.add(Direction.DOWN);
		}
		return returnSet;
	}

	/**
	 * @deprecated use {@link #generateStickingBorder()} instead, because typically the stickingpolygon is created for the own Rectangle, and the other method guarantees that the correct zoom level is applied (important to make alternative StickingPolygonGenerators like PointDoubleStickingPolygonGenerator work)
	 */
	@Deprecated
	@Override
	public final StickingPolygon generateStickingBorder(Rectangle rect) {
		return state.getStickingPolygonGenerator().generateStickingBorder(rect);
	}

	/**
	 * generates the StickingPolygon of the element using the rectangle as if the zoomlevel would be 100% (this is IMPORTANT because the sticking-calculation doesn't calculate the zoomlevel (see Issue 229 and 231)
	 * Should never be overwritten; if a specific StickingPolygon should be created, instead overwrite the StickingPolygonGenerator in PropertiesParserState eg: Class uses different Generators based on which facets are active (see Class.java)
	 */
	@Override
	public final StickingPolygon generateStickingBorder() {
		return generateStickingBorder(getRealRectangle()); // ALWAYS generate the stickingBorder as if zoom were 100%
	}

	private final void drawStickingPolygon(DrawHandler drawer) {
		Rectangle rect = new Rectangle(0, 0, getRealSize().width, getRealSize().height);
		StickingPolygon poly = this.generateStickingBorder(rect);
		drawer.setLineType(LineType.DASHED);
		drawer.setForegroundColor(Theme.getCurrentThemeColor().getStyleColorMap().get(ColorOwn.ColorStyle.STICKING_POLYGON));
		drawer.setBackgroundColor(Theme.getCurrentThemeColor().getColorMap().get(ColorOwn.PredefinedColors.TRANSPARENT));
		Vector<? extends Line> lines = poly.getStickLines();
		drawer.drawLines(lines.toArray(new Line[lines.size()]));
		drawer.setLineType(LineType.SOLID);
		drawer.resetColorSettings();
	}

	@Override
	public void setRectangle(Rectangle bounds) {
		component.setBoundsRect(bounds);
	}

	@Override
	public void setLocationDifference(int diffx, int diffy) {
		setLocation(getRectangle().x + diffx, getRectangle().y + diffy);
	}

	@Override
	public void setLocation(int x, int y) {
		Rectangle rect = getRectangle();
		rect.setLocation(x, y);
		component.setBoundsRect(rect);
	}

	@Override
	public void setSize(int width, int height) {
		if (width != getRectangle().width || height != getRectangle().height) { // only change size if it is really different
			Rectangle rect = getRectangle();
			rect.setSize(width, height);
			setRectangle(rect);
			if (!autoresizePossiblyInProgress) {
				updateModelFromText();
			}
		}
	}

	@Override
	public Rectangle getRectangle() {
		return component.getBoundsRect();
	}

	@Override
	public void repaint() {
		component.repaintComponent();
	}

	/**
	 * @see com.baselet.element.interfaces.GridElement#getRealSize()
	 */
	@Override
	public Dimension getRealSize() {
		return new Dimension(zoom(getRectangle().width), zoom(getRectangle().height));
	}

	public Rectangle getRealRectangle() {
		return new Rectangle(zoom(getRectangle().x), zoom(getRectangle().y), zoom(getRectangle().width), zoom(getRectangle().height));
	}

	private int zoom(int val) {
		return val * SharedConstants.DEFAULT_GRID_SIZE / getGridSize();
	}

	@Override
	public Component getComponent() {
		return component;
	}

	protected abstract Settings createSettings();

	@Override
	public List<AutocompletionText> getAutocompletionList() {
		List<AutocompletionText> returnList = new ArrayList<AutocompletionText>();
		addAutocompletionTexts(returnList, state.getSettings().getFacetsForFirstRun());
		addAutocompletionTexts(returnList, state.getSettings().getFacetsForSecondRun());
		return returnList;
	}

	private void addAutocompletionTexts(List<AutocompletionText> returnList, List<? extends Facet> facets) {
		for (Facet f : facets) {
			for (AutocompletionText t : f.getAutocompletionStrings()) {
				returnList.add(t);
			}
		}
	}

	@Override
	public Integer getLayer() {
		return state.getFacetResponse(LayerFacet.class, LayerFacet.DEFAULT_VALUE);
	}

	@Override
	public Integer getGroup() {
		return state.getFacetResponse(GroupFacet.class, null);
	}

	public void handleAutoresize(DimensionDouble necessaryElementDimension, AlignHorizontal alignHorizontal) {
		Dimension realSize = getRealSize();
		double diffw = necessaryElementDimension.getWidth() - realSize.width;
		double diffh = necessaryElementDimension.getHeight() - realSize.height;

		int diffwInt = SharedUtils.realignTo(false, unzoom(diffw), true, getGridSize());
		int diffhInt = SharedUtils.realignTo(false, unzoom(diffh), true, getGridSize());
		List<Direction> directions = null;
		if (alignHorizontal == AlignHorizontal.LEFT) {
			directions = Arrays.asList(Direction.RIGHT, Direction.DOWN);
		}
		else if (alignHorizontal == AlignHorizontal.RIGHT) {
			diffwInt = -diffwInt;
			directions = Arrays.asList(Direction.LEFT, Direction.DOWN);
		}
		else if (alignHorizontal == AlignHorizontal.CENTER) {
			diffwInt = SharedUtils.realignTo(false, diffwInt / 2.0, true, getGridSize()) * 2;
			directions = Arrays.asList(Direction.RIGHT, Direction.LEFT, Direction.DOWN);
		}
		drag(directions, diffwInt, diffhInt, new Point(0, 0), false, true, handler.getStickableMap(), false);
	}

	private double unzoom(double diffw) {
		return diffw / SharedConstants.DEFAULT_GRID_SIZE * getGridSize();
	}

	@Override
	public void setRectangleDifference(int diffx, int diffy, int diffw, int diffh, boolean firstDrag, StickableMap stickables, boolean undoable) {
		Rectangle oldRect = getRectangle();
		StickingPolygon stickingPolygonBeforeLocationChange = generateStickingBorder();
		String oldAddAttr = getAdditionalAttributes();
		setRectangle(new Rectangle(oldRect.x + diffx, oldRect.y + diffy, oldRect.getWidth() + diffw, oldRect.getHeight() + diffh));
		moveStickables(stickables, undoable, oldRect, stickingPolygonBeforeLocationChange, oldAddAttr);
	}

	@Override
	public void drag(Collection<Direction> resizeDirection, int diffX, int diffY, Point mousePosBeforeDrag, boolean isShiftKeyDown, boolean firstDrag, StickableMap stickables, boolean undoable) {
		Rectangle oldRect = getRectangle();
		StickingPolygon stickingPolygonBeforeLocationChange = generateStickingBorder();
		String oldAddAttr = getAdditionalAttributes();
		if (resizeDirection.isEmpty()) { // Move GridElement
			setLocationDifference(diffX, diffY);
		}
		else { // Resize GridElement
			Rectangle rect = getRectangle();
			if (isShiftKeyDown && diagonalResize(resizeDirection)) { // Proportional Resize
				boolean mouseToRight = diffX > 0 && diffX > diffY;
				boolean mouseDown = diffY > 0 && diffY > diffX;
				boolean mouseLeft = diffX < 0 && diffX < diffY;
				boolean mouseUp = diffY < 0 && diffY < diffX;
				if (mouseToRight || mouseLeft) {
					diffY = diffX;
				}
				if (mouseDown || mouseUp) {
					diffX = diffY;
				}
			}
			if (resizeDirection.contains(Direction.LEFT) && resizeDirection.contains(Direction.RIGHT)) {
				rect.setX(rect.getX() - diffX / 2);
				rect.setWidth(Math.max(rect.getWidth() + diffX, minSize()));
			}
			else if (resizeDirection.contains(Direction.LEFT)) {
				int newWidth = rect.getWidth() - diffX;
				if (newWidth >= minSize()) {
					rect.setX(rect.getX() + diffX);
					rect.setWidth(newWidth);
				}
			}
			else if (resizeDirection.contains(Direction.RIGHT)) {
				rect.setWidth(Math.max(rect.getWidth() + diffX, minSize()));
			}

			if (resizeDirection.contains(Direction.UP)) {
				int newHeight = rect.getHeight() - diffY;
				if (newHeight >= minSize()) {
					rect.setY(rect.getY() + diffY);
					rect.setHeight(newHeight);
				}
			}
			if (resizeDirection.contains(Direction.DOWN)) {
				rect.setHeight(Math.max(rect.getHeight() + diffY, minSize()));
			}

			setRectangle(rect);
			if (!autoresizePossiblyInProgress) {
				updateModelFromText();
			}
		}

		moveStickables(stickables, undoable, oldRect, stickingPolygonBeforeLocationChange, oldAddAttr);
	}

	private void moveStickables(StickableMap stickables, boolean undoable, Rectangle oldRect, StickingPolygon stickingPolygonBeforeLocationChange, String oldAddAttr) {
		Map<Stickable, List<PointChange>> stickableChanges = Stickables.moveStickPointsBasedOnPolygonChanges(stickingPolygonBeforeLocationChange, generateStickingBorder(), stickables, getGridSize());
		if (undoable) {
			undoStack.add(new UndoInformation(getRectangle(), oldRect, stickableChanges, getGridSize(), oldAddAttr, getAdditionalAttributes()));
		}
	}

	@Override
	public void dragEnd() {
		// only used by some specific elements like Relations
	}

	@Override
	public boolean isSelectableOn(Point point) {
		return getRectangle().contains(point);
	}

	private boolean diagonalResize(Collection<Direction> resizeDirection) {
		return resizeDirection.contains(Direction.UP) && resizeDirection.contains(Direction.RIGHT) ||
				resizeDirection.contains(Direction.UP) && resizeDirection.contains(Direction.LEFT) ||
				resizeDirection.contains(Direction.DOWN) && resizeDirection.contains(Direction.LEFT) ||
				resizeDirection.contains(Direction.DOWN) && resizeDirection.contains(Direction.RIGHT);
	}

	protected DrawHandlerInterface getHandler() {
		return handler;
	}

	public int getGridSize() {
		return getHandler().getGridSize();
	}

	private int minSize() {
		return handler.getGridSize() * 2;
	}

	@Override
	public void undoDrag() {
		execUndoInformation(true);
	}

	private void execUndoInformation(boolean undo) {
		UndoInformation undoInfo = undoStack.get(undo);
		if (undoInfo != null) {
			setRectangle(getRectangle().add(undoInfo.getDiffRectangle(getGridSize(), undo)));
			Stickables.applyChanges(undoInfo.getStickableMoves(undo), null);
			setAdditionalAttributes(undoInfo.getAdditionalAttributes(undo));
		}
	}

	@Override
	public void redoDrag() {
		execUndoInformation(false);
	}

	@Override
	public void mergeUndoDrag() {
		UndoInformation undoInfoA = undoStack.remove();
		UndoInformation undoInfoB = undoStack.remove();
		undoStack.add(undoInfoA.merge(undoInfoB));
	}

	@Override
	public GridElementDeprecatedAddons getDeprecatedAddons() {
		return GridElementDeprecatedAddons.NONE;
	}

	@Override
	public CursorOwn getCursor(Point point, Set<Direction> resizeDirections) {
		return getCursorStatic(this, point, resizeDirections);
	}

	public static CursorOwn getCursorStatic(GridElement e, Point point, Set<Direction> resizeDirections) {
		if (!e.isSelectableOn(point)) {
			return CursorOwn.DEFAULT;
		}
		else {
			if (resizeDirections.isEmpty()) {
				return CursorOwn.HAND;
			}
			else if (resizeDirections.contains(Direction.UP) && resizeDirections.contains(Direction.RIGHT)) {
				return CursorOwn.NE;
			}
			else if (resizeDirections.contains(Direction.UP) && resizeDirections.contains(Direction.LEFT)) {
				return CursorOwn.NW;
			}
			else if (resizeDirections.contains(Direction.DOWN) && resizeDirections.contains(Direction.LEFT)) {
				return CursorOwn.SW;
			}
			else if (resizeDirections.contains(Direction.DOWN) && resizeDirections.contains(Direction.RIGHT)) {
				return CursorOwn.SE;
			}
			else if (resizeDirections.contains(Direction.UP)) {
				return CursorOwn.N;
			}
			else if (resizeDirections.contains(Direction.RIGHT)) {
				return CursorOwn.E;
			}
			else if (resizeDirections.contains(Direction.DOWN)) {
				return CursorOwn.S;
			}
			else if (resizeDirections.contains(Direction.LEFT)) {
				return CursorOwn.W;
			}
			return CursorOwn.DEFAULT;
		}
	}

}
