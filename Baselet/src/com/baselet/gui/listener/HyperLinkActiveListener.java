package com.baselet.gui.listener;

import javax.swing.event.HyperlinkEvent;
import javax.swing.event.HyperlinkListener;

import com.baselet.gui.BrowserLauncher;

public class HyperLinkActiveListener implements HyperlinkListener {
	@Override
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
