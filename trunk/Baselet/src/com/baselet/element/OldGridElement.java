package com.baselet.element;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Component;
import java.awt.Composite;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Stroke;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.Vector;

import javax.swing.JComponent;

import org.apache.log4j.Logger;

import com.baselet.control.Constants;
import com.baselet.control.Main;
import com.baselet.control.Utils;
import com.baselet.control.enumerations.AlignHorizontal;
import com.baselet.control.enumerations.Direction;
import com.baselet.control.enumerations.LineType;
import com.baselet.diagram.draw.BaseDrawHandler;
import com.baselet.diagram.draw.geom.Dimension;
import com.baselet.diagram.draw.geom.DimensionDouble;
import com.baselet.diagram.draw.geom.Line;
import com.baselet.diagram.draw.geom.Point;
import com.baselet.diagram.draw.geom.Rectangle;
import com.baselet.diagram.draw.helper.ColorOwn;
import com.baselet.diagram.draw.helper.ColorOwn.Transparency;
import com.baselet.diagram.draw.swing.Converter;
import com.baselet.element.sticking.Stickable;
import com.baselet.element.sticking.StickingPolygon;
import com.baselet.gui.AutocompletionText;
import com.umlet.element.experimental.ElementId;
import com.umlet.element.experimental.facets.Facet;
import com.umlet.element.experimental.facets.defaults.BackgroundColorFacet;
import com.umlet.element.experimental.facets.defaults.ForegroundColorFacet;
import com.umlet.element.experimental.facets.defaults.LayerFacet;

public abstract class OldGridElement extends JComponent implements GridElement, com.umlet.element.experimental.Component {

	private static final long serialVersionUID = 1L;
	
	protected static final Logger log = Logger.getLogger(OldGridElement.class);

	public static final float ALPHA_MIDDLE_TRANSPARENCY = 0.5f;
	public static final float ALPHA_FULL_TRANSPARENCY = 0.0f;
	
	private boolean enabled;
	private boolean stickingBorderActive;
	private boolean autoresizeandmanualresizeenabled;
	protected GridElement group = null;
	protected String panelAttributes = "";

	// deselectedColor and fgColor must be stored separately because selection changes the actual fgColor but not the fgColorBase
	protected Color fgColorBase = Converter.convert(ColorOwn.BLACK);
	protected Color fgColor = fgColorBase;
	private String fgColorString = "";
	protected Color bgColor = Converter.convert(ColorOwn.WHITE);
	private String bgColorString = "";
	protected float alphaFactor;

	private Integer lastLayerValue;

	private boolean selected = false;

	public OldGridElement() {
		this.setSize(100, 100);
		this.setVisible(true);
		this.enabled = true;
		this.autoresizeandmanualresizeenabled = false;
		this.stickingBorderActive = true;
	}

	@Override
	public void setEnabled(boolean en) {
		super.setEnabled(en);
		if (!en && enabled) {
			this.removeMouseListener(Main.getHandlerForElement(this).getEntityListener(this));
			this.removeMouseMotionListener(Main.getHandlerForElement(this).getEntityListener(this));
			enabled = false;
		}
		else if (en && !enabled) {
			if (!this.isPartOfGroup()) {
				this.addMouseListener(Main.getHandlerForElement(this).getEntityListener(this));
				this.addMouseMotionListener(Main.getHandlerForElement(this).getEntityListener(this));
			}
			enabled = true;
		}
	}

	@Override
	public boolean isStickingBorderActive() {
		return stickingBorderActive;
	}

	@Override
	public void setStickingBorderActive(boolean stickingBordersActive) {
		this.stickingBorderActive = stickingBordersActive;
	}

	public boolean isManualResized() {
		autoresizeandmanualresizeenabled = true;
		return this.isManResized();
	}

