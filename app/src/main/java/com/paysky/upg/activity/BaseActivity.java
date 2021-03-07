package com.paysky.upg.activity;

import android.app.Activity;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.blankj.utilcode.util.ActivityUtils;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.kaopiz.kprogresshud.KProgressHUD;
import com.paysky.upg.BuildConfig;
import com.paysky.upg.R;
import com.paysky.upg.customviews.PayLinkFilter;
import com.paysky.upg.fragment.BaseFragment;
import com.paysky.upg.fragment.DashBoardFragment;
import com.paysky.upg.fragment.TransactionsLoadingFragment;
import com.paysky.upg.utils.DialogProgressUtils;
import com.paysky.upg.utils.NotificationCount;
import com.yinglan.keyboard.HideUtil;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.List;

import io.github.inflationx.viewpump.ViewPumpContextWrapper;
import io.paysky.upg.base.BaseView;
import io.paysky.upg.data.network.ApiClient;
import io.paysky.upg.util.LocaleUtil;
import io.paysky.upg.util.RootUtil;
import io.paysky.upg.util.ScreenshotUtil;
import io.paysky.upg.util.SessionManager;
import io.paysky.upg.util.ToastUtil;
import io.paysky.upg.util.ViewUtil;

public class BaseActivity extends AppCompatActivity implements BaseView {


    public static ImageView backbtn;
    public SessionManager session;
    public KProgressHUD progress;
    TextView merchantName;
    TextView trimnalId;
    TextView page_title;
    LinearLayout notiLL;
    public static boolean SplashActivityFound = true;
    private FirebaseAnalytics mFirebaseAnalytics;

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(ViewPumpContextWrapper.wrap(newBase));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (BuildConfig.DEBUG) {
            ScreenshotUtil.PREVENT_SCREENSHOT = false;
        }
        ScreenshotUtil.preventScreenshot(this);

        //  BuildUtil.setFlavor(io.paysky.upg.BuildConfig.FLAVOR);

        if (!io.paysky.upg.BuildConfig.DEBUG) {
            if (RootUtil.isDeviceRooted(this)) {
                ToastUtil.showLongToast(this, io.paysky.upg.R.string.upg_general_device_rooted_message);
                finish();
            }
        }

        if (LocaleUtil.isAppLanguageChanged(this)) {
            LocaleUtil.changeAppLanguage(this, SessionManager.getInstance().getLang());
        }


        SplashActivityFound = true;

