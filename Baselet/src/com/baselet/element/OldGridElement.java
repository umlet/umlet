package com.baselet.element;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Composite;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Stroke;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

import javax.swing.JComponent;

import org.apache.log4j.Logger;

import com.baselet.control.Constants;
import com.baselet.control.Main;
import com.baselet.control.Utils;
import com.baselet.control.enumerations.AlignHorizontal;
import com.baselet.control.enumerations.LineType;
import com.baselet.diagram.DiagramHandler;
import com.baselet.gui.AutocompletionText;
import com.umlet.element.experimental.settings.facets.DefaultGlobalFacet.GlobalSetting;

public abstract class OldGridElement extends JComponent implements GridElement {

	private static final long serialVersionUID = 1L;
	
	protected static final Logger log = Logger.getLogger(OldGridElement.class);

	private DiagramHandler handler;
	private boolean enabled;
	private boolean stickingBorderActive;
	private boolean autoresizeandmanualresizeenabled;
	private Group group = null;
	protected String panelAttributes = "";
	protected boolean isSelected = false;

	// deselectedColor and fgColor must be stored separately because selection changes the actual fgColor but not the fgColorBase
	protected Color fgColorBase = Color.black;
	protected Color fgColor = fgColorBase;
	private String fgColorString = "";
	protected Color bgColor = Color.white;
	private String bgColorString = "";
	protected float alphaFactor;

	private Integer lastLayerValue;

	public OldGridElement() {
		this.setSize(100, 100);
		this.setVisible(true);
		this.enabled = true;
		this.autoresizeandmanualresizeenabled = false;
		this.stickingBorderActive = true;
	}

	@Override
	public DiagramHandler getHandler() {
		return handler;
	}

	@Override
	public void setHandlerAndInitListeners(DiagramHandler handler) {
		if (this.getHandler() != null) {
			this.removeMouseListener(this.getHandler().getEntityListener(this));
			this.removeMouseMotionListener(this.getHandler().getEntityListener(this));
		}
		this.handler = handler;
		this.addMouseListener(this.getHandler().getEntityListener(this));
		this.addMouseMotionListener(this.getHandler().getEntityListener(this));
	}

