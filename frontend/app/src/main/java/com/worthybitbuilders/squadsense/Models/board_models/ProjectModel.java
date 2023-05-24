package com.worthybitbuilders.squadsense.models.board_models;

import com.worthybitbuilders.squadsense.utils.BoardTemplates;

import java.util.List;

public class ProjectModel {
    /**
     * It's the current board we are viewing
     */
    private int chosenPosition;
    private List<BoardContentModel> boards;

    public ProjectModel(int chosenPosition, List<BoardContentModel> boards) {
        this.chosenPosition = chosenPosition;
        this.boards = boards;
    }

    public int getChosenPosition() {
        return chosenPosition;
    }

    public void setChosenPosition(int chosenPosition) {
        this.chosenPosition = chosenPosition;
    }

    public List<BoardContentModel> getBoards() {
        return boards;
    }

    public void setBoards(List<BoardContentModel> boards) {
        this.boards = boards;
    }

    public void addBoard(BoardContentModel newBoard) {
        this.boards.add(newBoard);
    }
    public void removeBoardAt(int position) {
        if (chosenPosition == position) chosenPosition = 0;
        this.boards.remove(position);
    }
}
