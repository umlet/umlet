package com.baselet.gwt.client.view.widgets.propertiespanel;

import com.baselet.gui.AutocompletionText;
import com.google.gwt.user.client.ui.SuggestOracle.Suggestion;

public class MySuggestion implements Suggestion {

	private AutocompletionTextGwt autoCompletionText;

	public MySuggestion(AutocompletionTextGwt autoCompletionText) {
		this.autoCompletionText = autoCompletionText;
	}

	@Override
	public String getDisplayString() {
		return autoCompletionText.getHtmlInfo();
	}

	@Override
	public String getReplacementString() {
		return autoCompletionText.getText();
	}

}