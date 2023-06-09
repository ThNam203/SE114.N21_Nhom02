package com.worthybitbuilders.squadsense.activities;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.drawable.DrawableCompat;
import androidx.core.util.Pair;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.datepicker.MaterialDatePicker;
import com.skydoves.colorpickerview.ColorPickerDialog;
import com.skydoves.colorpickerview.listeners.ColorEnvelopeListener;
import com.worthybitbuilders.squadsense.R;
import com.worthybitbuilders.squadsense.adapters.BoardItemMemberAdapter;
import com.worthybitbuilders.squadsense.adapters.BoardItemOwnerAdapter;
import com.worthybitbuilders.squadsense.adapters.EditBoardsAdapter;
import com.worthybitbuilders.squadsense.adapters.StatusContentsAdapter;
import com.worthybitbuilders.squadsense.adapters.StatusEditItemAdapter;
import com.worthybitbuilders.squadsense.adapters.TableViewAdapter;
import com.worthybitbuilders.squadsense.databinding.ActivityProjectBinding;
import com.worthybitbuilders.squadsense.databinding.BoardAddItemPopupBinding;
import com.worthybitbuilders.squadsense.databinding.BoardAddNewRowPopupBinding;
import com.worthybitbuilders.squadsense.databinding.BoardColumnDescriptionPopupBinding;
import com.worthybitbuilders.squadsense.databinding.BoardColumnRenamePopupBinding;
import com.worthybitbuilders.squadsense.databinding.BoardDateItemPopupBinding;
import com.worthybitbuilders.squadsense.databinding.BoardEditBoardsViewBinding;
import com.worthybitbuilders.squadsense.databinding.BoardNumberItemPopupBinding;
import com.worthybitbuilders.squadsense.databinding.BoardOwnerItemPopupBinding;
import com.worthybitbuilders.squadsense.databinding.BoardStatusEditNewItemPopupBinding;
import com.worthybitbuilders.squadsense.databinding.BoardStatusEditViewBinding;
import com.worthybitbuilders.squadsense.databinding.BoardStatusItemPopupBinding;
import com.worthybitbuilders.squadsense.databinding.BoardTextItemPopupBinding;
import com.worthybitbuilders.squadsense.databinding.BoardTimelineItemPopupBinding;
import com.worthybitbuilders.squadsense.databinding.ColumnMoreOptionsBinding;
import com.worthybitbuilders.squadsense.databinding.PopupRenameBinding;
import com.worthybitbuilders.squadsense.databinding.ProjectMoreOptionsBinding;
import com.worthybitbuilders.squadsense.factory.ProjectActivityViewModelFactory;
import com.worthybitbuilders.squadsense.models.UserModel;
import com.worthybitbuilders.squadsense.models.board_models.BoardCheckboxItemModel;
import com.worthybitbuilders.squadsense.models.board_models.BoardColumnHeaderModel;
import com.worthybitbuilders.squadsense.models.board_models.BoardContentModel;
import com.worthybitbuilders.squadsense.models.board_models.BoardDateItemModel;
import com.worthybitbuilders.squadsense.models.board_models.BoardNumberItemModel;
import com.worthybitbuilders.squadsense.models.board_models.BoardStatusItemModel;
import com.worthybitbuilders.squadsense.models.board_models.BoardTextItemModel;
import com.worthybitbuilders.squadsense.models.board_models.BoardTimelineItemModel;
import com.worthybitbuilders.squadsense.models.board_models.BoardUpdateItemModel;
import com.worthybitbuilders.squadsense.models.board_models.BoardUserItemModel;
import com.worthybitbuilders.squadsense.models.board_models.ProjectModel;
import com.worthybitbuilders.squadsense.utils.ActivityUtils;
import com.worthybitbuilders.squadsense.utils.CustomUtils;
import com.worthybitbuilders.squadsense.utils.DialogUtils;
import com.worthybitbuilders.squadsense.utils.SharedPreferencesManager;
import com.worthybitbuilders.squadsense.utils.ToastUtils;
import com.worthybitbuilders.squadsense.viewmodels.BoardViewModel;
import com.worthybitbuilders.squadsense.viewmodels.ProjectActivityViewModel;
import com.worthybitbuilders.squadsense.viewmodels.UserViewModel;

import org.json.JSONException;

