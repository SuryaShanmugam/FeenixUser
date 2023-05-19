package com.app.feenix.eventbus;

import com.google.firebase.messaging.RemoteMessage;

/**
 * Created by Feenix Technologies on 03/12/17.
 */

public class RedirectFragmentModel {

    RemoteMessage message;


    public RedirectFragmentModel() {
    }

    public RedirectFragmentModel(RemoteMessage message) {
        this.message = message;
    }

    public RemoteMessage getMessage() {
        return message;
    }

    public void setMessage(RemoteMessage message) {
        this.message = message;
    }
}
