package com.app.feenix.eventbus;

/**
 * Created by Feenix Technologies on 03/12/17.
 */

public class CancelRequestModel {

    boolean enabled;

    public CancelRequestModel() {
    }


    public CancelRequestModel(boolean enabled) {

        this.enabled = enabled;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }
}
