package com.github.manueljonasgreub.api;

import java.util.List;
import java.util.Map;

public class MapRAW {
    private List<Map<String, Object>> settings;
    private List<Map<String, Object>> items;

    public List<Map<String, Object>> getSettings() {
        return settings;
    }

    public void setSettings(List<Map<String, Object>> settings) {
        this.settings = settings;
    }

    public List<Map<String, Object>> getItems() {
        return items;
    }

    public void setItems(List<Map<String, Object>> items) {
        this.items = items;
    }
}