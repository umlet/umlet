package com.baselet.gui.eclipse;


public class UpdateActionBars implements Runnable {

	private IActionBars bars;

	public UpdateActionBars(IActionBars bars) {
		this.bars = bars;
	}

	@Override
	public void run() {
		this.bars.updateActionBars();
	}

}
