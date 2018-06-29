package com.example.vedantiladda.quiz;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.vedantiladda.quiz.dto.ContestDTO;
//import com.example.vedantiladda.quiz.model.StaticContest;
import com.example.vedantiladda.quiz.dto.ContestwiseDTO;
import com.example.vedantiladda.quiz.dto.OverallDTO;
import com.example.vedantiladda.quiz.dto.UserDTO;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class LeaderboardActivity extends AppCompatActivity implements LeaderboardAdapter.LeaderboardCommunicator,LeaderboardAdapter1.LeaderboardCommunicator1 {
    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.Adapter mAdapter1;
    private RecyclerView.LayoutManager mLayoutManager;
    private static String url = "http://10.177.2.201:8080";
    private List<UserDTO> userDTOS = new ArrayList<>();
    private List<ContestDTO> contestDTOS = new ArrayList<>();
    private List<String> staticContestList = new ArrayList<>();
    private List<OverallDTO> overallDTOS = new ArrayList<>();
    private List<ContestwiseDTO> contestwiseDTOS = new ArrayList<>();
    final Map<String, String> staticContestMap= new HashMap<String,String>();
    String popUpContents[];
    Button contestListButton;
    String contest;
    OkHttpClient client = new OkHttpClient.Builder().build();
    final Retrofit retrofit = new Retrofit.Builder()
            .baseUrl(url)
            .addConverterFactory(GsonConverterFactory.create())
            .client(client)
            .build();


    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_leaderboard);




        contestListButton = findViewById(R.id.contest_list_button);
        initContestListButton();
        GetAllContest();
        int i = 0;
        popUpContents = new String[staticContestList.size()];
        staticContestList.toArray(popUpContents);
        mRecyclerView = (RecyclerView) findViewById(R.id.leaderboardRecycler);
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);


        mAdapter = new LeaderboardAdapter(overallDTOS, LeaderboardActivity.this);
        mAdapter1 = new LeaderboardAdapter1(contestwiseDTOS, LeaderboardActivity.this);



       // mAdapter.notifyDataSetChanged();
        //mAdapter1.notifyDataSetChanged();

//        UserDTO userDTO1 = new UserDTO();
//        UserDTO userDTO2 = new UserDTO();
//        userDTO1.setUserName("username1");
//        userDTO2.setUserName("username2");
//        userDTOS.add(userDTO1);
//        userDTOS.add(userDTO2);





