package com.worthybitbuilders.squadsense.models.board_models;

public abstract class BoardBaseItemModel {
    private BoardColumnHeaderModel.ColumnType columnType;
    private String content;
    private Integer columnPosition;
    private Integer rowPosition;

    public BoardBaseItemModel(BoardColumnHeaderModel.ColumnType columnType, String content, Integer columnPosition, Integer rowPosition) {
        this.columnType = columnType;
        this.content = content;
        this.columnPosition = columnPosition;
        this.rowPosition = rowPosition;
    }

    public BoardColumnHeaderModel.ColumnType getItemType() {
        return columnType;
    }

    public void setItemType(BoardColumnHeaderModel.ColumnType itemType) {
        this.columnType = itemType;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public BoardColumnHeaderModel.ColumnType getColumnType() {
        return columnType;
    }

    public void setColumnType(BoardColumnHeaderModel.ColumnType columnType) {
        this.columnType = columnType;
    }

    public Integer getColumnPosition() {
        return columnPosition;
    }

    public void setColumnPosition(Integer columnPosition) {
        this.columnPosition = columnPosition;
    }

    public Integer getRowPosition() {
        return rowPosition;
    }

    public void setRowPosition(Integer rowPosition) {
        this.rowPosition = rowPosition;
    }
}
