package com.worthybitbuilders.squadsense.adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.evrencoskun.tableview.adapter.AbstractTableAdapter;
import com.evrencoskun.tableview.adapter.recyclerview.holder.AbstractViewHolder;
import com.worthybitbuilders.squadsense.R;
import com.worthybitbuilders.squadsense.adapters.holders.BoardCheckboxItemViewHolder;
import com.worthybitbuilders.squadsense.adapters.holders.BoardColumnHeaderViewHolder;
import com.worthybitbuilders.squadsense.adapters.holders.BoardDateItemViewHolder;
import com.worthybitbuilders.squadsense.adapters.holders.BoardEmptyItemViewHolder;
import com.worthybitbuilders.squadsense.adapters.holders.BoardNumberItemViewHolder;
import com.worthybitbuilders.squadsense.adapters.holders.BoardRowHeaderViewHolder;
import com.worthybitbuilders.squadsense.adapters.holders.BoardStatusItemViewHolder;
import com.worthybitbuilders.squadsense.adapters.holders.BoardTextItemViewHolder;
import com.worthybitbuilders.squadsense.adapters.holders.BoardTimelineItemViewHolder;
import com.worthybitbuilders.squadsense.adapters.holders.BoardUpdateItemViewHolder;
import com.worthybitbuilders.squadsense.adapters.holders.BoardUserItemViewHolder;
import com.worthybitbuilders.squadsense.factory.BoardItemFactory;
import com.worthybitbuilders.squadsense.models.board_models.BoardBaseItemModel;
import com.worthybitbuilders.squadsense.models.board_models.BoardCheckboxItemModel;
import com.worthybitbuilders.squadsense.models.board_models.BoardColumnHeaderModel;
import com.worthybitbuilders.squadsense.models.board_models.BoardContentModel;
import com.worthybitbuilders.squadsense.models.board_models.BoardDateItemModel;
import com.worthybitbuilders.squadsense.models.board_models.BoardNumberItemModel;
import com.worthybitbuilders.squadsense.models.board_models.BoardRowHeaderModel;
import com.worthybitbuilders.squadsense.models.board_models.BoardStatusItemModel;
import com.worthybitbuilders.squadsense.models.board_models.BoardTextItemModel;
import com.worthybitbuilders.squadsense.models.board_models.BoardTimelineItemModel;
import com.worthybitbuilders.squadsense.models.board_models.BoardUpdateItemModel;
import com.worthybitbuilders.squadsense.models.board_models.BoardUserItemModel;
import com.worthybitbuilders.squadsense.viewmodels.BoardViewModel;

import java.util.ArrayList;
import java.util.List;

public class TableViewAdapter extends AbstractTableAdapter<BoardColumnHeaderModel, BoardRowHeaderModel, BoardBaseItemModel> {
    private final BoardViewModel boardViewModel;
    private final Context mContext;
    private final OnClickHandlers handlers;
    private TextView tvBoardTitle;
    public interface OnClickHandlers
            extends BoardStatusItemViewHolder.StatusItemClickHandlers,
                    BoardUserItemViewHolder.UserItemClickHandler,
                    BoardTextItemViewHolder.TextItemClickHandlers,
                    BoardUpdateItemViewHolder.UpdateItemClickHandlers,
                    BoardNumberItemViewHolder.NumberItemClickHandlers,
                    BoardCheckboxItemViewHolder.CheckboxItemClickHandlers,
                    BoardDateItemViewHolder.DateItemClickHandlers,
                    BoardTimelineItemViewHolder.TimelineItemClickHandlers
    {
        void onNewColumnHeaderClick();
        void onNewRowHeaderClick();
    }

    public TableViewAdapter(Context context, OnClickHandlers handlers) {
        mContext = context;
        this.boardViewModel = new BoardViewModel();
        this.handlers = handlers;
    }

