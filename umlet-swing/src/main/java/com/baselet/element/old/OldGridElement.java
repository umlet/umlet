package com.baselet.element.old;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Composite;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Stroke;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Vector;

import javax.swing.JComponent;

import com.baselet.diagram.draw.helper.ColorOwn;
import com.baselet.diagram.draw.helper.Theme;
import com.baselet.diagram.draw.helper.ThemeFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.baselet.control.HandlerElementMap;
import com.baselet.control.Main;
import com.baselet.control.SharedUtils;
import com.baselet.control.basics.Converter;
import com.baselet.control.basics.geom.Dimension;
import com.baselet.control.basics.geom.Line;
import com.baselet.control.basics.geom.Point;
import com.baselet.control.basics.geom.Rectangle;
import com.baselet.control.config.SharedConfig;
import com.baselet.control.constants.Constants;
import com.baselet.control.enums.AlignHorizontal;
import com.baselet.control.enums.Direction;
import com.baselet.control.enums.ElementId;
import com.baselet.control.enums.LineType;
import com.baselet.control.util.Utils;
import com.baselet.diagram.DiagramHandler;
import com.baselet.diagram.draw.DrawHandler;
import com.baselet.diagram.draw.helper.ColorOwn.Transparency;
import com.baselet.element.ElementUtils;
import com.baselet.element.NewGridElement;
import com.baselet.element.UndoHistory;
import com.baselet.element.UndoInformation;
import com.baselet.element.facet.KeyValueFacet;
import com.baselet.element.facet.common.GroupFacet;
import com.baselet.element.facet.common.LayerFacet;
import com.baselet.element.interfaces.CursorOwn;
import com.baselet.element.interfaces.GridElement;
import com.baselet.element.interfaces.GridElementDeprecatedAddons;
import com.baselet.element.sticking.PointChange;
import com.baselet.element.sticking.Stickable;
import com.baselet.element.sticking.StickableMap;
import com.baselet.element.sticking.Stickables;
import com.baselet.element.sticking.StickingPolygon;
import com.baselet.gui.AutocompletionText;

public abstract class OldGridElement extends JComponent implements GridElement, com.baselet.element.interfaces.Component {

	private static final long serialVersionUID = 1L;

	protected static final Logger log = LoggerFactory.getLogger(OldGridElement.class);

	public static final float ALPHA_MIDDLE_TRANSPARENCY = 0.5f;
	public static final float ALPHA_FULL_TRANSPARENCY = 0.0f;

	private boolean enabled;
	private boolean autoresizeandmanualresizeenabled;
	private List<String> panelAttributes = new ArrayList<String>();

	// deselectedColor and fgColor must be stored separately because selection changes the actual fgColor but not the fgColorBase
	/**
	 * contains the value of the fgColor of the element if not selected
	 */
	protected Color fgColorBase = Converter.convert(ThemeFactory.getCurrentTheme().getColor(Theme.PredefinedColors.BLACK));
	/**
	 * contains the current fgColor of the element. Will be overwritten by selectioncolor if it's selected
	 */
	protected Color fgColor = fgColorBase;

	private String fgColorString = "";
	protected Color bgColor = Converter.convert(ThemeFactory.getCurrentTheme().getColor(Theme.PredefinedColors.WHITE));
	private String bgColorString = "";
	protected float alphaFactor;

	protected final UndoHistory undoStack = new UndoHistory();

	public OldGridElement() {
		this.setSize(100, 100);
		setVisible(true);
		enabled = true;
		autoresizeandmanualresizeenabled = false;
	}

	@Override
	public void setEnabled(boolean en) {
		super.setEnabled(en);
		if (!en && enabled) {
			removeMouseListener(getDiagramHandler().getEntityListener(this));
			removeMouseMotionListener(getDiagramHandler().getEntityListener(this));
			enabled = false;
		}
		else if (en && !enabled) {
			addMouseListener(getDiagramHandler().getEntityListener(this));
			addMouseMotionListener(getDiagramHandler().getEntityListener(this));
			enabled = true;
		}
	}

	public boolean isManualResized() {
		autoresizeandmanualresizeenabled = true;
		return isManResized();
	}

	private boolean isManResized() {
		Vector<String> lines = Utils.decomposeStringsWithComments(getPanelAttributes());
		for (String line : lines) {
			if (line.startsWith("autoresize=false")) {
				return true;
			}
		}
		return false;
	}

