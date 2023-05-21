package com.worthybitbuilders.squadsense.models.board_models;

public class BoardEmptyItemModel extends BoardBaseItemModel {
    public BoardEmptyItemModel() {
        super(BoardColumnHeaderModel.ColumnType.NewColumn, "", -1, -1);
        // we set it (-1, -1) cause we don't need to do with it
    }
}
