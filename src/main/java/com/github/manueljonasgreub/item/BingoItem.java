package com.github.manueljonasgreub.item;

import com.google.gson.JsonObject;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.Map;

public class BingoItem {
    private int row;
    private int column;
    private String type;
    private String name;
    private String difficulty;
    private Map<String, Boolean> completed;

    public BingoItem(int row, int column, String type, String name, String difficulty) {
        this.row = row;
        this.column = column;
        this.type = type;
        this.name = name;
        this.difficulty = difficulty;
        this.completed = new HashMap<>();
    }

    public int getRow() {
        return row;
    }

    public int getColumn() {
        return column;
    }

    public String getType() {
        return type;
    }

    public String getName() {
        return name;
    }

    public String getDifficulty() {
        return difficulty;
    }

    public Map<String, Boolean> getCompleted() {
        return completed;
    }
}