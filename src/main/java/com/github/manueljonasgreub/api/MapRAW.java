package com.github.manueljonasgreub.api;

import com.github.manueljonasgreub.item.BingoItem;
import com.github.manueljonasgreub.item.BingoItemDTO;

import java.util.List;
import java.util.Map;

    public class MapRAW {
        private Settings settings;
        private List<BingoItemDTO> items;

        public Settings getSettings() {
            return settings;
        }

        public List<BingoItemDTO> getItems() {
            return items;
        }

        public void setItems(List<BingoItemDTO> items) {
            this.items = items;
        }
    }
