package com.paysky.upg.utils;

import android.os.Looper;
import android.view.Gravity;

import com.paysky.upg.R;
import com.paysky.upg.activity.MainActivity;
import com.paysky.upg.app.MainApp;

import io.paysky.upg.util.SessionManager;
import me.leolin.shortcutbadger.ShortcutBadger;
import q.rorbin.badgeview.Badge;
import q.rorbin.badgeview.QBadgeView;

public class NotificationCount {


    public static Badge badge;

    public static void increaseNotification() {
        int count = SessionManager.getInstance().getNotCount();
        count++;
        setCount(count);
    }


    public static void RefreshCount() {
        SessionManager sessionManager = SessionManager.getInstance();
        int count = sessionManager.getNotCount();
        badge = new QBadgeView(MainApp.appContext)
                .setBadgeGravity(Gravity.START | Gravity.TOP)
                .setBadgeTextSize(10, true).
                        setBadgePadding(5, true)
                .setBadgeBackgroundColor(MainApp.appContext.getResources().getColor(R.color.notification))
                .setShowShadow(false)
                .setGravityOffset(0, 0, true)
                .setShowShadow(false).
                        bindTarget(MainActivity.notificationIcon).setBadgeNumber(count);
    }

    public static void setCount(final int count) {
        new android.os.Handler(Looper.getMainLooper()).post(new Runnable() {
            @Override
            public void run() {
                SessionManager sessionManager = SessionManager.getInstance();
                sessionManager.setNotiCount(count);
                ShortcutBadger.applyCount(MainApp.appContext, count); //for 1.1.4+
                if (MainActivity.notificationIcon != null) {
                    if (badge == null) {
                        badge = new QBadgeView(MainApp.appContext)
                                .setBadgeGravity(Gravity.START | Gravity.TOP)
                                .setBadgeTextSize(10, true).
                                        setBadgePadding(5, true)
                                .setBadgeBackgroundColor(MainApp.appContext.getResources().getColor(R.color.notification))
                                .setShowShadow(false)
                                .setGravityOffset(0, 0, true)
                                .setShowShadow(false).
                                        bindTarget(MainActivity.notificationIcon).setBadgeNumber(count);
                        if (count == 0) {
                            badge.hide(true);
                        }

                    } else {
                        badge.hide(true);
                        badge = null;
                        badge = new QBadgeView(MainApp.appContext)
                                .setBadgeGravity(Gravity.START | Gravity.TOP)
                                .setBadgeTextSize(10, true).
                                        setBadgePadding(5, true)
                                // setExactMode(true)
                                .setBadgeBackgroundColor(MainApp.appContext.getResources().getColor(R.color.notification))
                                .setShowShadow(false)
                                .setGravityOffset(0, 0, true)
                                .setShowShadow(false).
                                        bindTarget(MainActivity.notificationIcon).setBadgeNumber(count);
                    }

                    badge.setBadgeNumber(count);
                }
            }
        });


    }


}
