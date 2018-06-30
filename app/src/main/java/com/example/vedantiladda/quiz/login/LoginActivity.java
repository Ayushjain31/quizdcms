package com.example.vedantiladda.quiz.login;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
//import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;


import com.example.vedantiladda.quiz.Navigation_Activity;
import com.example.vedantiladda.quiz.QuizMaster.QuizMasterActivity;
import com.example.vedantiladda.quiz.R;
import com.example.vedantiladda.quiz.dto.UserDTO;
import com.example.vedantiladda.quiz.dto.UserLogin;
import com.example.vedantiladda.quiz.user.UserMain;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.List;

import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class LoginActivity extends AppCompatActivity {

    private Retrofit retrofit;
    private String url = "http://10.177.2.201:8082/";
    private List<String> username;

    public boolean isValidEmailAddress2(String email) {
        String ePattern = "^[a-zA-Z0-9.!#$%&'*+/=?^_`{|}~-]+@((\\[[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\])|(([a-zA-Z\\-0-9]+\\.)+[a-zA-Z]{2,}))$";
        java.util.regex.Pattern p = java.util.regex.Pattern.compile(ePattern);
        java.util.regex.Matcher m = p.matcher(email);
        return m.matches();
    }




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_activity);

        final EditText editText = findViewById(R.id.editText);
        final EditText editText9 = findViewById(R.id.editText9);




        OkHttpClient client = new OkHttpClient.Builder().build();
        Gson gson = new GsonBuilder()
                .setLenient()
                .create();
        final Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(url)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .client(client)
                .build();
        final     SharedPreferences sharedPreferences = getSharedPreferences("user", Context.MODE_PRIVATE);
        if(sharedPreferences.getString("Role","ds").equals("User")){
            Intent intent = new Intent(this,UserMain.class);
            startActivity(intent);
            finish();

        }
        else if(sharedPreferences.getString("Role","ds").equals("Admin")){
            Intent intent = new Intent(this,Navigation_Activity.class);
            startActivity(intent);
            finish();

        }else if(sharedPreferences.getString("Role","ds").equals("QuizMaster")){
            Intent intent = new Intent(this,QuizMasterActivity.class);
            startActivity(intent);
            finish();

        }

        Button login_button = findViewById(R.id.login_button);

        login_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String editTextValue = editText.getText().toString();
                String editTextValue9 = editText9.getText().toString();


                if(!isValidEmailAddress2(editText.getText().toString().trim())){
                    editText.setError("Enter a valid email id!");
                    return;
                }

                if (editText9.getText().toString().isEmpty()) {
                    editText9.setError("Required field!");
                    return;
                }
                final UserLogin userLogin = new UserLogin();
                final UserDTO userDTO = new UserDTO();


                userLogin.setUserEmail(editText.getText().toString());
                userLogin.setPassword(editText9.getText().toString());

                final PostAll login = retrofit.create(PostAll.class);
                Call<List<String>> loginCall = login.loginDetails(userLogin);

                loginCall.enqueue(new Callback<List<String>>() {
                    @Override
                    public void onResponse(Call<List<String>> call, Response<List<String>> response) {

                        if(response.body() == null){
                            Toast.makeText(getApplicationContext(), "please enter valid credentials!", Toast.LENGTH_SHORT).show();

                        }
                        else {
                            username = response.body();
                            SharedPreferences sharedPreferences = getSharedPreferences("user", Context.MODE_PRIVATE);
                            SharedPreferences.Editor editor = sharedPreferences.edit();
                            editor.putString("userId", username.get(0));
                            Log.d("userId",username.get(0));
                            editor.putString("userName", username.get(1));
                            editor.putString("Email",editTextValue );
                            editor.putString("Role",username.get(2));
                            editor.apply();

                            Toast.makeText(getApplicationContext(), "successfully logged in!", Toast.LENGTH_SHORT).show();
                            if(username.get(2).equals("Admin")){
                            Intent i = new Intent(LoginActivity.this, Navigation_Activity.class);
                            startActivity(i);
                            finish();}
                            else if(username.get(2).equals("User")){
                                Intent i = new Intent(LoginActivity.this, UserMain.class);
                                startActivity(i);
                                finish();
                            }else if(username.get(2).equals("QuizMaster")){
                                Intent i = new Intent(LoginActivity.this, QuizMasterActivity.class);
                                startActivity(i);
                                finish();
                            }

                        }

                    }

                    @Override
                    public void onFailure(Call<List<String>> call, Throwable t) {
                        Log.d("hey",call.request().body().toString());

                        Log.d("hey",t.getMessage());
                        System.out.println(t);
                        Toast.makeText(getApplicationContext(), "login failed!", Toast.LENGTH_SHORT).show();

                    }
                });

            }
        });
    }
}

