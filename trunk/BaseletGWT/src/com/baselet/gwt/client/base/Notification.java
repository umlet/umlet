package com.baselet.gwt.client.base;

import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.ui.RootPanel;

public class Notification {

	private static String lastShownFeatureNotSupportedText;
	private static Element element = RootPanel.get("featurewarning").getElement();

	public static void showFeatureNotSupported(String text, boolean fadeOut) {
		if (text.equals(lastShownFeatureNotSupportedText)) {
			return; // don't repeat the last warning
		}
		lastShownFeatureNotSupportedText = text;
		element.getStyle().setColor("red");
		element.setInnerHTML(text);
		if (fadeOut) {
			ElementFader.fade(element, 1, 0, 7000, 3000);
		}
	}

	public static void showInfo(String text) {
		element.getStyle().setColor("blue");
		element.setInnerHTML(text);
		ElementFader.fade(element, 1, 0, 4000, 2000);
	}

	private static class ElementFader {
		private static int stepCount = 0;
		private static Timer timer;
		private static Timer timerFader;

		public synchronized static void fade(final Element element, final float startOpacity, final float endOpacity, final int delay, final int totalTimeMillis) {
			if (timer != null) {
				timer.cancel();
			}
			if (timerFader != null) {
				timerFader.cancel();
			}
			DOM.setStyleAttribute(element, "opacity", Float.toString(startOpacity));// set start opacity now to make sure the opacity of an interrupted previous timer is overwritten
			timer = new Timer() {
				@Override
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
				@Override
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
