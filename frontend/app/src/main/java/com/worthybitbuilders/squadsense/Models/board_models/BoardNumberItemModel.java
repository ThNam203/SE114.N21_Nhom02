package com.worthybitbuilders.squadsense.models.board_models;

import java.text.NumberFormat;
import java.util.Locale;

public class BoardNumberItemModel extends BoardBaseItemModel {
    public BoardNumberItemModel(String content, Integer columnPosition, Integer rowPosition) {
        super(BoardColumnHeaderModel.ColumnType.Number, content, columnPosition, rowPosition);
    }

    @Override
    public String getContent() {
        return super.getContent();
    }
}
