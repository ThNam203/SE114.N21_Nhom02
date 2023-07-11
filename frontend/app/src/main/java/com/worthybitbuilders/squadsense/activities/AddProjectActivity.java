package com.worthybitbuilders.squadsense.activities;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.worthybitbuilders.squadsense.R;
import com.worthybitbuilders.squadsense.adapters.ProjectTemplateAdapter;
import com.worthybitbuilders.squadsense.databinding.ActivityAddProjectBinding;
import com.worthybitbuilders.squadsense.models.MinimizedProjectTemplate;
import com.worthybitbuilders.squadsense.utils.ProjectTemplates;
import com.worthybitbuilders.squadsense.utils.ToastUtils;

import java.util.ArrayList;
import java.util.List;

public class AddProjectActivity extends AppCompatActivity {
    ActivityAddProjectBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAddProjectBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        getSupportActionBar().hide();

        binding.rvProjectTemplate.setLayoutManager(new LinearLayoutManager(this));
        LoadData();

        binding.btnClose.setOnClickListener(view -> AddProjectActivity.super.onBackPressed());
        binding.btnCreateNewBoard.setOnClickListener(view -> {
            Intent boardIntent = new Intent(AddProjectActivity.this, ProjectActivity.class);
            boardIntent.putExtra("whatToDo", "createNew");
            finish();
            startActivity(boardIntent);
        });
    }

    private void LoadData()
    {
        List<MinimizedProjectTemplate> listProjectTemplate = new ArrayList<>();
        listProjectTemplate.add(new MinimizedProjectTemplate("IT management", "Desc", ContextCompat.getColor(this, R.color.blue), MinimizedProjectTemplate.Type.IT_MANAGEMENT));
        listProjectTemplate.add(new MinimizedProjectTemplate("More title", "Desc", ContextCompat.getColor(this, R.color.orange), MinimizedProjectTemplate.Type.MORE));
        ProjectTemplateAdapter adapter = new ProjectTemplateAdapter(listProjectTemplate);
        binding.rvProjectTemplate.setAdapter(adapter);

        adapter.setOnClickListener(new ProjectTemplateAdapter.ClickHandler() {
            @Override
            public void OnITManagementClick(int position) {
                ToastUtils.showToastSuccess(AddProjectActivity.this, "IT click", Toast.LENGTH_SHORT);
            }

            @Override
            public void OnMoreTitleClick(int position) {
                ToastUtils.showToastSuccess(AddProjectActivity.this, "More click", Toast.LENGTH_SHORT);
            }
        });
    }
}