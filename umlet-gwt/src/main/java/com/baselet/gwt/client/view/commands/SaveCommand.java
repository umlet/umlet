package com.baselet.gwt.client.view.commands;

import com.baselet.gwt.client.view.MainView;
import com.google.gwt.core.client.Scheduler;

public class SaveCommand implements Scheduler.ScheduledCommand {
    protected MainView mainView;

    public void init(MainView mainView) {
        this.mainView = mainView;
    }

    @Override
    public void execute() {
        // Override if something shall be executed
    }
}