	protected boolean isAutoResizeandManualResizeEnabled() {
		return autoresizeandmanualresizeenabled;
	}

	public void setManualResized() {
		if (autoresizeandmanualresizeenabled) {
			if (!isManResized()) {
				setPanelAttributes(getPanelAttributes() + Constants.NEWLINE + "autoresize=false");
				if (equals(Main.getInstance().getEditedGridElement())) {
					Main.getInstance().setPropertyPanelToGridElement(this);
				}
			}
		}
	}

	// Some GridElements need additionalAttributes to be displayed correctly (eg: Relations need exact positions for edges)
	@Override
	public String getAdditionalAttributes() {
		return "";
	}

	@Override
	public void setAdditionalAttributes(String s) {}

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
		this.panelAttributes = Arrays.asList(panelAttributes.split("\n", -1)); // split with -1 to retain empty lines at the end
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
	public void setProperty(String key, Object newValue) {
		StringBuilder sb = new StringBuilder("");
		for (String line : panelAttributes) {
			if (!line.startsWith(key.toString())) {
				sb.append(line).append("\n");
			}
		}
		if (sb.length() > 0) {
			sb.setLength(sb.length() - 1); // remove last linebreak
		}
		if (newValue != null && !newValue.toString().isEmpty()) {
			sb.append("\n").append(key.toString()).append("=").append(newValue); // null will not be added as a value
		}
		setPanelAttributes(sb.toString());
	}

	public Composite[] colorize(Graphics2D g2) {
		Theme currentTheme = ThemeFactory.getCurrentTheme();
		bgColorString = "";
		fgColorString = "";
		bgColor = getDefaultBackgroundColor();
		fgColorBase = Converter.convert(currentTheme.getColor(Theme.ColorStyle.DEFAULT_FOREGROUND));
		List<String> v = panelAttributes;
		for (int i = 0; i < v.size(); i++) {
			String line = v.get(i);
			if (line.indexOf("bg=") >= 0) {
				bgColorString = line.substring("bg=".length());
				// OldGridElements apply transparency for background explicitly, therefore don't apply it here
				bgColor = Converter.convert(currentTheme.forStringOrNull(bgColorString, Transparency.FOREGROUND));
				if (bgColor == null) {
					bgColor = getDefaultBackgroundColor();
				}
			}
			else if (line.indexOf("fg=") >= 0) {
				fgColorString = line.substring("fg=".length());
				fgColorBase = Converter.convert(currentTheme.forStringOrNull(fgColorString, Transparency.FOREGROUND));
				if (fgColorBase == null) {
					fgColorBase = Converter.convert(currentTheme.getColor(Theme.ColorStyle.DEFAULT_FOREGROUND));
				}
				if (!getDiagramHandler().getDrawPanel().getSelector().isSelected(this)) {
					fgColor = fgColorBase;
				}
			}
		}

		alphaFactor = ALPHA_MIDDLE_TRANSPARENCY;
		if (bgColorString.equals("") || bgColorString.equals("default")) {
			alphaFactor = ALPHA_FULL_TRANSPARENCY;
		}

		Composite old = g2.getComposite();
		AlphaComposite alpha = AlphaComposite.getInstance(AlphaComposite.SRC_OVER, alphaFactor);
		Composite[] composites = { old, alpha };
		return composites;
	}

	protected Color getDefaultBackgroundColor() {
		return Converter.convert(ThemeFactory.getCurrentTheme().getColor(Theme.ColorStyle.DEFAULT_BACKGROUND));
	}

	@Override
	public Dimension getRealSize() {
		return new Dimension(getRectangle().width / getDiagramHandler().getGridSize() * Constants.DEFAULTGRIDSIZE, getRectangle().height / getDiagramHandler().getGridSize() * Constants.DEFAULTGRIDSIZE);
	}