    @NonNull
    @Override
    public AbstractViewHolder onCreateCellViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == BoardColumnHeaderModel.ColumnType.Status.getKey()) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.board_item_status, parent, false);
            return new BoardStatusItemViewHolder(view, handlers);
        } else if (viewType == BoardColumnHeaderModel.ColumnType.Text.getKey()) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.board_item_text, parent, false);
            return new BoardTextItemViewHolder(view, handlers);
        } else if (viewType == BoardColumnHeaderModel.ColumnType.User.getKey()) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.board_item_user, parent, false);
            return new BoardUserItemViewHolder(view, handlers);
        } else if (viewType == BoardColumnHeaderModel.ColumnType.NewColumn.getKey()) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.board_item_empty, parent, false);
            return new BoardEmptyItemViewHolder(view);
        } else if (viewType == BoardColumnHeaderModel.ColumnType.Number.getKey()) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.board_item_number, parent, false);
            return new BoardNumberItemViewHolder(view, handlers);
        } else if (viewType == BoardColumnHeaderModel.ColumnType.Update.getKey()) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.board_item_update, parent, false);
            return new BoardUpdateItemViewHolder(view, handlers);
        } else if (viewType == BoardColumnHeaderModel.ColumnType.Checkbox.getKey()) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.board_item_checkbox, parent, false);
            return new BoardCheckboxItemViewHolder(view, handlers);
        } else if (viewType == BoardColumnHeaderModel.ColumnType.Date.getKey()) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.board_item_date, parent, false);
            return new BoardDateItemViewHolder(view, handlers);
        } else if (viewType == BoardColumnHeaderModel.ColumnType.TimeLine.getKey()) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.board_item_timeline, parent, false);
            return new BoardTimelineItemViewHolder(view, handlers);
        }

        return null;
    }

    @Override
    public void onBindCellViewHolder(@NonNull AbstractViewHolder holder, @Nullable BoardBaseItemModel cellItemModel, int columnPosition, int rowPosition) {
        if (cellItemModel == null) return;
        String columnTitle = mColumnHeaderItems.get(columnPosition).getTitle();
        if (holder instanceof BoardStatusItemViewHolder) {
            ((BoardStatusItemViewHolder) holder).setItemModel((BoardStatusItemModel) cellItemModel, columnPosition, rowPosition);
        } else if (holder instanceof BoardTextItemViewHolder) {
            ((BoardTextItemViewHolder) holder).setItemModel((BoardTextItemModel) cellItemModel, columnTitle, columnPosition, rowPosition);
        } else if (holder instanceof BoardUserItemViewHolder) {
            ((BoardUserItemViewHolder) holder).setItemModel((BoardUserItemModel) cellItemModel, mContext);
        } else if (holder instanceof BoardUpdateItemViewHolder) {
            ((BoardUpdateItemViewHolder) holder).setItemModel((BoardUpdateItemModel) cellItemModel, columnTitle);
        } else if (holder instanceof BoardNumberItemViewHolder) {
            ((BoardNumberItemViewHolder) holder).setItemModel((BoardNumberItemModel) cellItemModel, columnTitle, columnPosition, rowPosition);
        } else if (holder instanceof BoardCheckboxItemViewHolder) {
            ((BoardCheckboxItemViewHolder) holder).setItemModel((BoardCheckboxItemModel) cellItemModel, columnPosition, rowPosition);
        } else if (holder instanceof BoardDateItemViewHolder) {
            ((BoardDateItemViewHolder) holder).setItemModel((BoardDateItemModel) cellItemModel, columnTitle, columnPosition, rowPosition);
        } else if (holder instanceof BoardTimelineItemViewHolder) {
            ((BoardTimelineItemViewHolder) holder).setItemModel((BoardTimelineItemModel) cellItemModel, columnTitle, columnPosition, rowPosition);
        }
    }

    @NonNull
    @Override
    public AbstractViewHolder onCreateColumnHeaderViewHolder(@NonNull ViewGroup parent, int viewType) {
        View layout = LayoutInflater.from(parent.getContext()).inflate(R.layout.board_column_header_view, parent, false);
        return new BoardColumnHeaderViewHolder(layout);
    }

    @Override
    public void onBindColumnHeaderViewHolder(@NonNull AbstractViewHolder holder, @Nullable BoardColumnHeaderModel columnHeaderItemModel, int columnPosition) {
        BoardColumnHeaderViewHolder columnHolder = (BoardColumnHeaderViewHolder) holder;
        if (columnHeaderItemModel.getColumnType() == BoardColumnHeaderModel.ColumnType.NewColumn)
            holder.itemView.setOnClickListener((view) -> handlers.onNewColumnHeaderClick());
        columnHolder.setColumnHeaderModel(columnHeaderItemModel);
    }

    @NonNull
    @Override
    public AbstractViewHolder onCreateRowHeaderViewHolder(@NonNull ViewGroup parent, int viewType) {
        View layout = LayoutInflater
                .from(parent.getContext())
                .inflate(R.layout.board_row_header_view, parent, false);
        return new BoardRowHeaderViewHolder(layout);
    }

    @Override
    public void onBindRowHeaderViewHolder(@NonNull AbstractViewHolder holder, @Nullable BoardRowHeaderModel rowHeaderItemModel, int rowPosition) {
        BoardRowHeaderViewHolder headerHolder = (BoardRowHeaderViewHolder) holder;
        headerHolder.setRowHeaderModel(rowHeaderItemModel);

        if (rowHeaderItemModel.getIsAddNewRowRow())
            headerHolder.itemView.setOnClickListener(view -> handlers.onNewRowHeaderClick());
    }

    @NonNull
    @Override
    public View onCreateCornerView(@NonNull ViewGroup parent) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.board_corner_view, parent, false);
        this.tvBoardTitle = view.findViewById(R.id.tvBoardTitle);
        tvBoardTitle.setText(boardViewModel.getBoardTitle());

        return view;
    }

    @Override
    public int getCellItemViewType(int columnPosition) {
        return boardViewModel.getCellItemViewType(columnPosition);
    }

    @Override
    public int getColumnHeaderItemViewType(int columnPosition) {
        return boardViewModel.getCellItemViewType(columnPosition);
    }

    public void setBoardContent(BoardContentModel content) {
        boardViewModel.setBoardTitle(content.getBoardTitle());
        boardViewModel.generateDataForBoard(content);
        setAllItems(
                boardViewModel.getColumHeaderModeList(),
                boardViewModel.getRowHeaderModelList(),
                boardViewModel.getCellModelList()
        );
    }

    public void renameBoard(String newTitle) {
        if (!newTitle.isEmpty()) this.tvBoardTitle.setText(newTitle);
    }

    public void createNewColumn(BoardColumnHeaderModel.ColumnType columnType) {
        BoardColumnHeaderModel newColumnModel = new BoardColumnHeaderModel(columnType, columnType.getName());
        List<BoardBaseItemModel> itemModels = new ArrayList<>();

        int columnPosition = mColumnHeaderItems.size() - 1;
        for (int i = 0; i < mRowHeaderItems.size(); i++) {
            BoardBaseItemModel newModel = BoardItemFactory.createNewItem(newColumnModel.getColumnType());
            itemModels.add(newModel);
        }
        boardViewModel.addNewColumn(newColumnModel, itemModels, columnPosition);
        setColumnHeaderItems(boardViewModel.getColumHeaderModeList());
        setCellItems(boardViewModel.getCellModelList());
        notifyDataSetChanged();
    }

    public void createNewRow(String title) {
        BoardRowHeaderModel newRowHeaderModel = new BoardRowHeaderModel(title, false);
        int columnPosition = mRowHeaderItems.size() - 1;
        boardViewModel.addNewRow(newRowHeaderModel, columnPosition);
        setCellItems(boardViewModel.getCellModelList());
        setRowHeaderItems(boardViewModel.getRowHeaderModelList());
        notifyDataSetChanged();
    }
}