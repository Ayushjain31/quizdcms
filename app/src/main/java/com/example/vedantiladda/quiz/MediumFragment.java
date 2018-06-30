package com.example.vedantiladda.quiz;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.Toast;

import com.example.vedantiladda.quiz.IApiCall;
import com.example.vedantiladda.quiz.PaginationAdapter;
import com.example.vedantiladda.quiz.R;
import com.example.vedantiladda.quiz.dto.QuestionDTO;
import com.example.vedantiladda.quiz.utils.EndlessRecyclerViewScrollListener;


import java.util.ArrayList;
import java.util.List;

import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MediumFragment extends Fragment implements PaginationAdapter.Communicator {

    List<QuestionDTO> selectedQuestionDTOS = new ArrayList<>();
    PaginationAdapter adapter;
    LinearLayoutManager linearLayoutManager;
    RecyclerView rv;
    private boolean isLoading = false;
    private boolean isLastPage = false;
    private List<QuestionDTO> questionDTOList = new ArrayList<>();
        OkHttpClient client = new OkHttpClient.Builder().build();
    final Retrofit retrofit = new Retrofit.Builder()
            .baseUrl(" http://10.177.2.201:8081/")
            .addConverterFactory(GsonConverterFactory.create())
            .client(client)
            .build();
    int i=2;
    String category;
    String contestType;
    public interface MediumQuestions {
        void onMediumDataPass(List<QuestionDTO> selectedQuestionDTOS);
    }

    MediumQuestions mediumQuestions;


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mediumQuestions = (MediumQuestions) context;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        selectedQuestionDTOS.clear();
        Intent contest = getActivity().getIntent();
        contestType = contest.getStringExtra("ContestType");
        category = contest.getStringExtra("Contest_CategoryId");
        View view =  inflater.inflate(R.layout.medium_tab, container, false);
        rv = view.findViewById(R.id.mediumRecycler);
        adapter = new PaginationAdapter(questionDTOList, this);
        linearLayoutManager = new LinearLayoutManager(getActivity());
        rv.setLayoutManager(linearLayoutManager);
        rv.setAdapter(adapter);
        loadFirstPage();
        rv.addOnScrollListener(new EndlessRecyclerViewScrollListener() {
            @Override
            public void onLoadMore() {
                loadNextPage();
            }
        });


        return view;


    }

    @Override
    public void onClickCheckBox(String id) {
        Boolean flag = true;
        for(QuestionDTO question: selectedQuestionDTOS){
            if(question.getQuestionId().equals(id)){
                flag = false;
            }
        }

        for(QuestionDTO questionDTO : questionDTOList) {
            if(questionDTO.getQuestionId().equals(id) && questionDTO.getChecked()&&flag)
                selectedQuestionDTOS.add(questionDTO);
            if(questionDTO.getQuestionId().equals(id) && !questionDTO.getChecked()) {
                selectedQuestionDTOS.remove(questionDTO);
                Log.d("REMOVE", selectedQuestionDTOS.toString());
            }

        }



        mediumQuestions.onMediumDataPass(selectedQuestionDTOS);
    }

    private void loadFirstPage() {


        IApiCall iApiCall = retrofit.create(IApiCall.class);
        Call<List<QuestionDTO>> getAllCall = iApiCall.getAllQuestions("medium", 1);
        if(contestType.equals("static")){
            getAllCall = iApiCall.getAllStatic("medium", category, 1);
        }

        getAllCall.enqueue(new Callback<List<QuestionDTO>>() {
            @Override
            public void onResponse(Call<List<QuestionDTO>> call, Response<List<QuestionDTO>> response) {

                if(response.body().size()>0) {
                    questionDTOList.addAll(response.body());
                    adapter.notifyDataSetChanged();
                }
                else{
                    Toast.makeText(getActivity(), "no questions", Toast.LENGTH_LONG).show();
                }

            }

            @Override
            public void onFailure(Call<List<QuestionDTO>> call, Throwable t) {
                Toast.makeText(getActivity(), "failed", Toast.LENGTH_LONG).show();
            }
        });
    }



    private void loadNextPage() {




        IApiCall iApiCall = retrofit.create(IApiCall.class);
        Call<List<QuestionDTO>> getAllCall = iApiCall.getAllQuestions("medium", i);
        if(contestType.equals("static")){
            getAllCall = iApiCall.getAllStatic("medium", category, i);
        }

        getAllCall.enqueue(new Callback<List<QuestionDTO>>() {
            @Override
            public void onResponse(Call<List<QuestionDTO>> call, Response<List<QuestionDTO>> response) {

                isLoading = false;

                if(response.body().size()>0) {
                    questionDTOList.addAll(response.body());
                    adapter.notifyDataSetChanged();
                    i++;
                }
                else{
                    Toast.makeText(getActivity(), "no questions", Toast.LENGTH_LONG).show();
                }


            }

            @Override
            public void onFailure(Call<List<QuestionDTO>> call, Throwable t) {
                Toast.makeText(getActivity(), "failed", Toast.LENGTH_LONG).show();

            }
        });
    }
}
