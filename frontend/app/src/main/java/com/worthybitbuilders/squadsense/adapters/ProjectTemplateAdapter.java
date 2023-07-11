package com.worthybitbuilders.squadsense.adapters;

import android.content.res.ColorStateList;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.worthybitbuilders.squadsense.R;
import com.worthybitbuilders.squadsense.databinding.ItemFilterTypeBinding;
import com.worthybitbuilders.squadsense.databinding.ItemProjectTemplateBinding;
import com.worthybitbuilders.squadsense.models.MinimizedProjectTemplate;
import com.worthybitbuilders.squadsense.models.UserModel;
import com.worthybitbuilders.squadsense.models.board_models.BoardContentModel;
import com.worthybitbuilders.squadsense.utils.SharedPreferencesManager;

import java.util.List;

public class ProjectTemplateAdapter extends RecyclerView.Adapter {

    private final List<MinimizedProjectTemplate> listTemplate;
    private ClickHandler handler;

    public interface ClickHandler {
        void OnITManagementClick(int position);
        void OnMoreTitleClick(int position);
    }

    public ProjectTemplateAdapter(List<MinimizedProjectTemplate> listTemplate) {
        this.listTemplate = listTemplate;
    }

    public void setOnClickListener(ClickHandler handler) {
        this.handler = handler;
    }


    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemProjectTemplateBinding binding = ItemProjectTemplateBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new ProjectTemplateHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        MinimizedProjectTemplate template = (MinimizedProjectTemplate) listTemplate.get(position);
        ((ProjectTemplateHolder) holder).bind(template, position);
    }


    @Override
    public int getItemCount() {
        return listTemplate.size();
    }


    private class ProjectTemplateHolder extends RecyclerView.ViewHolder {
        ItemProjectTemplateBinding binding;
        ProjectTemplateHolder(@NonNull ItemProjectTemplateBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        void bind(MinimizedProjectTemplate projectTemplate, int position) {
            binding.title.setText(projectTemplate.getTitle());
            binding.content.setText(projectTemplate.getContent());
            binding.icon.setBackgroundTintList(ColorStateList.valueOf(projectTemplate.getColor()));
            binding.btnPreview.setOnClickListener(view -> {
                if(projectTemplate.getType() == MinimizedProjectTemplate.Type.IT_MANAGEMENT)
                    handler.OnITManagementClick(position);
                else if (projectTemplate.getType() == MinimizedProjectTemplate.Type.MORE)
                    handler.OnMoreTitleClick(position);
            });
        }
    }
}