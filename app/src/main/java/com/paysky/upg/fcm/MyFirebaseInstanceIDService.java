package com.paysky.upg.fcm;

import com.google.firebase.messaging.FirebaseMessagingService;

/**
 * @author ton1n8o - antoniocarlos.dev@gmail.com on 6/13/16.
 */
public class MyFirebaseInstanceIDService extends FirebaseMessagingService {

    private static String TAG = "Registration";

    @Override
    public void onNewToken(String s) {
        super.onNewToken(s);
        //   Log.d("NEW_TOKEN",s);
    }

}
