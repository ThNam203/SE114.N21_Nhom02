package com.worthybitbuilders.squadsense.adapters.holders;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.evrencoskun.tableview.adapter.recyclerview.holder.AbstractViewHolder;
import com.worthybitbuilders.squadsense.R;
import com.worthybitbuilders.squadsense.models.board_models.BoardTextItemModel;

public class BoardTextItemViewHolder extends AbstractViewHolder {
    private final TextView tvContent;
    private TextItemClickHandlers handlers;

    public BoardTextItemViewHolder(@NonNull View itemView, TextItemClickHandlers handlers) {
        super(itemView);
        this.tvContent = itemView.findViewById(R.id.tvContentTextItem);
        this.handlers = handlers;
    }

    public void setItemModel(@NonNull BoardTextItemModel itemModel, String columnTitle) {
        tvContent.setText(itemModel.getContent());
        itemView.setOnClickListener((view -> handlers.onTextItemClick(itemModel, columnTitle)));
    }

    public interface TextItemClickHandlers {
        void onTextItemClick(BoardTextItemModel itemModel, String columnTitle);
    }
}
