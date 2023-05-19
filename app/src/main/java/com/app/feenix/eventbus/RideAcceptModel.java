package com.app.feenix.eventbus;

import com.google.firebase.messaging.RemoteMessage;

/**
 * Created by Feenix Technologies on 03/12/17.
 */

public class RideAcceptModel {

    RemoteMessage message;


    public RideAcceptModel() {
    }

    public RideAcceptModel(RemoteMessage message) {
        this.message = message;
    }

    public RemoteMessage getMessage() {
        return message;
    }

    public void setMessage(RemoteMessage message) {
        this.message = message;
    }
}
