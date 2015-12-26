package com.baselet.control.util;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class RecentlyUsedFilesList implements Iterable<String> {

	private static RecentlyUsedFilesList instance = new RecentlyUsedFilesList();

	public static RecentlyUsedFilesList getInstance() {
		return instance;
	}

	private static final int LIST_LENGTH = 10;

	private final List<String> recentFiles = new ArrayList<String>();

	public void add(String filename) {
		if (recentFiles.contains(filename)) {
			recentFiles.remove(filename);
		}
		recentFiles.add(0, filename);
		if (recentFiles.size() > RecentlyUsedFilesList.LIST_LENGTH) {
			recentFiles.remove(RecentlyUsedFilesList.LIST_LENGTH);
		}
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
