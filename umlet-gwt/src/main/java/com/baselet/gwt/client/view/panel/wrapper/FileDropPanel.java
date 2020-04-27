package com.baselet.gwt.client.view.panel.wrapper;

import org.vectomatic.dnd.DataTransferExt;
import org.vectomatic.dnd.DropPanel;
import org.vectomatic.file.FileList;

import com.baselet.gwt.client.view.interfaces.AutoresizeScrollDropTarget;
import com.google.gwt.event.dom.client.DomEvent;
import com.google.gwt.event.dom.client.DragEnterEvent;
import com.google.gwt.event.dom.client.DragEnterHandler;
import com.google.gwt.event.dom.client.DragLeaveEvent;
import com.google.gwt.event.dom.client.DragLeaveHandler;
import com.google.gwt.event.dom.client.DragOverEvent;
import com.google.gwt.event.dom.client.DragOverHandler;
import com.google.gwt.event.dom.client.DropEvent;
import com.google.gwt.event.dom.client.DropHandler;

public class FileDropPanel extends DropPanel {

	private final FileOpenHandler handler;

	public FileDropPanel(final AutoresizeScrollDropTarget diagram) {

		this.add(diagram);
		handler = new FileOpenHandler(diagram);

		addDragOverHandler(new DragOverHandler() {
			@Override
			public void onDragOver(DragOverEvent event) {
				avoidDefaultHandling(event);
			}
		});
		addDragEnterHandler(new DragEnterHandler() {
			@Override
			public void onDragEnter(DragEnterEvent event) {
				avoidDefaultHandling(event);
			}
		});
		addDragLeaveHandler(new DragLeaveHandler() {
			@Override
			public void onDragLeave(DragLeaveEvent event) {
				avoidDefaultHandling(event);
			}
		});
		addDropHandler(new DropHandler() {
			@Override
			public void onDrop(DropEvent event) {
				FileList files = event.getDataTransfer().<DataTransferExt> cast().getFiles();
				handler.processFiles(files);
				avoidDefaultHandling(event);
			}
		});

	}

	private void avoidDefaultHandling(DomEvent<?> event) {
		event.stopPropagation();
		event.preventDefault();
	}

}
