package com.baselet.util;

import java.util.LinkedHashMap;

/**
 * Simple cache based on a LinkedHashMap
 *
 * @param <K> Key
 * @param <V> Value
 */
public class LRUCache<K, V> extends LinkedHashMap<K, V> {

	private static final long serialVersionUID = 1L;
	private static final float LOAD_FACTOR = 0.8f;

	private final int cacheSize;

	/**
	 *
	 * @param cacheSize how many elements should fit in the cache (the actual capacity of the map may be bigger)
	 */
	public LRUCache(int cacheSize) {
		super((int) ((cacheSize + 1) / LOAD_FACTOR) + 1, LOAD_FACTOR, true);
		this.cacheSize = cacheSize;
	}

	@Override
	protected boolean removeEldestEntry(java.util.Map.Entry<K, V> eldest) {
		// if size >= capacity * loadfactor then rehashing occurs (see implementation of LinkedHashMap.addEntry)
		// this method is called before the size check, therefore remove the eldest entry if the threshold is reached
		return size() >= this.cacheSize;
	};

}