package com.worthybitbuilders.squadsense.models.board_models;

import java.util.List;

public class BoardContentModel {
    private String boardTitle;
    private List<String> rowTitles;
    private List<String> columnTitles;
    private List<List<BoardBaseItemModel>> items;

    public BoardContentModel(String boardTitle, List<String> rowTitles, List<String> columnTitles, List<List<BoardBaseItemModel>> items) {
        this.boardTitle = boardTitle;
        this.rowTitles = rowTitles;
        this.columnTitles = columnTitles;
        this.items = items;
    }

    public String getBoardTitle() {
        return boardTitle;
    }

    public void setBoardTitle(String boardTitle) {
        this.boardTitle = boardTitle;
    }

    public List<String> getRowTitles() {
        return rowTitles;
    }

    public void setRowTitles(List<String> rowTitles) {
        this.rowTitles = rowTitles;
    }

    public List<String> getColumnTitles() {
        return columnTitles;
    }

    public void setColumnTitles(List<String> columnTitles) {
        this.columnTitles = columnTitles;
    }

    public List<List<BoardBaseItemModel>> getItems() {
        return items;
    }

    public void setItems(List<List<BoardBaseItemModel>> items) {
        this.items = items;
    }
}
