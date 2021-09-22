package com.baselet.diagram.draw.helper.theme;

import com.baselet.diagram.draw.helper.ColorOwn;

import java.util.ArrayList;
import java.util.List;

public class ThemeFactory {
	public enum THEMES {
		LIGHT, DARK
	}

	private static Theme theme;
	private static String lastBackgroundColor;

	private static THEMES activeThemeEnum;

	private static final List<ThemeChangeListener> listeners;

	static {
		listeners = new ArrayList<ThemeChangeListener>();
	}

	public static void changeTheme(THEMES chosenTheme, String backgroundColor, boolean overrideBackground) {
		if (!chosenTheme.equals(activeThemeEnum)) {
			switch (chosenTheme) {
				case DARK:
					activeThemeEnum = chosenTheme;
					theme = new ThemeDark();
					break;
				case LIGHT:
					activeThemeEnum = chosenTheme;
					theme = new ThemeLight();
					break;
				default:
					activeThemeEnum = THEMES.LIGHT;
					theme = new ThemeLight();
			}
		}

		if (backgroundColor != null) {
			lastBackgroundColor = backgroundColor;
		}

		// In some occasions (e.g. PNG export) we don't want to overwrite the standard background of the theme
		if (lastBackgroundColor != null && overrideBackground) {
			theme.styleColorMap.put(Theme.ColorStyle.DEFAULT_DOCUMENT_BACKGROUND, new ColorOwn(lastBackgroundColor));
		}
		for (ThemeChangeListener listener : listeners) {
			listener.onThemeChange();
		}
	}

	public static Theme getCurrentTheme() {
		if (theme == null) {
			changeTheme(THEMES.LIGHT, null, true);
		}
		return theme;
	}

	public static THEMES getActiveThemeEnum() {
		return activeThemeEnum;
	}

	public static void addListener(ThemeChangeListener listener) {
		listeners.add(listener);
	}
}
