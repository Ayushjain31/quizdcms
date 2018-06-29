package com.example.vedantiladda.quiz;


import com.example.vedantiladda.quiz.dto.ContestDTO;
//import com.example.vedantiladda.quiz.model.StaticContest;
import com.example.vedantiladda.quiz.dto.ContestQuestionDTO;
import com.example.vedantiladda.quiz.dto.ContestwiseDTO;
import com.example.vedantiladda.quiz.dto.OverallDTO;
import com.example.vedantiladda.quiz.dto.ScreenDTO;
import com.example.vedantiladda.quiz.dto.UrlDTO;
import com.example.vedantiladda.quiz.dto.UserDTO;

import com.example.vedantiladda.quiz.dto.Category;
import com.example.vedantiladda.quiz.dto.Contest;
import com.example.vedantiladda.quiz.dto.ContestRulesDTO;
import com.example.vedantiladda.quiz.dto.QuestionDTO;

import java.util.List;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface IApiCall {
    @GET("question/approveAll")
    Call<Boolean>saveEverything();

    @POST("crawler/deleteDataForUrl")
    Call<Boolean> deleteDataForUrl(@Body UrlDTO urlDTO1);

    @POST("/contestQuestion/addQuestions")
    Call<Boolean> addQuestions(@Body List<ContestQuestionDTO> contestQuestionDTOList);

    @GET("/contest/getContestsByType/{contestType}")
    Call<List<ContestDTO>> getAllStaticContests(@Path("contestType") String contestType);

    @GET("/contest/getAll")
    Call<List<ContestDTO>> getAllOverall();

    @GET("/contest/getAllCompleted")
    Call<List<ContestDTO>> getAllCompleted();

    @GET("/contest/getContestsByType/{contestType}")
    Call<List<ContestDTO>> getAllDynamicContests(@Path("contestType") String contestType);

    @GET("/userPoints/contest/getOverAllLeaderBoard")
    Call<List<OverallDTO>> getOverAll();

    @GET("/userPoints/contest/getContestWiseLeaderBoard/{contestId}")
    Call<List<ContestwiseDTO>> getContestwisePoints(@Path("contestId") String contestId);

    @POST("/contest/createContest")
    Call<ContestDTO> addContest(@Body Contest contest);

    @GET("category/getAll")
    Call<List<Category>> getCategories();
    @GET("question/getByDifficulty/{difficulty}/{pageNumber}")
    Call<List<QuestionDTO>>getAllQuestions(@Path("difficulty") String difficulty, @Path("pageNumber") Integer pageNumber);

    @GET("question/getAll")
    Call<List<QuestionDTO>>getAll();

    @GET("question/getAllByStatus/{status}/{pageNumber}")
    Call<List<QuestionDTO>>getAllPending(@Path("status") String status, @Path("pageNumber") Integer pageNumber);

    @POST("question/saveAll/{List<QuestionDTO>}")
    Call<Boolean>saveAll(@Body List<QuestionDTO> questionList);

    @GET("question/getByCategoryIdAndDifficulty/{categoryId}/{difficulty}/{pageNumber}")
    Call<List<QuestionDTO>>getAllStatic(@Path("difficulty") String difficulty,@Path("categoryId") String categoryId,
                                        @Path("pageNumber") Integer pageNumber);

    @GET("contest/getRules")
    Call<ContestRulesDTO>getRules();

    @GET("question/category/getAll")
    Call<List<Category>> getall();

    @POST("/crawler/crawl/")
    Call<Boolean> crawl(@Body UrlDTO urlDTO);

    @POST("question/saveScreened/")
    Call<Boolean>saveScreened(@Body List<ScreenDTO> screenDTOList);

    @POST("/contest/setRules")
    Call<ContestRulesDTO> addRules(@Body ContestRulesDTO contestRulesDTO);

}