	private boolean isManResized() {
		Vector<String> lines = Utils.decomposeStringsWithComments(this.getPanelAttributes());
		for (String line : lines) {
			if (line.startsWith("autoresize=false")) return true;
		}
		return false;
	}

	protected boolean isAutoResizeandManualResizeEnabled() {
		return autoresizeandmanualresizeenabled;
	}

	public void setManualResized() {
		if (autoresizeandmanualresizeenabled) {
			if (!this.isManResized()) {
				this.setPanelAttributes(this.getPanelAttributes() + Constants.NEWLINE + "autoresize=false");
				if (this.equals(Main.getInstance().getEditedGridElement())) Main.getInstance().setPropertyPanelToGridElement(this);
			}
		}
	}

	@Override
	public void setGroup(GridElement group) {
		this.group = group;
	}

	@Override
	public boolean isPartOfGroup() {
		if (this.group != null) return true;
		return false;
	}

	// Some GridElements need additionalAttributes to be displayed correctly (eg: Relations need exact positions for edges)
	@Override
	public String getAdditionalAttributes() {
		return "";
	}

	@Override
	public void setAdditionalAttributes(String s) { }

	@Override
	public String getPanelAttributes() {
		return panelAttributes;
	}

	@Override
	public void setPanelAttributes(String panelAttributes) {
		this.panelAttributes = panelAttributes;
	}
	
	public void setSelected(Boolean selected) {
		this.selected = selected;
		if (selected) {
			fgColor = Converter.convert(ColorOwn.SELECTION_FG);
		} else {
			fgColor = fgColorBase;
			this.setStickingBorderActive(true);
		}
		this.repaint();
	}

	public ColorOwn getFgColor() {
		return Converter.convert(fgColor);
	}

	public ColorOwn getBgColor() {
		return Converter.convert(bgColor);
	}

	@Override
	public String getSetting(String key) {
		if (key == ForegroundColorFacet.KEY) return fgColorString;
		else if (key == BackgroundColorFacet.KEY) return bgColorString;
		else return "";
	}

	@Override
	public void updateProperty(String key, String newValue) {
		String newState = "";
		for (String line : Utils.decomposeStringsWithComments(this.getPanelAttributes())) {
			if (!line.startsWith(key.toString())) newState += line + "\n";
		}
		newState = newState.substring(0, newState.length()-1); //remove last linebreak
		if (newValue != null && !newValue.isEmpty()) newState += "\n" + key.toString() + "=" + newValue; // null will not be added as a value
		this.setPanelAttributes(newState);
		Main.getHandlerForElement(this).getDrawPanel().getSelector().updateSelectorInformation(); // update the property panel to display changed attributes
		this.repaint();
	}

	public Composite[] colorize(Graphics2D g2) {
		bgColorString = "";
		fgColorString = "";
		bgColor = Converter.convert(ColorOwn.DEFAULT_BACKGROUND);
		fgColorBase = Converter.convert(ColorOwn.DEFAULT_FOREGROUND);
		Vector<String> v = Utils.decomposeStringsWithComments(panelAttributes);
		for (int i = 0; i < v.size(); i++) {
			String line = v.get(i);
			if (line.indexOf("bg=") >= 0) {
				bgColorString = line.substring("bg=".length());
				// OldGridElements apply transparency for background explicitly, therefore don't apply it here
				bgColor = Converter.convert(ColorOwn.forString(bgColorString, Transparency.FOREGROUND));
				if (bgColor == null) bgColor = Converter.convert(ColorOwn.DEFAULT_BACKGROUND);
			}
			else if (line.indexOf("fg=") >= 0) {
				fgColorString = line.substring("fg=".length());
				fgColorBase = Converter.convert(ColorOwn.forString(fgColorString, Transparency.FOREGROUND));
				if (fgColorBase == null) fgColorBase = Converter.convert(ColorOwn.DEFAULT_FOREGROUND);
				if (Main.getHandlerForElement(this).getDrawPanel().getSelector().isSelected(this)) fgColor = fgColorBase;
			}
		}

		alphaFactor = ALPHA_MIDDLE_TRANSPARENCY;
		if (bgColorString.equals("") || bgColorString.equals("default")) alphaFactor = ALPHA_FULL_TRANSPARENCY;

		Composite old = g2.getComposite();
		AlphaComposite alpha = AlphaComposite.getInstance(AlphaComposite.SRC_OVER, alphaFactor);
		Composite[] composites = { old, alpha };
		return composites;
	}

