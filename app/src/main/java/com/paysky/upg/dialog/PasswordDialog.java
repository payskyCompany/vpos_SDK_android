package com.paysky.upg.dialog;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Handler;
import android.os.SystemClock;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.paysky.upg.R;
import com.paysky.upg.activity.MainActivity;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.paysky.upg.data.network.model.BaseResponse;
import io.paysky.upg.data.network.model.response.DateTransactions;
import io.paysky.upg.mvp.passworddialog.PasswordDialogPresenter;
import io.paysky.upg.mvp.passworddialog.PasswordDialogView;
import io.paysky.upg.util.ToastUtil;
import io.paysky.upg.util.ViewUtil;


/**
 * Created by Amr Abd Elrhim on 15/07/2017.
 */

public class PasswordDialog extends BaseDialog implements PasswordDialogView {

    //GUI
    private Activity activity;
    @BindView(R.id.userName)
    EditText emailInput;
    @BindView(R.id.send)
    TextView send;


    @BindView(R.id.login)
    LinearLayout login;
    @BindView(R.id.cancel)
    LinearLayout cancel;

    @BindView(R.id.iv_pin)
    ImageView pinImageView;

    public PasswordDialogListener passwordDialogListener;
    //Objects.
    private PasswordDialogPresenter passwordDialogPresenter;

    public PasswordDialog(Activity activity, DateTransactions dateTransactions) {
        super(activity);
        this.activity = activity;
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().getAttributes().windowAnimations = R.style.DialogTheme;
        passwordDialogPresenter = new PasswordDialogPresenter();
        passwordDialogPresenter.attachView(this);
        setContentView(R.layout.request_password);
        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(getWindow().getAttributes());
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        lp.gravity = Gravity.CENTER;
        getWindow().setAttributes(lp);
        getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        ButterKnife.bind(this);
        ViewUtil.alignRightIfAppRtl(emailInput);
        ViewUtil.preventCopyAndPaste(emailInput);

        passwordDialogPresenter.setDateTransaction(dateTransactions);


    }


    private void sendRequestToMail() {

        if (emailInput.getText().toString().isEmpty()) {
            emailInput.setError(activity.getString(R.string.error_field_required));
            return;
        }

        ((MainActivity) activity).showProgress(R.string.loading);
        // call presenter

        passwordDialogPresenter.checkOnlinePin("", "", emailInput.getText().toString().trim());
    }

    @OnClick(R.id.login)
    public void Login() {
        sendRequestToMail();
    }

    @OnClick(R.id.cancel)

    public void cancel() {

        dismiss();
    }


    @Override
    public void show() {
        super.show();

        FocusView(emailInput);
    }


    private void FocusView(final View view) {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                InputMethodManager imm = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.showSoftInput(view, InputMethodManager.SHOW_IMPLICIT);
                view.dispatchTouchEvent(MotionEvent.obtain(SystemClock.uptimeMillis(), SystemClock.uptimeMillis(), MotionEvent.ACTION_DOWN, 0, 0, 0));
                view.dispatchTouchEvent(MotionEvent.obtain(SystemClock.uptimeMillis(), SystemClock.uptimeMillis(), MotionEvent.ACTION_UP, 0, 0, 0));
            }
        }, 500);
    }


    @Override
    public void showProgress(int message) {

    }

    @Override
    public void showProgress(int message, int label) {

    }

    @Override
    public void showProgress() {

    }

    @Override
    public void dismissProgress() {
        ((MainActivity) activity).dismissProgress();
    }


    @Override
    public void closeDialog() {
        passwordDialogPresenter.detachView();
        super.dismiss();
    }

    @Override
    public void showRefundDialog(DateTransactions dateTransaction) {
        ReFundCardDialog reFundDialog = new ReFundCardDialog(activity, dateTransaction, true);
        if (!reFundDialog.isShowing()) reFundDialog.show();
    }

    @Override
    public void showRefundTahweelDialog(DateTransactions dateTransaction) {
        ReFundDialog reFundDialog = new ReFundDialog(activity, dateTransaction);
        if (!reFundDialog.isShowing()) reFundDialog.show();
    }

    @Override
    public void completeVoidAuthTransaction(BaseResponse body) {
        ToastUtil.showLongToast(activity, activity.getString(R.string.transaction_auth_void));
        dismiss();
    }

    @Override
    public void passwordSuccessfully() {
        closeDialog();
        passwordDialogListener.passwordSuccessfully();
    }

    public void setPasswordDialogListener(PasswordDialogListener passwordDialogListener) {
        this.passwordDialogListener = passwordDialogListener;
    }

    public interface PasswordDialogListener {

        void passwordSuccessfully();

    }

}