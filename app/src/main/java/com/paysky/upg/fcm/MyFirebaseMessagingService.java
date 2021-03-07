package com.paysky.upg.fcm;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.RingtoneManager;
import android.os.Build;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.google.gson.Gson;
import com.paysky.upg.BuildConfig;
import com.paysky.upg.R;
import com.paysky.upg.activity.MainActivity;
import com.paysky.upg.utils.NotificationCount;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.Map;
import java.util.Random;

import io.paysky.upg.data.network.model.response.TransactionDetailsEntities;
import io.paysky.upg.util.SessionManager;


public class MyFirebaseMessagingService extends FirebaseMessagingService {

    private static final String TAG = "MyGcmListenerService";
    public static final int ARCHIVE_INTENT_ID = 1;


    public static final String LABEL_ARCHIVE = "Archive";
    public static final String REPLY_ACTION = "com.hitherejoe.notifi.util.ACTION_MESSAGE_REPLY";

    public static boolean hasNewNotification = false;
    public static MyFirebaseMessagingService FCM = new MyFirebaseMessagingService();

    public void handleJson(Context context, JSONObject jsonObject) {

        hasNewNotification = true;
        NotificationData notificationData = new NotificationData();
        String obj = null, bodyObject = null, titleObject = null, payObject = null, id = null, referenceNumber = "";
        try {
            if (jsonObject.has("data"))
                obj = jsonObject.get("data").toString();
            if (jsonObject.has("body"))
                bodyObject = jsonObject.get("body").toString();
            if (jsonObject.has("title"))
                titleObject = jsonObject.get("title").toString();
            if (jsonObject.has("TransactionTypeName"))
                payObject = jsonObject.get("TransactionTypeName").toString();
            if (jsonObject.has("id"))
                id = jsonObject.get("id").toString();

            if (jsonObject.has("referencenumber"))
                referenceNumber = jsonObject.get("referencenumber").toString();

        } catch (JSONException e) {
            e.printStackTrace();
        }


        if (obj != null) {
            Gson gson = new Gson();

            TransactionDetailsEntities notiEntity = gson.fromJson(obj.toString(), TransactionDetailsEntities.class);
            notiEntity.setTransactionNo(referenceNumber);

            notificationData.setNotiEntity(notiEntity);


        }

        if (id != null) {
            notificationData.setId(Long.parseLong(id.toString()));
        }

        if (bodyObject != null) {
            notificationData.setTextMessage(bodyObject.toString());
        }
        if (titleObject != null) {
            notificationData.setTitle(titleObject.toString());
        }

        if (payObject != null) {
            notificationData.setTransactionTypeName(payObject.toString());
        }

        if (!referenceNumber.equals("")) {

            notificationData.setReferenceNumber(referenceNumber);

            EventBus.getDefault().post(notificationData);

        }


        this.sendNotification(context, notificationData);


        NotificationCount.increaseNotification();


    }


    @Override
    public void onMessageReceived(RemoteMessage message) {


        if (SessionManager.getInstance().isLogin()) {

            Map<String, String> params = message.getData();
            JSONObject object = new JSONObject(params);
            handleJson(getApplicationContext(), object);

        }


    }

    public void initChannels(Context context) {
        if (Build.VERSION.SDK_INT < 26) {
            return;
        }
        NotificationManager notificationManager = getSystemService(NotificationManager.class);
        NotificationChannel channel = new NotificationChannel("default", "UPG", NotificationManager.IMPORTANCE_HIGH);
        channel.setDescription("UPG Merchant app");
        channel.setShowBadge(true);
        channel.setLockscreenVisibility(Notification.VISIBILITY_PUBLIC);
        notificationManager.createNotificationChannel(channel);
    }


    private void sendNotification(Context context, NotificationData notificationData) {

        Intent intent = new Intent(context, MainActivity.class);
        intent.putExtra(NotificationData.TEXT, notificationData.getTextMessage());
        intent.putExtra("notification", notificationData);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);

        Random rand = new Random();

        int randomNumber = rand.nextInt(50) + 1;


        PendingIntent pendingIntent = PendingIntent.getActivity(context, randomNumber /* Request code */, intent,
                PendingIntent.FLAG_ONE_SHOT);

        Notification notification = null;
        try {


            initChannels(context);
            Bitmap largeIcon = BitmapFactory.decodeResource(context.getResources(), R.mipmap.ic_launcher);
            notification = new NotificationCompat.Builder(this, BuildConfig.APPLICATION_ID)
                    .setSmallIcon(R.mipmap.ic_launcher)
                    .setLargeIcon(largeIcon)
                    .setContentTitle(notificationData.getTitle())
                    .setContentText(URLDecoder.decode(notificationData.getTextMessage(), "UTF-8"))
                    .setAutoCancel(true)
                    //.setColor(context.getResources().getColor(R.color.blue))
                    .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION))
                    .setDefaults(Notification.DEFAULT_ALL)
                    .setPriority(NotificationCompat.PRIORITY_MAX)
                    .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
                    .setContentIntent(pendingIntent).build();

            EventBus.getDefault().post(notificationData);

            //Log.d("FROMNOTI", "sendNotification: ");


        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        if (notification != null) {
            NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this);
            notificationManager.notify(randomNumber, notification);
        } else {
            //Log.d(TAG, "Não foi possível criar objeto notificationBuilder");
        }


    }

}
