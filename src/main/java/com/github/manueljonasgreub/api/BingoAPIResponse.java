package com.github.manueljonasgreub.api;

import com.github.manueljonasgreub.item.BingoItem;

import java.util.List;

public class BingoAPIResponse {
    private Settings settings;
    private List<BingoItem> items;

    public Settings getSettings() {
        return settings;
    }

    public List<BingoItem> getItems() {
        return items;
    }
}
