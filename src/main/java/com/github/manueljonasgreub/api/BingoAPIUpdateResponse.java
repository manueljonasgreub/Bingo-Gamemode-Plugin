package com.github.manueljonasgreub.api;

public class BingoAPIUpdateResponse {

    private final String bingo;
    private final String imageUrl;

    public BingoAPIUpdateResponse(String bingo, String imageUrl) {
        this.bingo = bingo;
        this.imageUrl = imageUrl;
    }

    public String getBingo() {
        return bingo;
    }

    public String getImageUrl() {
        return imageUrl;
    }

}