	@Override
	public Set<Direction> getResizeArea(int x, int y) {
		Set<Direction> returnSet = new HashSet<Direction>();
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

	@Override
	public void setLocationDifference(int diffx, int diffy) {
		this.setLocation(getRectangle().x + diffx, getRectangle().y + diffy);
	}

	/**
	 * Must be overwritten because Swing uses this method to tell if 2 elements are overlapping
	 * It's also used to determine which element gets selected if there are overlapping elements (the smallest one)
	 * IMPORTANT: on overlapping elements, contains is called for all elements until the first one returns true, then the others contain methods are not called
	 */
	@Override
	public boolean contains(java.awt.Point p) {
		return this.contains(p.x, p.y);
	}

	/**
	 * Must be overwritten because Swing sometimes uses this method instead of contains(Point)
	 */
	@Override
	public boolean contains(int x, int y) {
		return ElementUtils.checkForOverlap(this, new Point(x, y));
	}

	@Override
	public boolean isInRange(Rectangle rect1) {
		return rect1.contains(getRectangle());
	}

	public void setInProgress(Graphics g, boolean flag) {
		if (flag) {
			Graphics2D g2 = (Graphics2D) g;
			g2.setFont(getDiagramHandler().getFontHandler().getFont());
			g2.setColor(Color.red);
			getDiagramHandler().getFontHandler().writeText(g2, "in progress...", getRectangle().width / 2 - 40, getRectangle().height / 2 + (int) getDiagramHandler().getFontHandler().getFontSize() / 2, AlignHorizontal.LEFT);
		}
		else {
			repaint();
		}
	}

	public GridElement cloneFromMe() {
		try {
			java.lang.Class<? extends GridElement> cx = this.getClass(); // get class of dynamic object
			GridElement c = cx.newInstance();
			c.setPanelAttributes(getPanelAttributes()); // copy states
			c.setRectangle(getRectangle());
			getDiagramHandler().setHandlerAndInitListeners(c);
			return c;
		} catch (Exception e) {
			log.error("Error at calling CloneFromMe() on entity", e);
		}
		return null;
	}

	private DiagramHandler getDiagramHandler() {
		return HandlerElementMap.getHandlerForElement(this);
	}

	@Override
	public StickingPolygon generateStickingBorder(Rectangle rect) {
		return generateStickingBorder(rect.x, rect.y, rect.width, rect.height);
	}

	public StickingPolygon generateStickingBorder(int x, int y, int width, int height) {
		StickingPolygon p = new StickingPolygon(x, y);
		p.addRectangle(0, 0, width, height);
		return p;
	}

	public final void drawStickingPolygon(Graphics2D g2) {
		Rectangle rect = new Rectangle(0, 0, getRectangle().width - 1, getRectangle().height - 1);
		StickingPolygon poly = this.generateStickingBorder(rect);
		if (poly != null) {
			Color c = g2.getColor();
			Stroke s = g2.getStroke();
			g2.setColor(Converter.convert(ThemeFactory.getCurrentTheme().getColor(Theme.ColorStyle.SELECTION_FG)));
			g2.setStroke(Utils.getStroke(LineType.DASHED, 1));
			for (Line line : poly.getStickLines()) {
				g2.drawLine(line.getStart().getX().intValue(), line.getStart().getY().intValue(), line.getEnd().getX().intValue(), line.getEnd().getY().intValue());
			}
			g2.setColor(c);
			g2.setStroke(s);
		}
	}

	private boolean translateForExport = false;

	@Override
	public void translateForExport() {
		translateForExport = true;
	}

	public boolean isDeprecated() {
		return true;
	}

	@Override
	public final void paint(Graphics g) {
		Theme currentTheme = ThemeFactory.getCurrentTheme();
		super.paint(g);
		Graphics2D g2 = (Graphics2D) g;
		if (translateForExport) {
			g2.translate(Constants.EXPORT_DISPLACEMENT, Constants.EXPORT_DISPLACEMENT);
		}

		boolean selected = getDiagramHandler().getDrawPanel().getSelector().isSelected(this);
		if (selected) {
			if (SharedConfig.getInstance().isDev_mode()) {
				Color oldColor = g2.getColor();
				g2.setColor(Converter.convert(currentTheme.getColor(Theme.PredefinedColors.BLACK)));
				String text = "Type: " + getClass().getName();
				g2.drawString(text, getWidth() - (int) getDiagramHandler().getFontHandler().getTextWidth(text), getHeight() - 5);
				g2.setColor(oldColor);
			}
			if (isDeprecated()) {
				Color oldColor = g2.getColor();
				g2.setColor(Converter.convert(currentTheme.getColor(Theme.PredefinedColors.RED).transparency(Transparency.SELECTION_BACKGROUND)));
				g2.fillRect(0, 0, getWidth(), getHeight());
				g2.setColor(oldColor);
				g2.setColor(Converter.convert(currentTheme.getColor(Theme.PredefinedColors.RED).transparency(Transparency.DEPRECATED_WARNING)));
				g2.drawString("DEPRECATED ELEMENT", 10, 15);
				g2.drawString("WILL SOON BE REMOVED", 10, 30);
			}
			fgColor = Converter.convert(currentTheme.getColor(Theme.ColorStyle.SELECTION_FG));
			if (SharedConfig.getInstance().isShow_stickingpolygon()) {
				drawStickingPolygon(g2);
			}
		}
		else {
			fgColor = fgColorBase;
		}
		updateModelFromText();
		paintEntity(g2);
	}

	public abstract void paintEntity(Graphics g);

	@Override
	public com.baselet.element.interfaces.Component getComponent() {
		return this;
	}

	@Override
	public List<AutocompletionText> getAutocompletionList() {
		return new ArrayList<AutocompletionText>();
	}

	@Override
	public void updateModelFromText() {
		/* OldGridElement has no model but simply parses the properties text within every paint() call */
	}

	@Override
	public Integer getLayer() {
		return getLayerHelper(LayerFacet.DEFAULT_VALUE);
	}

	protected Integer getLayerHelper(Integer defaultLayer) {
		try {
			return Integer.valueOf(getSettingHelper(LayerFacet.KEY, defaultLayer.toString()));
		} catch (NumberFormatException e) {/* default value applies */}
		return defaultLayer;
	}

	private String getSettingHelper(String key, String defaultValue) {
		key = key + KeyValueFacet.SEP;
		for (String s : panelAttributes) {
			if (s.startsWith(key)) {
				String[] value = s.split(key);
				if (value.length == 0) {
					return "";
				}
				return value[1];
			}
		}
		return defaultValue;
	}

	@Override
	public Integer getGroup() {
		try {
			return Integer.valueOf(getSettingHelper(GroupFacet.KEY, null));
		} catch (NumberFormatException e) {/* default value applies */}
		return null;
	}

	@Override
	public Rectangle getRectangle() {
		return Converter.convert(getBounds());
	}

	@Override
	public void setRectangle(Rectangle rect) {
		setBounds(rect.x, rect.y, rect.width, rect.height);
	}

	@Override
	public void setBoundsRect(Rectangle rect) {
		setBounds(Converter.convert(rect));
	}

	@Override
	public Rectangle getBoundsRect() {
		return Converter.convert(getBounds());
	}

	@Override
	public void repaintComponent() {
		this.repaint();
	}

	@Override
	public DrawHandler getDrawHandler() {
		return null;
	}

	@Override
	public DrawHandler getMetaDrawHandler() {
		return null;
	}

	@Override
	public ElementId getId() {
		return null;
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
			updateModelFromText();
		}

		moveStickables(stickables, undoable, oldRect, stickingPolygonBeforeLocationChange, oldAddAttr);
	}

