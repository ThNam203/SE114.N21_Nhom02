package com.worthybitbuilders.squadsense.adapters;

import android.view.View;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.evrencoskun.tableview.TableView;
import com.worthybitbuilders.squadsense.R;

public class ProjectAdapter extends RecyclerView.Adapter {
    public static class ProjectViewHolder extends RecyclerView.ViewHolder {
        private TableView tableView;
        public ProjectViewHolder(@NonNull View itemView) {
            super(itemView);
            this.tableView = itemView.findViewById(R.id.tableView);
        }

        public void bind() {

        }
    }
}
