package com.worthybitbuilders.squadsense.models.board_models;

public class BoardUpdateItemModel extends BoardBaseItemModel {
    public BoardUpdateItemModel(Integer columnPosition, Integer rowPosition) {
        super(BoardColumnHeaderModel.ColumnType.Update, "", columnPosition, rowPosition);
    }
}
