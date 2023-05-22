package com.worthybitbuilders.squadsense.utils;

import com.worthybitbuilders.squadsense.models.board_models.BoardBaseItemModel;
import com.worthybitbuilders.squadsense.models.board_models.BoardContentModel;
import com.worthybitbuilders.squadsense.models.board_models.BoardStatusItemModel;
import com.worthybitbuilders.squadsense.models.board_models.BoardTextItemModel;
import com.worthybitbuilders.squadsense.models.board_models.BoardUserItemModel;

import java.util.ArrayList;
import java.util.List;

public class BoardTemplates {
    public static List<BoardContentModel> sampleBoardContent() {
        List<String> rowTitles = new ArrayList<>();
        rowTitles.add("Nam");
        rowTitles.add("Dat");
        rowTitles.add("Khoi");
        rowTitles.add("Son");

        List<String> columnTitles = new ArrayList<>();
        columnTitles.add("Avatar");
        columnTitles.add("Frontend");
        columnTitles.add("Backend");
        columnTitles.add("Fullstack");

        List<List<BoardBaseItemModel>> cells = new ArrayList<>();
        List<String> statusContent = new ArrayList<>();
        statusContent.add("Lam chua xong");
        statusContent.add("Bat dau lam");
        statusContent.add("Lam gan xong");
        statusContent.add("Da xong");

        List<BoardBaseItemModel> firstRow = new ArrayList<>();
        firstRow.add(new BoardUserItemModel(0, 0));
        firstRow.add(new BoardStatusItemModel("Lam chua xong", statusContent, 1, 0));
        firstRow.add(new BoardTextItemModel("Bat dau lam", 2, 0));
        firstRow.add(new BoardTextItemModel("Lam gan xong", 3, 0));

        List<BoardBaseItemModel> secondRow = new ArrayList<>();
        secondRow.add(new BoardUserItemModel(0, 1));
        secondRow.add(new BoardStatusItemModel("Da xong", statusContent, 1, 1));
        secondRow.add(new BoardTextItemModel("Lam chua xong", 2, 1));
        secondRow.add(new BoardTextItemModel("Bat dau lam", 3, 1));

        List<BoardBaseItemModel> thirdRow = new ArrayList<>();
        thirdRow.add(new BoardUserItemModel(0, 2));
        thirdRow.add(new BoardStatusItemModel("Lam gan xong", statusContent, 1, 2));
        thirdRow.add(new BoardTextItemModel("Da xong", 2, 2));
        thirdRow.add(new BoardTextItemModel("Bat dau lam", 3, 2));

        List<BoardBaseItemModel> fourthRow = new ArrayList<>();
        fourthRow.add(new BoardUserItemModel(0, 3));
        fourthRow.add(new BoardStatusItemModel("Lam gan xong", statusContent, 1, 3));
        fourthRow.add(new BoardTextItemModel("Da xong", 2, 3));
        fourthRow.add(new BoardTextItemModel("Bat dau lam", 3, 3));

        cells.add(firstRow);
        cells.add(secondRow);
        cells.add(thirdRow);
        cells.add(fourthRow);

        List<BoardContentModel> models = new ArrayList<>();
        models.add(new BoardContentModel("Members", rowTitles, columnTitles, cells));
        return models;
    }
}
