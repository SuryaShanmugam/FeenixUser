package com.app.feenix.eventbus;

/**
 * Created by Feenix Technologies on 03/12/17.
 */

public class GetMyLocationModel {

    Boolean getlocation;


    public GetMyLocationModel(Boolean getlocation) {
        this.getlocation = getlocation;
    }

    public Boolean getGetlocation() {
        return getlocation;
    }

    public void setGetlocation(Boolean getlocation) {
        this.getlocation = getlocation;
    }
}
