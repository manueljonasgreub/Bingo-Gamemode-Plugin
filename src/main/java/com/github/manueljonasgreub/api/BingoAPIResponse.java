package com.github.manueljonasgreub.api;

import com.github.manueljonasgreub.item.BingoItem;

import java.util.List;

public class BingoAPIResponse {
    private String mapURL;
    private MapRAW mapRAW;

    public void setMapURL(String mapURL) {
        this.mapURL = mapURL;
    }

    public void setMapRAW(MapRAW mapRAW) {
        this.mapRAW = mapRAW;
    }

    public String getMapURL() {
        return mapURL;
    }

    public MapRAW getMapRAW() {
        return mapRAW;
    }
}
