package com.example.vedantiladda.quiz.user;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.vedantiladda.quiz.R;
import com.example.vedantiladda.quiz.adapter.RVContestNameAdapter;
import com.example.vedantiladda.quiz.adapter.RVContestsAdapter;
import com.example.vedantiladda.quiz.dto.CategoryDTO;
import com.example.vedantiladda.quiz.dto.ContestDTO;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class UserDynamicContests extends Fragment implements RVContestNameAdapter.ContestADapterCommunicator{

    private Retrofit retrofit;
    private OkHttpClient client;
    private RecyclerView recyclerView;
    private RVContestNameAdapter rvContestNameAdapter;
    private List<ContestDTO> contestDTOSList = new ArrayList<>();


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_dynamic_contests, container, false);
        recyclerView = view.findViewById(R.id.rv_dynamic_list);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        rvContestNameAdapter = new RVContestNameAdapter(contestDTOSList, this);
        recyclerView.setAdapter(rvContestNameAdapter);

        getAllDynamicContests();
        return view;
    }


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getActivity().setTitle(getString(R.string.title_activity_dynamic_name));
    }

    void getAllDynamicContests(){

        client = new OkHttpClient.Builder().build();

        retrofit = new Retrofit.Builder()
                .baseUrl(getString(R.string.base_url_contest))
                .addConverterFactory(GsonConverterFactory.create())
                .client(client)
                .build();

        UserApiCall userApiCall = retrofit.create(UserApiCall.class);
        Call<List<ContestDTO>> getAllDynamicContest = userApiCall.getDynamicContests();

        getAllDynamicContest.enqueue(new Callback<List<ContestDTO>>() {
            @Override
            public void onResponse(Call<List<ContestDTO>> call, Response<List<ContestDTO>> response) {
             contestDTOSList.clear();
             contestDTOSList.addAll(response.body());
             rvContestNameAdapter.notifyDataSetChanged();
             Log.i("DynamicContests", response.body().toString());
            }

            @Override
            public void onFailure(Call<List<ContestDTO>> call, Throwable t) {
                Log.i("DynamicContests", t.getMessage().toString());

            }
        });


    }


    @Override
    public void onItemClick(ContestDTO contestDTO) {
        Intent intent = new Intent(getActivity(), DynamicGame.class);
        intent.putExtra("contestDTO", (Serializable) contestDTO);
        startActivity(intent);
    }
}
