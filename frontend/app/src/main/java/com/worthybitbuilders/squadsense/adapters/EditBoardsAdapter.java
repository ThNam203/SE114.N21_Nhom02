package com.worthybitbuilders.squadsense.adapters;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.graphics.drawable.DrawableCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.worthybitbuilders.squadsense.R;
import com.worthybitbuilders.squadsense.models.board_models.ProjectModel;

public class EditBoardsAdapter extends RecyclerView.Adapter<EditBoardsAdapter.EditBoardItemViewHolder> {
    ProjectModel projectModel;
    ClickHandlers handlers;

    public EditBoardsAdapter(ProjectModel projectModel, ClickHandlers handlers) {
        this.projectModel = projectModel;
        this.handlers = handlers;
    }

    @NonNull
    @Override
    public EditBoardItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.board_edit_boards_item_view, parent, false);
        return new EditBoardItemViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull EditBoardItemViewHolder holder, int position) {
        holder.bind(projectModel.getBoards().get(projectModel.getChosenPosition()).getBoardTitle(), handlers, projectModel.getChosenPosition(), position);
    }

    @Override
    public int getItemCount() {
        return projectModel.getBoards().size();
    }

    public static class EditBoardItemViewHolder extends RecyclerView.ViewHolder {
        private EditText etBoardName;
        private ImageButton btnMoreOptions;
        private LinearLayout container;
        public EditBoardItemViewHolder(@NonNull View itemView) {
            super(itemView);
            this.etBoardName = itemView.findViewById(R.id.etBoardName);
            this.btnMoreOptions = itemView.findViewById(R.id.btnMoreOptions);
            this.container = itemView.findViewById(R.id.boardEditContainer);
        }

        public void bind(String boardName, ClickHandlers handlers, int chosenPosition, int position) {
            this.etBoardName.setText(boardName);
            this.btnMoreOptions.setOnClickListener(view -> handlers.onMoreOptionsClick(position));
            if (chosenPosition == position) this.container.setBackgroundColor(Color.parseColor("#464646"));
        }
    }

    public interface ClickHandlers {
        void onMoreOptionsClick(int position);
    }
}
