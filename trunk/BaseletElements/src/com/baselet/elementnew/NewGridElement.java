package com.baselet.elementnew;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Vector;

import org.apache.log4j.Logger;

import com.baselet.control.SharedConstants;
import com.baselet.control.SharedUtils;
import com.baselet.control.enumerations.AlignHorizontal;
import com.baselet.control.enumerations.Direction;
import com.baselet.control.enumerations.LineType;
import com.baselet.diagram.draw.DrawHandler;
import com.baselet.diagram.draw.geom.Dimension;
import com.baselet.diagram.draw.geom.DimensionDouble;
import com.baselet.diagram.draw.geom.Line;
import com.baselet.diagram.draw.geom.Point;
import com.baselet.diagram.draw.geom.PointDouble;
import com.baselet.diagram.draw.geom.Rectangle;
import com.baselet.diagram.draw.helper.ColorOwn;
import com.baselet.diagram.draw.helper.ColorOwn.Transparency;
import com.baselet.element.GridElement;
import com.baselet.element.UndoHistory;
import com.baselet.element.UndoInformation;
import com.baselet.element.sticking.PointChange;
import com.baselet.element.sticking.Stickable;
import com.baselet.element.sticking.StickableMap;
import com.baselet.element.sticking.Stickables;
import com.baselet.element.sticking.StickingPolygon;
import com.baselet.elementnew.facet.Facet;
import com.baselet.elementnew.facet.GlobalFacet;
import com.baselet.elementnew.facet.common.ElementStyleFacet.ElementStyleEnum;
import com.baselet.elementnew.facet.common.GroupFacet;
import com.baselet.elementnew.facet.common.LayerFacet;
import com.baselet.elementnew.settings.Settings;
import com.baselet.gui.AutocompletionText;

public abstract class NewGridElement implements GridElement {

