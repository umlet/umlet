package com.baselet.gwt.client.view.widgets.propertiespanel;

import java.util.Collection;

import com.google.gwt.event.logical.shared.CloseEvent;
import com.google.gwt.event.logical.shared.CloseHandler;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.SuggestBox;
import com.google.gwt.user.client.ui.SuggestBox.DefaultSuggestionDisplay;
import com.google.gwt.user.client.ui.SuggestBox.SuggestionCallback;
import com.google.gwt.user.client.ui.SuggestOracle.Suggestion;

/**
 * The purpose of this class is to avoid mouse interaction with the palette as long as the suggestbox is visible
 * a clean approach would be avoiding event-propagation (eg: with event.stopPropagation()) but unfortunately
 * GWT DefaultSuggestionDisplay is very inflexible and it would be necessary to change the behavior of MenuBar.onBrowserEvent(Event event)
 * This is not possible without copying many classes because of package-private visibility
 * 
 * This alternative approach uses a timer to avoid palette interaction for some time after the popup closes to make sure mouseevents can be avoided
 */
public class MySuggestionDisplay extends DefaultSuggestionDisplay {
	private boolean paletteShouldIgnoreMouseClicks = false;
	private Timer popupHideTimer = new Timer() {
		@Override
		public void run() {
			paletteShouldIgnoreMouseClicks = false;
		}
	};

	@Override
	protected PopupPanel createPopup() {
		PopupPanel p = super.createPopup();
		p.addCloseHandler(new CloseHandler<PopupPanel>() {
			@Override
			public void onClose(CloseEvent<PopupPanel> event) {
				popupHideTimer.schedule(400);
			}
		});
		return p;
	}

	@Override
	protected void showSuggestions(SuggestBox suggestBox, Collection<? extends Suggestion> suggestions, boolean isDisplayStringHTML, boolean isAutoSelectEnabled, SuggestionCallback callback) {
		super.showSuggestions(suggestBox, suggestions, isDisplayStringHTML, isAutoSelectEnabled, callback);
		if (isSuggestionListShowing()) {
			popupHideTimer.cancel();
			paletteShouldIgnoreMouseClicks = true;
		}
	}

	public boolean getPaletteShouldIgnoreMouseClicks() {
		return paletteShouldIgnoreMouseClicks;
	}
};