        session = SessionManager.getInstance();
        progress = DialogProgressUtils.createCancelableProgressDialog(this, "");
        EventBus.getDefault().register(this);


    }


    @Subscribe(threadMode = ThreadMode.MAIN)
    public void closeProgress(ApiClient.NoConnectivityException x) {
        if (progress != null && progress.isShowing()) {
            progress.dismiss();

        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        SplashActivityFound = false;
        if (progress != null && progress.isShowing()) progress.dismiss();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(String x) {
        try {
            if (!x.equals("FINISH")) {
                DashBoardFragment.ResultTerminalidString = null;
                DashBoardFragment.StartDate = null;
                DashBoardFragment.EndDate = null;
                DashBoardFragment.TahweeltransactionId = null;
                DashBoardFragment.ResultTypeString = "";
            }
            if (x.equals("OTHER")) {
                ToastUtil.showLongToast(this, R.string.SigninFromOtherAccount);
            }

            if (x.equals("logOut")) {
                SessionManager.getInstance().setUserName("");
                SessionManager.getInstance().setServiceData(null);
            }

            NotificationCount.setCount(0);
            NotificationCount.badge = null;
            session.setEmpData(null);
            session.setLogin(false);
            NotificationManager nMgr = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
            if (nMgr != null) {
                nMgr.cancelAll();
            }
            ActivityUtils.finishAllActivities();
            ActivityUtils.startActivity(LoginActivity.class);
        } catch (Exception e) {
            e.printStackTrace();
        }

        PayLinkFilter.SelectedMerchantReferance = null;
        PayLinkFilter.SelectedPaymerntStatus = 0;
        PayLinkFilter.SelectedResultTo = null;
        PayLinkFilter.SelectedResultFrom = null;

    }

    public void addNewFragmentPush(Fragment fragment, String TAG) {
        if (BaseFragment.fromWhere.equals("daynamic")) {
            addNewFragmentNullBackStack(fragment, TAG);
            return;
        }

        if (progress.isShowing()) {
            progress.dismiss();
        }


        FragmentManager myfragmentManager = getSupportFragmentManager();
        FragmentTransaction mytransaction = myfragmentManager.beginTransaction();
        mytransaction.setCustomAnimations(
                R.anim.enter_from_right, R.anim.exit_to_left,
                R.anim.enter_from_left, R.anim.exit_to_right);

        mytransaction.addToBackStack("title_dashboard");
        mytransaction.replace(R.id.fragment_container, fragment, TAG).commit();
        BaseActivity.backbtn.setVisibility(View.VISIBLE);
        setHeaderName(TAG);
    }


    public void addNewFragment(Fragment fragment, String TAG) {
        if (BaseFragment.fromWhere.equals("daynamic") || fragment instanceof TransactionsLoadingFragment) {
            addNewFragmentNullBackStack(fragment, TAG);
            return;
        }

        if (progress.isShowing()) {
            progress.dismiss();
        }


        FragmentManager myfragmentManager = getSupportFragmentManager();
        FragmentTransaction mytransaction = myfragmentManager.beginTransaction();
        mytransaction.setCustomAnimations(
                R.anim.enter_from_right, R.anim.exit_to_left,
                R.anim.enter_from_left, R.anim.exit_to_right);
        mytransaction.addToBackStack("title_dashboard");
        mytransaction.replace(R.id.fragment_container, fragment, TAG).commit();
        setHeaderName(TAG);

    }


    public void addNewFragmentNullBackStack(Fragment fragment, String TAG) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.setCustomAnimations(
                R.anim.enter_from_right, R.anim.exit_to_left,
                R.anim.enter_from_left, R.anim.exit_to_right);
        if (fragment != null) {
            fragmentTransaction.replace(R.id.fragment_container, fragment, TAG).commit();
            if (BaseActivity.backbtn != null) {
                BaseActivity.backbtn.setVisibility(View.VISIBLE);
            }
        } else {
            ToastUtil.showLongToast(this, R.string.service_not_available);
        }
        setHeaderName(TAG);
    }


    @Override
    public void onBackPressed() {
        ActionBack();
    }

    public void onBackRealyPressed() {
        super.onBackPressed();
    }


    @Override
    protected void onResume() {
        super.onResume();
        HideUtil.init(this);

    }


    public void setHeaderName(String namePage) {


    }

    public void ActionBack() {
        BaseFragment.fromWhere = "STATIC";

        int fragments = getSupportFragmentManager().getBackStackEntryCount();
        if (fragments == 1 || fragments == 0) {
            finish();
        } else {
            if (getSupportFragmentManager().getBackStackEntryCount() > 1) {
                getSupportFragmentManager().popBackStack();
            } else {
                super.onBackPressed();
            }
        }

        if (fragments == 2) {
            backbtn.setVisibility(View.GONE);
        }
        int fragmentsC = getSupportFragmentManager().getBackStackEntryCount();
        if (fragmentsC == 1) {
            backbtn.setVisibility(View.GONE);
        }

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (getVisibleFragment() != null && getVisibleFragment().getTag() != null)
                    setHeaderName(getVisibleFragment().getTag());

            }
        }, 500);
    }

    public void BackBtn(View view) {
        ActionBack();
    }

    public void close(View view) {
        finish();
    }


    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);
        Fragment fragment = getVisibleFragment();
        if (fragment != null) {
            fragment.onActivityResult(requestCode, resultCode, intent);
        }
    }

    public Fragment getVisibleFragment() {
        FragmentManager fragmentManager = getSupportFragmentManager();
        List<Fragment> fragments = fragmentManager.getFragments();
        if (fragments != null) {
            for (Fragment fragment : fragments) {
                if (fragment != null && fragment.isVisible())
                    return fragment;
            }
        }
        return null;
    }


    @Override
    protected void onDestroy() {
        EventBus.getDefault().unregister(this);
        super.onDestroy();
    }


    @Override
    public Context getContext() {
        return this;
    }

    @Override
    public void showProgress(int message) {
        //  progress.setLabel(getString(message));
        progress.show();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (progress.isShowing()) {
                    progress.dismiss();
                }

            }
        }, 5000);
    }

    @Override
    public void showProgress(int message, int label) {
        progress.setLabel(getString(message));
        progress.show();
    }

    @Override
    public void showProgress() {
        progress.show();


        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (progress.isShowing())
                    progress.dismiss();
            }
        }, 150000);
    }

    @Override
    public void showError(int string) {
        dismissProgress();

    }

    @Override
    public void dismissProgress() {
        if (progress != null && progress.isShowing()) {
            progress.dismiss();
        }
    }


    public void hideKeyboard() {
        InputMethodManager imm = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
        //Find the currently focused view, so we can grab the correct window token from it.
        View view = getCurrentFocus();
        //If no view currently has focus, create a new one, just so we can grab a window token from it
        if (view == null) {
            view = new View(this);
        }
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    protected String getText(TextView textView) {
        return ViewUtil.getText(textView);
    }
}