	private final Logger log = Logger.getLogger(NewGridElement.class);

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
		state = new PropertiesParserState(createSettings());
		setAdditionalAttributes(additionalAttributes);
	}

	public DrawHandler getDrawer() {
		return drawer;
	}

	public DrawHandler getMetaDrawer() {
		return metaDrawer;
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
			PropertiesParser.drawPropertiesText(this, state);
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
		drawer.setForegroundColor(ColorOwn.RED);
		drawer.setBackgroundColor(ColorOwn.RED.transparency(Transparency.SELECTION_BACKGROUND));
		drawer.setLineWidth(0.2);
		drawer.drawRectangle(0, 0, getRealSize().width, getRealSize().height); // draw dotted rect (to enforce background color even if element has no border)
		resetMetaDrawer(metaDrawer);
		drawer.print(errorText, 3, getRealSize().height / 2 - drawer.textHeightMax(), AlignHorizontal.LEFT);
	}

	void resetMetaDrawerAndDrawCommonContent() {
		drawCommonContent(drawer, state); // must be before properties.drawPropertiesText (to make sure a possible background color is behind the text)
		resetMetaDrawer(metaDrawer); // must be after properties.initSettingsFromText() because stickingpolygon size can be based on some settings (eg: Actor uses this)
	}

	protected abstract void drawCommonContent(DrawHandler drawer, PropertiesParserState state);

	protected void resetMetaDrawer(DrawHandler drawer) {
		drawer.clearCache();
		drawer.setForegroundColor(ColorOwn.TRANSPARENT);
		drawer.setBackgroundColor(ColorOwn.SELECTION_BG);
		drawer.drawRectangle(0, 0, getRealSize().width, getRealSize().height);
		if (SharedConstants.dev_mode) {
			drawer.setForegroundColor(ColorOwn.BLACK);
			drawer.setFontSize(10.5);
			drawer.print(getId().toString(), new PointDouble(getRealSize().width - 3, getRealSize().height - 2), AlignHorizontal.RIGHT);
		}
		drawer.resetColorSettings();
		if (SharedConstants.show_stickingpolygon) {
			drawStickingPolygon(drawer);
		}
	}

	@Override
	public void setProperty(String key, Object newValue) {
		String newState = "";
		for (String line : getPanelAttributesAsList()) {
			if (!line.startsWith(key)) {
				newState += line + "\n";
			}
		}
		if (newState.endsWith("\n")) { // remove last linebreak
			newState = newState.substring(0, newState.length() - 1);
		}
		if (newValue != null) {
			newState += "\n" + key + Facet.SEP + newValue.toString(); // null will not be added as a value
		}
		setPanelAttributes(newState);
	}

	@Override
	public String getSetting(String key) {
		for (String line : getPanelAttributesAsList()) {
			if (line.startsWith(key + Facet.SEP)) {
				String[] split = line.split(Facet.SEP, 2);
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
		if (state.getElementStyle() == ElementStyleEnum.NORESIZE || state.getElementStyle() == ElementStyleEnum.AUTORESIZE) {
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

	/* method is final because it's not flexible enough. instead overwrite StickingPolygonGenerator in PropertiesParserState eg: Class uses this to change the stickingpolygon based on which facets are active (see Class.java) */
	@Override
	public final StickingPolygon generateStickingBorder(Rectangle rect) {
		return state.getStickingPolygonGenerator().generateStickingBorder(rect);
	}

	private StickingPolygon generateStickingBorder() {
		return generateStickingBorder(getRectangle());
	}

	private final void drawStickingPolygon(DrawHandler drawer) {
		// The Java Implementations in the displaceDrawingByOnePixel list start at (1,1) to draw while any others start at (0,0)
		int start = handler.displaceDrawingByOnePixel() ? 1 : 0;
		Rectangle rect = new Rectangle(start, start, getRealSize().width, getRealSize().height);
		StickingPolygon poly = this.generateStickingBorder(rect);
		drawer.setLineType(LineType.DASHED);
		drawer.setForegroundColor(ColorOwn.STICKING_POLYGON);
		Vector<? extends Line> lines = poly.getStickLines();
		drawer.drawLines(lines.toArray(new Line[lines.size()]));
		drawer.setLineType(LineType.SOLID);
		drawer.resetColorSettings();
	}

	@Override
	public void changeSize(int diffx, int diffy) {
		setSize(getRectangle().width + diffx, getRectangle().height + diffy);
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
	 * @see com.baselet.element.GridElement#getRealSize()
	 */
	@Override
	public Dimension getRealSize() {
		return new Dimension(getRectangle().width * SharedConstants.DEFAULT_GRID_SIZE / handler.getGridSize(), getRectangle().height * SharedConstants.DEFAULT_GRID_SIZE / handler.getGridSize());
	}

	@Override
	public Component getComponent() {
		return component;
	}

	protected abstract Settings createSettings();

	@Override
	public List<AutocompletionText> getAutocompletionList() {
		List<AutocompletionText> returnList = new ArrayList<AutocompletionText>();
		for (List<? extends Facet> f : state.getSettings().getGlobalFacets().values()) {
			addAutocompletionTexts(returnList, f);
		}
		addAutocompletionTexts(returnList, state.getSettings().getLocalFacets());
		return returnList;
	}

	private void addAutocompletionTexts(List<AutocompletionText> returnList, List<? extends Facet> facets) {
		for (Facet f : facets) {
			for (AutocompletionText t : f.getAutocompletionStrings()) {
				t.setGlobal(f instanceof GlobalFacet);
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

	@Override
	public void handleAutoresize(DimensionDouble necessaryElementDimension, AlignHorizontal alignHorizontal) {
		double hSpaceLeftAndRight = drawer.getDistanceBorderToText() * 2;
		double width = necessaryElementDimension.getWidth() + hSpaceLeftAndRight;
		double height = necessaryElementDimension.getHeight() + drawer.textHeightMax() / 2;
		Dimension realSize = getRealSize();
		double diffw = width - realSize.width;
		double diffh = height - realSize.height;

		int diffwInt = SharedUtils.realignTo(false, diffw, true, getGridSize());
		int diffhInt = SharedUtils.realignTo(false, diffh, true, getGridSize());

		List<Direction> directions = null;
		if (alignHorizontal == AlignHorizontal.LEFT) {
			directions = Arrays.asList(Direction.RIGHT, Direction.DOWN);
		}
		else if (alignHorizontal == AlignHorizontal.RIGHT) {
			diffwInt = -diffwInt;
			directions = Arrays.asList(Direction.LEFT, Direction.DOWN);
		}
		else if (alignHorizontal == AlignHorizontal.CENTER) {
			diffwInt = SharedUtils.realignToGrid(false, diffwInt / 2, true) * 2;
			directions = Arrays.asList(Direction.RIGHT, Direction.LEFT, Direction.DOWN);
		}
		drag(directions, diffwInt, diffhInt, new Point(0, 0), false, true, handler.getStickableMap(), false);
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
			undoStack.add(new UndoInformation(getRectangle(), oldRect, stickableChanges, getGridSize(), oldAddAttr));
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
			setAdditionalAttributes(undoInfo.getAdditionalAttributes());
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
}
