package com.cookery.services;

import android.util.Log;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

/**
 * Created by ajit on 1/11/17.
 */

public class MyFirebaseMessagingService extends FirebaseMessagingService {
    private static final String CLASS_NAME = MyFirebaseMessagingService.class.getName();

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        // TODO: Handle FCM messages here.
        // If the application is in the foreground handle both data and notification messages here.
        // Also if you intend on generating your own notifications as a result of a received FCM
        // message, here is where that should be initiated.
        Log.d(CLASS_NAME, "From: " + remoteMessage.getFrom());
        Log.d(CLASS_NAME, "Notification Message Body: " + remoteMessage.getNotification().getBody());
    }
}
