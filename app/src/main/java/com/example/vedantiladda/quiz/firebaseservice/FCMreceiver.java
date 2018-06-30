package com.example.vedantiladda.quiz.firebaseservice;

import android.app.Service;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.widget.Toast;

import com.example.vedantiladda.quiz.QuizMaster.QuizMasterActivity;
import com.example.vedantiladda.quiz.dto.FCMQuestion;
import com.example.vedantiladda.quiz.user.DynamicGame;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import java.io.Serializable;
import java.util.Map;

public class FCMreceiver extends FirebaseMessagingService {

    private String questionId = "questionId";
    private String contestQuestionId = "contestQuestionId";
    private String categoryId = "categoryId";
    private String difficulty = "difficulty";
    private String questionType = "questionType";
    private String questionText = "questionText";
    private String questionUrl = "questionUrl";
    private String optionOne = "optionOne";
    private String optionTwo = "optionTwo";
    private String optionThree = "optionThree";
    private String optionFour = "optionFour";
    private String answerType = "answerType";
    private String points = "points";
    private String status = "status";

    public FCMreceiver() {
    }

    @Override
    public void onMessageReceived(final RemoteMessage remoteMessage) {
        // ...
        Log.d("Message ", "From:  User" + remoteMessage.getFrom());
        Log.d("MESSAGE # ", remoteMessage.getData().toString());


        if(remoteMessage.getFrom().equals("/topics/user")){

        // Check if message contains a data payload.
            if (remoteMessage.getData().size() > 0) {
                Log.d("Message", "Message data payload: from USer " + remoteMessage.getData());
                Handler mainHandler = new Handler(getMainLooper());
                mainHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        FCMQuestion fcmQuestion = new FCMQuestion();
                        fcmQuestion.setQuestionId(remoteMessage.getData().get(questionId));
                        fcmQuestion.setContestQuestionId(remoteMessage.getData().get(contestQuestionId));
                        fcmQuestion.setCategoryId(remoteMessage.getData().get(categoryId));
                        fcmQuestion.setDifficulty(remoteMessage.getData().get(difficulty));
                        fcmQuestion.setQuestionType(remoteMessage.getData().get(questionType));
                        fcmQuestion.setQuestionUrl(remoteMessage.getData().get(questionUrl));
                        fcmQuestion.setQuestionText(remoteMessage.getData().get(questionText));
                        fcmQuestion.setOptionOne(remoteMessage.getData().get(optionOne));
                        fcmQuestion.setOptionTwo(remoteMessage.getData().get(optionTwo));
                        fcmQuestion.setOptionThree(remoteMessage.getData().get(optionThree));
                        fcmQuestion.setOptionFour(remoteMessage.getData().get(optionFour));
                        fcmQuestion.setAnswerType(remoteMessage.getData().get(answerType));
                        fcmQuestion.setStatus(remoteMessage.getData().get(status));

                        Toast.makeText(getApplicationContext(), remoteMessage.getData().get("detail"), Toast.LENGTH_LONG).show();


                        sendMessage(fcmQuestion);
                    }
                });
            }


        // Check if message contains a notification payload.
            if (remoteMessage.getNotification() != null) {
                Log.d("Message", "Message Notification Body: " + remoteMessage.getNotification().getBody());
            }

        // Also if you intend on generating your own notifications as a result of a received FCM
        // message, here is where that should be initiated. See sendNotification method below.
        }if(remoteMessage.getFrom().equals("/topics/quizMaster")){
            Handler mainHandler = new Handler(getMainLooper());
            mainHandler.post(new Runnable() {
                @Override
                public void run() {
                    if (remoteMessage.getData().get("status").equals("next")) {
                        //                        waitForUsers.dismissDiologBox();

                        Log.d("sender from QM", "Broadcasting message");
                        // You can also include some extra data.
                        bhejo();
                    }
                    else if(remoteMessage.getData().get("status").equals("start")) {
                        // Log.d("sender", "Broadcasting message");

                        Intent intent= new Intent(FCMreceiver.this,QuizMasterActivity.class);
                        startActivity(intent);
                    }
                    else if(remoteMessage.getData().get("status").equals("end")){
                        Intent intent= new Intent(FCMreceiver.this,QuizMasterActivity.class);
                        startActivity(intent);
                    }
                    Toast.makeText(getApplicationContext(), remoteMessage.getData().get("detail"), Toast.LENGTH_LONG).show();
                }

            });

        }


    }


    private void bhejo(){
        Intent intent = new Intent("custom-event");
        intent.putExtra("message", "This is my message!");
        Log.e("FCM CHECK", "THIS IS MY MESSAGE");
        LocalBroadcastManager.getInstance(this).sendBroadcast(intent);
    }
        //  TODO : change it accordingly - used to send msgs to dynamic game
    private void sendMessage(FCMQuestion fcmQuestion) {
        Log.d("sender", "Broadcasting message User");
        Intent intent = new Intent("DynamicGame");
        // You can also include some extra data.
        intent.putExtra("messagequestion",(Serializable) fcmQuestion);
        LocalBroadcastManager.getInstance(this).sendBroadcast(intent);
    }
}