	@Override
	public void setEnabled(boolean en) {
		super.setEnabled(en);
		if (!en && enabled) {
			this.removeMouseListener(this.getHandler().getEntityListener(this));
			this.removeMouseMotionListener(this.getHandler().getEntityListener(this));
			enabled = false;
		}
		else if (en && !enabled) {
			if (!this.isPartOfGroup()) {
				this.addMouseListener(this.getHandler().getEntityListener(this));
				this.addMouseMotionListener(this.getHandler().getEntityListener(this));
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
	public Group getGroup() {
		return this.group;
	}

	@Override
	public void setGroup(Group group) {
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

	@Override
	public boolean isSelected() {
		return isSelected;
	}

	@Override
	public void onSelected() {
		isSelected = true;
		fgColor = Constants.DEFAULT_SELECTED_COLOR;
		this.repaint();
	}

	@Override
	public void onDeselected() {
		isSelected = false;
		fgColor = fgColorBase;
		this.setStickingBorderActive(true);
		this.repaint();
	}

	public Color getFgColor() {
		return fgColor;
	}

	public Color getBgColor() {
		return bgColor;
	}

	@Override
	public String getSetting(GlobalSetting key) {
		if (key == GlobalSetting.FOREGROUND_COLOR) return fgColorString;
		else if (key == GlobalSetting.BACKGROUND_COLOR) return bgColorString;
		else return "";
	}

	@Override
	public void updateProperty(GlobalSetting key, String newValue) {
		String newState = "";
		for (String line : Utils.decomposeStringsWithComments(this.getPanelAttributes())) {
			if (!line.startsWith(key.toString())) newState += line + "\n";
		}
		newState = newState.substring(0, newState.length()-1); //remove last linebreak
		if (newValue != null && !newValue.isEmpty()) newState += "\n" + key.toString() + "=" + newValue; // null will not be added as a value
		this.setPanelAttributes(newState);
		this.getHandler().getDrawPanel().getSelector().updateSelectorInformation(); // update the property panel to display changed attributes
		this.repaint();
	}

	public Composite[] colorize(Graphics2D g2) {
		bgColorString = "";
		fgColorString = "";
		bgColor = Constants.DEFAULT_BACKGROUND_COLOR;
		fgColorBase = Constants.DEFAULT_FOREGROUND_COLOR;
		Vector<String> v = Utils.decomposeStringsWithComments(panelAttributes);
		for (int i = 0; i < v.size(); i++) {
			String line = v.get(i);
			if (line.indexOf("bg=") >= 0) {
				bgColorString = line.substring("bg=".length());
				bgColor = Utils.getColor(bgColorString);
				if (bgColor == null) bgColor = Constants.DEFAULT_BACKGROUND_COLOR;
			}
			else if (line.indexOf("fg=") >= 0) {
				fgColorString = line.substring("fg=".length());
				fgColorBase = Utils.getColor(fgColorString);
				if (fgColorBase == null) fgColorBase = Constants.DEFAULT_FOREGROUND_COLOR;
				if (!isSelected) fgColor = fgColorBase;
			}
		}

		alphaFactor = Constants.ALPHA_MIDDLE_TRANSPARENCY;
		if (bgColorString.equals("") || bgColorString.equals("default")) alphaFactor = Constants.ALPHA_FULL_TRANSPARENCY;

		Composite old = g2.getComposite();
		AlphaComposite alpha = AlphaComposite.getInstance(AlphaComposite.SRC_OVER, alphaFactor);
		Composite composites[] = { old, alpha };
		return composites;
	}

	@Override
	public Dimension getRealSize() {
		return new Dimension(getZoomedSize().width / handler.getGridSize() * Constants.DEFAULTGRIDSIZE, getZoomedSize().height / handler.getGridSize() * Constants.DEFAULTGRIDSIZE);
	}

	@Override
	public int getResizeArea(int x, int y) {
		int ret = 0;
		if ((x <= 5) && (x >= 0)) ret = Constants.RESIZE_LEFT;
		else if ((x <= this.getZoomedSize().width) && (x >= this.getZoomedSize().width - 5)) ret = Constants.RESIZE_RIGHT;

		if ((y <= 5) && (y >= 0)) ret = ret | Constants.RESIZE_TOP;
		else if ((y <= this.getZoomedSize().height) && (y >= this.getZoomedSize().height - 5)) ret = ret | Constants.RESIZE_BOTTOM;
		return ret;
	}

	@Override
	public int getPossibleResizeDirections() {
		return Constants.RESIZE_TOP | Constants.RESIZE_LEFT | Constants.RESIZE_BOTTOM | Constants.RESIZE_RIGHT;
	}

	@Override
	public void changeSize(int diffx, int diffy) {
		this.setSize(this.getZoomedSize().width + diffx, this.getZoomedSize().height + diffy);
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
	public boolean contains(Point p) {
		return Utils.contains(this, p);
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
			g2.setFont(this.getHandler().getFontHandler().getFont());
			g2.setColor(Color.red);
			this.getHandler().getFontHandler().writeText(g2, "in progress...", this.getZoomedSize().width / 2 - 40, this.getZoomedSize().height / 2 + (int) this.getHandler().getFontHandler().getFontSize() / 2, AlignHorizontal.LEFT);
		}
		else {
			repaint();
		}
	}

	@Override
	public GridElement CloneFromMe() {
		try {
			java.lang.Class<? extends GridElement> cx = this.getClass(); // get class of dynamic object
			GridElement c = cx.newInstance();
			c.setPanelAttributes(this.getPanelAttributes()); // copy states
			c.setRectangle(this.getRectangle());
			c.setHandlerAndInitListeners(this.getHandler());
			return c;
		} catch (Exception e) {
			log.error("Error at calling CloneFromMe() on entity", e);
		}
		return null;
	}

	/**
	 * Define and return the polygon on which relations stick on
	 */
	@Override
	public StickingPolygon generateStickingBorder(int x, int y, int width, int height) {
		StickingPolygon p = new StickingPolygon();
		p.addPoint(new Point(x, y));
		p.addPoint(new Point(x + width, y));
		p.addPoint(new Point(x + width, y + height));
		p.addPoint(new Point(x, y + height), true);
		return p;
	}

	public final void drawStickingPolygon(Graphics2D g2) {
		StickingPolygon poly;
		// The Java Implementations in the displaceDrawingByOnePixel list start at (1,1) to draw while any others start at (0,0)
		if (Utils.displaceDrawingByOnePixel()) poly = this.generateStickingBorder(1, 1, this.getZoomedSize().width - 1, this.getZoomedSize().height - 1);
		else poly = this.generateStickingBorder(0, 0, this.getZoomedSize().width - 1, this.getZoomedSize().height - 1);
		if (poly != null) {
			Color c = g2.getColor();
			Stroke s = g2.getStroke();
			g2.setColor(Constants.DEFAULT_SELECTED_COLOR);
			g2.setStroke(Utils.getStroke(LineType.DASHED, 1));
			poly.draw(g2);
			g2.setColor(c);
			g2.setStroke(s);
		}
	}

	@Override
	public final void paint(Graphics g) {
		super.paint(g);
		Graphics2D g2 = (Graphics2D) g;

		if (this.isSelected && Constants.show_stickingpolygon && !this.isPartOfGroup()) this.drawStickingPolygon(g2);

		// Zoom per transformation for certain elements. unused!
		// if (this instanceof SequenceDiagram) {
		// AffineTransform t = g2.getTransform();
		// float scale = ((float) this.getHandler().getGridSize()) / 10;
		// t.scale(scale, scale);
		// g2.setTransform(t);
		// }
		updateModelFromText();

		this.paintEntity(g2);
	}

	public abstract void paintEntity(Graphics g);

	protected final int textHeight() {
		return (int) (this.getHandler().getFontHandler().getFontSize(false) + this.getHandler().getFontHandler().getDistanceBetweenTexts(false));
	}

	protected final int textWidth(String text, boolean applyZoom) {
		return (int) (getHandler().getFontHandler().getTextSize(text, applyZoom).getWidth() + (int) getHandler().getFontHandler().getDistanceBetweenTexts(applyZoom));
	}

	@Override
	public JComponent getComponent() {
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
			handler.getDrawPanel().setLayer(getComponent(), lastLayerValue);
		}
	}

	@Override
	public Integer getLayer() {
		lastLayerValue = Integer.valueOf(GlobalSetting.LAYER.getValue());
		try {
			for (String s : Utils.decomposeStringsWithComments(panelAttributes)) {
				String key = GlobalSetting.LAYER.toString() + GlobalSetting.SEPARATOR;
				if (s.startsWith(key)) {
					String value = s.split(key)[1];
					lastLayerValue = Integer.valueOf(value);
				}
			}
		} catch (Exception e) {/* in case of an error return default layer*/}
		return lastLayerValue;
	}
	
	@Override
	public Dimension getZoomedSize() {
		return new Dimension(getWidth(), getHeight());
	}
	
	@Override
	public Rectangle getRectangle() {
		java.awt.Rectangle rect = getBounds();
		return new Rectangle(rect.x, rect.y, rect.width, rect.height);
	}
	
	@Override
	public void setRectangle(Rectangle rect) {
		setBounds(rect.x, rect.y, rect.width, rect.height);
	}
	
	@Override
	public Rectangle getVisibleRectangle() {
		java.awt.Rectangle rect = getVisibleRect();
		return new Rectangle(rect.x, rect.y, rect.width, rect.height);
	}
}
