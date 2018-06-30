package com.example.vedantiladda.quiz.QuizMaster;

import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.vedantiladda.quiz.R;
import com.example.vedantiladda.quiz.dto.ContestDTO;
import com.example.vedantiladda.quiz.dto.ContestQuestionDTO;
import com.example.vedantiladda.quiz.dto.QuestionDTO;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class QuizMaterContestActivity extends AppCompatActivity implements Quiz_mater_question_recycler_view_adapter.IPostsAdapterCommunicatorQuestionQuizMaster{
    private RecyclerView questionRecyclerView;

    private List<ContestQuestionDTO> contestQuestionDTOList=new ArrayList<>();
     //  private List<ContestQuestionCopy> contestQuestionCopyList=new ArrayList<>();
    //Collections.copy(contestQue)
    //private List<QuestionDTO> questionDTOList=new ArrayList<>();
    //private String questionId;
    //private String questionText;
    private QuestionDTO questionDTO=new QuestionDTO();
    private ContestDTO contestDTO=new ContestDTO();
    private Intent intent;
    private Button endContest;
    private OkHttpClient client,postClient,endClient;
    private Retrofit questionRetrofit,postRetrofit,endRetrofit;
    private  String contestId;
    private Quiz_mater_question_recycler_view_adapter adapter;
    public String userId="testUserId";
    TextView contestName;
    DialogsUtils dialogsUtils;

    //public Boolean Valid;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
//        public ProgressDialog progressDialog = new ProgressDialog(QuizMaterContestActivity.this,
//                R.style.AppTheme_Dark_Dialog);
        LocalBroadcastManager.getInstance(this).registerReceiver(mMessageReceiver,
                new IntentFilter("custom-event-name"));
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz_mater_contest);
        endContest=(Button)findViewById(R.id.end_contest_id);
        intent=getIntent();
        contestId=intent.getStringExtra("contestId");
        client=new OkHttpClient.Builder().build();
        endClient=new OkHttpClient.Builder().build();
        questionRetrofit=new  Retrofit.Builder().baseUrl("http://10.177.2.201:8080/contest/")
                .addConverterFactory(GsonConverterFactory.create()).client(client).build();
        endRetrofit=new Retrofit.Builder().baseUrl("http://10.177.2.201:8080/contest/")
                .addConverterFactory(GsonConverterFactory.create()).client(endClient).build();
        contestName=(TextView)findViewById(R.id.contestName_id);

        dialogsUtils = new DialogsUtils(this);

        questionRecyclerView=(RecyclerView)findViewById(R.id.question_recycler_view_id);
        adapter=new Quiz_mater_question_recycler_view_adapter(contestQuestionDTOList,getApplication(),QuizMaterContestActivity.this);
        questionRecyclerView.setAdapter(adapter);
        questionRecyclerView.setLayoutManager(new LinearLayoutManager(QuizMaterContestActivity.this));




        final IApiCall iApiCall=questionRetrofit.create(IApiCall.class);
        Call<ContestDTO> getAllCall=iApiCall.getQuestionbyContest(contestId,userId);
        Log.i("contestId",contestId);
        getAllCall.enqueue(new Callback<ContestDTO>() {
            @Override
            public void onResponse(Call<ContestDTO> call, Response<ContestDTO> response) {
              //  contestDTO=response.body();
                contestQuestionDTOList.clear();
                contestQuestionDTOList.addAll(response.body().getContestQuestionDTOList());
                Log.i("TAG",contestQuestionDTOList.toString());
                adapter.notifyDataSetChanged();
                contestName.setText(response.body().getContestName());
                Toast.makeText(QuizMaterContestActivity.this, "Got list of Question Successfully" , Toast.LENGTH_LONG).show();

            }

            @Override
            public void onFailure(Call<ContestDTO> call, Throwable t) {
               Toast.makeText(QuizMaterContestActivity.this, "on failure" , Toast.LENGTH_LONG).show();

            }
        });


        endContest.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View view) {
         IApiCall iApiCall=endRetrofit.create(IApiCall.class);
         Call<Boolean> getAllCall=iApiCall.endContest(contestId);
         getAllCall.enqueue(new Callback<Boolean>() {
             @Override
             public void onResponse(Call<Boolean> call, Response<Boolean> response) {

             }

             @Override
             public void onFailure(Call<Boolean> call, Throwable t) {

             }
         });

        }
    });
    }

//    @Override
//    public void itemClick(String questionId) {
//
//    }


    @Override
    public void itemClick(String contestQuestionId) {

        dialogsUtils.showProgressDialog();


        //diolog box method
//        progressDialog.setIndeterminate(true);
//        progressDialog.setMessage("Wait.....");
//        progressDialog.show();
        //
//TODO change part to dip
        postClient=new OkHttpClient.Builder().build();
        postRetrofit= questionRetrofit=new  Retrofit.Builder().baseUrl("http://10.177.1.245:8080/")
                .addConverterFactory(GsonConverterFactory.create()).client(client).build();
        IApiCall iApiCall=postRetrofit.create(IApiCall.class);
        Call<Boolean> getAllCall=iApiCall.postQuestion(contestQuestionId);
        getAllCall.enqueue(new Callback<Boolean>() {
            @Override
            public void onResponse(Call<Boolean> call, Response<Boolean> response) {
                Toast.makeText(QuizMaterContestActivity.this, "Pushed Question Successfully", Toast.LENGTH_LONG).show();

//           intent Method
//                if(response.equals(true)) {
//
//
//
//
//
//                    Intent intent = new Intent(QuizMaterContestActivity.this, WaitActivity.class);
//                    startActivity(intent);
//                }

//

            }

            @Override
            public void onFailure(Call<Boolean> call, Throwable t) {
                Toast.makeText(QuizMaterContestActivity.this, "Not Pushed", Toast.LENGTH_LONG).show();
                Log.i("push",t.getMessage());
            }
        });


    }
    private BroadcastReceiver mMessageReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            // Get extra data included in the Intent
            String message = intent.getStringExtra("message");
            dialogsUtils.dismissDialogBox();
            Log.d("receiver", "Got message: " + message);

        }
    };

//    @Override
//    public void dismissDiologBox() {
//        dialogsUtils.dismissDialogBox();
//        // progressDialog.dismiss();
//    }
}