	@Override
	public Dimension getRealSize() {
		return new Dimension(getRectangle().width / Main.getHandlerForElement(this).getGridSize() * Constants.DEFAULTGRIDSIZE, getRectangle().height / Main.getHandlerForElement(this).getGridSize() * Constants.DEFAULTGRIDSIZE);
	}


	@Override
	public Set<Direction> getResizeArea(int x, int y) {
		Set<Direction> returnSet = new HashSet<Direction>();
		if ((x <= 5) && (x >= 0)) returnSet.add(Direction.LEFT);
		else if ((x <= this.getRectangle().width) && (x >= this.getRectangle().width - 5)) returnSet.add(Direction.RIGHT);

		if ((y <= 5) && (y >= 0)) returnSet.add(Direction.UP);
		else if ((y <= this.getRectangle().height) && (y >= this.getRectangle().height - 5)) returnSet.add(Direction.DOWN);
		return returnSet;
	}
	
	@Override
	public void changeSize(int diffx, int diffy) {
		this.setSize(this.getRectangle().width + diffx, this.getRectangle().height + diffy);
	}

	@Override
	public void setLocationDifference(int diffx, int diffy) {
		this.setLocation(this.getRectangle().x + diffx, this.getRectangle().y + diffy);
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
		return Utils.contains(this, new Point(x, y));
	}

	@Override
	public boolean isInRange(Rectangle rect1) {
		return (rect1.contains(getRectangle()));
		}

	public void setInProgress(Graphics g, boolean flag) {
		if (flag) {
			Graphics2D g2 = (Graphics2D) g;
			g2.setFont(Main.getHandlerForElement(this).getFontHandler().getFont());
			g2.setColor(Color.red);
			Main.getHandlerForElement(this).getFontHandler().writeText(g2, "in progress...", this.getRectangle().width / 2 - 40, this.getRectangle().height / 2 + (int) Main.getHandlerForElement(this).getFontHandler().getFontSize() / 2, AlignHorizontal.LEFT);
		}
		else {
			repaint();
		}
	}

	public GridElement CloneFromMe() {
		try {
			java.lang.Class<? extends GridElement> cx = this.getClass(); // get class of dynamic object
			GridElement c = cx.newInstance();
			c.setPanelAttributes(this.getPanelAttributes()); // copy states
			c.setRectangle(this.getRectangle());
			Main.getHandlerForElement(this).setHandlerAndInitListeners(c);
			return c;
		} catch (Exception e) {
			log.error("Error at calling CloneFromMe() on entity", e);
		}
		return null;
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
		// The Java Implementations in the displaceDrawingByOnePixel list start at (1,1) to draw while any others start at (0,0)
		int start = Utils.displaceDrawingByOnePixel() ? 1 : 0;
		Rectangle rect = new Rectangle(start, start, this.getRectangle().width - 1, this.getRectangle().height - 1);
		StickingPolygon poly = this.generateStickingBorder(rect);
		if (poly != null) {
			Color c = g2.getColor();
			Stroke s = g2.getStroke();
			g2.setColor(Converter.convert(ColorOwn.SELECTION_FG));
			g2.setStroke(Utils.getStroke(LineType.DASHED, 1));
			for (Line line : poly.getStickLines()) {
				g2.drawLine((int)line.getStart().getX(), (int)line.getStart().getY(), (int)line.getEnd().getX(), (int)line.getEnd().getY());
			}
			g2.setColor(c);
			g2.setStroke(s);
		}
	}
	
