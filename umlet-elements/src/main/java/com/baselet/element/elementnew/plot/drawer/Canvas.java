package com.baselet.element.elementnew.plot.drawer;

import com.baselet.control.basics.geom.Dimension;
import com.baselet.control.basics.geom.Rectangle;
import com.baselet.diagram.draw.DrawHandler;
import com.baselet.diagram.draw.helper.ColorOwn;
import com.baselet.diagram.draw.helper.ColorOwn.Transparency;
import com.baselet.diagram.draw.helper.Theme;
import com.baselet.diagram.draw.helper.ThemeFactory;

/**
 * <pre>
 * | GGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGG
 * | G                                G
 * | G  OOOOOOOOOOOOOOO               G
 * | G  O             O               G
 * | G  O  IIIIIIIII  O               G
 * | G  O  I       I  O               G
 * | G  O  I       I  O               G
 * | G  O  IIIIIIIII  O               G
 * | G  O             O               G
 * | G  OOOOOOOOOOOOOOO               G
 * | G                                G
 * | G                                G
 * | G                                G
 * | G                                G
 * | G                                G
 * | G                                G
 * | GGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGG
 * </pre>
 */
public class Canvas {

	private int borderspace;
	private final Rectangle outerBorder; // Nothing is drawn between outerBorder and the GridElement-Border
	private final Rectangle innerBorder; // Only axis are drawn between outerBorder and innerBorder; inside the innerBorder the plot is drawn

	private final Dimension gridElementSize;

	public Canvas(Dimension gridElementSize) {
		super();
		this.gridElementSize = gridElementSize;
		outerBorder = new Rectangle();
		innerBorder = new Rectangle();
		setBorder(0, 0, 0, 0, 0);
	}

	public void setBorder(int x, int y, int width, int height, int borderspace) {
		this.borderspace = borderspace;
		outerBorder.setBounds(x, y, width, height);
		updateInnerBorder();
	}

	public void setBorderX(int x) {
		outerBorder.setBounds(x, outerBorder.y, outerBorder.width, outerBorder.height);
		updateInnerBorder();
	}

	public void setBorderY(int y) {
		outerBorder.setBounds(outerBorder.x, y, outerBorder.width, outerBorder.height);
		updateInnerBorder();
	}

	public void setBorderWidth(int width) {
		outerBorder.setBounds(outerBorder.x, outerBorder.y, width, outerBorder.height);
		updateInnerBorder();
	}

	public void setBorderHeight(int height) {
		outerBorder.setBounds(outerBorder.x, outerBorder.y, outerBorder.width, height);
		updateInnerBorder();
	}

	private void updateInnerBorder() {
		innerBorder.setBounds(outerBorder.x + borderspace, outerBorder.y + borderspace, outerBorder.width + borderspace, outerBorder.height + borderspace);
	}

	/**
	 * <pre>
	 * {@literal
	 * | <----->
	 * | G  O  I       I  O               G
	 * }
	 * </pre>
	 */
	public int getInnerLeftPos() {
		return innerBorder.x;
	}

	public int getInnerUpPos() {
		return innerBorder.y;
	}

	/**
	 * <pre>
	 * {@literal
	 * |               <------------------>
	 * | G  O  I       I  O               G
	 * }
	 * </pre>
	 */
	public int getInnerRightBorderWidth() {
		return innerBorder.width;
	}

	public int getInnerDownBorderHeight() {
		return innerBorder.height;
	}

	/**
	 * <pre>
	 * {@literal
	 * | <------------->
	 * | G  O  I       I  O               G
	 * }
	 * </pre>
	 */
	public int getInnerRightPos() {
		return gridElementSize.width - getInnerRightBorderWidth();
	}

	public int getInnerDownPos() {
		return gridElementSize.height - getInnerDownBorderHeight();
	}

	/**
	 * <pre>
	 * {@literal
	 * | <----->       <------------------>
	 * | G  O  I       I  O               G
	 * }
	 * </pre>
	 */
	public int getInnerHorizontalSum() {
		return getInnerLeftPos() + getInnerRightBorderWidth();
	}

	public int getInnerVerticalSum() {
		return getInnerUpPos() + getInnerDownBorderHeight();
	}

	/**
	 * <pre>
	 * {@literal
	 * |       <------->
	 * | G  O  I       I  O               G
	 * }
	 * </pre>
	 */
	public int getInnerHorizontalDrawspace() {
		return getInnerRightPos() - getInnerLeftPos();
	}

	public int getInnerVerticalDrawspace() {
		return getInnerDownPos() - getInnerUpPos();
	}

	public int getOuterLeftPos() {
		return outerBorder.x;
	}

	public int getOuterUpPos() {
		return outerBorder.y;
	}

	public int getOuterRightBorderWidth() {
		return outerBorder.width;
	}

	public int getOuterDownBorderHeight() {
		return outerBorder.height;
	}

	public int getOuterRightPos() {
		return gridElementSize.width - getOuterRightBorderWidth();
	}

	public int getOuterDownPos() {
		return gridElementSize.height - getOuterDownBorderHeight();
	}

	public int getOuterHorizontalSum() {
		return getOuterLeftPos() + getOuterRightBorderWidth();
	}

	public int getOuterVerticalSum() {
		return getOuterUpPos() + getOuterDownBorderHeight();
	}

	public boolean hasHorizontalDrawspace() {
		return gridElementSize.width > getOuterHorizontalSum();
	}

	public boolean hasVerticalDrawspace() {
		return gridElementSize.width > getOuterHorizontalSum();
	}

	public void draw(DrawHandler baseDrawHandler) {
		Theme currentTheme = ThemeFactory.getCurrentTheme();
		baseDrawHandler.setBackgroundColor(currentTheme.getColor(Theme.PredefinedColors.TRANSPARENT));
		baseDrawHandler.setForegroundColor(currentTheme.getColor(Theme.PredefinedColors.RED).transparency(Transparency.BACKGROUND));
		baseDrawHandler.drawRectangle(getOuterLeftPos(), getOuterUpPos(), getOuterRightPos() - getOuterLeftPos() - 1, getOuterDownPos() - getOuterUpPos());
		baseDrawHandler.setForegroundColor(currentTheme.getColor(Theme.PredefinedColors.BLUE));
		baseDrawHandler.drawRectangle(getInnerLeftPos(), getInnerUpPos(), getInnerRightPos() - getInnerLeftPos(), getInnerDownPos() - getInnerUpPos());
	}

}
