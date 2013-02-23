package com.baselet.client;

import java.util.ArrayList;
import java.util.List;

import org.vectomatic.dnd.DataTransferExt;
import org.vectomatic.dnd.DropPanel;
import org.vectomatic.file.ErrorCode;
import org.vectomatic.file.File;
import org.vectomatic.file.FileError;
import org.vectomatic.file.FileList;
import org.vectomatic.file.FileReader;
import org.vectomatic.file.events.LoadEndEvent;
import org.vectomatic.file.events.LoadEndHandler;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.DomEvent;
import com.google.gwt.event.dom.client.DragEnterEvent;
import com.google.gwt.event.dom.client.DragEnterHandler;
import com.google.gwt.event.dom.client.DragLeaveEvent;
import com.google.gwt.event.dom.client.DragLeaveHandler;
import com.google.gwt.event.dom.client.DragOverEvent;
import com.google.gwt.event.dom.client.DragOverHandler;
import com.google.gwt.event.dom.client.DropEvent;
import com.google.gwt.event.dom.client.DropHandler;
import com.google.gwt.user.client.Window;

public class OwnDropPanel {

	DropPanel dropPanel = new DropPanel();
	
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
				processFiles(event.getDataTransfer().<DataTransferExt>cast().getFiles());
				avoidDefaultHandling(event);
			}
		});

		reader = new FileReader();
		reader.addLoadEndHandler(new LoadEndHandler() {
			@Override
			public void onLoadEnd(LoadEndEvent event) {
				if (reader.getError() == null) {
					if (readQueue.size() > 0) {
						try {
							createText(readQueue.get(0));
						} finally {
							readQueue.remove(0);
							readNext();
						}
					}
				}
			}
		});
	}

	private void avoidDefaultHandling(DomEvent<?> event) {
		event.stopPropagation();
		event.preventDefault();
	}

	private void createText(final File file) {
		String result = reader.getStringResult();
		Window.alert(result);
	}

	protected FileReader reader;
	protected List<File> readQueue = new ArrayList<File>();

	private void processFiles(FileList files) {
		GWT.log("length=" + files.getLength());
		for (File file : files) {
			readQueue.add(file);
		}
		readNext();
	}

	private void readNext() {
		if (readQueue.size() > 0) {
			File file = readQueue.get(0);
			try {
				reader.readAsText(file);
			} catch(Throwable t) {
				// Necessary for FF (see bug https://bugzilla.mozilla.org/show_bug.cgi?id=701154)
				// Standard-complying browsers will to go in this branch
				handleError(file);
				readQueue.remove(0);
				readNext();
			}
		}
	}

	private void handleError(File file) {
		FileError error = reader.getError();
		String errorDesc = "";
		if (error != null) {
			ErrorCode errorCode = error.getCode();
			if (errorCode != null) {
				errorDesc = ": " + errorCode.name();
			}
		}
		Window.alert("File loading error for file: " + file.getName() + "\n" + errorDesc);
	}

}
