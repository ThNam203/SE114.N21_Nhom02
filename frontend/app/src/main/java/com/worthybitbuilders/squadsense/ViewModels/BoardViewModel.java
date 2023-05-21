package com.worthybitbuilders.squadsense.viewmodels;

import android.graphics.Color;

import com.worthybitbuilders.squadsense.models.board_models.BoardBaseItemModel;
import com.worthybitbuilders.squadsense.models.board_models.BoardColumnHeaderModel;
import com.worthybitbuilders.squadsense.models.board_models.BoardContentModel;
import com.worthybitbuilders.squadsense.models.board_models.BoardRowHeaderModel;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class BoardViewModel {
    private String boardTitle;
    private List<BoardColumnHeaderModel> mColumnHeaderModelList;
    private List<BoardRowHeaderModel> mRowHeaderModelList;
    private List<List<BoardBaseItemModel>> mCellModelList;

    public int getCellItemViewType(int columnPosition) {
        return mColumnHeaderModelList.get(columnPosition).getColumnType().getKey();
    }

    public String getBoardTitle() {
        if (boardTitle == null || boardTitle.isEmpty()) return "Unknown title";
        return boardTitle;
    }

    public void setBoardTitle(String boardTitle) {
        this.boardTitle = boardTitle;
    }

    public List<BoardColumnHeaderModel> getColumHeaderModeList() {
        return mColumnHeaderModelList;
    }

    public List<BoardRowHeaderModel> getRowHeaderModelList() {
        return mRowHeaderModelList;
    }

    public List<List<BoardBaseItemModel>> getCellModelList() {
        return mCellModelList;
    }

    public void generateDataForBoard(BoardContentModel contentModel) {
        mColumnHeaderModelList = createColumnHeaderModelList(contentModel);
        mCellModelList = createCellModelList(contentModel);
        mRowHeaderModelList = createRowHeaderList(contentModel);
    }

    private List<BoardRowHeaderModel> createRowHeaderList(BoardContentModel contentModel) {
        List<BoardRowHeaderModel> data = new ArrayList<>();
        contentModel.getRowTitles().forEach(title -> {
            data.add(new BoardRowHeaderModel(title));
        });

        return data;
    }

    private List<List<BoardBaseItemModel>> createCellModelList(BoardContentModel contentModel) {
        List<List<BoardBaseItemModel>> data = new ArrayList<>();
        contentModel.getItems().forEach(itemRow -> {
            List<BoardBaseItemModel> newItemRow = new ArrayList<>();
            itemRow.forEach(item -> {
                if (item.getItemType() != null) newItemRow.add(item);
            });
            data.add(newItemRow);
        });

        return data;
    }

    private List<BoardColumnHeaderModel> createColumnHeaderModelList(BoardContentModel contentModel) {
        List<BoardColumnHeaderModel> data = new ArrayList<>();
        List<String> titles = contentModel.getColumnTitles();
        List<BoardBaseItemModel> items = contentModel.getItems().get(0);
        for (int i = 0; i < contentModel.getColumnTitles().size(); i++) {
            data.add(new BoardColumnHeaderModel(items.get(i).getItemType(), titles.get(i)));
        }

        return data;
    }

    public void addNewColumn(BoardColumnHeaderModel columnHeaderModel, List<BoardBaseItemModel> itemModels, int columnPosition) {
        mColumnHeaderModelList.add(columnPosition, columnHeaderModel);
        for (int i = 0; i < mCellModelList.size(); i++) {
            mCellModelList.get(i).add(columnPosition, itemModels.get(i));
        }
    }

    public int getRandomColor() {
        Random random = new Random();
        int red = random.nextInt(255);
        int green = random.nextInt(255);
        int blue = random.nextInt(255);
        int brightness = 200; // Giá trị độ sáng tối đa
        int max = Math.max(Math.max(red, green), blue);
        if (max > brightness) {
            red = (int) ((double) red * ((double) brightness / (double) max));
            green = (int) ((double) green * ((double) brightness / (double) max));
            blue = (int) ((double) blue * ((double) brightness / (double) max));
        }
        int color = Color.rgb(red, green, blue);

        return color;
    }
}