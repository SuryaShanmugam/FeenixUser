package com.app.feenix.eventbus;

/**
 * Created by Feenix Technologies on 03/12/17.
 */

public class OnHomeLocationEnableModel {

    boolean enabled;

    public OnHomeLocationEnableModel() {
    }


    public OnHomeLocationEnableModel(boolean enabled) {

        this.enabled = enabled;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }
}
