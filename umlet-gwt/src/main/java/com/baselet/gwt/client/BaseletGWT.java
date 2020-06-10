package com.baselet.gwt.client;

import com.baselet.diagram.draw.helper.theme.Theme;
import com.baselet.diagram.draw.helper.theme.ThemeChangeListener;
import com.baselet.diagram.draw.helper.theme.ThemeFactory;
import com.baselet.gwt.client.base.Converter;
import com.baselet.gwt.client.logging.CustomLogger;
import com.baselet.gwt.client.logging.CustomLoggerFactory;

import com.baselet.control.config.SharedConfig;
import com.baselet.control.enums.Program;
import com.baselet.control.enums.RuntimeType;
import com.baselet.gwt.client.base.Browser;
import com.baselet.gwt.client.base.Notification;
import com.baselet.gwt.client.element.WebStorage;
import com.baselet.gwt.client.version.BuildInfoProperties;
import com.baselet.gwt.client.view.MainView;
import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.RunAsyncCallback;
import com.google.gwt.event.shared.UmbrellaException;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.Window.Location;
import com.google.gwt.user.client.ui.RootLayoutPanel;

public class BaseletGWT implements EntryPoint, ThemeChangeListener {

	CustomLogger log = CustomLoggerFactory.getLogger(BaseletGWT.class);

	@Override
	public void onModuleLoad() {
        setUncaughtExceptionHandler();

        ThemeFactory.addListener(this);
        onThemeChange();

		log.info("Starting GUI ...");
		Program.init(BuildInfoProperties.getVersion(), RuntimeType.GWT);
		SharedConfig.getInstance().setDev_mode(Location.getParameter("dev") != null);

		if (!WebStorage.initLocalStorage()) {
			if (Browser.get() == Browser.INTERNET_EXPLORER && GWT.getHostPageBaseURL().startsWith("file:")) {
				Notification.showFeatureNotSupported("You have opened this webpage from your filesystem, therefore<br/>Internet Explorer will not support local storage<br/><br/>Please use another browser like Firefox or Chrome,<br/>or open this application using the web url", false);
			}
			else {
				Notification.showFeatureNotSupported("Sorry, but your browser does not support the required HTML 5 feature 'local storage' (or has cookies disabled)<br/>Suggested browsers are Firefox, Chrome, Opera, Internet Explorer 10+", false);
			}
		}
		else if (!browserSupportsFileReader()) {
			Notification.showFeatureNotSupported("Sorry, but your browser does not support the required HTML 5 feature 'file reader'<br/>Suggested browsers are Firefox, Chrome, Opera, Internet Explorer 10+", false);
		}
		else {
			Notification.showInfo("Loading application ... please wait ...");
			GWT.runAsync(new RunAsyncCallback() {
				@Override
				public void onSuccess() {
					Notification.showInfo("");
					RootLayoutPanel.get().add(new MainView());
				}

				@Override
				public void onFailure(Throwable reason) {
					Notification.showFeatureNotSupported("Cannot load application from server", false);
				}
			});
			if (!SharedConfig.getInstance().isDev_mode()) {
				Window.addWindowClosingHandler(new Window.ClosingHandler() {
					@Override
					public void onWindowClosing(Window.ClosingEvent closingEvent) {
						closingEvent.setMessage("Do you really want to leave the page? You will lose any unsaved changes.");
					}
				});
			}
		}
		log.info("GUI started");
	}

    private void setUncaughtExceptionHandler() {
        GWT.setUncaughtExceptionHandler(throwable -> {
            Throwable unwrapped = unwrap(throwable);
            Notification.showFeatureNotSupported("Sorry, the program just crashed. Please check logs and report a bug.", false);
            log.error(unwrapped.getMessage());
        });
    }

    public Throwable unwrap(Throwable e) {
        if (e instanceof UmbrellaException) {
            UmbrellaException ue = (UmbrellaException) e;
            if (ue.getCauses().size() == 1) {
                return unwrap(ue.getCauses().iterator().next());
            }
        }
        return e;
    }

	@Override
	public void onThemeChange() {
		RootLayoutPanel.get().getElement().getStyle().setBackgroundColor(Converter.convert(ThemeFactory.getCurrentTheme().getColor(Theme.ColorStyle.DEFAULT_CANVAS)).value());
	}

	private final native boolean browserSupportsFileReader() /*-{
    	return typeof FileReader != "undefined";
    }-*/;
}
