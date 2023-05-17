package com.app.feenix.eventbus;

import android.app.Dialog;

/**
 * Created by Feenix Technologies on 03/12/17.
 */

public class CancelRequestModel {

    boolean enabled;
    String reason;
    Dialog dialog;

    public CancelRequestModel() {
    }


    public CancelRequestModel(boolean enabled, String Reason, Dialog dialog) {

        this.enabled = enabled;
        this.reason = Reason;
        this.dialog = dialog;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public Dialog getDialog() {
        return dialog;
    }

    public void setDialog(Dialog dialog) {
        this.dialog = dialog;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }
}
