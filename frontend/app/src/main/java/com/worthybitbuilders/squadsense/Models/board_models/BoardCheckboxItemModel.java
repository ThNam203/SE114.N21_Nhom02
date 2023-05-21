package com.worthybitbuilders.squadsense.models.board_models;

public class BoardCheckboxItemModel extends BoardBaseItemModel {
    private Boolean isChecked;
    public BoardCheckboxItemModel(Boolean isChecked, Integer columnPosition, Integer rowPosition) {
        super(BoardColumnHeaderModel.ColumnType.Checkbox, "", columnPosition, rowPosition);
        this.isChecked = isChecked;
    }

    public Boolean getChecked() {
        return isChecked;
    }

    public void setChecked(Boolean checked) {
        isChecked = checked;
    }
}
