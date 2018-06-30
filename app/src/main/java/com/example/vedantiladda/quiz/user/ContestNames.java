package com.example.vedantiladda.quiz.user;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.widget.Toast;

import com.example.vedantiladda.quiz.R;
import com.example.vedantiladda.quiz.adapter.RVContestNameAdapter;
import com.example.vedantiladda.quiz.dto.CategoryDTO;
import com.example.vedantiladda.quiz.dto.ContestDTO;

import java.util.ArrayList;
import java.util.List;

import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ContestNames extends AppCompatActivity implements RVContestNameAdapter.ContestADapterCommunicator{

    private Intent intent;
    private Retrofit retrofit;
    private OkHttpClient client;
    private RecyclerView recyclerView;
    private RVContestNameAdapter rvContestNameAdapter;
    private List<ContestDTO> contestDTOS = new ArrayList<>();
    private CategoryDTO categoryDTO;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contest_names);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        recyclerView = findViewById(R.id.rv_contest_names);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        rvContestNameAdapter = new RVContestNameAdapter(contestDTOS, this);
        recyclerView.setAdapter(rvContestNameAdapter);

        categoryDTO = (CategoryDTO)getIntent().getSerializableExtra("categoryDTO");

        Toast.makeText(ContestNames.this, categoryDTO.getCategoryName(),Toast.LENGTH_SHORT).show();

        displayAllContests(categoryDTO);
    }


    void displayAllContests(CategoryDTO categoryDTO){
        client = new OkHttpClient.Builder().build();

        retrofit = new Retrofit.Builder()
                .baseUrl(getString(R.string.base_url_contest))
                .addConverterFactory(GsonConverterFactory.create())
                .client(client)
                .build();

        UserApiCall contestApiCall = retrofit.create(UserApiCall.class);
        Call<List<ContestDTO>> contestsByCategory = contestApiCall.getContestsByCategory(categoryDTO.getCategoryId());
        contestsByCategory.enqueue(new Callback<List<ContestDTO>>() {
            @Override
            public void onResponse(Call<List<ContestDTO>> call, Response<List<ContestDTO>> response) {
                contestDTOS.clear();
                contestDTOS.addAll(response.body());
                rvContestNameAdapter.notifyDataSetChanged();
                Toast.makeText(ContestNames.this,"API CALLED",Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(Call<List<ContestDTO>> call, Throwable t) {
                Toast.makeText(ContestNames.this,"API FAILED",Toast.LENGTH_SHORT).show();

            }
        });


    }


    @Override
    public void onItemClick(ContestDTO contestDTO) {
        Intent intent = new Intent(ContestNames.this, GameActivity.class);
        intent.putExtra("contestDTO", contestDTO);
        startActivity(intent);

    }
}
