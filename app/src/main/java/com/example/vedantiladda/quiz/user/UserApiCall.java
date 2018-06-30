package com.example.vedantiladda.quiz.user;

import com.example.vedantiladda.quiz.dto.CategoryDTO;
import com.example.vedantiladda.quiz.dto.ContestDTO;
import com.example.vedantiladda.quiz.dto.UserAnswerDTO;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface UserApiCall {

    @GET("category/getAll")
    Call<List<CategoryDTO>> getCategoryForContest();


    @GET("contest/getContestsByCategory/{categoryId}")
    Call<List<ContestDTO>> getContestsByCategory(@Path("categoryId")String categoryId);

    @GET("contest/getContestQuestions/{contestId}/{userId}")
    Call<ContestDTO> getQuestionsByContest(@Path("contestId")String contestId, @Path("userId")String userId);

    @GET("contest/getContestsByType/dynamic")
    Call<List<ContestDTO>> getDynamicContests();

    @POST("userAnswers/saveAnswer")
    Call<Boolean> saveAnswer(@Body UserAnswerDTO userAnswerDTO);

    @GET("contest/getContestPoints/{contestId}/{userId}")
    Call<Boolean> dynamicLeaderboard(@Path("contestId")String contestId, @Path("userId")String userId);

    @GET("contest/getContestPoints/{contestId}/{userId}")
    Call<Boolean> callLeaderboard(@Path("contestId")String contestId, @Path("userId")String userId);

}
