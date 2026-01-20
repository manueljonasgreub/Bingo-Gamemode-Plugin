package com.github.manueljonasgreub.utils;

public enum TeamColor {
    RED,
    ORANGE,
    YELLOW,
    LIME,
    GREEN,
    LIGHT_BLUE,
    CYAN,
    BLUE,
    PINK,
    MAGENTA,
    PURPLE,
    BLACK,
    GRAY,
    LIGHT_GRAY,
    BROWN;

    public String GetHexFromColor() {
        return switch (this) {
            case RED -> "#FF0000";
            case ORANGE -> "#FFA500";
            case YELLOW -> "#FFFF00";
            case LIME -> "#00FF00";
            case GREEN -> "#008000";
            case LIGHT_BLUE -> "#ADD8E6";
            case CYAN -> "#00FFFF";
            case BLUE -> "#0000FF";
            case PINK -> "#FFC0CB";
            case MAGENTA -> "#FF00FF";
            case PURPLE -> "#800080";
            case BLACK -> "#000000";
            case GRAY -> "#808080";
            case LIGHT_GRAY -> "#D3D3D3";
            case BROWN -> "#A52A2A";
        };
    }

}
