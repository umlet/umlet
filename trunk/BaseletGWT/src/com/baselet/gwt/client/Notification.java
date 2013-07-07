package com.baselet.gwt.client;

import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.ui.RootPanel;

public class Notification {
	
	private static String lastShownFeatureNotSupportedText;

	public static void showFeatureNotSupported(String text) {
		if (text.equals(lastShownFeatureNotSupportedText)) {
			return; // don't repeat the last warning
		}
		lastShownFeatureNotSupportedText = text;
		RootPanel.get("featurewarning").getElement().setInnerHTML(text);
		ElementFader.fade(RootPanel.get("featurewarning").getElement(), 1, 0, 15000);
	}
	
	private static class ElementFader {
		private static Timer timer = null;
	    private static int stepCount = 0;
	    static synchronized void fade(final Element element, final float startOpacity, final float endOpacity, int totalTimeMillis) {
	        final int numberOfSteps = 30;
	        int stepLengthMillis = totalTimeMillis / numberOfSteps;
	        stepCount = 0;
	        final float deltaOpacity = (endOpacity - startOpacity) / numberOfSteps;
	        if (timer != null) {
	        	timer.cancel();
	        }
	        timer = new Timer() {
	            @Override
	            public void run() {
	                float opacity = startOpacity + (stepCount * deltaOpacity);
	                DOM.setStyleAttribute(element, "opacity", Float.toString(opacity));
	                stepCount++;
	                if (stepCount == numberOfSteps) {
	                    DOM.setStyleAttribute(element, "opacity", Float.toString(endOpacity));
	                    this.cancel();
	                }
	            }
	        };
	        timer.scheduleRepeating(stepLengthMillis);
	    }
	}
}
