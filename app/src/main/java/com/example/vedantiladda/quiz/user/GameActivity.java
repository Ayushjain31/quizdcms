package com.example.vedantiladda.quiz.user;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaPlayer;
import android.net.Uri;
import android.nfc.Tag;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.media.session.MediaSessionCompat;
import android.support.v4.media.session.PlaybackStateCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.MediaController;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import com.example.vedantiladda.quiz.LeaderboardActivity;
import com.example.vedantiladda.quiz.LeaderboardAdapter;
import com.example.vedantiladda.quiz.R;
import com.example.vedantiladda.quiz.dto.CategoryDTO;
import com.example.vedantiladda.quiz.dto.ContestDTO;
import com.example.vedantiladda.quiz.dto.ContestQuestionDTO;
import com.example.vedantiladda.quiz.dto.QuestionDTO;
import com.example.vedantiladda.quiz.dto.UserAnswerDTO;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class GameActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = "GAMEACTIVITY";

    private TextView questionText, skipCountText, questionNumberText;
    private LinearLayout linearLayout;
    private CheckBox optionOne, optionTwo, optionThree, optionFour;
    private Button beginButton, finishButton;
    private Button replay;
    private MediaSessionCompat mMediaSession;
    private PlaybackStateCompat.Builder mStateBuilder;
    private VideoView videoView;
    private ContestDTO contestDTO;
    private List<ContestQuestionDTO> contestQuestionDTOList = new ArrayList<>();
    private ImageView imageView;
    private Uri videoUri;
    private MediaController mediaController;
    private MediaPlayer mediaPlayer;
    private FloatingActionButton nextQuestionFab, skipQuestionFab;

    private OkHttpClient client;
    private Retrofit retrofit;

    static final String MEDIA_LOG_TAG = "MediaLogTag";
    static final int MY_PERMISSIONS_REQUEST_READ_STORAGE = 1;

    private int skipCount = 0;

    private ContestQuestionDTO globalContestQuestionDTO;
    private String answerType;
    private String userId;
    private Integer points;
    private int answered,totalCount;
    private Boolean skipped;

    private String testUserId = "testUserId";

    private String contestId = "c6575890-8bc8-4edd-a443-8fd97234e496";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);
        //Toolbar toolbar = findViewById(R.id.toolbar);
       // setSupportActionBar(toolbar);
        setAllViews();
        requestPermissions();

        contestDTO = (ContestDTO)getIntent().getSerializableExtra("contestDTO");
        contestId = contestDTO.getContestId();

        //toolbar.setTitle(contestDTO.getContestName());
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle((CharSequence) contestDTO.getContestName());

        SharedPreferences sharedPreferences = getSharedPreferences("user", Context.MODE_PRIVATE);
        testUserId = sharedPreferences.getString("userId", " ");

        client = new OkHttpClient.Builder().build();
        retrofit = new Retrofit.Builder()
                .baseUrl(getString(R.string.base_url_contest))
                .addConverterFactory(GsonConverterFactory.create())
                .client(client)
                .build();

        contestQuestionDTOList = new ArrayList<>();
        contestQuestionDTOList = getAllQuestionsByContest(contestId, false);//contestDTO.getContestId());

        mMediaSession = new MediaSessionCompat(this, MEDIA_LOG_TAG);
        //Creating MediaController
        mediaController= new MediaController(this);
        mediaController.setAnchorView(videoView);

        nextQuestionFab.setOnClickListener(this);
        skipQuestionFab.setOnClickListener(this);
        beginButton.setOnClickListener(this);
        finishButton.setOnClickListener(this);

        questionNumberText.setVisibility(View.INVISIBLE);
        nextQuestionFab.setVisibility(View.INVISIBLE);
        skipQuestionFab.setVisibility(View.INVISIBLE);
    }

    void setAllViews(){
        questionText = findViewById(R.id.question_text);
        questionText.setMovementMethod(new ScrollingMovementMethod());
        skipCountText = findViewById(R.id.skip_count_text);
        questionNumberText = findViewById(R.id.question_number);


        videoView = findViewById(R.id.video);
        imageView = findViewById(R.id.image);

        nextQuestionFab = findViewById(R.id.next_question);
        skipQuestionFab = findViewById(R.id.skip_question_button);

        beginButton = findViewById(R.id.begin_button);
        finishButton = findViewById(R.id.finish_button);
        replay = findViewById(R.id.replay_button);

        linearLayout = findViewById(R.id.linear);

        optionOne = findViewById(R.id.option_A);
        optionTwo = findViewById(R.id.option_B);
        optionThree = findViewById(R.id.option_C);
        optionFour = findViewById(R.id.option_D);




    }

    private void showQuestionViews(){
        questionText.setVisibility(View.VISIBLE);
        linearLayout.setVisibility(View.VISIBLE);
        optionOne.setChecked(false);
        optionTwo.setChecked(false);
        optionThree.setChecked(false);
        optionFour.setChecked(false);
   }

    private List<ContestQuestionDTO> getAllQuestionsByContest(String contestId, final Boolean flag) {

        UserApiCall userApiCall = retrofit.create(UserApiCall.class);

        Call<ContestDTO> getAllQuestions = userApiCall.getQuestionsByContest(contestId, testUserId);
        getAllQuestions.enqueue(new Callback<ContestDTO>() {
            @Override
            public void onResponse(Call<ContestDTO> call, Response<ContestDTO> response) {
                Log.d(TAG,response.body().getContestQuestionDTOList().toString());
                if(flag) {
                    contestQuestionDTOList.clear();
                }contestQuestionDTOList = response.body().getContestQuestionDTOList();
                countSkips();
                Toast.makeText(GameActivity.this,"Retrieved questions Successfully" , Toast.LENGTH_SHORT).show();
                if(flag){
                    findEligibleQuestion();
                }
            }

            @Override
            public void onFailure(Call<ContestDTO> call, Throwable t) {
                Toast.makeText(GameActivity.this,"API call failed" , Toast.LENGTH_SHORT).show();
                Log.e(TAG, t.getMessage());
            }
        });
        return contestDTO.getContestQuestionDTOList();
    }

    private void countSkips(){
        skipCount=0;
        totalCount=0;
        answered=1;
        for(ContestQuestionDTO contestQuestionDTO : contestQuestionDTOList){
            totalCount++;
            questionNumberText.setText(String.valueOf(answered)+"/"+String.valueOf(totalCount));
            if(contestQuestionDTO.getUserAnswerDTO()!=null){
                if(contestQuestionDTO.getUserAnswerDTO().getSkipped()) {
                    skipCount++;
                    answered++;
                }

                if(contestQuestionDTO.getUserAnswerDTO().getAnswered())
                    answered++;
            }

        }
        Log.d("SKIPCOUNT", String.valueOf(skipCount));
    }


    private void displayQuestion(ContestQuestionDTO contestQuestionDTO){

        if(contestQuestionDTO == null){
      //      Log.d(TAG, "NO MORE QUESTIONS"  + contestQuestionDTO.getQuestionDTO().toString());
            Toast.makeText(GameActivity.this,"No more questions", Toast.LENGTH_SHORT).show();
            linearLayout.setVisibility(View.GONE);
            questionNumberText.setVisibility(View.INVISIBLE);
            nextQuestionFab.setVisibility(View.INVISIBLE);
            skipQuestionFab.setVisibility(View.INVISIBLE);
            finishButton.setVisibility(View.VISIBLE);
            return;
        }
        Log.d(TAG, contestQuestionDTO.getQuestionDTO().toString());
        points = contestQuestionDTO.getPoints();

        QuestionDTO questionDTO = globalContestQuestionDTO.getQuestionDTO();
        switch (questionDTO.getQuestionType()){
            case "text":
                showQuestionViews();
                answerType = questionDTO.getAnswerType();
                questionText.setText(questionDTO.getQuestionText());
                optionOne.setText("A: " + questionDTO.getOptionOne());
                optionTwo.setText("B: " + questionDTO.getOptionTwo());
                optionThree.setText("C: " +   questionDTO.getOptionThree());
                optionFour.setText("D: " + questionDTO.getOptionFour());

                break;
            case "audio":
                try {
                    playAudio(questionDTO);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                break;
            case "video":
                displayVideo(questionDTO);
                break;
            case "image":
                displayImage(questionDTO);
                break;
        }

        //check the question type....accordingly display the user the video, audio and then the question
        // when the user presses the next button, make an API call
        // return question is null then, endgame

    }

    private void findEligibleQuestion(){
        Boolean flag = true;
        for(ContestQuestionDTO contestQuestionDTO: contestQuestionDTOList){
            Log.d(TAG + "contestQLis", contestQuestionDTO.toString());
//
            if(contestQuestionDTO.getUserAnswerDTO() != null){
                if(contestQuestionDTO.getUserAnswerDTO().getAnswered()) {
                    continue;
                }
            }

            if(skipCount<=2 && contestQuestionDTO.getUserAnswerDTO() == null){
                globalContestQuestionDTO = contestQuestionDTO;
                displayQuestion(contestQuestionDTO);
                return;
            }else if(skipCount<=2 && !contestQuestionDTO.getUserAnswerDTO().getSkipped() &&
                    !contestQuestionDTO.getUserAnswerDTO().getAnswered()) {
                globalContestQuestionDTO = contestQuestionDTO;
                displayQuestion(contestQuestionDTO);
                return;
            }

//
        }
        for(ContestQuestionDTO contestQuestionDTO: contestQuestionDTOList){
            if(contestQuestionDTO.getUserAnswerDTO().getSkipped()){
                globalContestQuestionDTO = contestQuestionDTO;
                displayQuestion(contestQuestionDTO);
                return;
            }
        }
        displayQuestion(null);

    }



    private String getSuitableAnswer(){
        StringBuilder answerBuilder = new StringBuilder();
        Boolean flag=false;
        if(answerType.equals("sequence")){
            //Todo: Drag and drop
            // //return sequenceText.getText().toString().toUpperCase();
        }
        if(optionOne.isChecked()) {
            answerBuilder.append("A");
            flag=true;
        }
        if(optionTwo.isChecked()) {
            answerBuilder.append("B");
            flag=true;
        }if(optionThree.isChecked()) {
            answerBuilder.append("C");
            flag=true;
        }
        if(optionFour.isChecked()) {
            flag=true;
            answerBuilder.append("D");
        }

        if(flag)
            return answerBuilder.toString();
        else{
            return null;
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){

            case R.id.next_question:
                /* TODO: First see which all checkboxes are checked
                            then, or see if the answer is sequence
                            make the pojo of user answer and then
                            Call API to save the answer state
                            and then after successful api call find eligible question

                 */
                imageView.setVisibility(View.INVISIBLE);
                linearLayout.setVisibility(View.INVISIBLE);
                questionText.setVisibility(View.INVISIBLE);
                replay.setVisibility(View.INVISIBLE);
                UserAnswerDTO userAnswerDTO = new UserAnswerDTO();
//                userAnswerDTO.getContestQuestionDTO().setContestQuestionId(globalContestQuestionDTO.getContestQuestionId());

                Log.e("CHECK ONE", contestDTO.toString());
                globalContestQuestionDTO.setContestDTO(contestDTO);
                Log.e("CHECK TWO", globalContestQuestionDTO.getContestDTO().toString());

                userAnswerDTO.setContestQuestionDTO(globalContestQuestionDTO);
                if(globalContestQuestionDTO.getUserAnswerDTO()!=null) {
                    if (globalContestQuestionDTO.getUserAnswerDTO().getUserAnswerId() != null) {
                        userAnswerDTO.setUserAnswerId(globalContestQuestionDTO.getUserAnswerDTO().getUserAnswerId());
                    }
                }
                userAnswerDTO.setAnswer(getSuitableAnswer());
                userAnswerDTO.setUserId(testUserId);
                userAnswerDTO.setPoints(points);
                userAnswerDTO.setSkipped(false);
                userAnswerDTO.setTimeOfAnswer(null);
                userAnswerDTO.setAnswered(true);
                Log.d("TAG" + "CONTESTDTO", userAnswerDTO.toString());
                UserApiCall userApiCall = retrofit.create(UserApiCall.class);
                Call<Boolean> saveAnswer = userApiCall.saveAnswer(userAnswerDTO);
                saveAnswer.enqueue(new Callback<Boolean>() {
                    @Override
                    public void onResponse(Call<Boolean> call, Response<Boolean> response) {
                      //  Log.d(TAG, response.body().toString());
                        getAllQuestionsByContest(contestId, true);

                    }

                    @Override
                    public void onFailure(Call<Boolean> call, Throwable t) {

                    }
                });


                break;

            case R.id.skip_question_button :
                imageView.setVisibility(View.INVISIBLE);
                linearLayout.setVisibility(View.INVISIBLE);
                questionText.setVisibility(View.INVISIBLE);
                replay.setVisibility(View.INVISIBLE);

                if(skipCount == 2){
                    Toast.makeText(GameActivity.this,"Skips over",Toast.LENGTH_SHORT).show();
                    for(ContestQuestionDTO contestQuestionDTO : contestQuestionDTOList){
                        if(contestQuestionDTO.getUserAnswerDTO()!=null){
                            if(contestQuestionDTO.getUserAnswerDTO().getSkipped()){

                                Log.d("ERROR",contestQuestionDTO.getUserAnswerDTO().toString());
                                UserAnswerDTO skipUserAnswerDTO = new UserAnswerDTO();
                                skipUserAnswerDTO.setUserAnswerId(contestQuestionDTO.getUserAnswerDTO().getUserAnswerId());


                                skipUserAnswerDTO.setContestQuestionDTO(contestQuestionDTO);
                                skipUserAnswerDTO.getContestQuestionDTO().setContestDTO(contestDTO);


                                skipUserAnswerDTO.setContestQuestionDTO(contestQuestionDTO);
                                skipUserAnswerDTO.setAnswer(getSuitableAnswer());
                                skipUserAnswerDTO.setUserId(testUserId);
                                skipUserAnswerDTO.setPoints(points);
                                skipUserAnswerDTO.setSkipped(false);
                                skipUserAnswerDTO.setAnswered(false);
                                Log.d("ERROR",skipUserAnswerDTO.toString());

                                UserApiCall skipuserApiCall = retrofit.create(UserApiCall.class);

                                Call<Boolean> skipsaveAnswer = skipuserApiCall.saveAnswer(skipUserAnswerDTO);
                                skipsaveAnswer.enqueue(new Callback<Boolean>() {
                                    @Override
                                    public void onResponse(Call<Boolean> call, Response<Boolean> response) {

                                        Log.d(TAG, response.body().toString());
                                        getAllQuestionsByContest(contestId, true);
                                    }

                                    @Override
                                    public void onFailure(Call<Boolean> call, Throwable t) {
                                        Log.e("ERROR", t.getMessage());

                                    }
                                });


                            }
                        }
                    }
                    break;
                }else if(skipCount < 2) {
                    UserAnswerDTO skipUserAnswerDTO = new UserAnswerDTO();
                    globalContestQuestionDTO.setContestDTO(contestDTO);
                    skipUserAnswerDTO.setContestQuestionDTO(globalContestQuestionDTO);
                    skipUserAnswerDTO.setAnswer(getSuitableAnswer());
                    if(globalContestQuestionDTO.getUserAnswerDTO()!=null) {
                        if (globalContestQuestionDTO.getUserAnswerDTO().getUserAnswerId() != null) {
                            skipUserAnswerDTO.setUserAnswerId(globalContestQuestionDTO.getUserAnswerDTO().getUserAnswerId());
                        }
                    }
                    skipUserAnswerDTO.setUserId(testUserId);
                    skipUserAnswerDTO.setPoints(points);
                    skipUserAnswerDTO.setSkipped(true);
                    skipUserAnswerDTO.setAnswered(false);
                    UserApiCall skipuserApiCall = retrofit.create(UserApiCall.class);

                    Call<Boolean> skipsaveAnswer = skipuserApiCall.saveAnswer(skipUserAnswerDTO);
                    skipsaveAnswer.enqueue(new Callback<Boolean>() {
                        @Override
                        public void onResponse(Call<Boolean> call, Response<Boolean> response) {
                            Log.d(TAG, response.body().toString());
                            getAllQuestionsByContest(contestId, true);
                        }

                        @Override
                        public void onFailure(Call<Boolean> call, Throwable t) {

                        }
                    });
                }
                break;

            case R.id.begin_button:
                beginButton.setVisibility(View.GONE);
                questionNumberText.setVisibility(View.VISIBLE);
                nextQuestionFab.setVisibility(View.VISIBLE);
                skipQuestionFab.setVisibility(View.VISIBLE);
                findEligibleQuestion();
                break;

            case R.id.finish_button:

                SharedPreferences sharedPreferences = getSharedPreferences("user", Context.MODE_PRIVATE);
                String userId = sharedPreferences.getString("userId", " ");
                UserApiCall callLeaderboard = retrofit.create(UserApiCall.class);
                Call<Boolean> call = callLeaderboard.callLeaderboard(contestId, userId);

                call.enqueue(new Callback<Boolean>() {
                    @Override
                    public void onResponse(Call<Boolean> call, Response<Boolean> response) {
                        Log.d("LEADERBOARD", "Ban gaya leaderboard.. aisa server kehta h");
                    }

                    @Override
                    public void onFailure(Call<Boolean> call, Throwable t) {
                        Log.d("LEADERBOARDERROR", "error in leaderboard.. aisa server kehta h");


                    }
                });
                startActivity(new Intent(GameActivity.this,LeaderboardActivity.class));
                finish();

        }
    }

    private void displayVideo(final QuestionDTO questionDTO){

        //specify the location of media file
        videoView.setVisibility(View.VISIBLE);
        videoUri=Uri.parse(questionDTO.getQuestionUrl());//https://degrading-turnaroun.000webhostapp.com/small.mp4");//Environment.getExternalStorageDirectory().getPath()+"/media/1.mp4");
        //Setting MediaController and URI, then starting the videoView
        videoView.setMediaController(mediaController);
        videoView.setVideoURI(videoUri);
        videoView.requestFocus();
        videoView.start();
        replay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                videoView.setVisibility(View.VISIBLE);
                videoView.start();
            }
        });
        videoView.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mediaPlayer) {
                try {
                    replay.setVisibility(View.VISIBLE);
                    showQuestionViews();
                    answerType = questionDTO.getAnswerType();
                    questionText.setText(questionDTO.getQuestionText());
                    optionOne.setText("A: " + questionDTO.getOptionOne());
                    optionTwo.setText("B: " + questionDTO.getOptionTwo());
                    optionThree.setText("C: " + questionDTO.getOptionThree());
                    optionFour.setText("D: " + questionDTO.getOptionFour());
                    //mediaPlayer.stop();
                    videoView.setVisibility(View.INVISIBLE);

                    //playAudio();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

    }

    private void playAudio(final QuestionDTO questionDTO) throws IOException {
        mediaPlayer = new MediaPlayer();
        mediaPlayer.setDataSource(questionDTO.getQuestionUrl());//"http://www.hubharp.com/web_sound/BachGavotteShort.mp3");
        mediaPlayer.prepare();
        mediaPlayer.start();
        replay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mediaPlayer.start();
            }
        });

        mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mediaPlayer) {
                replay.setVisibility(View.VISIBLE);
                showQuestionViews();
                answerType = questionDTO.getAnswerType();
                questionText.setText(questionDTO.getQuestionText());
                optionOne.setText("A: " + questionDTO.getOptionOne());
                optionTwo.setText("B: " + questionDTO.getOptionTwo());
                optionThree.setText("C: " + questionDTO.getOptionThree());
                optionFour.setText("D: " + questionDTO.getOptionFour());
                mediaPlayer.stop();
            }
        });


    }

    private void displayImage(QuestionDTO questionDTO){
        imageView.setVisibility(View.VISIBLE);
        new DownLoadImageTask(imageView).execute(questionDTO.getQuestionUrl());//"https://i.ytimg.com/vi/y0VODhYvq3s/maxresdefault.jpg");
        showQuestionViews();
        answerType = questionDTO.getAnswerType();
        questionText.setText(questionDTO.getQuestionText());
        optionOne.setText("A: " + questionDTO.getOptionOne());
        optionTwo.setText("B: " + questionDTO.getOptionTwo());
        optionThree.setText("C: " + questionDTO.getOptionThree());
        optionFour.setText("D: " + questionDTO.getOptionFour());
    }

    private class DownLoadImageTask extends AsyncTask<String,Void,Bitmap> {
        ImageView imageView;

        public DownLoadImageTask(ImageView imageView){
            this.imageView = imageView;
        }

        /*
            doInBackground(Params... params)
                Override this method to perform a computation on a background thread.
         */
        protected Bitmap doInBackground(String...urls){
            String urlOfImage = urls[0];
            Bitmap logo = null;
            try{
                InputStream is = new URL(urlOfImage).openStream();
                /*
                    decodeStream(InputStream is)
                        Decode an input stream into a bitmap.
                 */
                logo = BitmapFactory.decodeStream(is);
            }catch(Exception e){ // Catch the download exception
                e.printStackTrace();
            }
            return logo;
        }

        /*
            onPostExecute(Result result)
                Runs on the UI thread after doInBackground(Params...).
         */
        protected void onPostExecute(Bitmap result){
            imageView.setImageBitmap(result);
        }
    }

    private void requestPermissions() {

        // Here, thisActivity is the current activity
        if (ContextCompat.checkSelfPermission(GameActivity.this,
                Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {

            // Permission is not granted
            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(GameActivity.this,
                    Manifest.permission.READ_EXTERNAL_STORAGE)) {
                // Show an explanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.
                Toast.makeText(GameActivity.this," here", Toast.LENGTH_SHORT).show();

            } else {
                // No explanation needed; request the permission
                ActivityCompat.requestPermissions(GameActivity.this,
                        new String[]{Manifest.permission.READ_CONTACTS},
                        MY_PERMISSIONS_REQUEST_READ_STORAGE);

                // MY_PERMISSIONS_REQUEST_READ_CONTACTS is an
                // app-defined int constant. The callback method gets the
                // result of the request.
            }
        } else {
            // Permission has already been granted
            Toast.makeText(GameActivity.this,"permission already granted", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_READ_STORAGE: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // permission was granted, yay! Do the
                    // contacts-related task you need to do.
                } else {
                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                }
                return;
            }

            // other 'case' lines to check for other
            // permissions this app might request.
        }
    }


}
