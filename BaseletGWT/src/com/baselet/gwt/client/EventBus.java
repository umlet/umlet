package com.baselet.gwt.client;

import com.baselet.gwt.client.EventBus.PropertiesTextChanged.PropertiesTextChangedEventHandler;
import com.google.web.bindery.event.shared.Event;
import com.google.web.bindery.event.shared.Event.Type;
import com.google.web.bindery.event.shared.SimpleEventBus;

public class EventBus {

	private static com.google.web.bindery.event.shared.EventBus eventBus = new SimpleEventBus();
	private static EventBus instance = new EventBus();
	
	public static final EventBus getInstance() {
		return instance;
	}

	public void fireEvent(Event<?> event) {
		eventBus.fireEvent(event);
	}

	public <H> void addHandler(Type<H> type, H handler) {
		eventBus.addHandler(type, handler);
	}

	public static class PropertiesTextChanged extends Event<PropertiesTextChangedEventHandler> {
		// EVENT BOILERPLATE
		public static Type<PropertiesTextChangedEventHandler> TYPE = new Type<PropertiesTextChangedEventHandler>();
		@Override
		public Type<PropertiesTextChangedEventHandler> getAssociatedType() {
			return TYPE;
		}
		@Override
		protected void dispatch(PropertiesTextChangedEventHandler handler) {
			handler.onPropertiesTextChange(this);
		}
		// HANDLER INTERFACE
		public static interface PropertiesTextChangedEventHandler {
			void onPropertiesTextChange(PropertiesTextChanged event);
		}
		// EVENT CONTENT
		private String propertiesText;
		public PropertiesTextChanged(String propertiesText) {
			this.propertiesText = propertiesText;
		}
		public String getPropertiesText() {
			return propertiesText;
		}
	}

//	public static class LayerChangeEvent extends Event<LayerChangeEventHandler> {
//		// EVENT BOILERPLATE
//		public static Type<LayerChangeEventHandler> TYPE = new Type<LayerChangeEventHandler>();
//		@Override
//		public Type<LayerChangeEventHandler> getAssociatedType() {
//			return TYPE;
//		}
//		@Override
//		protected void dispatch(LayerChangeEventHandler handler) {
//			handler.onLayerChange(this);
//		}
//		// HANDLER INTERFACE
//		public static interface LayerChangeEventHandler {
//			void onLayerChange(LayerChangeEvent event);
//		}
//		// EVENT CONTENT
//		private GridElement gridElement;
//		public LayerChangeEvent(GridElement gridElement) {
//			this.gridElement = gridElement;
//		}
//		public GridElement getGridElement() {
//			return gridElement;
//		}
//	}
}
