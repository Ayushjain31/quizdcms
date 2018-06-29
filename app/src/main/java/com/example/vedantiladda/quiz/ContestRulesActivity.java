package com.example.vedantiladda.quiz;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.vedantiladda.quiz.R;
//import com.example.vedantiladda.quiz.Rules.RulesPojo.ContestRulesDTO;
import com.example.vedantiladda.quiz.dto.ContestRulesDTO;

import java.util.ArrayList;
import java.util.List;

import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ContestRulesActivity extends AppCompatActivity {
    EditText numberOfQuestions;
    EditText numberOfTextQuestions;
    EditText numberOfImageQuestions;
    EditText numberOfAudioQuestions;
    EditText numberOfVideoQuestions;
    EditText numberOfEasyQuestions;
    EditText numberOfMediumQuestions;
    EditText numberOfDifficultQuestions;
    private ContestRulesDTO contestRulesDTO=new ContestRulesDTO();
    Button saveRules;
    private Retrofit retrofit;
    private  OkHttpClient client= new OkHttpClient.Builder().build();
    // private List<ContestRulesDTO> ruleList=new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contest_rules);
        numberOfQuestions=(EditText)findViewById(R.id.noq_id);
        numberOfTextQuestions=(EditText)findViewById(R.id.notq_id);
        numberOfImageQuestions=(EditText)findViewById(R.id.noiq_id);
        numberOfAudioQuestions=(EditText)findViewById(R.id.noaq_id);
        numberOfVideoQuestions=(EditText)findViewById(R.id.novq_id);
        numberOfEasyQuestions=(EditText)findViewById(R.id.noeq_id);
        numberOfMediumQuestions=(EditText)findViewById(R.id.nomq_id);
        numberOfDifficultQuestions=(EditText)findViewById(R.id.nodq_id);
        retrofit = new Retrofit.Builder().baseUrl("http://10.177.2.201:8080").addConverterFactory(GsonConverterFactory.create()).client(client).build();
        IApiCall iApiCall=retrofit.create(IApiCall.class);
        Call<ContestRulesDTO> getAllCall=iApiCall.getRules();
        getAllCall.enqueue(new Callback<ContestRulesDTO>() {
            @Override
            public void onResponse(Call<ContestRulesDTO> call, Response<ContestRulesDTO>response) {
                contestRulesDTO=response.body();
                Toast.makeText(ContestRulesActivity.this, "Rules  Successfully" , Toast.LENGTH_LONG).show();
                Log.i("API",String.valueOf(contestRulesDTO));
                numberOfQuestions.setText(String.valueOf(contestRulesDTO.getNumQuestions()));
                numberOfTextQuestions.setText(String.valueOf(contestRulesDTO.getNumTextQ()));
                numberOfImageQuestions.setText(String.valueOf(contestRulesDTO.getNumImageQ()));
                numberOfAudioQuestions.setText(String.valueOf(contestRulesDTO.getNumAudioQ()));
                numberOfVideoQuestions.setText(String.valueOf(contestRulesDTO.getNumVideoQ()));
                numberOfEasyQuestions.setText(String.valueOf(contestRulesDTO.getNumEasyQ()));
                numberOfMediumQuestions.setText(String.valueOf(contestRulesDTO.getNumMediumQ()));
                numberOfDifficultQuestions.setText(String.valueOf(contestRulesDTO.getNumHardQ()));

            }

            @Override
            public void onFailure(Call<ContestRulesDTO> call, Throwable t) {
                Log.i("API",t.getMessage());
            }
        });

        saveRules=(Button) findViewById(R.id.saveRules_id);
        saveRules.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                save();
            }
        });
    }
    public void save(){
//        if(!Validate()){
//            onSaveFailed();
//            return;
//        }
        saveRules.setEnabled(false);
        final Integer noQuestions=Integer.parseInt(numberOfQuestions.getText().toString());
        final Integer noTextQuestions=Integer.parseInt(numberOfTextQuestions.getText().toString());
        final Integer noImageQuestions=Integer.parseInt(numberOfImageQuestions.getText().toString());
        final Integer noAudioQuestions=Integer.parseInt(numberOfAudioQuestions.getText().toString());
        final Integer noVideoQuestions=Integer.parseInt(numberOfVideoQuestions.getText().toString());
        final Integer noEasyQuestions=Integer.parseInt(numberOfEasyQuestions.getText().toString());
        final Integer noMediumQuestions=Integer.parseInt(numberOfMediumQuestions.getText().toString());
        final Integer noDifficultQuestions=Integer.parseInt(numberOfDifficultQuestions.getText().toString());
        // retrofit = new Retrofit.Builder().baseUrl("http://10.177.1.85:8090").addConverterFactory(GsonConverterFactory.create()).client(client).build();

        ContestRulesDTO contestRulesDTO =new ContestRulesDTO();
        contestRulesDTO.setNumQuestions(noQuestions);
        contestRulesDTO.setNumTextQ(noTextQuestions);
        contestRulesDTO.setNumImageQ(noImageQuestions);
        contestRulesDTO.setNumAudioQ(noAudioQuestions);
        contestRulesDTO.setNumVideoQ(noVideoQuestions);
        contestRulesDTO.setNumEasyQ(noEasyQuestions);
        contestRulesDTO.setNumMediumQ(noMediumQuestions);
        contestRulesDTO.setNumHardQ(noDifficultQuestions);

        //Intent intent=new Intent(getApplicationContext(), LoginActivity.class);
        IApiCall iApiCall=retrofit.create(IApiCall.class);
        Call<ContestRulesDTO> getAllCall=iApiCall.addRules(contestRulesDTO);
        getAllCall.enqueue(new Callback<ContestRulesDTO>() {
            @Override
            public void onResponse(Call<ContestRulesDTO> call, Response<ContestRulesDTO>response) {
                onSaveSuccess();
            }

            @Override
            public void onFailure(Call<ContestRulesDTO> call, Throwable t) {
                onSaveFailed();
            }
        });



    }
    public void onSaveSuccess(){
        saveRules.setEnabled(true);
        Toast.makeText(getBaseContext(), "Rules Saved", Toast.LENGTH_LONG).show();
        finish();

    }
    public void onSaveFailed(){
        Toast.makeText(getBaseContext(), "Save failed", Toast.LENGTH_LONG).show();
        saveRules.setEnabled(true);
    }
    public Boolean Validate(){
        boolean valid =true;
        Integer noQuestions=Integer.parseInt(numberOfQuestions.getText().toString());
        Integer noTextQuestions=Integer.parseInt(numberOfTextQuestions.getText().toString());
        Integer noImageQuestions=Integer.parseInt(numberOfImageQuestions.getText().toString());
        Integer noAudioQuestions=Integer.parseInt(numberOfAudioQuestions.getText().toString());
        Integer noVideoQuestions=Integer.parseInt(numberOfVideoQuestions.getText().toString());
        Integer noEasyQuestions=Integer.parseInt(numberOfEasyQuestions.getText().toString());
        Integer noMediumQuestions=Integer.parseInt(numberOfMediumQuestions.getText().toString());
        Integer noDifficultQuestions=Integer.parseInt(numberOfDifficultQuestions.getText().toString());
        if(noQuestions<10){
            numberOfQuestions.setError("at least 10 questions");
            valid=false;
        }
        if(noTextQuestions<noQuestions/5){
            numberOfTextQuestions.setError("more text questions required");
            valid=false;
        }

        if(noImageQuestions<noQuestions/5){
            numberOfImageQuestions.setError("more Image questions required");
            valid=false;
        }
        if(noAudioQuestions<noQuestions/5){
            numberOfAudioQuestions.setError("more audio questions required");
            valid=false;
        }

        if(noVideoQuestions<noQuestions/5){
            numberOfVideoQuestions.setError("more video questions required");
            valid=false;
        }
        if(noEasyQuestions<noQuestions/4){
            numberOfEasyQuestions.setError("more easy questions required");
            valid=false;
        }

        if(noMediumQuestions<noQuestions/4){
            numberOfMediumQuestions.setError("more medium questions required");
            valid=false;
        }

        if(noDifficultQuestions<noQuestions/4){
            numberOfDifficultQuestions.setError("more difficult questions required");
            valid=false;
        }

        if(noTextQuestions+noImageQuestions+noVideoQuestions+noAudioQuestions!=noQuestions||noEasyQuestions+noMediumQuestions+noDifficultQuestions!=noQuestions){
            saveRules.setError("all question do not add up to total number of questions");
        }
        return valid;
    }
}
