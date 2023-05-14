package com.app.feenix.eventbus;

/**
 * Created by Feenix Technologies on 03/12/17.
 */

public class MenuIconDisableModel {

    boolean enabled;

    public MenuIconDisableModel() {
    }


    public MenuIconDisableModel(boolean enabled) {

        this.enabled = enabled;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }
}
