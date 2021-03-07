package com.paysky.upg.dialog;

import android.app.Dialog;
import android.content.Context;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.paysky.upg.activity.MainActivity;

import io.paysky.upg.base.BaseView;

public class BaseDialog extends Dialog implements BaseView {
    Context context;

    public BaseDialog(@NonNull Context context) {
        super(context);

        this.context = context;

    }

    public BaseDialog(@NonNull Context context, int themeResId) {

        super(context, themeResId);
        this.context = context;

    }

    protected BaseDialog(@NonNull Context context, boolean cancelable, @Nullable OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
        this.context = context;

    }


    @Override
    public void showProgress(int message) {
        ((MainActivity) context).showProgress(message);
    }

    @Override
    public void showProgress(int message, int label) {
        ((MainActivity) context).showProgress();

    }

    @Override
    public void showProgress() {
        ((MainActivity) context).showProgress();

    }

    @Override
    public void showError(int string) {

    }

    @Override
    public void dismissProgress() {
        ((MainActivity) context).dismissProgress();

    }
}
