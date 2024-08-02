package me.realized.de.arenaregen.util;

public final class Pair<K, V> {

	private final K key;
	private final V value;

	public Pair(final K key, final V value) {

		this.key = key;
		this.value = value;

	}

	public K getKey() {

		return key;

	}

	public V getValue() {

		return value;

	}

}