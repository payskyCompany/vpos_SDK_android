package com.paysky.upg.app;

import android.content.Context;

import androidx.appcompat.app.AppCompatDelegate;
import androidx.multidex.MultiDexApplication;

import com.jenzz.appstate.AppStateListener;
import com.jenzz.appstate.AppStateMonitor;
import com.orhanobut.logger.AndroidLogAdapter;
import com.orhanobut.logger.Logger;
import com.paysky.upg.BuildConfig;

import io.paysky.upg.util.BuildUtil;
import timber.log.Timber;

public class MainApp extends MultiDexApplication {
    public static Context appContext;

    @Override
    public void onCreate() {
        super.onCreate();
        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true);
        BuildUtil.setFlavor(BuildConfig.FLAVOR);
        Logger.addLogAdapter(new AndroidLogAdapter() {
            @Override
            public boolean isLoggable(int priority, String tag) {
                return BuildConfig.DEBUG;
            }
        });
        Timber.plant(new Timber.DebugTree() {
            @Override
            protected void log(int priority, String tag, String message, Throwable t) {
                Logger.log(priority, tag, message, t);
            }
        });
        appContext = getApplicationContext();
        AppStateMonitor appStateMonitor = AppStateMonitor.create(this);
        appStateMonitor.addListener(new SampleAppStateListener());
        appStateMonitor.start();

    }

    private class SampleAppStateListener implements AppStateListener {
        @Override
        public void onAppDidEnterForeground() {
        }

        @Override
        public void onAppDidEnterBackground() {
        }
    }
}
