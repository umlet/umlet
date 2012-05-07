package com.baselet.diagram.draw.objects;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Rectangle;

import com.baselet.control.Constants;
import com.baselet.diagram.draw.BaseDrawHandler;


/**
 * GGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGG
 * G                                G
 * G  OOOOOOOOOOOOOOO               G
 * G  O             O               G
 * G  O  IIIIIIIII  O               G
 * G  O  I       I  O               G
 * G  O  I       I  O               G
 * G  O  IIIIIIIII  O               G
 * G  O             O               G
 * G  OOOOOOOOOOOOOOO               G
 * G                                G
 * G                                G
 * G                                G
 * G                                G
 * G                                G
 * G                                G
 * GGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGG
 */
public class Canvas {

	private int borderspace;
	private Rectangle outerBorder; // Nothing is drawn between outerBorder and the GridElement-Border
	private Rectangle innerBorder; // Only axis are drawn between outerBorder and innerBorder; inside the innerBorder the plot is drawn

	private Dimension gridElementSize;

	public Canvas(Dimension gridElementSize) {
		super();
		this.gridElementSize = gridElementSize;
		this.outerBorder = new Rectangle();
		this.innerBorder = new Rectangle();
		setBorder(0,0,0,0,0);
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
	 * <----->
	 * G  O  I       I  O               G
	 */
	public int getInnerLeftPos() {
		return innerBorder.x;
	}

	public int getInnerUpPos() {
		return innerBorder.y;
	}

	/**
	 *               <------------------>
	 * G  O  I       I  O               G
	 */
	public int getInnerRightBorderWidth() {
		return innerBorder.width;
	}

	public int getInnerDownBorderHeight() {
		return innerBorder.height;
	}

	/**
	 * <------------->
	 * G  O  I       I  O               G
	 */
	public int getInnerRightPos() {
		return gridElementSize.width - getInnerRightBorderWidth();
	}

	public int getInnerDownPos() {
		return gridElementSize.height - getInnerDownBorderHeight();
	}

	/**
	 * <----->       <------------------>
	 * G  O  I       I  O               G
	 */
	public int getInnerHorizontalSum() {
		return getInnerLeftPos() + getInnerRightBorderWidth();
	}

	public int getInnerVerticalSum() {
		return getInnerUpPos() + getInnerDownBorderHeight();
	}

	/**
	 *       <------->
	 * G  O  I       I  O               G
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
		return (gridElementSize.width > getOuterHorizontalSum());
	}

	public boolean hasVerticalDrawspace() {
		return (gridElementSize.width > getOuterHorizontalSum());
	}

	public void draw(BaseDrawHandler baseDrawHandler) {
		baseDrawHandler.setBackgroundAlpha(Constants.ALPHA_FULL_TRANSPARENCY);
		baseDrawHandler.setForegroundAlpha(Constants.ALPHA_MIDDLE_TRANSPARENCY);
		baseDrawHandler.setForegroundColor(Color.RED);
		baseDrawHandler.drawRectangle(getOuterLeftPos(), getOuterUpPos(), getOuterRightPos() - getOuterLeftPos() -1, getOuterDownPos() - getOuterUpPos());
		baseDrawHandler.setForegroundColor(Color.BLUE);
		baseDrawHandler.drawRectangle(getInnerLeftPos(), getInnerUpPos(), getInnerRightPos() - getInnerLeftPos(), getInnerDownPos() - getInnerUpPos());
	}

}
