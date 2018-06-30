package com.example.vedantiladda.quiz;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.vedantiladda.quiz.dto.Category;
import com.example.vedantiladda.quiz.dto.UrlDTO;

import java.util.ArrayList;
import java.util.List;

import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class CrawlActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    List<String> category = new ArrayList<>();
    String[] difficulty = {"easy", "medium", "hard"};
    String[] answerType = {"single", "multi", "arrange"};
    ArrayAdapter aa1;
    private UrlDTO urlDTO;
    private UrlDTO urlDTO1;

    private List<Category> categorieList;

    OkHttpClient okHttpClient = new OkHttpClient.Builder().build();

    final Retrofit retrofit = new Retrofit.Builder().baseUrl("http://10.177.2.201:8081/")
            .addConverterFactory(GsonConverterFactory.create())
            .client(okHttpClient)
            .build();

//    final EditText urlText=findViewById(R.id.editText_url);
//    final Spinner categorySpinner=findViewById(R.id.spinner_category);
//    final Spinner difficultySpinner=findViewById(R.id.spinner_difficulty);
//    OkHttpClient okHttpClient1 = new OkHttpClient.Builder().build();
//    final Retrofit retrofit1 = new Retrofit.Builder().baseUrl("http://10.177.2.201.8081/")
//            .addConverterFactory(GsonConverterFactory.create())
//            .client(okHttpClient1)
//            .build();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crawl);
        urlDTO = new UrlDTO();


        Spinner spinner_category = (Spinner) findViewById(R.id.spinner_category);
        spinner_category.setOnItemSelectedListener(this);

        aa1 = new ArrayAdapter(this, android.R.layout.simple_spinner_item, category);

        aa1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_category.setAdapter(aa1);

        Spinner spinner_difficulty = (Spinner) findViewById(R.id.spinner_difficulty);
        spinner_difficulty.setOnItemSelectedListener(this);
        ArrayAdapter aa2 = new ArrayAdapter(this, android.R.layout.simple_spinner_item, difficulty);
        aa2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_difficulty.setAdapter(aa2);

        Spinner spinner_answerType = (Spinner) findViewById(R.id.spinner_answerType);
        spinner_difficulty.setOnItemSelectedListener(this);
        ArrayAdapter aa3 = new ArrayAdapter(this, android.R.layout.simple_spinner_item, answerType);
        aa3.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_answerType.setAdapter(aa3);


        categorieList = new ArrayList<>();

        IApiCall iApiCall = retrofit.create(IApiCall.class);

        final Call<List<Category>> getall = iApiCall.getall();

        getall.enqueue(new Callback<List<Category>>() {

            @Override
            public void onResponse(Call<List<Category>> call, Response<List<Category>> response) {

                List<String> categoryList = new ArrayList<>();

                Toast.makeText(CrawlActivity.this, "success.....", Toast.LENGTH_LONG).show();
                //System.out.println("Success...");
                //System.out.println(response.body());
                categorieList = response.body();
                if (categorieList != null && categorieList.size() > 0) {
                    for (Category category : categorieList) {
                        categoryList.add(category.getCategoryName());
                        category.getCategoryId();


                    }
                    category.addAll(categoryList);
                    aa1.notifyDataSetChanged();
                }
            }

            @Override
            public void onFailure(Call<List<Category>> call, Throwable t) {
                Toast.makeText(CrawlActivity.this, "failure.....", Toast.LENGTH_LONG).show();

                // System.out.println("Failure.....");

            }

        });


        Button back_button = findViewById(R.id.back_button);
        back_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent;
                intent = new Intent(CrawlActivity.this, Navigation_Activity.class);
                startActivity(intent);
                //finish();
            }

        });


