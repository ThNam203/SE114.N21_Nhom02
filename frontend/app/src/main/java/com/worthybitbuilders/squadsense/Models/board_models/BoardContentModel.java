package com.worthybitbuilders.squadsense.models.board_models;

import java.util.List;

public class BoardContentModel {
    private String boardTitle;
    private List<String> rowCells;
    private List<BoardColumnHeaderModel> columnCells;
    // TODO: COLUMN SHOULD HOLD THE COLUMN TYPE, NOT THE CELL
    private List<List<BoardBaseItemModel>> cells;

    public BoardContentModel(String boardTitle, List<String> rowCells, List<BoardColumnHeaderModel> columnCells, List<List<BoardBaseItemModel>> cells) {
        this.boardTitle = boardTitle;
        this.rowCells = rowCells;
        this.columnCells = columnCells;
        this.cells = cells;
    }

    public String getBoardTitle() {
        return boardTitle;
    }

    public void setBoardTitle(String boardTitle) {
        this.boardTitle = boardTitle;
    }

    public List<String> getRowCells() {
        return rowCells;
    }

    public void setRowCells(List<String> rowCells) {
        this.rowCells = rowCells;
    }

    public List<BoardColumnHeaderModel> getColumnCells() {
        return columnCells;
    }

    public void setColumnCells(List<BoardColumnHeaderModel> columnCells) {
        this.columnCells = columnCells;
    }

    public List<List<BoardBaseItemModel>> getCells() {
        return cells;
    }

    public void setCells(List<List<BoardBaseItemModel>> cells) {
        this.cells = cells;
    }
}