	private void moveStickables(StickableMap stickables, boolean undoable, Rectangle oldRect, StickingPolygon stickingPolygonBeforeLocationChange, String oldAddAttr) {
		Map<Stickable, List<PointChange>> stickableChanges = Stickables.moveStickPointsBasedOnPolygonChanges(stickingPolygonBeforeLocationChange, generateStickingBorder(), stickables, getGridSize());
		if (undoable) {
			undoStack.add(new UndoInformation(getRectangle(), oldRect, stickableChanges, getGridSize(), oldAddAttr, getAdditionalAttributes()));
		}
	}

	public int getGridSize() {
		return getDiagramHandler().getGridSize();
	}

	@Override
	public void afterModelUpdate() {}

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

	@Override
	public void dragEnd() {
		// only used by some specific elements like Relations
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
	public StickingPolygon generateStickingBorder() {
		return generateStickingBorder(getRectangle());
	}

	private int minSize() {
		return getDiagramHandler().getGridSize() * 2;
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
			// setAdditionalAttributes(undoInfo.getAdditionalAttributes(undo)); //Issue 217: of all OldGridElements only the Relation uses additional attributes and in that case they must not be set because of zooming errors (see Issue 217)
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
		return NewGridElement.getCursorStatic(this, point, resizeDirections);
	}
}