//        final ImageView Back = (ImageView) findViewById(R.id.back);
//        Back.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Intent i = new Intent(LeaderboardActivity.this,Navigation_Activity.class);
//                startActivity(i);
//                finish();
//
//            }
//        });


    }
    private void initContestListButton()
    {
        final PopupWindow popupWindowContests = popupWindowContests();


        // button on click listener

        View.OnClickListener handler = new View.OnClickListener() {
            public void onClick(View v) {

                switch (v.getId()) {

                    case R.id.contest_list_button:
                        // show the list view as dropdown
                        popupWindowContests.showAsDropDown(v, -5, 0);
                        break;
                }
            }
        };

        contestListButton.setOnClickListener(handler);
    }




    public PopupWindow popupWindowContests() {

        // initialize a pop up window type
        PopupWindow popupWindow = new PopupWindow(this);

        // the drop down list is a list view
        ListView listViewContest = new ListView(this);

        // set our adapter and pass our pop up window contents
        listViewContest.setAdapter(contestListAdapter(popUpContents));

        // set the item click listener
        listViewContest.setOnItemClickListener(new CustomOnClickListner());

        // some other visual settings
        popupWindow.setFocusable(true);
        popupWindow.setWidth(WindowManager.LayoutParams.MATCH_PARENT);
        popupWindow.setHeight(WindowManager.LayoutParams.WRAP_CONTENT);

        // set the list view as pop up window content
        popupWindow.setContentView(listViewContest);

        return popupWindow;
    }
    private ArrayAdapter<String> contestListAdapter(String popUpContents[]) {

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1,staticContestList) {

            @Override
            public View getView(int position, View convertView, ViewGroup parent) {

                TextView listItem = new TextView(LeaderboardActivity.this);
                String item = getItem(position);
                listItem.setText(item);
                listItem.setTextSize(22);
                listItem.setPadding(10, 10, 10, 10);
                listItem.setTextColor(Color.WHITE);

                return listItem;
            }
        };
        return adapter;
    }
    public void GetContestWiseLeaderboard(String contestId){

        IApiCall iApiCall = retrofit.create(IApiCall.class);
        final Call<List<ContestwiseDTO>> getContestWiseCall = iApiCall.getContestwisePoints(contestId);
        getContestWiseCall.enqueue(new Callback<List<ContestwiseDTO>>() {
            @Override
            public void onResponse(Call<List<ContestwiseDTO>> call, Response<List<ContestwiseDTO>> response) {
                mRecyclerView.setAdapter(mAdapter1);
                contestwiseDTOS.clear();
                contestwiseDTOS.addAll(response.body());
                mAdapter1.notifyDataSetChanged();
                Toast.makeText(LeaderboardActivity.this, "received", Toast.LENGTH_LONG).show();
            }

            @Override
            public void onFailure(Call<List<ContestwiseDTO>> call, Throwable t) {
                Toast.makeText(LeaderboardActivity.this, "failed", Toast.LENGTH_LONG).show();

            }

        });
    }
    public void GetOverallLeaderboard(){


         IApiCall iApiCall = retrofit.create(IApiCall.class);
        final Call<List<OverallDTO>> getAllCall = iApiCall.getOverAll();
        getAllCall.enqueue(new Callback<List<OverallDTO>>() {
            @Override
            public void onResponse(Call<List<OverallDTO>> call, Response<List<OverallDTO>> response) {
                mRecyclerView.setAdapter(mAdapter);
                overallDTOS.clear();
                overallDTOS.addAll(response.body());
                mAdapter.notifyDataSetChanged();
                Toast.makeText(LeaderboardActivity.this, "received", Toast.LENGTH_LONG).show();
            }

            @Override
            public void onFailure(Call<List<OverallDTO>> call, Throwable t) {
                Toast.makeText(LeaderboardActivity.this, "failed", Toast.LENGTH_LONG).show();

            }

        });
    }

    public List<String> GetAllContest(){
        final IApiCall iApiCall = retrofit.create(IApiCall.class);
        final Call<List<ContestDTO>> getAllCall = iApiCall.getAllCompleted();
        getAllCall.enqueue(new Callback<List<ContestDTO>>() {
            @Override
            public void onResponse(Call<List<ContestDTO>> call, Response<List<ContestDTO>> response) {
                contestDTOS.clear();
                contestDTOS.addAll(response.body());
                for(ContestDTO contestDTO:contestDTOS)
                {
                    staticContestList.add(contestDTO.getContestName());
                    staticContestMap.put(contestDTO.getContestName(),contestDTO.getContestId());
                }
                staticContestList.add("Overall");
                //staticContestList1.add("0");

                Toast.makeText(LeaderboardActivity.this, "received", Toast.LENGTH_LONG).show();


            }

            @Override
            public void onFailure(Call<List<ContestDTO>> call, Throwable t) {
                Log.d("API",t.getMessage());
                Toast.makeText(LeaderboardActivity.this, "failed", Toast.LENGTH_LONG).show();
                staticContestList.add("Test Element");
            }
        });
        return staticContestList;
    }

    public class CustomOnClickListner implements AdapterView.OnItemClickListener {


        @Override
        public void onItemClick(AdapterView<?> arg0, View v, int arg2, long arg3) {

            // get the context and main activity to access variables
            contestListButton = findViewById(R.id.contest_list_button);
            Context mContext = v.getContext();
            LeaderboardActivity lActivity = ((LeaderboardActivity) mContext);

            // add some animation when a list item was clicked
            Animation fadeInAnimation = AnimationUtils.loadAnimation(v.getContext(), android.R.anim.fade_in);
            fadeInAnimation.setDuration(10);
            v.startAnimation(fadeInAnimation);

            // dismiss the pop up
            lActivity.popupWindowContests().dismiss();

            // get the text and set it as the button text
            String selectedItemText = ((TextView) v).getText().toString();
            lActivity.contestListButton.setText(selectedItemText);
            if(contestListButton.getText().equals("Overall"))
            {
                GetOverallLeaderboard();
            }
            else
            {
                GetContestWiseLeaderboard(staticContestMap.get(contestListButton.getText().toString()));
            }


        }

    }


    @Override
    public void onClickTextView(String id) {

    }
}
