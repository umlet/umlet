package com.baselet.client;

import org.vectomatic.dnd.DataTransferExt;
import org.vectomatic.dnd.DropPanel;

import com.google.gwt.event.dom.client.DomEvent;
import com.google.gwt.event.dom.client.DragEnterEvent;
import com.google.gwt.event.dom.client.DragEnterHandler;
import com.google.gwt.event.dom.client.DragLeaveEvent;
import com.google.gwt.event.dom.client.DragLeaveHandler;
import com.google.gwt.event.dom.client.DragOverEvent;
import com.google.gwt.event.dom.client.DragOverHandler;
import com.google.gwt.event.dom.client.DropEvent;
import com.google.gwt.event.dom.client.DropHandler;

public class OwnDropPanel {

	DropPanel dropPanel = new DropPanel();
	
	FileOpenHandler handler = new FileOpenHandler();
	
	public DropPanel getDropPanel() {
		return dropPanel;
	}

	public OwnDropPanel() {
		dropPanel.addDragOverHandler(new DragOverHandler() {
			@Override
			public void onDragOver(DragOverEvent event) {
				avoidDefaultHandling(event);
			}
		});
		dropPanel.addDragEnterHandler(new DragEnterHandler() {
			@Override
			public void onDragEnter(DragEnterEvent event) {
				avoidDefaultHandling(event);
			}
		});
		dropPanel.addDragLeaveHandler(new DragLeaveHandler() {
			@Override
			public void onDragLeave(DragLeaveEvent event) {
				avoidDefaultHandling(event);
			}
		});
		dropPanel.addDropHandler(new DropHandler() {
			@Override
			public void onDrop(DropEvent event) {
				handler.processFiles(event.getDataTransfer().<DataTransferExt>cast().getFiles());
				avoidDefaultHandling(event);
			}
		});

	}

	private void avoidDefaultHandling(DomEvent<?> event) {
		event.stopPropagation();
		event.preventDefault();
	}

}
