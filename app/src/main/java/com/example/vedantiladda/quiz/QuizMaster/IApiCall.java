package com.example.vedantiladda.quiz.QuizMaster;

import com.example.vedantiladda.quiz.dto.ContestDTO;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

interface IApiCall {
    @GET("getContestsByType/dynamic")
     Call<List<ContestDTO>> getDynamicContest();
    @GET("getContestQuestions/{contestId}/{userId}")
     Call<ContestDTO> getQuestionbyContest(@Path("contestId") String contestId, @Path("userId") String userId);
    @GET("contestQuestion/pushQuestion/{contestQuestionId}")
    Call<Boolean> postQuestion(@Path("contestQuestionId") String contestQuestionId);
    @GET("/contestQuestion/endContest/{contestId}")
    Call<Boolean> endContest(@Path("contestId") String contestId);
}
