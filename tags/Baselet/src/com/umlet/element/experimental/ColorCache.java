package com.umlet.element.experimental;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Composite;
import java.awt.Graphics2D;
import java.util.Vector;

import com.baselet.control.Constants;
import com.baselet.control.Utils;

public class ColorCache {

	// deselectedColor and fgColor must be stored separately because selection changes the actual fgColor but not the fgColorBase
	protected Color fgColorBase = Color.black;
	protected Color fgColor = fgColorBase;
	private String fgColorString = "";
	protected Color bgColor = Color.white;
	private String bgColorString = "";
	protected float alphaFactor;



	public Color getFgColor() {
		return fgColor;
	}

	public String getFGColorString() {
		return fgColorString;
	}

	public Color getBgColor() {
		return bgColor;
	}

	public String getBGColorString() {
		return bgColorString;
	}

	public Composite[] colorize(Graphics2D g2, String panelAttributes, boolean isSelected) {
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
}
