package com.github.manueljonasgreub.item;

import java.util.HashMap;
import java.util.Map;

public class BingoItemDTO {
    private int row;
    private int column;
    private String id;
    private String name;
    private String sprite;
    private String difficulty;
    private Map<String, Boolean> completed;

    public BingoItemDTO(int row, int column, String id, String name, String sprite, String difficulty, Map<String, Boolean> completed) {
        this.row = row;
        this.column = column;
        this.id = id;
        this.name = name;
        this.sprite = sprite;
        this.difficulty = difficulty;
        this.completed = completed;
    }


    public int getRow() {
        return row;
    }

    public int getColumn() {
        return column;
    }

    public String getId() {
        return id;
    }

    public String getSprite() {
        return sprite;
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
