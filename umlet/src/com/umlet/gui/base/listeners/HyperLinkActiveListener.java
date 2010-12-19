package com.umlet.gui.base.listeners;

import javax.swing.event.HyperlinkEvent;
import javax.swing.event.HyperlinkListener;

import com.umlet.control.BrowserLauncher;

public class HyperLinkActiveListener implements HyperlinkListener {
	public void hyperlinkUpdate(HyperlinkEvent e) {
		if (e.getEventType() == HyperlinkEvent.EventType.ACTIVATED) {
			try {
				BrowserLauncher.openURL(e.getURL().toString());
			} catch (Throwable t) {
				t.printStackTrace();
			}
		}
	}
}
