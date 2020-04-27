package com.baselet.gwt.client.view.interfaces;

import com.google.gwt.user.client.ui.IsWidget;

public interface AutoresizeScrollDropTarget extends IsWidget, AcceptDiagram, Redrawable {

	void setAutoresizeScrollDrop(HasScrollPanel autoResizeScrollDropPanel);

}