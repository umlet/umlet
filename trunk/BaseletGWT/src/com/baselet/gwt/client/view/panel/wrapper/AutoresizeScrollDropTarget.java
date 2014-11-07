package com.baselet.gwt.client.view.panel.wrapper;

import com.google.gwt.user.client.ui.IsWidget;

public interface AutoresizeScrollDropTarget extends IsWidget, SetDiagramTarget {

	void setAutoresizeScrollDrop(HasScrollPanel autoResizeScrollDropPanel);

	void redraw();

}