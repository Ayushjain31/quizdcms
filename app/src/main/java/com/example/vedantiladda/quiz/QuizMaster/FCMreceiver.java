package com.example.vedantiladda.quiz.QuizMaster;

import android.app.Service;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.widget.Toast;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

public class FCMreceiver extends FirebaseMessagingService {

//    WaitForUsers waitForUsers;
//
//    public FCMreceiver(WaitForUsers waitForUsers) {
//        this.waitForUsers = waitForUsers;
//    }

    public FCMreceiver() {

    }

    @Override
    public void onMessageReceived(final RemoteMessage remoteMessage) {
        // ...

        // TODO(developer): Handle FCM messages here.
        // Not getting messages here? See why this may be: https://goo.gl/39bRNJ
        Log.d("Message", "From: " + remoteMessage.getFrom());

        // Check if message contains a data payload.
        if (remoteMessage.getData().size() > 0) {
            Log.d("Message", "Message data payload: from QM " + remoteMessage.getData());
            Handler mainHandler = new Handler(getMainLooper());

            mainHandler.post(new Runnable() {
                @Override
                public void run() {

                    if (remoteMessage.getData().get("status").equals("next")) {
                        //                        waitForUsers.dismissDiologBox();

                        Log.d("sender from QM", "Broadcasting message");
                        Intent intent = new Intent("custom-event-name");
                        // You can also include some extra data.
                        intent.putExtra("message", "This is my message!");
                        LocalBroadcastManager.getInstance(FCMreceiver.this).sendBroadcast(intent);
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

            if (/* Check if data needs to be processed by long running job */ true) {
                // For long-running tasks (10 seconds or more) use Firebase Job Dispatcher.
                //scheduleJob();
            } else {
                // Handle message within 10 seconds
                //handleNow();
            }

        }

        // Check if message contains a notification payload.
        if (remoteMessage.getNotification() != null) {
            Log.d("Message", "Message Notification Body: " + remoteMessage.getNotification().getBody());
        }

        // Also if you intend on generating your own notifications as a result of a received FCM
        // message, here is where that should be initiated. See sendNotification method below.
    }

//    public interface WaitForUsers {
//        void dismissDiologBox();
//    }
}