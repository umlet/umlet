package com.baselet.gwt.client;

import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.ui.RootPanel;

public class Notification {

	private static String lastShownFeatureNotSupportedText;

	public static void showFeatureNotSupported(String text, boolean fadeOut) {
		if (text.equals(lastShownFeatureNotSupportedText)) {
			return; // don't repeat the last warning
		}
		lastShownFeatureNotSupportedText = text;
		RootPanel.get("featurewarning").getElement().setInnerHTML(text);
		if (fadeOut) {
			ElementFader.fade(RootPanel.get("featurewarning").getElement(), 1, 0, 7000 ,3000);
		}
	}

	private static class ElementFader {
		private static int stepCount = 0;
		private static Timer timer;
		private static Timer timerFader;

		public static void fade(final Element element, final float startOpacity, final float endOpacity, final int delay, final int totalTimeMillis) {
			if (timer != null) timer.cancel();
			if (timerFader != null) timerFader.cancel();
			timer = new Timer() {
				public void run() {
					fade(element, startOpacity, endOpacity, totalTimeMillis);
				}
			};
			timer.schedule(delay);
		}
		private static void fade(final Element element, final float startOpacity, final float endOpacity, final int totalTimeMillis) {
			final int numberOfSteps = 30;
			int stepLengthMillis = totalTimeMillis / numberOfSteps;
			stepCount = 0;
			final float deltaOpacity = (endOpacity - startOpacity) / numberOfSteps;
			timerFader = new Timer() {
				public void run() {
					float opacity = startOpacity + stepCount * deltaOpacity;
					DOM.setStyleAttribute(element, "opacity", Float.toString(opacity));

					stepCount++;
					if (stepCount == numberOfSteps) {
						DOM.setStyleAttribute(element, "opacity", Float.toString(endOpacity));
						cancel();
					}
				}
			};
			timerFader.scheduleRepeating(stepLengthMillis);
		}
	}
}
