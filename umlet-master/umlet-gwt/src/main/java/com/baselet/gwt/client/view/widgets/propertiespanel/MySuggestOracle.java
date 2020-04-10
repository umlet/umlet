package com.baselet.gwt.client.view.widgets.propertiespanel;

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;

import com.baselet.gui.AutocompletionText;
import com.google.gwt.user.client.ui.SuggestOracle;

public class MySuggestOracle extends SuggestOracle {

	@Override
	public boolean isDisplayStringHTML() {
		return true;
	}

	private List<Suggestion> suggestions = new ArrayList<Suggestion>();

	@Override
	public void requestSuggestions(final Request request, final Callback callback) {
		Collection<Suggestion> result = getSuggestionsForText(request.getQuery());
		Response response = new Response(result);
		callback.onSuggestionsReady(request, response);
	}

	public Collection<Suggestion> getSuggestionsForText(String userInput) {
		String userInputLc = userInput.toLowerCase(Locale.ENGLISH);
		Collection<Suggestion> result = new LinkedList<Suggestion>();
		for (Suggestion suggestion : suggestions) {
			if (suggestion.getReplacementString().toLowerCase(Locale.ENGLISH).startsWith(userInputLc)) {
				result.add(highlightUserInput(suggestion, userInputLc));
			}
		}
		return result;
	}

	private Suggestion highlightUserInput(final Suggestion suggestion, final String userInput) {
		return new Suggestion() {
			@Override
			public String getReplacementString() {
				return suggestion.getReplacementString();
			}

			@Override
			public String getDisplayString() {
				return "<strong>" + userInput + "</strong>" + suggestion.getDisplayString().substring(userInput.length());
			}
		};
	}

	public void setAutocompletionList(List<AutocompletionText> autocompletionList) {
		suggestions.clear();
		for (AutocompletionText text : autocompletionList) {
			suggestions.add(new MySuggestion(text));
		}
	}

	private boolean showAllAsDefault = false;

	public void setShowAllAsDefault(boolean showAllAsDefault) {
		this.showAllAsDefault = showAllAsDefault;
	}

	@Override
	public void requestDefaultSuggestions(final Request request, final Callback callback) {
		if (showAllAsDefault) {
			callback.onSuggestionsReady(request, new Response(suggestions));
		}
		else {
			super.requestDefaultSuggestions(request, callback);
		}
	}
}
