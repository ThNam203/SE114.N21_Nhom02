package com.worthybitbuilders.squadsense.models.board_models;

public class BoardTextItemModel extends BoardBaseItemModel {
    public BoardTextItemModel(String content, Integer columnPosition, Integer rowPosition) {
        super(BoardColumnHeaderModel.ColumnType.Text, content, columnPosition, rowPosition);
    }
}