import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicInteger;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProjectActivity extends AppCompatActivity {
    private TableViewAdapter boardAdapter;
    private ProjectActivityViewModel projectActivityViewModel;
    // This differs from "projectActivityViewModel", this holds logic for only TableView
    private BoardViewModel boardViewModel;
    private ActivityProjectBinding activityBinding;
    private UserViewModel userViewModel;
    private boolean isNewProjectCreateRequest = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Objects.requireNonNull(getSupportActionBar()).hide();
        activityBinding = ActivityProjectBinding.inflate(getLayoutInflater());
        setContentView(activityBinding.getRoot());
        activityBinding.tableView.setShowCornerView(true);
        activityBinding.btnShowTables.setOnClickListener(view -> showTables());
        activityBinding.btnNewBoardOnEmpty.setOnClickListener(view -> showTables());

        Intent intent = getIntent();
        ProjectActivityViewModelFactory factory = new ProjectActivityViewModelFactory(intent.getStringExtra("projectId"));
        projectActivityViewModel = new ViewModelProvider(this, factory).get(ProjectActivityViewModel.class);
        userViewModel = new ViewModelProvider(this).get(UserViewModel.class);
        boardViewModel = new ViewModelProvider(this).get(BoardViewModel.class);

        boardAdapter = new TableViewAdapter(this, boardViewModel, new TableViewAdapter.OnClickHandlers() {
            @Override
            public void OnTimelineItemClick(BoardTimelineItemModel itemModel, String columnTitle, int columnPos, int rowPos) {
                showTimelineItemPopup(itemModel, columnTitle, columnPos, rowPos);
            }

            @Override
            public void OnDateItemClick(BoardDateItemModel itemModel, String columnTitle, int columnPos, int rowPos) {
                showDateItemPopup(itemModel, columnTitle, columnPos, rowPos);
            }

            @Override
            public void onCheckboxItemClick(BoardCheckboxItemModel itemModel, int columnPos, int rowPos) {
                onCheckboxItemClicked(itemModel, columnPos, rowPos);
            }

            @Override
            public void onUpdateItemClick(BoardUpdateItemModel itemModel, int rowPosition, String rowTitle, String columnTitle) {
                Intent updateIntent = new Intent(ProjectActivity.this, BoardItemDetailActivity.class);
                updateIntent.putExtra("projectId", projectActivityViewModel.getProjectId());
                updateIntent.putExtra("boardId", boardViewModel.getBoardId());
                updateIntent.putExtra("rowPosition", rowPosition);
                updateIntent.putExtra("rowTitle", rowTitle);
                updateIntent.putExtra("projectTitle", projectActivityViewModel.getProjectModel().getTitle());
                updateIntent.putExtra("boardTitle", boardViewModel.getBoardTitle());
                updateIntent.putExtra("updateCellId", itemModel.get_id());
                updateIntent.putExtra("updateCellTitle", columnTitle);
                updateIntent.putExtra("isFromUpdateColumn", true);
                startActivity(updateIntent);
            }

            @Override
            public void onNumberItemClick(BoardNumberItemModel itemModel, String columnTitle, int columnPos, int rowPos) {
                showNumberItemPopup(itemModel, columnTitle, columnPos, rowPos);
            }

            @Override
            public void onNewColumnHeaderClick() {
                showAddBoardItemPopup();
            }

            @Override
            public void onNewRowHeaderClick() {
                showNewRowPopup();
            }

            @Override
            public void onRowHeaderClick(int rowPosition, String rowTitle) {
                Intent showRowIntent = new Intent(ProjectActivity.this, BoardItemDetailActivity.class);
                showRowIntent.putExtra("projectId", projectActivityViewModel.getProjectId());
                showRowIntent.putExtra("boardId", boardViewModel.getBoardId());
                showRowIntent.putExtra("projectTitle", projectActivityViewModel.getProjectModel().getTitle());
                showRowIntent.putExtra("boardTitle", boardViewModel.getBoardTitle());
                showRowIntent.putExtra("isFromUpdateColumn", false);
                showRowIntent.putExtra("rowPosition", rowPosition);
                showRowIntent.putExtra("rowTitle", rowTitle);
                startActivity(showRowIntent);
            }

            @Override
            public void onColumnHeaderClick(BoardColumnHeaderModel headerModel, int columnPosition, View anchor) {
                showColumnHeaderOptions(headerModel, columnPosition, anchor);
            }

            @Override
            public void onTextItemClick(BoardTextItemModel itemModel, String columnTitle, int columnPos, int rowPos) {
                showTextItemPopup(itemModel, columnTitle, columnPos, rowPos);
            }

            @Override
            public void onUserItemClick(BoardUserItemModel userItemModel) {
                showOwnerPopup(userItemModel);
            }

            @Override
            public void onStatusItemClick(BoardStatusItemModel itemModel, int columnPos, int rowPos) {
                showTaskStatusPopup(itemModel, columnPos, rowPos);
            }
        });
        activityBinding.tableView.setAdapter(boardAdapter);

        projectActivityViewModel.getProjectModelLiveData().observe(this, projectModel -> {
            if (projectModel == null) return;
            // set cells content, pass the adapter to let them call the set item

            activityBinding.tvProjectTitle.setText(projectModel.getTitle());
            if (projectModel.getBoards().size() == 0) {
                activityBinding.emptyBoardNotification.setVisibility(View.VISIBLE);
                return;
            } else activityBinding.emptyBoardNotification.setVisibility(View.GONE);

            // the case is if user remove all board and add one, but the chosenPosition is bigger than 1
            if (projectModel.getChosenPosition() >= projectModel.getBoards().size()) projectModel.setChosenPosition(0);

            BoardContentModel content = projectModel.getBoards().get(projectModel.getChosenPosition());
            boardViewModel.setBoardContent(content, projectModel.get_id(), boardAdapter);
            // set board title for "more table" drop down
            activityBinding.btnShowTables.setText(
                    projectModel.getBoards()
                            .get(projectModel.getChosenPosition())
                            .getBoardTitle());
        });

        activityBinding.btnMoreOptions.setOnClickListener(this::showProjectOptions);
        activityBinding.btnBack.setOnClickListener((view) -> onBackPressed());
        createNewProjectIfRequest();
    }

    private void showColumnHeaderOptions(BoardColumnHeaderModel headerModel, int columnPosition, View anchor) {
        ColumnMoreOptionsBinding binding = ColumnMoreOptionsBinding.inflate(getLayoutInflater());
        PopupWindow popupWindow = new PopupWindow(binding.getRoot(), LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT, false);
        popupWindow.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        popupWindow.setElevation(50);

        if (headerModel.getColumnType() == BoardColumnHeaderModel.ColumnType.Update) {
            binding.btnSortAsc.setVisibility(View.GONE);
            binding.btnSortDesc.setVisibility(View.GONE);
        } else if (boardViewModel.getSortingColumnPosition() == columnPosition) {
            if (boardViewModel.getSortState() == BoardViewModel.SortState.ASCENDING)
                DrawableCompat.setTint(binding.btnSortAscContainer.getBackground(), Color.parseColor("#8ecae6"));
            else DrawableCompat.setTint(binding.btnSortAscContainer.getBackground(), ContextCompat.getColor(ProjectActivity.this, R.color.transparent));
        }

        binding.btnSortAsc.setOnClickListener(view -> {
            boardViewModel.sortColumn(columnPosition, BoardViewModel.SortState.ASCENDING, boardAdapter);
            popupWindow.dismiss();
        });

        binding.btnSortDesc.setOnClickListener(view -> {
            boardViewModel.sortColumn(columnPosition, BoardViewModel.SortState.DESCENDING, boardAdapter);
            popupWindow.dismiss();
        });

        binding.btnDescription.setOnClickListener(view -> {
            popupWindow.dismiss();
            showColumnDescription(headerModel, columnPosition);
        });

        String userId = SharedPreferencesManager.getData(SharedPreferencesManager.KEYS.USER_ID);
        if (Objects.equals(userId, projectActivityViewModel.getProjectModel().getCreatorId())) {
            binding.btnRemove.setOnClickListener(view -> {
                popupWindow.dismiss();
                showConfirmDeleteColumn(headerModel, columnPosition);
            });

            binding.btnRename.setOnClickListener(view -> {
                popupWindow.dismiss();
                showRenameColumnDialog(headerModel, columnPosition);
            });
        } else {
            binding.btnRemove.setVisibility(View.GONE);
            binding.btnRename.setVisibility(View.GONE);
        }

        popupWindow.setTouchable(true);
        popupWindow.setOutsideTouchable(true);
        popupWindow.showAsDropDown(anchor, 0, 0);
    }

    private void showRenameColumnDialog(BoardColumnHeaderModel itemModel, int columnPosition) {
        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        BoardColumnRenamePopupBinding binding = BoardColumnRenamePopupBinding.inflate(getLayoutInflater());
        dialog.setContentView(binding.getRoot());

        binding.btnClosePopup.setOnClickListener((view) -> dialog.dismiss());
        binding.etDescription.setText(itemModel.getTitle());
        binding.btnClearDescription.setOnClickListener((view) -> binding.etDescription.setText(""));
        binding.btnSaveTextItem.setOnClickListener(view -> {
            Dialog loadingDialog = DialogUtils.GetLoadingDialog(ProjectActivity.this);
            loadingDialog.show();
            String newName = binding.etDescription.getText().toString();
            if (newName.isEmpty()) {
                ToastUtils.showToastError(ProjectActivity.this, "Unable to save empty name", Toast.LENGTH_SHORT);
                return;
            }
            try {
                boardViewModel.updateColumn(columnPosition, newName, false, new BoardViewModel.ApiCallHandler() {
                    @Override
                    public void onSuccess() {
                        dialog.dismiss();
                        loadingDialog.dismiss();
                    }

                    @Override
                    public void onFailure(String message) {
                        ToastUtils.showToastError(ProjectActivity.this, "Unable to save new name, please try again", Toast.LENGTH_SHORT);
                        loadingDialog.dismiss();
                    }
                });
            } catch (JSONException e) {
                ToastUtils.showToastError(ProjectActivity.this, "Unable to save new name, please try again", Toast.LENGTH_SHORT);
                loadingDialog.dismiss();
            }
        });

        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.getWindow().getAttributes().windowAnimations = R.style.PopupAnimationBottom;
        dialog.getWindow().setGravity(Gravity.BOTTOM);
        dialog.show();
    }

    private void showProjectOptions(View anchor) {
        ProjectMoreOptionsBinding binding = ProjectMoreOptionsBinding.inflate(getLayoutInflater());
        PopupWindow popupWindow = new PopupWindow(binding.getRoot(), LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT, false);
        popupWindow.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        popupWindow.setElevation(50);

        //change view for different roles in project like view for user, admin, creator
        String creatorId = projectActivityViewModel.getProjectModel().getCreatorId();
        List<String> adminIds = projectActivityViewModel.getProjectModel().getAdminIds();
        String userId = SharedPreferencesManager.getData(SharedPreferencesManager.KEYS.USER_ID);
        if(creatorId.equals(userId))
            showProjectOptionFor(Role.CREATOR, binding);
        else if(adminIds.contains(userId))
            showProjectOptionFor(Role.ADMIN, binding);
        else
            showProjectOptionFor(Role.MEMBER, binding);


        binding.btnShowMember.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ActivityUtils.switchToActivity(ProjectActivity.this, MemberActivity.class);
            }
        });

        binding.btnRenameProject.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showRenamePopup();
            }
        });

        binding.btnRequestAdmin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String projectId = projectActivityViewModel.getProjectId();
                projectActivityViewModel.requestAdmin(projectId, new ProjectActivityViewModel.ApiCallHandlers() {
                    @Override
                    public void onSuccess() {
                        ToastUtils.showToastSuccess(ProjectActivity.this, "Your request was sent to admins of this project", Toast.LENGTH_SHORT);
                        popupWindow.dismiss();
                    }

                    @Override
                    public void onFailure(String message) {
                        ToastUtils.showToastError(ProjectActivity.this, message, Toast.LENGTH_SHORT);
                    }
                });
            }
        });

        binding.btnActivityLog.setOnClickListener(view -> {
            Intent activityLogIntent = new Intent(ProjectActivity.this, ProjectActivityLogActivity.class);
            activityLogIntent.putExtra("projectId", projectActivityViewModel.getProjectId());
            startActivity(activityLogIntent);
        });

        binding.btnDeleteProject.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String titleConfirmDialog = "Delete";
                String contentConfirmDialog = "Do you want to delete this project ?";
                DialogUtils.showConfirmDialogDelete(ProjectActivity.this, titleConfirmDialog, contentConfirmDialog, new DialogUtils.ConfirmAction() {
                    @Override
                    public void onAcceptToDo(Dialog thisDialog) {
                        thisDialog.dismiss();
                        Dialog loadingDialog = DialogUtils.GetLoadingDialog(ProjectActivity.this);
                        loadingDialog.show();
                        String projectId = SharedPreferencesManager.getData(SharedPreferencesManager.KEYS.CURRENT_PROJECT_ID);
                        projectActivityViewModel.deleteProject(projectId, new ProjectActivityViewModel.ApiCallHandlers() {
                            @Override
                            public void onSuccess() {
                                ToastUtils.showToastSuccess(ProjectActivity.this, "Project deleted", Toast.LENGTH_SHORT);
                                loadingDialog.dismiss();
                                ProjectActivity.this.onBackPressed();
                            }

                            @Override
                            public void onFailure(String message) {
                                ToastUtils.showToastError(ProjectActivity.this, "You are not allowed to delete this project", Toast.LENGTH_SHORT);
                                loadingDialog.dismiss();
                            }
                        });

                    }

                    @Override
                    public void onCancel(Dialog thisDialog) {
                        thisDialog.dismiss();
                    }
                });
            }
        });

        popupWindow.setTouchable(true);
        popupWindow.setOutsideTouchable(true);
        popupWindow.showAsDropDown(anchor, 0, 0);
    }

    private enum Role{MEMBER, CREATOR, ADMIN}
    private void showProjectOptionFor(Role role, ProjectMoreOptionsBinding projectMoreOptionsBinding)
    {
        projectMoreOptionsBinding.btnRequestAdmin.setVisibility(View.VISIBLE);
        projectMoreOptionsBinding.btnLeaveProject.setVisibility(View.VISIBLE);
        projectMoreOptionsBinding.btnDeleteProject.setVisibility(View.VISIBLE);
        projectMoreOptionsBinding.btnRenameProject.setVisibility(View.VISIBLE);
        projectMoreOptionsBinding.btnShowMember.setVisibility(View.VISIBLE);
        projectMoreOptionsBinding.btnActivityLog.setVisibility(View.VISIBLE);

        switch (role)
        {
            case CREATOR:
                projectMoreOptionsBinding.btnLeaveProject.setVisibility(View.GONE);
                projectMoreOptionsBinding.btnRequestAdmin.setVisibility(View.GONE);
                break;
            case ADMIN:
                projectMoreOptionsBinding.btnRequestAdmin.setVisibility(View.GONE);
                projectMoreOptionsBinding.btnDeleteProject.setVisibility(View.GONE);
                projectMoreOptionsBinding.btnRenameProject.setVisibility(View.GONE);
                break;
            case MEMBER:
                projectMoreOptionsBinding.btnDeleteProject.setVisibility(View.GONE);
                projectMoreOptionsBinding.btnRenameProject.setVisibility(View.GONE);
                break;
        }
    }

    private void showRenamePopup()
    {
        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        PopupRenameBinding popupRenameBinding = PopupRenameBinding.inflate(getLayoutInflater());
        dialog.setContentView(popupRenameBinding.getRoot());

        popupRenameBinding.input.setText(projectActivityViewModel.getProjectModel().getTitle());
        popupRenameBinding.input.requestFocus();

        popupRenameBinding.btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ProjectModel projectToUpdate = projectActivityViewModel.getProjectModel();
                projectToUpdate.setTitle(popupRenameBinding.input.getText().toString());
                projectActivityViewModel.updateProject(projectToUpdate, new ProjectActivityViewModel.ApiCallHandlers() {
                    @Override
                    public void onSuccess() {
                        activityBinding.tvProjectTitle.setText(popupRenameBinding.input.getText().toString());
                        dialog.dismiss();
                    }

                    @Override
                    public void onFailure(String message) {
                        ToastUtils.showToastError(ProjectActivity.this, message, Toast.LENGTH_SHORT);
                    }
                });
            }
        });

        popupRenameBinding.btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });

        dialog.setCanceledOnTouchOutside(false);
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.getWindow().getAttributes().windowAnimations = R.style.PopupAnimationBottom;
        dialog.getWindow().setGravity(Gravity.CENTER);
        dialog.show();
    }

    private void showColumnDescription(BoardColumnHeaderModel itemModel, int columnPos) {
        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        BoardColumnDescriptionPopupBinding binding = BoardColumnDescriptionPopupBinding.inflate(getLayoutInflater());
        dialog.setContentView(binding.getRoot());

        binding.btnClosePopup.setOnClickListener((view) -> dialog.dismiss());
        binding.textItemTitle.setText(itemModel.getTitle());
        if (!itemModel.getDescription().isEmpty()) {
            binding.tvContent.setText(itemModel.getDescription());
            binding.etDescription.setText(itemModel.getDescription());
        }

        String userId = SharedPreferencesManager.getData(SharedPreferencesManager.KEYS.USER_ID);
        // if the user is the author, then make the description editable
        if (Objects.equals(projectActivityViewModel.getProjectModel().getCreatorId(), userId)) {
            binding.btnSaveTextItem.setVisibility(View.VISIBLE);
            binding.tvContent.setVisibility(View.GONE);
            binding.editDescriptionContainer.setVisibility(View.VISIBLE);
            binding.btnClearDescription.setOnClickListener((view) -> binding.etDescription.setText(""));

            binding.btnSaveTextItem.setOnClickListener(view -> {
                Dialog loadingDialog = DialogUtils.GetLoadingDialog(ProjectActivity.this);
                loadingDialog.show();
                String newDescription = binding.etDescription.getText().toString();
                try {
                    boardViewModel.updateColumn(columnPos, newDescription, true, new BoardViewModel.ApiCallHandler() {
                        @Override
                        public void onSuccess() {
                            dialog.dismiss();
                            loadingDialog.dismiss();
                        }

                        @Override
                        public void onFailure(String message) {
                            ToastUtils.showToastError(ProjectActivity.this, "Unable to save the description, please try again", Toast.LENGTH_SHORT);
                            loadingDialog.dismiss();
                        }
                    });
                } catch (JSONException e) {
                    ToastUtils.showToastError(ProjectActivity.this, "Unable to save the description, please try again", Toast.LENGTH_SHORT);
                    loadingDialog.dismiss();
                }
            });
        }

        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.getWindow().getAttributes().windowAnimations = R.style.PopupAnimationBottom;
        dialog.getWindow().setGravity(Gravity.BOTTOM);
        dialog.show();
    }

    private void showConfirmDeleteColumn(BoardColumnHeaderModel headerModel, int columnPosition) {
        String titleConfirmDialog = String.format(Locale.US, "Delete column \"%s\"?", headerModel.getTitle());
        String contentConfirmDialog = "This column will be removed from the board";
        DialogUtils.showConfirmDialogDelete(this, titleConfirmDialog, contentConfirmDialog, new DialogUtils.ConfirmAction() {
            @Override
            public void onAcceptToDo(Dialog thisDialog) {
                boardViewModel.deleteColumn(columnPosition, new BoardViewModel.ApiCallHandler() {
                    @Override
                    public void onSuccess() {
                        thisDialog.dismiss();
                    }

                    @Override
                    public void onFailure(String message) {
                        ToastUtils.showToastError(ProjectActivity.this, "Something went wrong", Toast.LENGTH_SHORT);
                    }
                });
            }

            @Override
            public void onCancel(Dialog thisDialog) {
                thisDialog.dismiss();
            }
        });
    }

    private void onCheckboxItemClicked(BoardCheckboxItemModel itemModel, int columnPos, int rowPos) {
        Dialog loadingDialog = DialogUtils.GetLoadingDialog(ProjectActivity.this);
        loadingDialog.show();
        itemModel.setChecked(!itemModel.getChecked());
        boardViewModel.updateACell(itemModel).enqueue(new Callback<Void>() {
            @Override
            public void onResponse(@NonNull Call<Void> call, @NonNull Response<Void> response) {
                if (response.isSuccessful()) {
                    boardAdapter.changeCellItem(columnPos, rowPos, itemModel);
                } else {
                    ToastUtils.showToastError(ProjectActivity.this, "Unable to update the cell", Toast.LENGTH_LONG);
                }

                loadingDialog.dismiss();
            }

            @Override
            public void onFailure(@NonNull Call<Void> call, @NonNull Throwable t) {
                ToastUtils.showToastError(ProjectActivity.this, "Unable to update the cell", Toast.LENGTH_LONG);
                loadingDialog.dismiss();
            }
        });
    }

    /**
     * The reason to use onStart() is the onStop() will eventually navigate to this
     */
    @Override
    protected void onStart() {
        super.onStart();
        if (!isNewProjectCreateRequest) updateProject();
    }

    /**
     * TODO: better way to handle this ("fetch", "createNew")
     * @whatToDo is the thing that specify how the activity should handle
     * the case
     * One is create new board, it needs to send and get data from server ("createNew")
     * Two is fetch the board which is created before ("fetch")
     */

    private void createNewProjectIfRequest() {
        Intent intent = getIntent();
        String whatToDo = intent.getStringExtra("whatToDo");
        Dialog loadingDialog = DialogUtils.GetLoadingDialog(this);
        if (whatToDo.equals("createNew")) {
            String templateName = intent.getStringExtra("templateName");
            if (templateName == null) {
                ToastUtils.showToastError(ProjectActivity.this, "Something went wrong, please try again", Toast.LENGTH_LONG);
                finish();
                return;
            }
            isNewProjectCreateRequest = true;
            loadingDialog.show();
            projectActivityViewModel.saveNewProjectToRemote(new ProjectActivityViewModel.ApiCallHandlers() {
                @Override
                public void onSuccess() {
                    loadingDialog.dismiss();
                    isNewProjectCreateRequest = false;
                    saveRecentAccessProject(projectActivityViewModel.getProjectId());
                }

                @Override
                public void onFailure(String message) {
                    ToastUtils.showToastError(ProjectActivity.this, "Failed to create new project, please try again", Toast.LENGTH_LONG);
                    loadingDialog.dismiss();
                    finish();
                }
            }, templateName);
        }
    }

    private void updateProject() {
        Dialog loadingDialog = DialogUtils.GetLoadingDialog(ProjectActivity.this);
        loadingDialog.show();
        projectActivityViewModel.getProjectById(new ProjectActivityViewModel.ApiCallHandlers() {
            @Override
            public void onSuccess() {
                loadingDialog.dismiss();
            }

            @Override
            public void onFailure(String message) {
                loadingDialog.dismiss();
                ToastUtils.showToastError(ProjectActivity.this, message, Toast.LENGTH_LONG);
                finish();
            }
        });
    }

    private void showTables() {
        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        BoardEditBoardsViewBinding binding = BoardEditBoardsViewBinding.inflate(getLayoutInflater());
        dialog.setContentView(binding.getRoot());

        EditBoardsAdapter editBoardsAdapter = new EditBoardsAdapter(this.projectActivityViewModel, this);
        editBoardsAdapter.setHandlers(new EditBoardsAdapter.ClickHandlers() {
            @Override
            public void onRemoveClick(int position) {
                editBoardsAdapter.notifyItemRemoved(position);
                editBoardsAdapter.notifyItemRangeChanged(position, projectActivityViewModel.getProjectModel().getBoards().size());
            }

            @Override
            public void onRenameClick(int position, String newTitle) {
                editBoardsAdapter.notifyItemChanged(position);
                if (position == projectActivityViewModel.getProjectModel().getChosenPosition()) {
                    boardViewModel.setBoardTitle(newTitle);
                    activityBinding.btnShowTables.setText(newTitle);

                }
            }

            @Override
            public void onItemClick(int position) {
                if (position == projectActivityViewModel.getProjectModel().getChosenPosition()) {
                    dialog.dismiss();
                    return;
                }

                projectActivityViewModel.getProjectModel().setChosenPosition(position);
                BoardContentModel newContent = projectActivityViewModel.getProjectModel().getBoards().get(position);
                boardViewModel.setBoardContent(newContent, projectActivityViewModel.getProjectModel().get_id(), boardAdapter);
                activityBinding.btnShowTables.setText(newContent.getBoardTitle());
                dialog.dismiss();
            }


        });
        binding.rvBoards.setLayoutManager(new LinearLayoutManager(this));
        binding.rvBoards.setAdapter(editBoardsAdapter);

        binding.btnClose.setOnClickListener(view -> {
            if (projectActivityViewModel.getProjectModel().getBoards().size() == 0) {
                activityBinding.emptyBoardNotification.setVisibility(View.VISIBLE);
            }
            dialog.dismiss();
        });
        binding.btnNewBoard.setOnClickListener(view -> {
            Dialog loadingDialog = DialogUtils.GetLoadingDialog(ProjectActivity.this);
            loadingDialog.show();
            projectActivityViewModel.addNewBoardToProject(new ProjectActivityViewModel.ApiCallHandlers() {
                @Override
                public void onSuccess() {
                    editBoardsAdapter.notifyItemInserted(projectActivityViewModel.getProjectModel().getBoards().size() - 1);
                    loadingDialog.dismiss();
                }

                @Override
                public void onFailure(String message) {
                    ToastUtils.showToastError(ProjectActivity.this, "Unable to add new board, please try again", Toast.LENGTH_LONG);
                    loadingDialog.dismiss();
                }
            });
        });

        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.getWindow().getAttributes().windowAnimations = R.style.PopupAnimationBottom;
        dialog.getWindow().setGravity(Gravity.BOTTOM);
        dialog.show();
    }

    private void showNewRowPopup() {
        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        BoardAddNewRowPopupBinding binding = BoardAddNewRowPopupBinding.inflate(getLayoutInflater());
        dialog.setContentView(binding.getRoot());

        binding.btnClosePopup.setOnClickListener(view -> dialog.dismiss());
        binding.btnAdd.setOnClickListener(view -> {
            String newRowTitle = binding.etContent.getText().toString();
            if (newRowTitle.isEmpty()) { dialog.dismiss(); return; }
            boardViewModel.createNewRow(newRowTitle);
            dialog.dismiss();
        });

        dialog.getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.getWindow().getAttributes().windowAnimations = R.style.PopupAnimationBottom;
        dialog.getWindow().setGravity(Gravity.CENTER);
        dialog.show();
    }

    private void showOwnerPopup(BoardUserItemModel userItemModel)
    {
        final Dialog dialog = new Dialog(ProjectActivity.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        BoardOwnerItemPopupBinding binding = BoardOwnerItemPopupBinding.inflate(getLayoutInflater());
        dialog.setContentView(binding.getRoot());

        List<UserModel> listMember = new ArrayList<>();
        List<UserModel> listOwner = new ArrayList<>();
        binding.rvMembers.setLayoutManager(new LinearLayoutManager(ProjectActivity.this));
        binding.rvOwers.setLayoutManager(new LinearLayoutManager(ProjectActivity.this, LinearLayoutManager.HORIZONTAL, false));
        BoardItemMemberAdapter boardItemMemberAdapter = new BoardItemMemberAdapter(listMember);
        BoardItemOwnerAdapter boardItemOwnerAdapter = new BoardItemOwnerAdapter(listOwner);

        String projectId = SharedPreferencesManager.getData(SharedPreferencesManager.KEYS.CURRENT_PROJECT_ID);
        projectActivityViewModel.getMember(projectId, new ProjectActivityViewModel.ApiCallMemberHandlers() {
            @Override
            public void onSuccess(List<UserModel> listMemberData) {
                listMember.addAll(listMemberData);
                boardItemMemberAdapter.notifyDataSetChanged();
                binding.rvMembers.setAdapter(boardItemMemberAdapter);
                if(listMember.size() > 0) binding.rvMembers.setVisibility(View.VISIBLE);
                else binding.rvMembers.setVisibility(View.GONE);
            }

            @Override
            public void onFailure(String message) {
                ToastUtils.showToastError(ProjectActivity.this, message, Toast.LENGTH_SHORT);
            }
        });

        boardItemMemberAdapter.setOnClickListener(new BoardItemMemberAdapter.OnActionCallback() {
            @Override
            public void OnClick(int position, boolean status) {
                if(status)
                {
                    listOwner.add(listMember.get(position));
                    boardItemOwnerAdapter.notifyDataSetChanged();
                    binding.rvOwers.setAdapter(boardItemOwnerAdapter);
                    binding.layoutRvOwers.setVisibility(View.VISIBLE);
                }
                else
                {
                    listOwner.remove(listMember.get(position));
                    boardItemOwnerAdapter.notifyDataSetChanged();
                    binding.rvOwers.setAdapter(boardItemOwnerAdapter);
                    if(listOwner.size() > 0) binding.layoutRvOwers.setVisibility(View.VISIBLE);
                    else binding.layoutRvOwers.setVisibility(View.GONE);
                }
            }
        });

        boardItemOwnerAdapter.setOnClickListener(new BoardItemOwnerAdapter.OnActionCallback() {
            @Override
            public void OnClick(int position) {
                listOwner.remove(listOwner.get(position));
                boardItemOwnerAdapter.notifyDataSetChanged();
                binding.rvOwers.setAdapter(boardItemOwnerAdapter);
                if(listOwner.size() > 0) binding.layoutRvOwers.setVisibility(View.VISIBLE);
                else binding.layoutRvOwers.setVisibility(View.GONE);
            }
        });

        binding.btnClosePopup.setOnClickListener(view -> dialog.dismiss());

        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.getWindow().getAttributes().windowAnimations = R.style.PopupAnimationBottom;
        dialog.getWindow().setGravity(Gravity.BOTTOM);
        dialog.show();
    }

    private void showTaskStatusPopup(BoardStatusItemModel statusItemModel, int columnPos, int rowPos)
    {
        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        BoardStatusItemPopupBinding binding = BoardStatusItemPopupBinding.inflate(getLayoutInflater());
        dialog.setContentView(binding.getRoot());

        StatusContentsAdapter statusContentsAdapter = new StatusContentsAdapter(statusItemModel);
        statusContentsAdapter.setHandlers((itemModel, newContent) -> {
            itemModel.setContent(newContent);
            Dialog loadingDialog = DialogUtils.GetLoadingDialog(ProjectActivity.this);
            loadingDialog.show();
            boardViewModel.updateACell(itemModel).enqueue(new Callback<Void>() {
                @Override
                public void onResponse(@NonNull Call<Void> call, @NonNull Response<Void> response) {
                    if (response.isSuccessful()) {
                        boardAdapter.changeCellItem(columnPos, rowPos, itemModel);
                    } else {
                        ToastUtils.showToastError(ProjectActivity.this, "Unable to save the cell", Toast.LENGTH_LONG);
                    }

                    loadingDialog.dismiss();
                    dialog.dismiss();
                }

                @Override
                public void onFailure(@NonNull Call<Void> call, @NonNull Throwable t) {
                    ToastUtils.showToastError(ProjectActivity.this, "Unable to save the cell", Toast.LENGTH_LONG);
                    loadingDialog.dismiss();
                    dialog.dismiss();
                }
            });
        });
        binding.rvStatusContents.setLayoutManager(new LinearLayoutManager(this));
        binding.rvStatusContents.setAdapter(statusContentsAdapter);

        binding.btnClose.setOnClickListener(view -> dialog.dismiss());
        binding.btnEditLabels.setOnClickListener(view -> {
            showStatusContentsEdit(statusItemModel, statusContentsAdapter, columnPos, rowPos);
        });

        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.getWindow().getAttributes().windowAnimations = R.style.PopupAnimationBottom;
        dialog.getWindow().setGravity(Gravity.BOTTOM);
        dialog.show();
    }

    private void showStatusContentsEdit(BoardStatusItemModel statusItemModel, StatusContentsAdapter statusContentsAdapter, int columnPos, int rowPos) {
        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        BoardStatusEditViewBinding binding = BoardStatusEditViewBinding.inflate(getLayoutInflater());
        dialog.setContentView(binding.getRoot());

        BoardStatusItemModel clonedItemModel = new BoardStatusItemModel(statusItemModel);

        StatusEditItemAdapter statusEditItemAdapter = new StatusEditItemAdapter(clonedItemModel);
        statusEditItemAdapter.setHandlers(new StatusEditItemAdapter.ClickHandlers() {
            @Override
            public void onChooseColorClick(int position, BoardStatusItemModel itemModel) {
                new ColorPickerDialog.Builder(ProjectActivity.this)
                        .setTitle("Choose color")
                        .setPositiveButton("SELECT", (ColorEnvelopeListener) (envelope, fromUser) -> {
                            itemModel.setColorAt(position, '#' + envelope.getHexCode());
                            statusEditItemAdapter.notifyItemChanged(position);
                        })
                        .setNegativeButton("CANCEL", (dialogInterface, i) -> dialogInterface.dismiss())
                        .attachAlphaSlideBar(true)
                        .attachBrightnessSlideBar(true)
                        .setBottomSpace(12)
                        .show();
            }

            @Override
            public void onDeleteClick(int position, BoardStatusItemModel itemModel) {
                if (position >= itemModel.getContents().size()) return;
                if (Objects.equals(itemModel.getContent(), itemModel.getContents().get(position))) itemModel.setContent("");
                itemModel.removeContentAt(position);
                statusEditItemAdapter.notifyItemRemoved(position);
                statusEditItemAdapter.notifyItemRangeChanged(position, itemModel.getContents().size());
            }
        });

        binding.rvStatusItems.setLayoutManager(new LinearLayoutManager(ProjectActivity.this, LinearLayoutManager.VERTICAL, false));
        binding.rvStatusItems.setAdapter(statusEditItemAdapter);
        binding.btnAdd.setOnClickListener(view -> {
            showAddNewStatusDialog(clonedItemModel, statusEditItemAdapter);
        });

        binding.btnClose.setOnClickListener(view -> dialog.dismiss());
        binding.btnSave.setOnClickListener(view -> {
            statusItemModel.copyDataFromAnotherInstance(clonedItemModel);

            Dialog loadingDialog = DialogUtils.GetLoadingDialog(ProjectActivity.this);
            loadingDialog.show();
            boardViewModel.updateACell(statusItemModel).enqueue(new Callback<Void>() {
                @Override
                public void onResponse(@NonNull Call<Void> call, @NonNull Response<Void> response) {
                    if (response.isSuccessful()) {
                        boardAdapter.changeCellItem(columnPos, rowPos, statusItemModel);
                        statusContentsAdapter.notifyDataSetChanged();
                    } else {
                        ToastUtils.showToastError(ProjectActivity.this, "Unable to save", Toast.LENGTH_LONG);
                    }

                    loadingDialog.dismiss();
                    dialog.dismiss();
                }

                @Override
                public void onFailure(@NonNull Call<Void> call, @NonNull Throwable t) {
                    ToastUtils.showToastError(ProjectActivity.this, "Unable to save", Toast.LENGTH_LONG);
                    loadingDialog.dismiss();
                    dialog.dismiss();
                }
            });
        });

        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.getWindow().getAttributes().windowAnimations = R.style.PopupAnimationBottom;
        dialog.getWindow().setGravity(Gravity.BOTTOM);
        dialog.show();
    }

    private void showAddNewStatusDialog(BoardStatusItemModel itemModel, StatusEditItemAdapter statusEditItemAdapter) {
        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        BoardStatusEditNewItemPopupBinding binding = BoardStatusEditNewItemPopupBinding.inflate(getLayoutInflater());
        dialog.setContentView(binding.getRoot());

        binding.btnClosePopup.setOnClickListener((view) -> dialog.dismiss());
        binding.btnAdd.setOnClickListener(view -> {
            String newContent = binding.etTextItem.getText().toString();
            for (int i = 0; i < itemModel.getContents().size(); i++) {
                if (itemModel.getContents().get(i).equals(newContent)) {
                    ToastUtils.showToastError(ProjectActivity.this, "Content already existed", Toast.LENGTH_LONG);
                    return;
                }
            }
            itemModel.addNewContent(newContent);
            statusEditItemAdapter.notifyItemInserted(itemModel.getContents().size());
            dialog.dismiss();
        });

        dialog.getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.getWindow().getAttributes().windowAnimations = R.style.PopupAnimationBottom;
        dialog.getWindow().setGravity(Gravity.CENTER);
        dialog.show();
    }

    private void showTextItemPopup(BoardTextItemModel itemModel, String title, int columnPos, int rowPos) {
        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        BoardTextItemPopupBinding binding = BoardTextItemPopupBinding.inflate(getLayoutInflater());
        dialog.setContentView(binding.getRoot());

        binding.textItemTitle.setText(title);
        binding.etTextItem.setText(itemModel.getContent());
        binding.btnClosePopup.setOnClickListener((view) -> dialog.dismiss());

        binding.btnSaveTextItem.setOnClickListener(view -> {
            String newContent = String.valueOf(binding.etTextItem.getText());
            itemModel.setContent(newContent);

            Dialog loadingDialog = DialogUtils.GetLoadingDialog(ProjectActivity.this);
            loadingDialog.show();
            boardViewModel.updateACell(itemModel).enqueue(new Callback<Void>() {
                @Override
                public void onResponse(@NonNull Call<Void> call, @NonNull Response<Void> response) {
                    if (response.isSuccessful()) {
                        boardAdapter.changeCellItem(columnPos, rowPos, itemModel);
                    } else {
                        ToastUtils.showToastError(ProjectActivity.this, "Unable to update the cell", Toast.LENGTH_LONG);
                    }

                    loadingDialog.dismiss();
                    dialog.dismiss();
                }

                @Override
                public void onFailure(@NonNull Call<Void> call, @NonNull Throwable t) {
                    ToastUtils.showToastError(ProjectActivity.this, "Unable to update the cell", Toast.LENGTH_LONG);
                    loadingDialog.dismiss();
                    dialog.dismiss();
                }
            });
        });

        binding.btnClearTextItem.setOnClickListener((view) -> binding.etTextItem.setText(""));

        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.getWindow().getAttributes().windowAnimations = R.style.PopupAnimationBottom;
        dialog.getWindow().setGravity(Gravity.BOTTOM);
        dialog.show();
    }

    private void showNumberItemPopup(BoardNumberItemModel itemModel, String title, int columnPos, int rowPos) {
        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        BoardNumberItemPopupBinding binding = BoardNumberItemPopupBinding.inflate(getLayoutInflater());
        dialog.setContentView(binding.getRoot());

        binding.textNumberTitle.setText(title);
        binding.etNumberItem.setText(itemModel.getContent());
        binding.btnClosePopup.setOnClickListener((view) -> dialog.dismiss());
        binding.btnSaveNumberItem.setOnClickListener(view -> {
            String newContent = String.valueOf(binding.etNumberItem.getText());
            itemModel.setContent(newContent);

            Dialog loadingDialog = DialogUtils.GetLoadingDialog(ProjectActivity.this);
            loadingDialog.show();
            boardViewModel.updateACell(itemModel).enqueue(new Callback<Void>() {
                @Override
                public void onResponse(@NonNull Call<Void> call, @NonNull Response<Void> response) {
                    if (response.isSuccessful()) {
                        boardAdapter.changeCellItem(columnPos, rowPos, itemModel);
                    } else {
                        ToastUtils.showToastError(ProjectActivity.this, "Unable to update the cell", Toast.LENGTH_LONG);
                    }

                    loadingDialog.dismiss();
                    dialog.dismiss();
                }

                @Override
                public void onFailure(@NonNull Call<Void> call, @NonNull Throwable t) {
                    ToastUtils.showToastError(ProjectActivity.this, "Unable to update the cell", Toast.LENGTH_LONG);
                    loadingDialog.dismiss();
                    dialog.dismiss();
                }
            });

            boardAdapter.changeCellItem(columnPos, rowPos, itemModel);
            dialog.dismiss();
        });

        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.getWindow().getAttributes().windowAnimations = R.style.PopupAnimationBottom;
        dialog.getWindow().setGravity(Gravity.BOTTOM);
        dialog.show();
    }

    private void showTimelineItemPopup(BoardTimelineItemModel itemModel, String title, int columnPos, int rowPos) {
        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        BoardTimelineItemPopupBinding binding = BoardTimelineItemPopupBinding.inflate(getLayoutInflater());
        dialog.setContentView(binding.getRoot());

        final AtomicInteger dialogStartYear = new AtomicInteger(-1);
        final AtomicInteger dialogStartMonth = new AtomicInteger(-1);
        final AtomicInteger dialogStartDay = new AtomicInteger(-1);
        final AtomicInteger dialogEndYear = new AtomicInteger(-1);
        final AtomicInteger dialogEndMonth = new AtomicInteger(-1);
        final AtomicInteger dialogEndDay = new AtomicInteger(-1);

        if (!itemModel.getContent().isEmpty()) {
            dialogStartDay.set(itemModel.getStartDay());
            dialogStartMonth.set(itemModel.getStartMonth());
            dialogStartYear.set(itemModel.getStartYear());
            dialogEndDay.set(itemModel.getEndDay());
            dialogEndMonth.set(itemModel.getEndMonth());
            dialogEndYear.set(itemModel.getEndYear());
            binding.tvTimelineValue.setText(itemModel.getContent());
            binding.tvAddTimelineTitle.setText("Clear");
            binding.tvAddTimelineTitle.setOnClickListener((view) -> {
                dialogStartDay.set(-1);
                dialogStartMonth.set(-1);
                dialogStartYear.set(-1);
                dialogEndDay.set(-1);
                dialogEndMonth.set(-1);
                dialogEndYear.set(-1);
                binding.tvTimelineValue.setText("");
                binding.tvAddTimelineTitle.setText("Add time");
                binding.tvAddTimelineTitle.setOnClickListener(null);
            });
        }

        binding.tvTimelineItemTitle.setText(title);
        binding.btnClosePopup.setOnClickListener((view) -> dialog.dismiss());

        binding.btnSaveTimelineItem.setOnClickListener(view -> {
            Dialog loadingDialog = DialogUtils.GetLoadingDialog(ProjectActivity.this);
            loadingDialog.show();

            // TODO: The function expects no problems or exceptions, should not update the item if the call failed
            itemModel.setStartYear(dialogStartYear.get());
            itemModel.setStartMonth(dialogStartMonth.get());
            itemModel.setStartDay(dialogStartDay.get());
            itemModel.setEndYear(dialogEndYear.get());
            itemModel.setEndMonth(dialogEndMonth.get());
            itemModel.setEndDay(dialogEndDay.get());

            boardViewModel.updateACell(itemModel).enqueue(new Callback<Void>() {
                @Override
                public void onResponse(@NonNull Call<Void> call, @NonNull Response<Void> response) {
                    if (response.isSuccessful()) {
                        boardAdapter.changeCellItem(columnPos, rowPos, itemModel);
                    } else {
                        ToastUtils.showToastError(ProjectActivity.this, "Unable to save the cell", Toast.LENGTH_LONG);
                    }
                    loadingDialog.dismiss();
                    dialog.dismiss();
                }

                @Override
                public void onFailure(@NonNull Call<Void> call, @NonNull Throwable t) {
                    ToastUtils.showToastError(ProjectActivity.this, "Unable to save the cell", Toast.LENGTH_LONG);
                    loadingDialog.dismiss();
                    dialog.dismiss();
                }
            });
        });

        binding.addTimeContainer.setOnClickListener((view) -> {
            MaterialDatePicker<Pair<Long, Long>> materialDatePicker = MaterialDatePicker.Builder.dateRangePicker()
                .setSelection(Pair.create(
                        CustomUtils.getTimeInMillis(dialogStartDay.get(), dialogStartMonth.get(), dialogStartYear.get()),
                        CustomUtils.getTimeInMillis(dialogEndDay.get(), dialogEndMonth.get(), dialogEndYear.get())
                ))
                .build();

            materialDatePicker.addOnPositiveButtonClickListener(selection -> {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    String startDate = Instant
                            .ofEpochMilli(selection.first)
                            .atZone(ZoneId.systemDefault())
                            .format(DateTimeFormatter.ofPattern("dd-MM-yyyy"));
                    String[] startData = startDate.split("-");
                    dialogStartDay.set(Integer.parseInt(startData[0]));
                    dialogStartMonth.set(Integer.parseInt(startData[1]));
                    dialogStartYear.set(Integer.parseInt(startData[2]));

                    String endDate = Instant
                            .ofEpochMilli(selection.second)
                            .atZone(ZoneId.systemDefault())
                            .format(DateTimeFormatter.ofPattern("dd-MM-yyyy"));
                    String[] endData = endDate.split("-");
                    dialogEndDay.set(Integer.parseInt(endData[0]));
                    dialogEndMonth.set(Integer.parseInt(endData[1]));
                    dialogEndYear.set(Integer.parseInt(endData[2]));
                } else {
                    Calendar calendar = Calendar.getInstance();
                    calendar.setTimeInMillis(selection.first);
                    dialogStartDay.set(calendar.get(Calendar.DAY_OF_MONTH));
                    dialogStartMonth.set(calendar.get(Calendar.MONTH));
                    dialogStartYear.set(calendar.get(Calendar.YEAR));

                    calendar.setTimeInMillis(selection.second);
                    dialogEndDay.set(calendar.get(Calendar.DAY_OF_MONTH));
                    dialogEndMonth.set(calendar.get(Calendar.MONTH));
                    dialogEndYear.set(calendar.get(Calendar.YEAR));
                }

                String finalContent = "";
                if (dialogStartDay.get() != -1 && dialogStartMonth.get() != -1 && dialogStartYear.get() != -1) {
                    if (dialogStartDay.get() == dialogEndDay.get() && dialogStartMonth.get() == dialogStartMonth.get() && dialogStartYear.get() == dialogEndYear.get()) {

                    } else if (dialogStartMonth.get() == dialogStartMonth.get() && dialogStartYear.get() == dialogEndYear.get())
                        finalContent += String.format(Locale.US, "%d", dialogStartDay.get());
                    else if (dialogStartYear.get() == dialogEndYear.get()) {
                        finalContent += String.format(Locale.US, "%s %d", CustomUtils.convertIntToMonth(dialogStartMonth.get()), dialogStartDay.get());
                    } else finalContent += String.format(Locale.US, "%s %d, %d", CustomUtils.convertIntToMonth(dialogStartMonth.get()), dialogStartDay.get(), dialogStartYear.get());

                    if (finalContent.isEmpty())
                        finalContent += String.format(Locale.US, "%s %d, %d", CustomUtils.convertIntToMonth(dialogStartMonth.get()), dialogEndDay.get(), dialogEndYear.get());
                    else finalContent += String.format(Locale.US, " - %s %d, %d", CustomUtils.convertIntToMonth(dialogStartMonth.get()), dialogEndDay.get(), dialogEndYear.get());
                }

                binding.tvTimelineValue.setText(finalContent);
                binding.tvAddTimelineTitle.setText("Clear");
                binding.tvAddTimelineTitle.setOnClickListener((lolView) -> {
                    dialogStartDay.set(-1);
                    dialogStartMonth.set(-1);
                    dialogStartYear.set(-1);
                    dialogEndDay.set(-1);
                    dialogEndMonth.set(-1);
                    dialogEndYear.set(-1);
                    binding.tvTimelineValue.setText("");
                    binding.tvAddTimelineTitle.setText("Add time");
                    binding.tvAddTimelineTitle.setOnClickListener(null);
                });
            });

            materialDatePicker.show(getSupportFragmentManager(), "I DONT KNOW WHAT THIS IS");
        });

        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.getWindow().getAttributes().windowAnimations = R.style.PopupAnimationBottom;
        dialog.getWindow().setGravity(Gravity.BOTTOM);
        dialog.show();
    }

    private void showDateItemPopup(@NonNull BoardDateItemModel itemModel, String title, int columnPos, int rowPos) {
        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        BoardDateItemPopupBinding binding = BoardDateItemPopupBinding.inflate(getLayoutInflater());
        dialog.setContentView(binding.getRoot());

        final AtomicInteger dialogYear = new AtomicInteger(itemModel.getYear());
        final AtomicInteger dialogMonth = new AtomicInteger(itemModel.getMonth());
        final AtomicInteger dialogDay = new AtomicInteger(itemModel.getDay());
        final AtomicInteger dialogHour = new AtomicInteger(itemModel.getHour());
        final AtomicInteger dialogMinute = new AtomicInteger(itemModel.getMinute());

        if (!itemModel.getDate().isEmpty()) {
            binding.tvDateValue.setText(itemModel.getDate());
            binding.tvAddDateTitle.setText("Clear date");
            binding.tvAddDateTitle.setOnClickListener((view) -> {
                dialogYear.set(-1);
                dialogMonth.set(-1);
                dialogDay.set(-1);
                binding.tvDateValue.setText("");
                binding.tvAddDateTitle.setText("Add date");
                binding.tvAddDateTitle.setOnClickListener(null);
            });
        }
        if (!itemModel.getTime().isEmpty()) {
            binding.tvTimeValue.setText(itemModel.getTime());
            binding.tvAddTimeTitle.setText("Clear time");
            binding.tvAddTimeTitle.setOnClickListener((view) -> {
                dialogHour.set(-1);
                dialogMinute.set(-1);
                binding.tvTimeValue.setText("");
                binding.tvAddTimeTitle.setText("Add time");
                binding.tvAddTimeTitle.setOnClickListener(null);
            });
        }

        binding.tvDateItemTitle.setText(title);
        binding.btnClosePopup.setOnClickListener((view) -> dialog.dismiss());
        binding.btnSaveDateItem.setOnClickListener(view -> {
            itemModel.setYear(dialogYear.get());
            itemModel.setMonth(dialogMonth.get());
            itemModel.setDay(dialogDay.get());
            itemModel.setHour(dialogHour.get());
            itemModel.setMinute(dialogMinute.get());

            Dialog loadingDialog = DialogUtils.GetLoadingDialog(ProjectActivity.this);
            loadingDialog.show();
            Call<Void> cellUpdateCall = boardViewModel.updateACell(itemModel);
            cellUpdateCall.enqueue(new Callback<Void>() {
                @Override
                public void onResponse(@NonNull Call<Void> call, @NonNull Response<Void> response) {
                    if (response.isSuccessful()) {
                        boardAdapter.changeCellItem(columnPos, rowPos, itemModel);
                    } else {
                        ToastUtils.showToastError(ProjectActivity.this, "Unable to update the cell", Toast.LENGTH_LONG);
                    }

                    loadingDialog.dismiss();
                    dialog.dismiss();
                }

                @Override
                public void onFailure(@NonNull Call<Void> call, @NonNull Throwable t) {
                    ToastUtils.showToastError(ProjectActivity.this, "Unable to update the cell", Toast.LENGTH_LONG);
                    loadingDialog.dismiss();
                    dialog.dismiss();
                }
            });

        });

        binding.dateItemDateContainer.setOnClickListener((view) -> {
            Calendar calendar = Calendar.getInstance();
            DatePickerDialog datePickerDialog = new DatePickerDialog(
                    ProjectActivity.this,
                    null,
                    dialogYear.get() == -1 ? calendar.get(Calendar.YEAR) : dialogYear.get(),
                    dialogMonth.get() == -1 ? calendar.get(Calendar.MONTH) : dialogMonth.get(),
                    dialogDay.get() == - 1 ? calendar.get(Calendar.DAY_OF_MONTH) : dialogDay.get()
            );
            datePickerDialog.setOnDateSetListener((datePicker, newYear, newMonth, newDay) -> {
                dialogYear.set(newYear);
                dialogMonth.set(newMonth);
                dialogDay.set(newDay);
                binding.tvDateValue.setText(String.format(Locale.US,"%s %d, %d", CustomUtils.convertIntToMonth(dialogMonth.get()), dialogDay.get(), dialogYear.get()));
                binding.tvAddDateTitle.setText("Clear date");
                binding.tvAddDateTitle.setOnClickListener((titleView) -> {
                    dialogYear.set(-1);
                    dialogMonth.set(-1);
                    dialogDay.set(-1);
                    binding.tvDateValue.setText("");
                    binding.tvAddDateTitle.setText("Add date");
                    binding.tvAddDateTitle.setOnClickListener(null);
                });
            });
            datePickerDialog.show();
        });

        binding.dateItemTimeContainer.setOnClickListener((view) -> {
            Calendar calendar = Calendar.getInstance();
            new TimePickerDialog(ProjectActivity.this,
                    (timePicker, newHour, newMinute) -> {
                        dialogHour.set(newHour);
                        dialogMinute.set(newMinute);
                        binding.tvTimeValue.setText(String.format(Locale.US, "%02d:%02d", dialogHour.get(), dialogMinute.get()));
                        binding.tvAddTimeTitle.setText("Clear time");
                        binding.tvAddTimeTitle.setOnClickListener((titleView) -> {
                            dialogHour.set(-1);
                            dialogMinute.set(-1);
                            binding.tvTimeValue.setText("");
                            binding.tvAddTimeTitle.setText("Add time");
                            binding.tvAddTimeTitle.setOnClickListener(null);
                        });
                    },
                    dialogHour.get() == -1 ? calendar.get(Calendar.HOUR_OF_DAY) : dialogHour.get(),
                    dialogMinute.get() == - 1 ? calendar.get(Calendar.MINUTE) : dialogMinute.get(),
                    true
            ).show();
        });

        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.getWindow().getAttributes().windowAnimations = R.style.PopupAnimationBottom;
        dialog.getWindow().setGravity(Gravity.BOTTOM);
        dialog.show();
    }

    private void showAddBoardItemPopup() {
        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        BoardAddItemPopupBinding binding = BoardAddItemPopupBinding.inflate(getLayoutInflater());
        dialog.setContentView(binding.getRoot());

        binding.btnClosePopup.setOnClickListener((view) -> dialog.dismiss());

        binding.btnAddTextItem.setOnClickListener((view) -> {
            boardViewModel.createNewColumn(BoardColumnHeaderModel.ColumnType.Text);
        });

        binding.btnAddUserItem.setOnClickListener((view) -> {
            boardViewModel.createNewColumn(BoardColumnHeaderModel.ColumnType.User);
        });

        binding.btnAddStatusItem.setOnClickListener((view) -> {
            boardViewModel.createNewColumn((BoardColumnHeaderModel.ColumnType.Status));
        });

        binding.btnAddNumberItem.setOnClickListener((view -> {
            boardViewModel.createNewColumn(BoardColumnHeaderModel.ColumnType.Number);
        }));

        binding.btnAddUpdateItem.setOnClickListener((view -> {
            boardViewModel.createNewColumn(BoardColumnHeaderModel.ColumnType.Update);
        }));

        binding.btnAddCheckboxItem.setOnClickListener((view -> {
            boardViewModel.createNewColumn(BoardColumnHeaderModel.ColumnType.Checkbox);
        }));

        binding.btnAddDateItem.setOnClickListener((view) -> {
            boardViewModel.createNewColumn(BoardColumnHeaderModel.ColumnType.Date);
        });

        binding.btnAddTimelineItem.setOnClickListener((view) -> {
            boardViewModel.createNewColumn(BoardColumnHeaderModel.ColumnType.TimeLine);
        });

        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.getWindow().getAttributes().windowAnimations = R.style.PopupAnimationBottom;
        dialog.getWindow().setGravity(Gravity.BOTTOM);
        dialog.show();
    }

    private void saveRecentAccessProject(String projectId)
    {
        if(projectId == null) return;
        userViewModel.saveRecentProjectId(projectId, new UserViewModel.DefaultCallback() {
            @Override
            public void onSuccess() {
                //just save recent access and do no thing when success
            }

            @Override
            public void onFailure(String message) {
                ToastUtils.showToastError(ProjectActivity.this, message, Toast.LENGTH_SHORT);
            }
        });
    }
}