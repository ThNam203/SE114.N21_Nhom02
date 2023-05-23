package com.worthybitbuilders.squadsense.models.board_models;

public abstract class BoardBaseItemModel {
    private String content;

    public BoardBaseItemModel(String content) {
        this.content = content;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
