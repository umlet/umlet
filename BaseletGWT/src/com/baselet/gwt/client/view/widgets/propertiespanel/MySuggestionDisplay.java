package com.baselet.gwt.client.view.widgets.propertiespanel;

import java.util.Collection;

import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.NodeList;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.event.logical.shared.CloseEvent;
import com.google.gwt.event.logical.shared.CloseHandler;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.ScrollPanel;
import com.google.gwt.user.client.ui.SuggestBox;
import com.google.gwt.user.client.ui.SuggestBox.DefaultSuggestionDisplay;
import com.google.gwt.user.client.ui.SuggestBox.SuggestionCallback;
import com.google.gwt.user.client.ui.SuggestOracle.Suggestion;
import com.google.gwt.user.client.ui.Widget;

/**
 * The purpose of this class is to avoid mouse interaction with the palette as long as the suggestbox is visible
 * a clean approach would be avoiding event-propagation (eg: with event.stopPropagation()) but unfortunately
 * GWT DefaultSuggestionDisplay is very inflexible and it would be necessary to change the behavior of MenuBar.onBrowserEvent(Event event)
 * This is not possible without copying many classes because of package-private visibility
 *
 * This alternative approach uses a timer to avoid palette interaction for some time after the popup closes to make sure mouseevents can be avoided
 *
 * The display also supports a maximum height and uses the width of the suggestionBox, if these
 * limits are exceeded scrollbars will appear and it will auto scroll to the selected element.
 */
public class MySuggestionDisplay extends DefaultSuggestionDisplay {

	/**
	 * padding from the browser frame to the top of the display box.
	 */
	private static final int DISPLAY_BOX_TOP_PADDING = 40;
	private static final int DISPLAY_BOX_MIN_HEIGHT = 60;

	private boolean paletteShouldIgnoreMouseClicks = false;
	private final Timer popupHideTimer = new Timer() {
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
		getPopupPanel().getWidget().setWidth(suggestBox.getElement().getScrollWidth() + Unit.PX.getType());
		getPopupPanel().getWidget().getElement().getStyle().setProperty("maxHeight",
				Math.max(DISPLAY_BOX_MIN_HEIGHT, suggestBox.getElement().getAbsoluteTop() - DISPLAY_BOX_TOP_PADDING) + Unit.PX.getType());
		super.showSuggestions(suggestBox, suggestions, isDisplayStringHTML, isAutoSelectEnabled, callback);
		if (isSuggestionListShowing()) {
			popupHideTimer.cancel();
			paletteShouldIgnoreMouseClicks = true;
		}
	}

	@Override
	protected void moveSelectionDown() {
		super.moveSelectionDown();
		scrollToSelected();
	}

	@Override
	protected void moveSelectionUp() {
		super.moveSelectionUp();
		scrollToSelected();
	}

	private void scrollToSelected() {
		// since the DefaultSuggestionDisplay does not provide a way to access the selected Element
		// we need to search for the "item-selected" class in the td tags
		NodeList<Element> tdChilds = getPopupPanel().getWidget().getElement().getElementsByTagName("td");
		for (int i = 0; i < tdChilds.getLength(); i++) {
			Element e = tdChilds.getItem(i);
			if (e.getClassName().contains("item-selected")) {
				((ScrollPanel) getPopupPanel().getWidget()).setVerticalScrollPosition(e.getOffsetTop());
				break;
			}
		}
	}

	@Override
	protected Widget decorateSuggestionList(Widget suggestionList) {
		// if the decoration is changed check the other methods, because some assume that there is only a scroll panel
		suggestionList = new ScrollPanel(suggestionList);
		return super.decorateSuggestionList(suggestionList);
	}

	public boolean getPaletteShouldIgnoreMouseClicks() {
		return paletteShouldIgnoreMouseClicks;
	}
};