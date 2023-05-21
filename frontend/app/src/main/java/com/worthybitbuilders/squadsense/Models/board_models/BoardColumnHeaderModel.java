package com.worthybitbuilders.squadsense.models.board_models;

public class BoardColumnHeaderModel {
    public enum ColumnType {
        Status(1, "Status"),
        Update(2, "Update"),
        Text(3, "Text"),
        Number(4, "Number"),
        TimeLine(5, "Timeline"),
        Date(6, "Date"),
        User(7, "People"),
        Checkbox(8, "Checkbox"),
        NewColumn(9, "+ New Column");

        private int key;
        private String name;
        ColumnType(int key, String name) {
            this.key = key;
            this.name = name;
        }

        public int getKey() {
            return key;
        }
        public String getName() { return name; }
    }
    private ColumnType columnType;
    private String title;

    public BoardColumnHeaderModel(ColumnType columnType, String title) {
        this.columnType = columnType;
        this.title = title;
    }

    public ColumnType getColumnType() {
        return columnType;
    }

    public void setColumnType(ColumnType columnType) {
        this.columnType = columnType;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
