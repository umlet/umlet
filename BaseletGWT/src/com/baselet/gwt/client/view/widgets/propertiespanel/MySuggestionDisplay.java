package com.baselet.gwt.client.view.widgets.propertiespanel;

import java.util.Collection;

import org.apache.log4j.Logger;

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
 */
public class MySuggestionDisplay extends DefaultSuggestionDisplay {

	/**
	 * padding from the browser frame to the top of the display box.
	 */
	private static final int DISPLAY_BOX_TOP_PADDING = 40;
	private static final int DISPLAY_BOX_MIN_HEIGHT = 60;

	private int suggestionSize;
	private int height;
	private int counter;

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
		// getPopupPanel().getElement().getStyle().setProperty("maxHeight",
		getPopupPanel().getWidget().getElement().getStyle().setProperty("maxHeight",
				Math.max(DISPLAY_BOX_MIN_HEIGHT, suggestBox.getElement().getAbsoluteTop() - DISPLAY_BOX_TOP_PADDING) + Unit.PX.getType());
		// getPopupPanel().getWidget().getel.setHeight("100%");
		suggestionSize = suggestions.size();
		height = suggestBox.getElement().getAbsoluteTop() - DISPLAY_BOX_TOP_PADDING;
		counter = 0;
		super.showSuggestions(suggestBox, suggestions, isDisplayStringHTML, isAutoSelectEnabled, callback);
		// getPopupPanel().getElement().getStyle().setProperty("maxHeight",
		// Math.max(DISPLAY_BOX_MIN_HEIGHT, suggestBox.getElement().getAbsoluteTop() - DISPLAY_BOX_TOP_PADDING) + Unit.PX.getType());
		// ((ScrollPanel)getPopupPanel().getWidget()).set
		// getPopupPanel().getWidget().setHeight(Math.min(getPopupPanel().getWidget().getElement().getScrollHeight(), Math.max(DISPLAY_BOX_MIN_HEIGHT,
		// suggestBox.getElement().getAbsoluteTop() - DISPLAY_BOX_TOP_PADDING)) + Unit.PX.getType());
		if (isSuggestionListShowing()) {
			popupHideTimer.cancel();
			paletteShouldIgnoreMouseClicks = true;
		}
	}

	@Override
	protected void moveSelectionDown() {
		// TODO Auto-generated method stub
		super.moveSelectionDown();
		// ScrollPanel scp = (ScrollPanel) getPopupPanel().getWidget();
		// counter++;
		// scp.setVerticalScrollPosition(counter * height / suggestionSize);
		// SearchU
		scrollToSelected();
	}

	@Override
	protected void moveSelectionUp() {
		// TODO Auto-generated method stub
		super.moveSelectionUp();

		// counter--;
		// ((ScrollPanel) getPopupPanel().getWidget()).setVerticalScrollPosition(counter * height / suggestionSize);
		scrollToSelected();
	}

	private void scrollToSelected() {
		NodeList<Element> tdChilds = getPopupPanel().getWidget().getElement().getElementsByTagName("td");
		for (int i = 0; i < tdChilds.getLength(); i++) {
			Element e = tdChilds.getItem(i);
			Logger.getLogger(MySuggestionDisplay.class).info(e.getId() + ": class='" + e.getClassName() + "'");
			if (e.getClassName().contains("item-selected")) {
				((ScrollPanel) getPopupPanel().getWidget()).setVerticalScrollPosition(e.getOffsetTop());
				// e.getFirstChild()..scrollIntoView();
				break;
			}
		}
	}

	@Override
	protected Widget decorateSuggestionList(Widget suggestionList) {
		suggestionList = new ScrollPanel(suggestionList);
		return super.decorateSuggestionList(suggestionList);
	}

	public boolean getPaletteShouldIgnoreMouseClicks() {
		return paletteShouldIgnoreMouseClicks;
	}
};