package com.app.feenix.eventbus;

/**
 * Created by Feenix Technologies on 03/12/17.
 */

public class NoDriversFoundModel {

    String type;
    String title;
    String message;
    String details;

    public NoDriversFoundModel() {
    }


    public NoDriversFoundModel(String type, String title, String message, String details) {
        this.type = type;
        this.title = title;
        this.message = message;
        this.details = details;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getDetails() {
        return details;
    }

    public void setDetails(String details) {
        this.details = details;
    }
}
