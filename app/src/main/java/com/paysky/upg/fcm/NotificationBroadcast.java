package com.paysky.upg.fcm;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import io.paysky.upg.data.network.model.response.NotiEntity;

/**
 * Created by PaySky-3 on 10/26/2017.
 */

public class NotificationBroadcast extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        //Log.d("Here", "I am here");
        NotiEntity data = (NotiEntity) intent.getExtras().getSerializable("data");
//        Intent intents = new Intent(context, ConfirmationActivity.class);
//        intents.putExtra("noti", data);
//        intents.setFlags(FLAG_ACTIVITY_NEW_TASK);
//        context.startActivity(intents);

    }
}
