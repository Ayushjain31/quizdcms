package com.example.vedantiladda.quiz.QuizMaster;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.WindowInsets;
import android.widget.Button;
import android.widget.Toast;

import com.example.vedantiladda.quiz.R;
import com.example.vedantiladda.quiz.dto.ContestDTO;
import com.example.vedantiladda.quiz.login.LoginActivity;
import com.example.vedantiladda.quiz.user.UserMain;
import com.google.firebase.FirebaseApp;
import com.google.firebase.messaging.FirebaseMessaging;

import java.util.ArrayList;
import java.util.List;

import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class QuizMasterActivity extends AppCompatActivity implements QuizMaster_recycler_view_adapter.IPostsAdapterCommunicatorDynamicContests {
    private RecyclerView qmRecyclerView;
    private QuizMaster_recycler_view_adapter qmadapter;
    private Retrofit qmretrofit;
    private OkHttpClient qmclient;
    private List<ContestDTO> contestDTOList = new ArrayList<>();
    private Button logout;
    // private String dynamic;
    // private String static;
    // private Intent intent;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz_master);
        // FirebaseApp.initializeApp(getApplicationContext());
        FirebaseApp.initializeApp(this);


        qmclient = new OkHttpClient.Builder().build();
        qmretrofit = new Retrofit.Builder().baseUrl("http://10.177.2.201:8080/contest/")
                .addConverterFactory(GsonConverterFactory.create()).client(qmclient).build();
        qmRecyclerView = (RecyclerView) findViewById(R.id.dynamic_contests_recyclerView_id);
        qmadapter = new QuizMaster_recycler_view_adapter(contestDTOList, getApplication(), QuizMasterActivity.this);
        qmRecyclerView.setLayoutManager(new LinearLayoutManager(QuizMasterActivity.this));
        qmRecyclerView.setAdapter(qmadapter);

        logout = findViewById(R.id.qm_logout);
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharedPreferences sharedPreferences = getSharedPreferences("user", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.remove("userName");
                editor.remove("Role");
                editor.commit();

                FirebaseMessaging.getInstance().subscribeToTopic("quizMaster");

                Intent intent = new Intent(QuizMasterActivity.this,LoginActivity.class);
                startActivity(intent);
                finish();

            }
        });


        IApiCall iApiCall = qmretrofit.create(IApiCall.class);
        Call<List<ContestDTO>> getAllCall = iApiCall.getDynamicContest();
        getAllCall.enqueue(new Callback<List<ContestDTO>>() {
            @Override
            public void onResponse(Call<List<ContestDTO>> call, Response<List<ContestDTO>> response) {
                try {
                    //contestDTOList.clear();
                    contestDTOList.addAll(response.body());
                    qmadapter.notifyDataSetChanged();
                } catch (Exception e) {
                    Log.e("API", e.getMessage().toString());
                }
                Toast.makeText(QuizMasterActivity.this, "Got list of Contests Successfully", Toast.LENGTH_LONG).show();
            }

            @Override
            public void onFailure(Call<List<ContestDTO>> call, Throwable t) {
                Log.i("API", t.getMessage().toString());
                //Toast.makeText(QuizMasterActivity.this, "On Failure 1" , Toast.LENGTH_LONG).show();
            }
        });

    }


    @Override
    public void itemClick(String contestId) {
        //TODO: Change Topic to quizMaster
        FirebaseMessaging.getInstance().subscribeToTopic("quizMaster");
        Intent intent = new Intent(this, QuizMaterContestActivity.class);
        intent.putExtra("contestId", contestId);
        startActivity(intent);
    }
}
