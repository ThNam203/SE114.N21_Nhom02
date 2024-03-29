package com.worthybitbuilders.squadsense.models;

import com.worthybitbuilders.squadsense.models.board_models.BoardBaseItemModel;
import com.worthybitbuilders.squadsense.models.board_models.BoardRowHeaderModel;

import java.util.List;

public class BoardDetailItemModel {
    private List<BoardBaseItemModel> cells;
    private List<String> columnTitles;
    private BoardRowHeaderModel rowTitle;

    public BoardDetailItemModel(List<BoardBaseItemModel> cells, List<String> columnTitles, BoardRowHeaderModel rowTitle) {
        this.cells = cells;
        this.columnTitles = columnTitles;
        this.rowTitle = rowTitle;
    }

    public List<BoardBaseItemModel> getCells() {
        return cells;
    }

    public void setCells(List<BoardBaseItemModel> cells) {
        this.cells = cells;
    }

    public List<String> getColumnTitles() {
        return columnTitles;
    }

    public void setColumnTitles(List<String> columnTitles) {
        this.columnTitles = columnTitles;
    }

    public BoardRowHeaderModel getRowTitle() {
        return rowTitle;
    }

    public void setRowTitle(BoardRowHeaderModel rowTitle) {
        this.rowTitle = rowTitle;
    }
}