//......
        Button request_button = findViewById(R.id.request_button);
        request_button.setOnClickListener(new View.OnClickListener() {


            OkHttpClient okHttpClient1 = new OkHttpClient.Builder().build();
            final Retrofit retrofit1 = new Retrofit.Builder().baseUrl("http://10.177.2.201:8085")
                    .addConverterFactory(GsonConverterFactory.create())
                    .client(okHttpClient1)
                    .build();

            @Override
            public void onClick(View view) {
                EditText urlText = findViewById(R.id.editText_url);
                Spinner categorySpinner = findViewById(R.id.spinner_category);
                Spinner difficultySpinner = findViewById(R.id.spinner_difficulty);
                Spinner answerTypeSpinner = findViewById(R.id.spinner_answerType);

                String url = urlText.getText().toString();
                String categoryName = categorySpinner.getSelectedItem().toString();
                String difficulty = difficultySpinner.getSelectedItem().toString();
                String answerType = answerTypeSpinner.getSelectedItem().toString();

                String categoryId = "";
                for (Category category1 : categorieList) {
                    //System.out.println("inside for");
                    if (category1.getCategoryName().equals(categoryName)) {
                        categoryId = category1.getCategoryId();
                        System.out.println("inside if");
                        //  break;
                    }
                }
                urlDTO.setUrl(url);
                urlDTO.setCategoryId(categoryId);
                urlDTO.setAnswerType(answerType);
                urlDTO.setDifficulty(difficulty);
                System.out.println(urlDTO);

                IApiCall iApiCall = retrofit1.create(IApiCall.class);

                Call<Boolean> crawl = iApiCall.crawl(urlDTO);

                crawl.enqueue(new Callback<Boolean>() {

                    @Override
                    public void onResponse(Call<Boolean> call, Response<Boolean> response) {

                        if (response.body()) {


                            Toast.makeText(CrawlActivity.this, "crawl request accepted", Toast.LENGTH_SHORT).show();
                            Intent i = new Intent(CrawlActivity.this, ContentScreeningActivity.class);
                            startActivity(i);

                        }
                        else
                        {
                            Toast.makeText(CrawlActivity.this, "website is not accepted", Toast.LENGTH_SHORT).show();
                        }
                    }
                    @Override
                    public void onFailure(Call<Boolean> call, Throwable t) {

                        Toast.makeText(CrawlActivity.this, "fail to crawl", Toast.LENGTH_SHORT).show();

                    }

                });

            }

        });

        Button delete = (Button) findViewById(R.id.delete_button);
        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                urlDTO1 = new UrlDTO();

                final Retrofit retrofit2 = new Retrofit.Builder().baseUrl("http://10.177.2.201:8085/")
                        .addConverterFactory(GsonConverterFactory.create())
                        .client(okHttpClient)
                        .build();
                EditText urlText = findViewById(R.id.editText_url);
                Spinner categorySpinner = findViewById(R.id.spinner_category);
                Spinner difficultySpinner = findViewById(R.id.spinner_difficulty);
                Spinner answerTypeSpinner = findViewById(R.id.spinner_answerType);

                String url = urlText.getText().toString();
                String category = categorySpinner.getSelectedItem().toString();
                String difficulty = difficultySpinner.getSelectedItem().toString();
                String answerType = answerTypeSpinner.getSelectedItem().toString();
                String categoryId = "";
                for (Category object : categorieList) {
                    if (object.getCategoryName().equals(category)) {
                        categoryId = object.getCategoryId();
                        break;
                    }
                }

                urlDTO1.setDifficulty(difficulty);
                urlDTO1.setAnswerType(answerType);
                urlDTO1.setUrl(url);
                urlDTO1.setCategoryId(categoryId);
                System.out.println(urlDTO1);

                IApiCall iApiCall1 = retrofit2.create(IApiCall.class);

                Call<Boolean> deleteUrl = iApiCall1.deleteDataForUrl(urlDTO1);
                deleteUrl.enqueue(new Callback<Boolean>() {
                    @Override
                    public void onResponse(Call<Boolean> call, Response<Boolean> response) {
                        if (response.body()) {
                            Toast.makeText(CrawlActivity.this, "Successfully Deleted", Toast.LENGTH_SHORT).show();
                        }
                        else
                        {
                            Toast.makeText(CrawlActivity.this, "null ", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<Boolean> call, Throwable t) {

                        Toast.makeText(CrawlActivity.this, "Fail to Delete", Toast.LENGTH_SHORT).show();


                    }
                });


            }
        });


    }


//.....

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }
}