	private boolean translateForExport = false;
	public void translateForExport() {
		translateForExport = true;
	}

	@Override
	public final void paint(Graphics g) {
		super.paint(g);
		Graphics2D g2 = (Graphics2D) g;
		if (translateForExport) {
			g2.translate(Constants.EXPORT_DISPLACEMENT, Constants.EXPORT_DISPLACEMENT);
		}

		// TODO (same problem as in @see com.umlet.element.experimental.ComponentSwing#paint(Graphics g))
		// the selected state stored in GridElements is NOT the same as the selector holds, therefore it must be set explicitly through a setSelected() method.
		// TODO make sure the selector holds the correct state and a repaint is triggered, then the following line should work:
//		boolean selected = Main.getHandlerForElement(gridElement).getDrawPanel().getSelector().isSelected(gridElement);
		if (selected && Constants.show_stickingpolygon && !this.isPartOfGroup()) {
			this.drawStickingPolygon(g2);
		}

		updateModelFromText();

		this.paintEntity(g2);
	}

	public abstract void paintEntity(Graphics g);

	protected final int textHeight() {
		return (int) (Main.getHandlerForElement(this).getFontHandler().getFontSize(false) + Main.getHandlerForElement(this).getFontHandler().getDistanceBetweenTexts(false));
	}

	protected final int textWidth(String text, boolean applyZoom) {
		return (int) (Main.getHandlerForElement(this).getFontHandler().getTextSize(text, applyZoom).getWidth() + (int) Main.getHandlerForElement(this).getFontHandler().getDistanceBetweenTexts(applyZoom));
	}

	@Override
	public com.umlet.element.experimental.Component getComponent() {
		return this;
	}

	@Override
	public List<AutocompletionText> getAutocompletionList() {
		return new ArrayList<AutocompletionText>();
	}

	@Override
	public void updateModelFromText() {
		/*OldGridElement has no model but simply parses the properties text within every paint() call*/
		Integer oldLayer = lastLayerValue;
		if (oldLayer != null && !oldLayer.equals(getLayer())) {
			Main.getHandlerForElement(this).getDrawPanel().setLayer((Component) getComponent(), lastLayerValue);
		}
	}

	@Override
	public Integer getLayer() {
		lastLayerValue = LayerFacet.DEFAULT_VALUE;
		try {
			for (String s : Utils.decomposeStringsWithComments(panelAttributes)) {
				String key = LayerFacet.KEY + Facet.SEP;
				if (s.startsWith(key)) {
					String value = s.split(key)[1];
					lastLayerValue = Integer.valueOf(value);
				}
			}
		} catch (Exception e) {/* in case of an error return default layer*/}
		return lastLayerValue;
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
	public BaseDrawHandler getDrawHandler() {
		return null;
	}

	@Override
	public BaseDrawHandler getMetaDrawHandler() {
		return null;
	}

	@Override
	public void handleAutoresize(DimensionDouble necessaryElementDimension, AlignHorizontal alignHorizontal) {
		/* not possible on OldGridElement */
	}
	
	@Override
	public ElementId getId() {
		return null;
	}
	
	@Override
	public void drag(Collection<Direction> resizeDirection, int diffX, int diffY, Point mousePosBeforeDrag, boolean isShiftKeyDown, boolean firstDrag, Collection<? extends Stickable> stickables) {
		// TODO Auto-generated method stub
		
	}
	
	@Override
	public void afterModelUpdate() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean isSelectableOn(Point point) {
		return getRectangle().contains(point);
	}
	
	@Override
	public void dragEnd() {
		// TODO Auto-generated method stub
		
	}
	
	@Override
	public void setLocationDifference(int diffx, int diffy, boolean firstDrag, Collection<? extends Stickable> stickables) {
		// TODO Auto-generated method stub
		
	}
	
}
