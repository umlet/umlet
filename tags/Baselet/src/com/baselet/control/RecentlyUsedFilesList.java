package com.baselet.control;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class RecentlyUsedFilesList implements Iterable<String> {

	private List<String> recentFiles = new ArrayList<String>();

	public void add(String filename) {
		if (recentFiles.contains(filename)) recentFiles.remove(filename);
		recentFiles.add(0, filename);
		if (recentFiles.size() > Constants.RECENT_FILES_LIST_LENGTH) recentFiles.remove(Constants.RECENT_FILES_LIST_LENGTH);
	}

	public List<String> getList() {
		return recentFiles;
	}

	@Override
	public Iterator<String> iterator() {
		return recentFiles.iterator();
	}

	public void addAll(List<String> items) {
		recentFiles.addAll(items);
	}

	public boolean isEmpty() {
		return recentFiles.isEmpty();
	}
}
