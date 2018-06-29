package com.example.vedantiladda.quiz;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
//import android.support.design.widget.FloatingActionButton;
//import android.support.design.widget.Snackbar;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
//import android.util.Log;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.vedantiladda.quiz.dto.ContestDTO;
import com.example.vedantiladda.quiz.login.LoginActivity;
//import com.example.vedantiladda.quiz.model.StaticContest;

import java.util.ArrayList;
import java.util.List;

import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class Navigation_Activity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, Navigation_Activity_Adapter.Navigation_Activity_communicator, Navigation_Activity_Adapter_1.Navigation_Activity_communicator_1 {

    private RecyclerView recyclerView;
    private RecyclerView recyclerView1;
    private RecyclerView.Adapter adapter;
    private RecyclerView.Adapter adapter1;
    private RecyclerView.LayoutManager layoutManager;
    private RecyclerView.LayoutManager layoutManager1;
    private List<ContestDTO> contestDTOS = new ArrayList<>();
    private List<ContestDTO> contestDTOS1 = new ArrayList<>();

    TextView nav_header_nam, nav_header_emal;
    ImageView nav_header_imag;
    Button c1, c2, c3;
    String url = "http://10.177.2.201:8080";
    String contesttype;
    String contesttype1;
    TextView dynamic_contest_name;


    OkHttpClient client = new OkHttpClient.Builder().build();
    final Retrofit retrofit = new Retrofit.Builder()
            .baseUrl(url)
            .addConverterFactory(GsonConverterFactory.create())
            .client(client)
            .build();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_navigation_);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        /*FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });*/

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        recyclerView = (RecyclerView) findViewById(R.id.contestRecycler);
        recyclerView1 = (RecyclerView) findViewById(R.id.dynamicRecycler);

        contesttype = "static";
        contesttype1 = "dynamic";
        recyclerView.setHasFixedSize(true);
        recyclerView1.setHasFixedSize(true);

        layoutManager = new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false);
        layoutManager1 = new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView1.setLayoutManager(layoutManager1);
        adapter = new Navigation_Activity_Adapter(contestDTOS,Navigation_Activity.this);
        adapter1 = new Navigation_Activity_Adapter_1(contestDTOS1, (Navigation_Activity_Adapter_1.Navigation_Activity_communicator_1) Navigation_Activity.this);
        recyclerView.setAdapter(adapter);
        recyclerView1.setAdapter(adapter1);

        GetStaticContest(contesttype);
//        ContestDTO staticContest1 = new ContestDTO();
//        staticContest1.setContestName("ha ha ha ha");
//        ContestDTO staticContest2 = new ContestDTO();
//        staticContest2.setContestName("ha ha ha ha ha");
//        contestDTOS.add(staticContest1);
//        contestDTOS.add(staticContest2);

        GetDynamicContest(contesttype1);

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        String username = "Pratik";
        String mail = "pratik.biswas@coviam.com";



        View header = navigationView.getHeaderView(0);
        nav_header_nam = (TextView) header.findViewById(R.id.username);
        nav_header_emal = (TextView) header.findViewById(R.id.mail);
        nav_header_imag = (ImageView) header.findViewById(R.id.pic);

        final String TextViewValue = nav_header_nam.getText().toString();
        final String TextViewValue1 = nav_header_emal.getText().toString();

        final     SharedPreferences sharedPreferences = getSharedPreferences("user", Context.MODE_PRIVATE);
        nav_header_nam.setText(sharedPreferences.getString("userName",TextViewValue));
        nav_header_emal.setText(sharedPreferences.getString("Email",TextViewValue1));

       // dynamic_contest_name = (TextView) findViewById(R.id.dynamicContestName);





//        nav_header_nam.setText(username);
//        nav_header_emal.setText(mail);
        nav_header_imag.setImageResource(R.drawable.man);



    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.navigation_, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_crawl) {
            // Handle the camera action
            Intent intent = new Intent(Navigation_Activity.this,CrawlActivity.class);
            startActivity(intent);
        } else if (id == R.id.nav_contest) {
            Intent intent = new Intent(Navigation_Activity.this,ContestCreationActivity.class);
            startActivity(intent);

        } else if (id == R.id.nav_screen) {
            Intent intent = new Intent(Navigation_Activity.this,ContentScreeningActivity.class);
            startActivity(intent);

        } else if (id == R.id.nav_leaderboard) {
            Intent intent = new Intent(this, LeaderboardActivity.class);
            startActivity(intent);
            //finish();

        } else if (id == R.id.nav_report) {

        }else if (id == R.id.nav_set_rules) {
            Intent intent = new Intent(this, ContestRulesActivity.class);
            startActivity(intent);

        }else if (id == R.id.nav_logout) {
            SharedPreferences sharedPreferences = getSharedPreferences("user", Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.remove("userId");
            editor.remove("userName");
            editor.remove("Role");

            editor.commit();

            Toast.makeText(getApplicationContext(),"Succesfully logged out!",Toast.LENGTH_SHORT).show();

            //Put the updated launcher activity here
            Intent intent = new Intent(Navigation_Activity.this,LoginActivity.class);
            startActivity(intent);


        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onClickTextView(String name) {

    }

    public void GetDynamicContest(String contesttype1){
        IApiCall iApiCall = retrofit.create(IApiCall.class);
        final Call<List<ContestDTO>> getAllCall = iApiCall.getAllDynamicContests(contesttype1);
        getAllCall.enqueue(new Callback<List<ContestDTO>>() {
            @Override
            public void onResponse(Call<List<ContestDTO>> call, Response<List<ContestDTO>> response) {
                contestDTOS1.clear();
                contestDTOS1.addAll(response.body());
                adapter1.notifyDataSetChanged();


                Toast.makeText(Navigation_Activity.this, "received", Toast.LENGTH_LONG).show();


            }

            @Override
            public void onFailure(Call<List<ContestDTO>> call, Throwable t) {
                Log.d("API",t.getMessage());
                Toast.makeText(Navigation_Activity.this, "failed", Toast.LENGTH_LONG).show();
            }
        });
    }



    public void GetStaticContest(String contesttype){
        final IApiCall iApiCall = retrofit.create(IApiCall.class);
        final Call<List<ContestDTO>> getAllCall = iApiCall.getAllStaticContests(contesttype);
        getAllCall.enqueue(new Callback<List<ContestDTO>>() {
            @Override
            public void onResponse(Call<List<ContestDTO>> call, Response<List<ContestDTO>> response) {
                contestDTOS.clear();
                contestDTOS.addAll(response.body());
                adapter.notifyDataSetChanged();

                Toast.makeText(Navigation_Activity.this, "received", Toast.LENGTH_LONG).show();


            }

            @Override
            public void onFailure(Call<List<ContestDTO>> call, Throwable t) {
                Log.d("API",t.getMessage());
                Toast.makeText(Navigation_Activity.this, "failed", Toast.LENGTH_LONG).show();
            }
        });
    }
}
