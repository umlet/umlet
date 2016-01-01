package com.baselet.element.interfaces;

public interface GridElementDeprecatedAddons {

	void doBeforeExport();

	public static final GridElementDeprecatedAddons NONE = new GridElementDeprecatedAddons() {
		@Override
		public void doBeforeExport() {}
	};
}
