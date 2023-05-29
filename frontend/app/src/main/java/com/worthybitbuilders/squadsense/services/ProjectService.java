package com.worthybitbuilders.squadsense.services;

import com.worthybitbuilders.squadsense.models.MinimizedProjectModel;
import com.worthybitbuilders.squadsense.models.board_models.BoardContentModel;
import com.worthybitbuilders.squadsense.models.board_models.ProjectModel;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface ProjectService {
    @GET("{userId}/project")
    Call<List<MinimizedProjectModel>> getAllProject(@Path("userId") String userId);

    @GET("{userId}/project/{projectId}")
    Call<ProjectModel> getProjectById(@Path("userId") String userId, @Path("projectId") String projectId);

    @POST("{userId}/project")
    Call<ProjectModel> saveProject(@Path("userId") String userId, @Body ProjectModel projectModel);

    /**
     * Send a request and get a new empty board from server
     */
    @POST("{userId}/project/{projectId}/board")
    Call<BoardContentModel> createAndGetNewBoardToProject(@Path("userId") String userId, @Path("projectId") String projectId);

//    @POST("{userId}/project/{projectId}/board/{boardId}/column")
//    Call<BoardContentModel> addNewBoardToProject(@Path("userId") String userId, @Path("projectId") String projectId, @Body BoardContentModel boardContentModel);
//
//    @POST("{userId}/project/{projectId}/board/{boardId}/row")
//    Call<BoardContentModel> addNewBoardToProject(@Path("userId") String userId, @Path("projectId") String projectId, @Body BoardContentModel boardContentModel);
}
