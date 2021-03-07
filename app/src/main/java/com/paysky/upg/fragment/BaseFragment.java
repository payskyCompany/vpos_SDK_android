package com.paysky.upg.fragment;

import android.app.Activity;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.kaopiz.kprogresshud.KProgressHUD;
import com.paysky.upg.R;
import com.paysky.upg.activity.BaseActivity;
import com.paysky.upg.activity.MainActivity;
import com.paysky.upg.utils.DialogProgressUtils;
import com.yinglan.keyboard.HideUtil;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import io.paysky.upg.base.BaseView;
import io.paysky.upg.data.network.ApiClient;
import io.paysky.upg.data.network.ApiInterface;
import io.paysky.upg.util.SessionManager;
import io.paysky.upg.util.ToastUtil;


/**
 * Created by amrel on 01/02/2018.
 */

public class BaseFragment extends Fragment implements BaseView {
    //Objects.
    public KProgressHUD progress;
    public ApiInterface apiService;
    protected SessionManager sessionManager;
    //Variables.
    public boolean isViewHidden = false;
    public static String fromWhere = "";


    @Subscribe(threadMode = ThreadMode.MAIN)
    public void closeProgesr(ApiClient.NoConnectivityException x) {
        if (progress != null && progress.isShowing()) {
            progress.dismiss();

        }
    }

    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser) {
            Activity a = getActivity();
            if (a != null) a.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        isViewHidden = true;
        if (progress.isShowing()) {
            progress.dismiss();

        }
        EventBus.getDefault().unregister(this);

    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        fromWhere = "";


        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this);
        }

        progress = DialogProgressUtils.createCancelableProgressDialog(getContext(), "");
        sessionManager = SessionManager.getInstance();

        apiService = ApiClient.getPayLinkInterface();


    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        isViewHidden = false;
        if (container != null) {
            container.removeAllViews();
        }

        if (!sessionManager.isLogin()) {
            FragmentActivity activity = getActivity();
            if (activity != null) {
                activity.finish();
            }
        }

        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this);
        }

        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    public void onResume() {
        super.onResume();
        HideUtil.init(getActivity());
    }

    public void removeStack() {
        FragmentActivity activity = getActivity();
        if (activity != null) {
            FragmentManager fm = activity.getSupportFragmentManager();
            if (fm != null)
                for (int i = 1; i < fm.getBackStackEntryCount(); ++i) {
                    fm.popBackStack();
                }
        }


        MainActivity.backbtn.setVisibility(View.GONE);

    }


    public void addNewFragment(Fragment fragment, String TAG) {
        if (TAG != null) {
            TAG = TAG.replaceAll("\\s+", "");
        }

        if (fromWhere.equals("daynamic")) {
            addNewFragmentNullBackStack(fragment, TAG);
            return;
        }

        if (getContext() == null) {
            return;
        }
        if (progress.isShowing()) {
            progress.dismiss();
        }

        FragmentManager fragmentManager = ((AppCompatActivity) getContext()).getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.setCustomAnimations(
                R.anim.enter_from_right, R.anim.exit_to_left,
                R.anim.enter_from_left, R.anim.exit_to_right);

        fragmentTransaction.addToBackStack(TAG);
        if (fragment != null) {
            isViewHidden = true;

            fragmentTransaction.replace(R.id.fragment_container, fragment).commitAllowingStateLoss();
            BaseActivity.backbtn.setVisibility(View.VISIBLE);
        } else {
            ToastUtil.showShortToast(getActivity(), R.string.service_not_available);
        }

        if (getActivity() != null)
            ((BaseActivity) getActivity()).setHeaderName(TAG);
    }

    public void addNewFragmentNullBackStack(Fragment fragment, String TAG) {
        if (progress.isShowing()) {
            progress.dismiss();
        }

        if (((AppCompatActivity) getContext()) == null) {
            return;
        }

        FragmentManager fragmentManager = ((AppCompatActivity) getContext()).getSupportFragmentManager();

        if (fragmentManager == null) {
            return;
        }
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.setCustomAnimations(
                R.anim.enter_from_right, R.anim.exit_to_left,
                R.anim.enter_from_left, R.anim.exit_to_right);

        if (fragment != null) {
            isViewHidden = true;

            try {
                fragmentTransaction.replace(R.id.fragment_container, fragment, TAG).commit();
                BaseActivity.backbtn.setVisibility(View.VISIBLE);
            } catch (Exception e) {

            }

        } else {
            ToastUtil.showShortToast(getActivity(), R.string.service_not_available);
        }

        if (getActivity() != null)
            ((BaseActivity) getActivity()).setHeaderName(TAG);

    }


    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(String x) {
        //Log.d("Test Event", "onMessageEvent: ");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }


    @Override
    public void showProgress(int message) {
        progress.setLabel(getString(message));
        progress.show();
//        new Handler().postDelayed(new Runnable() {
//            @Override
//            public void run() {
//                if (progress!= null && progress.isShowing())
//                    progress.dismiss();
//            }
//        }, 15000);
    }

    @Override
    public void showProgress(int message, int label) {
        progress.setLabel(getString(message));
    }

    @Override
    public void showProgress() {
        progress.show();
//
//        new Handler().postDelayed(new Runnable() {
//            @Override
//            public void run() {
//                if (progress.isShowing())
//                    progress.dismiss();
//            }
//        }, 15000);
    }

    @Override
    public void showError(int error) {
        Toast.makeText(getContext(), getString(error), Toast.LENGTH_LONG).show();
        dismissProgress();


    }

    @Override
    public void dismissProgress() {
        if (progress != null && progress.isShowing()) {
            progress.dismiss();
        }
    }


}
