package com.baselet.element.interfaces;

public interface GridElementDeprecatedAddons {

	boolean isOldAllInOneDiagram();

	void zoomDeprecatedSequenceAllInOne();

	public static final GridElementDeprecatedAddons NONE = new GridElementDeprecatedAddons() {

		@Override
		public boolean isOldAllInOneDiagram() {
			return false;
		}

		@Override
		public void zoomDeprecatedSequenceAllInOne() {}

	};
}
