package com.baselet.client;

import org.vectomatic.dnd.DataTransferExt;
import org.vectomatic.dnd.DropPanel;
import org.vectomatic.file.FileList;

import com.google.gwt.canvas.client.Canvas;
import com.google.gwt.event.dom.client.DomEvent;
import com.google.gwt.event.dom.client.DragEnterEvent;
import com.google.gwt.event.dom.client.DragEnterHandler;
import com.google.gwt.event.dom.client.DragLeaveEvent;
import com.google.gwt.event.dom.client.DragLeaveHandler;
import com.google.gwt.event.dom.client.DragOverEvent;
import com.google.gwt.event.dom.client.DragOverHandler;
import com.google.gwt.event.dom.client.DropEvent;
import com.google.gwt.event.dom.client.DropHandler;
import com.google.gwt.user.client.ui.ScrollPanel;

public class ScrollDropPanel extends DropPanel {

	private FileOpenHandler handler;
	
	public ScrollDropPanel(DrawPanelCanvas diagramCanvas) {
		this.add(new ScrollPanel(diagramCanvas.getCanvas()));
		 handler = new FileOpenHandler(diagramCanvas);
		
		this.addDragOverHandler(new DragOverHandler() {
			@Override
			public void onDragOver(DragOverEvent event) {
				avoidDefaultHandling(event);
			}
		});
		this.addDragEnterHandler(new DragEnterHandler() {
			@Override
			public void onDragEnter(DragEnterEvent event) {
				avoidDefaultHandling(event);
			}
		});
		this.addDragLeaveHandler(new DragLeaveHandler() {
			@Override
			public void onDragLeave(DragLeaveEvent event) {
				avoidDefaultHandling(event);
			}
		});
		this.addDropHandler(new DropHandler() {
			@Override
			public void onDrop(DropEvent event) {
				FileList files = event.getDataTransfer().<DataTransferExt>cast().getFiles();
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
