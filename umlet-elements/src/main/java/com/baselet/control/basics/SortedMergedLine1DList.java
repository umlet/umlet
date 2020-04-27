package com.baselet.control.basics;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

/**
 * Maintains a sorted list of none overlapping Line1D elements.
 * On insertion the existing elements are searched if the new element
 * would overlap with some of them then all the overlapping elements
 * including the new one are merged into one element.
 */
public class SortedMergedLine1DList implements List<Line1D> {

	private final List<Line1D> list = new ArrayList<Line1D>();

	@Override
	public int size() {
		return list.size();
	}

	@Override
	public boolean isEmpty() {
		return list.isEmpty();
	}

	@Override
	public boolean contains(Object o) {
		return list.contains(o);
	}

	@Override
	public Iterator<Line1D> iterator() {
		return list.iterator();
	}

	@Override
	public Object[] toArray() {
		return list.toArray();
	}

	@Override
	public <T> T[] toArray(T[] a) {
		return list.toArray(a);
	}

	/**
	 * The element is NOT inserted at the END.
	 * It will be inserted according to the sorting
	 * and maybe a merge of two or more elements will happen.
	 *
	 * @param e the element which will be inserted at the correct position to maintain a sorted list. Maybe a merge of elements happens. Low and High must be != null.
	 */
	@Override
	public boolean add(Line1D e) {
		if (contains(e)) {
			return false;
		}
		else {
			ListIterator<Line1D> listIter = list.listIterator();
			// while high < e.low no overlap
			while (listIter.hasNext()) {
				if (listIter.next().getHigh() >= e.getLow()) {
					listIter.previous();
					break;
				}
			}
			if (!listIter.hasNext()) {
				list.add(e);
				return true;
			}
			else {
				int insertIndex = listIter.nextIndex();
				Line1D tmpLine = listIter.next();
				if (tmpLine.getLow() <= e.getHigh()) {
					double low = Math.min(tmpLine.getLow(), e.getLow());
					double high = tmpLine.getHigh();
					while (listIter.hasNext()) {
						tmpLine = listIter.next();
						if (e.isIntersecting(tmpLine)) {
							listIter.remove();
							high = tmpLine.getHigh();
						}
						else {
							break;
						}
					}
					list.set(insertIndex, new Line1D(low, Math.max(e.getHigh(), high)));
				}
				else {
					list.add(insertIndex, e);
				}
				return true;
			}
		}
	}

	@Override
	public boolean remove(Object o) {
		return list.remove(o);
	}

	@Override
	public boolean containsAll(Collection<?> c) {
		return list.containsAll(c);
	}

	/**
	 * The element is NOT inserted at the END.
	 * @see #add(Line1D)
	 */
	@Override
	public boolean addAll(Collection<? extends Line1D> c) {
		boolean changed = false;
		for (Line1D l : c) {
			changed = add(l) || changed;
		}
		return changed;
	}

	/**
	 * The element is NOT inserted at the END.
	 * @see #add(Line1D)
	 */
	public boolean addAll(Line1D[] c) {
		boolean changed = false;
		for (Line1D l : c) {
			changed = add(l) || changed;
		}
		return changed;
	}

	/**
	 * NOT supported.
	 */
	@Override
	public boolean addAll(int index, Collection<? extends Line1D> c) {
		throw new UnsupportedOperationException();
	}

	@Override
	public boolean removeAll(Collection<?> c) {
		return list.removeAll(c);
	}

	@Override
	public boolean retainAll(Collection<?> c) {
		return list.retainAll(c);
	}

	@Override
	public void clear() {
		list.clear();
	}

	@Override
	public Line1D get(int index) {
		return list.get(index);
	}

	/**
	 * NOT supported.
	 */
	@Override
	public Line1D set(int index, Line1D element) {
		throw new UnsupportedOperationException();
	}

	/**
	 * NOT supported.
	 */
	@Override
	public void add(int index, Line1D element) {
		throw new UnsupportedOperationException();
	}

	@Override
	public Line1D remove(int index) {
		return list.remove(index);
	}

	@Override
	public int indexOf(Object o) {
		return list.indexOf(o);
	}

	@Override
	public int lastIndexOf(Object o) {
		return list.lastIndexOf(o);
	}

	@Override
	public ListIterator<Line1D> listIterator() {
		return list.listIterator();
	}

	@Override
	public ListIterator<Line1D> listIterator(int index) {
		return list.listIterator(index);
	}

	@Override
	public List<Line1D> subList(int fromIndex, int toIndex) {
		return list.subList(fromIndex, toIndex);
	}

}
