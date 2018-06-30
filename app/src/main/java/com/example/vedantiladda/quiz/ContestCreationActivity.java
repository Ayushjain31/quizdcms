package com.example.vedantiladda.quiz;


import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.vedantiladda.quiz.dto.Category;
import com.example.vedantiladda.quiz.dto.Contest;
import com.example.vedantiladda.quiz.dto.ContestRulesDTO;

import java.io.Serializable;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import java.util.Map;

public class ContestCreationActivity extends AppCompatActivity {
    private EditText contestName,startTime,duration,bonus;
    private Spinner contestTypeSpinner,selectCategorySpinner;
    private Button createButton;
    private Retrofit retrofit;
    private OkHttpClient client;
    final Contest contest = new Contest();
    final List<String> categoriesList = new ArrayList<>();
    final Map<String ,String> categoryListMap = new HashMap<>();
    String AdminEmailId ;
    int numberOfQuestions;
    ArrayAdapter<String>  spinnerArrayAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.create_contest_layout);
        addListnerToStartTimeET();
        getCategortries();
        addListenerOnButton();
        addListnerOnContestTypeItemSelection();
        onFocusChangeListners();


    }

    private int getNumberOfQuestions() {
        client =  new OkHttpClient.Builder().build();
        retrofit = new Retrofit.Builder().
                baseUrl(getString(R.string.get_rules_dto))
                .addConverterFactory(GsonConverterFactory.create())
                .client(client)
                .build();

        IApiCall iApiCall = retrofit.create(IApiCall.class);
        Call<ContestRulesDTO> getCategoriesCall = iApiCall.getRules();
        getCategoriesCall.enqueue(new Callback<ContestRulesDTO>() {
            @Override

            public void onResponse(Call<ContestRulesDTO> call, Response<ContestRulesDTO> response) {
                numberOfQuestions = response.body().getNumQuestions();
                            }

            @Override
            public void onFailure(Call<ContestRulesDTO> call, Throwable t) {

            }
        });
        return numberOfQuestions;

    }

    private void onFocusChangeListners() {
        duration = findViewById(R.id.duration_et);
        duration.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    hideKeyboard(v);
                }
            }
        });
        bonus = findViewById(R.id.bonus_et);
        bonus.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    hideKeyboard(v);
                }
            }
        });

    }

    public void hideKeyboard(View view) {
        InputMethodManager inputMethodManager =(InputMethodManager)getSystemService(AppCompatActivity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    private void addListenerOnCategorySelectSpinner() {
        selectCategorySpinner = (Spinner) findViewById(R.id.select_category_spinner);
        String[] empty = new String[]{
                "Select"
        };
        final List<String> finalCategoryList = new ArrayList<>(Arrays.asList(empty));

        spinnerArrayAdapter = new ArrayAdapter<String>(
                this,R.layout.support_simple_spinner_dropdown_item,finalCategoryList);

        spinnerArrayAdapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
        selectCategorySpinner.setAdapter(spinnerArrayAdapter);
        for(String category:categoriesList)
        {
            finalCategoryList.add(category);
        }
        spinnerArrayAdapter.notifyDataSetChanged();
        selectCategorySpinner.setOnItemSelectedListener(new CustomOnItemSelectListener2());


    }


    public void addListnerOnContestTypeItemSelection()
    {
        contestTypeSpinner = (Spinner) findViewById(R.id.contest_type_spinner);
        // Log.i("CONTESTCREATION", contestTypeSpinner.getSelectedItem().toString());
        contestTypeSpinner.setOnItemSelectedListener(new CustomOnItemSelectListener());

    }

    public List<String> getCategortries()
    {
        final List<Category> categories = new ArrayList<>();

        client =  new OkHttpClient.Builder().build();
        retrofit = new Retrofit.Builder().
                baseUrl(getString(R.string.get_categories_base_url))
                .addConverterFactory(GsonConverterFactory.create())
                .client(client)
                .build();

        IApiCall iApiCall = retrofit.create(IApiCall.class);
        Call<List<Category>> getCategoriesCall = iApiCall.getCategories();
        getCategoriesCall.enqueue(new Callback<List<Category>>() {
            @Override
            public void onResponse(Call<List<Category>> call, Response<List<Category>> response) {
                Toast.makeText(ContestCreationActivity.this,"Success",Toast.LENGTH_LONG).show();
                categories.addAll(response.body());
                for(Category category : categories)
                {
                    categoriesList.add(category.getCategoryName());
                    categoryListMap.put(category.getCategoryName(),category.getCategoryId());
                }
                addListenerOnCategorySelectSpinner();
            }

            @Override
            public void onFailure(Call<List<Category>> call, Throwable t) {
                Toast.makeText(ContestCreationActivity.this,"Fail",Toast.LENGTH_LONG).show();
                categoriesList.add("Nothing to load::-1");

            }
        });

        return categoriesList;
    }

    public void  addListnerToStartTimeET()
    {   final EditText startTime = findViewById(R.id.start_time_et);
        startTime.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                Calendar mcurrentTime = Calendar.getInstance();
                int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
                int minute = mcurrentTime.get(Calendar.MINUTE);
                TimePickerDialog mTimePicker;
                mTimePicker = new TimePickerDialog(ContestCreationActivity.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                        startTime.setText( selectedHour + ":" + selectedMinute);
                    }
                }, hour, minute, true);
                mTimePicker.setTitle("Select Time");
                mTimePicker.show();

            }
        });
    }
    public Contest intiContest() throws ParseException {   final  SharedPreferences sharedPreferences = getSharedPreferences("user", Context.MODE_PRIVATE);
        contestTypeSpinner = (Spinner) findViewById(R.id.contest_type_spinner);
        contestName = findViewById(R.id.contest_name_et);
        startTime = findViewById(R.id.start_time_et);
        duration = findViewById(R.id.duration_et);
        bonus = findViewById(R.id.bonus_et);
        contest.setCategoryName(selectCategorySpinner.getSelectedItem().toString());
        contest.setCategoryId(categoryListMap.get(selectCategorySpinner.getSelectedItem().toString()));
        contest.setAdminId(sharedPreferences.getString("userName","Admin"));
        contest.setBonus(Integer.parseInt(bonus.getText().toString()));
        contest.setContestType(contestTypeSpinner.getSelectedItem().toString());
        contest.setContestName(contestName.getText().toString());
        contest.setEmail(sharedPreferences.getString("Email","Admin@gmail.com"));
        SimpleDateFormat dateFormat = new SimpleDateFormat(
                "yyyy-MM-dd hh:mm:ss:SSS");

        Date date = new Date();
        String str = dateFormat.format(date).split(" ")[0].replaceAll("/","-") + " " +startTime.getText().toString()+":00";

        Date parsedTimeStamp = dateFormat.parse(str+":000");

        Timestamp timestamp = new Timestamp(parsedTimeStamp.getTime());
        contest.setStartDate(new Timestamp(timestamp.getTime()));
        Timestamp endTime = new Timestamp(Timestamp.valueOf(str).getTime()+Integer.parseInt(duration.getText().toString())*60*1000);
        contest.setEndDate(endTime);
        contest.setQuestionVisibilityDuration(21);
        contest.setNumberOfQuestions(getNumberOfQuestions());
        return contest;
    }
    public void addListenerOnButton() {

        contestTypeSpinner = (Spinner) findViewById(R.id.contest_type_spinner);
        contestName = findViewById(R.id.contest_name_et);
        startTime = findViewById(R.id.start_time_et);
        duration = findViewById(R.id.duration_et);
        bonus = findViewById(R.id.bonus_et);
        createButton = (Button) findViewById(R.id.create_button);
        createButton.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onClick(View v) {



                if((contestName.getText().toString().length()!=0)&&(bonus.getText().toString().length()!=0)&&(startTime.getText().toString().length()!=0)&&(!contestTypeSpinner.getSelectedItem().toString().equals("Select")))
                {

                    Intent intent = new Intent(ContestCreationActivity.this,QuestionBankActivity.class);
                    intent.putExtra("ContestType",contestTypeSpinner.getSelectedItem().toString());
                    intent.putExtra("Contest_CategoryId",categoryListMap.get(selectCategorySpinner.getSelectedItem().toString()));
                    try {
                        intent.putExtra("Contest",(Serializable) intiContest());
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                    startActivity(intent);

                }
                else
                {
                    Toast.makeText(ContestCreationActivity.this,"Required Fields Missing",Toast.LENGTH_LONG).show();
                }
            }



        });
    }

    public class CustomOnItemSelectListener implements AdapterView.OnItemSelectedListener {

        public void onItemSelected(AdapterView<?> parent, View view, int pos,long id) {
            selectCategorySpinner = findViewById(R.id.select_category_spinner);
            if(contestTypeSpinner.getSelectedItem().toString().equals("static"))
            {
                selectCategorySpinner.setVisibility(View.VISIBLE);
            }
            else selectCategorySpinner.setVisibility(View.INVISIBLE);
        }

        @Override
        public void onNothingSelected(AdapterView<?> arg0) {
            // TODO Auto-generated method stub
        }



    }


    public class CustomOnItemSelectListener2 implements AdapterView.OnItemSelectedListener {

        public void onItemSelected(AdapterView<?> parent, View view, int pos,long id) {

        }

        @Override
        public void onNothingSelected(AdapterView<?> arg0) {
            // TODO Auto-generated method stub
        }


    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(this,Navigation_Activity.class);
        startActivity(intent);
    }
}