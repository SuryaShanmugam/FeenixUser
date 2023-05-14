package com.app.feenix.utils.customcomponents

class PlacePredictions {

    fun getPlaces(): ArrayList<PlaceAutoComplete>? {
        return predictions
    }

    fun setPlaces(places: ArrayList<PlaceAutoComplete>?) {
        predictions = places
    }

    private var predictions: ArrayList<PlaceAutoComplete>? = null

}