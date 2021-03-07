package com.paysky.upg.dialog;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
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

import javax.annotation.Nullable;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Optional;
import io.paysky.upg.data.network.model.response.DateTransactions;
import io.paysky.upg.mvp.sendemaildialog.SendEmailDialogPresenter;
import io.paysky.upg.mvp.sendemaildialog.SendEmailDialogView;
import io.paysky.upg.util.SessionManager;
import io.paysky.upg.util.ToastUtil;
import io.paysky.upg.util.ValidateUtil;
import io.paysky.upg.util.ViewUtil;

/**
 * Created by Amr Abd Elrhim on 15/07/2017.
 */

public class SendEmailDialog extends BaseDialog implements SendEmailDialogView {
    //Gui.
    private Activity activity;
    private DateTransactions dateTransactions;
    @BindView(R.id.userName)
    EditText emailInput;
    @BindView(R.id.login)
    LinearLayout login;
    @BindView(R.id.cancel)
    LinearLayout cancel;

    @BindView(R.id.userMobile)
    EditText userMobile;
    @BindView(R.id.mobileNumberLL)
    LinearLayout mobileNumberLL;
    @BindView(R.id.emailMobileTitle)
    TextView emailMobileTitle;
    @BindView(R.id.emailLL)
    LinearLayout emailLL;
    @BindView(R.id.iv_email)
    ImageView emailImageView;
    @BindView(R.id.iv_phone)
    ImageView phoneImageView;

    ////////////// This part is related to YallaVpos ///////////////////
    @Nullable
    @BindView(R.id.email_ll)
    LinearLayout email_ll;
    @Nullable
    @BindView(R.id.sms_ll)
    LinearLayout sms_ll;
    @Nullable
    @BindView(R.id.share_ll)
    LinearLayout share_ll;
    @Nullable
    @BindView(R.id.pay_link_email_ll)
    LinearLayout pay_link_email_ll;
    @Nullable
    @BindView(R.id.pay_link_mobile_ll)
    LinearLayout pay_link_mobile_ll;
    @Nullable
    @BindView(R.id.pay_link_send_close_ll)
    LinearLayout pay_link_send_close_ll;
    @Nullable
    @BindView(R.id.qrLink_ll)
    LinearLayout qrLink_ll;
    @Nullable
    @BindView(R.id.et_yalla_userMobile)
    EditText et_yalla_userMobile;


    @Nullable
    @BindView(R.id.iv_close)
    ImageView iv_close;

    @Nullable
    @BindView(R.id.et_yalla_userName)
    EditText et_yalla_userName;

    private String cardNumberLastFourDigits = "";
    private String receiptBody = "";

    //Objects.
    private SendEmailDialogPresenter sendEmailDialogPresenter = new SendEmailDialogPresenter();
    private String message;

    public SendEmailDialog(Activity activity, DateTransactions dateTransactions) {
        super(activity);
        this.activity = activity;
        this.dateTransactions = dateTransactions;
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().getAttributes().windowAnimations = R.style.DialogTheme;
        setContentView(R.layout.request_email);
        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(getWindow().getAttributes());
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        lp.gravity = Gravity.CENTER;
        getWindow().setAttributes(lp);
        getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        //Button Cancel = (Button) findViewById(R.id.Cancel);
        ButterKnife.bind(this);
        ViewUtil.alignRightIfAppRtl(emailInput);
        ViewUtil.preventCopyAndPaste(emailInput);
        ViewUtil.alignRightIfAppRtl(userMobile);
        ViewUtil.preventCopyAndPaste(userMobile);

//        userMobile.setText(SessionManager.getInstance().getEmpData().getM);

        mobileNumberLL.setVisibility(View.VISIBLE);
        //  emailMobileTitle.setText(R.string.upg_dialog_request_email_mobile_title);


        sendEmailDialogPresenter.attachView(this);
    }

    private void doCancelBusiness() {
        if (email_ll != null) {
            email_ll.getBackground().setAlpha(255);  // 25% transparent
        }

        if (sms_ll != null) {
            sms_ll.getBackground().setAlpha(255);  // 25% transparent
        }
        if (share_ll != null) {
            share_ll.getBackground().setAlpha(255);  // 25% transparent
        }

        if (qrLink_ll != null) {
            qrLink_ll.getBackground().setAlpha(255);  // 25% transparent
        }

        if (pay_link_email_ll != null) {
            pay_link_email_ll.setVisibility(View.GONE);
        }
        if (pay_link_mobile_ll != null) {
            pay_link_mobile_ll.setVisibility(View.GONE);
        }

        if (pay_link_send_close_ll != null) {
            pay_link_send_close_ll.setVisibility(View.GONE);
        }
    }

    private void showEmailLayout() {
        if (pay_link_email_ll != null) {
            pay_link_email_ll.setVisibility(View.VISIBLE);
        }
        if (pay_link_mobile_ll != null) {
            pay_link_mobile_ll.setVisibility(View.GONE);
        }

        if (share_ll != null) {
            share_ll.setAlpha(1.0f);
        }
        if (sms_ll != null) {
            sms_ll.setAlpha(1.0f);
        }

        if (email_ll != null) {
            email_ll.getBackground().setAlpha(255);  // 100% transparent
        }

        if (sms_ll != null) {
            sms_ll.getBackground().setAlpha(64);  // 25% transparent
        }
        if (share_ll != null) {
            share_ll.getBackground().setAlpha(64);  // 25% transparent
        }

        if (qrLink_ll != null) {
            qrLink_ll.getBackground().setAlpha(64);  // 25% transparent
        }

        if (pay_link_send_close_ll != null) {
            pay_link_send_close_ll.setVisibility(View.VISIBLE);
        }

        if (et_yalla_userName != null) {
            et_yalla_userName.setError(null);
        }
    }

    private void showMobileLayout() {

        if (pay_link_email_ll != null) {
            pay_link_email_ll.setVisibility(View.GONE);
        }
        if (email_ll != null) {
            email_ll.getBackground().setAlpha(64);  // 25% transparent
        }
        if (share_ll != null) {
            share_ll.getBackground().setAlpha(64);  // 25% transparent
        }

        if (qrLink_ll != null) {
            qrLink_ll.getBackground().setAlpha(64);  // 25% transparent
        }

        if (sms_ll != null) {
            sms_ll.getBackground().setAlpha(255);  // 100% transparent
        }

        if (pay_link_mobile_ll != null) {
            pay_link_mobile_ll.setVisibility(View.VISIBLE);
        }

        if (pay_link_send_close_ll != null) {
            pay_link_send_close_ll.setVisibility(View.VISIBLE);
        }
        if (et_yalla_userMobile != null) {
            et_yalla_userMobile.setError(null);
        }
    }

    @Optional
    @OnClick({R.id.sms_ll, R.id.email_ll, R.id.share_ll, R.id.cancel_pay_link_ll, R.id.send_pay_link_ll, R.id.qrLink_ll, R.id.iv_close})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.sms_ll:
                showMobileLayout();
                break;
            case R.id.email_ll:
                showEmailLayout();
                break;
            case R.id.share_ll:
                shareReceiptToSocialMedia();
                break;
            case R.id.send_pay_link_ll:
                sendEmailMobileReceipt();
                break;
            case R.id.cancel_pay_link_ll:
                doCancelBusiness();
                break;
            case R.id.iv_close:
                dismiss();
                break;
            case R.id.qrLink_ll:
                sendReceiptToQRCode();
                break;
        }
    }


    private void shareReceiptToSocialMedia() {


        if (dateTransactions.getCardNo() != null)
            if (dateTransactions.getCardNo().length() > 4)
                cardNumberLastFourDigits = dateTransactions.getCardNo().substring(dateTransactions.getCardNo().length() - 4);
            else cardNumberLastFourDigits = "";

        message = String.format("" + activity.getResources().getString(R.string.upg_general_receipt_share_social_media),
                SessionManager.getInstance().getEmpData().getPrimaryMerchant().getMerchantName(),
                dateTransactions.getFullTxnDateTime(),
                dateTransactions.getRRN(),
                dateTransactions.getCurrency(),
                dateTransactions.getAmnt(),
                cardNumberLastFourDigits);
        Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
        sharingIntent.setType("text/plain");
        sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT,
                activity.getResources().getString(R.string.upg_general_app_name));
        sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, message);

        Intent intent = Intent.createChooser(sharingIntent, activity.getResources()
                .getString(R.string.sharerecipt));
        activity.startActivity(intent);
    }

    private void sendReceiptToQRCode() {
        sendEmailDialogPresenter.getReceiptLink(dateTransactions.getTransactionId());
    }

    private void sendEmailMobileReceipt() {

        if (et_yalla_userName.getText().toString().isEmpty() && et_yalla_userMobile.getText().toString().isEmpty()) {
            et_yalla_userName.setError(activity.getString(R.string.error_field_required));
            et_yalla_userMobile.setError(activity.getString(R.string.error_field_required));
            return;
        } else if (!ValidateUtil.validMail(et_yalla_userName.getText().toString()) && !et_yalla_userName.getText().toString().isEmpty()) {
            et_yalla_userName.setError(activity.getString(R.string.error_email));
            if (!et_yalla_userMobile.getText().toString().isEmpty() && et_yalla_userMobile.getText().toString().length() < 11)
                et_yalla_userMobile.setError(activity.getString(R.string.validmobileNumber));
            return;
        } else if (!et_yalla_userMobile.getText().toString().isEmpty() && et_yalla_userMobile.getText().toString().length() < 11) {
            et_yalla_userMobile.setError(activity.getString(R.string.validmobileNumber));
            return;
        }

        if (!et_yalla_userName.getText().toString().isEmpty())
            sendEmailDialogPresenter.sendReceiptToEmail(et_yalla_userName.getText().toString().trim(), dateTransactions.getTransactionId(), dateTransactions.getTransactionChannel());

        if (!et_yalla_userMobile.getText().toString().isEmpty()) {
            sendEmailDialogPresenter.sendReceiptToMobile(et_yalla_userMobile.getText().toString().trim(), dateTransactions.getTransactionId());
        }
    }

    private void sendRequestToMail() {
        if (emailInput.getText().toString().isEmpty() && userMobile.getText().toString().isEmpty()) {
            ToastUtil.showShortToast(context, R.string.upg_dialog_request_email_mobile_title);
            return;
        } else if (userMobile.getText().toString().isEmpty() && emailInput.getText().toString().isEmpty()) {
            ToastUtil.showShortToast(context, R.string.upg_dialog_request_email_mobile_title);
            return;
        } else if (!ValidateUtil.validMail(emailInput.getText().toString()) && !emailInput.getText().toString().isEmpty()) {
            emailInput.setError(activity.getString(R.string.error_email));
            if (!userMobile.getText().toString().isEmpty() && userMobile.getText().toString().length() < 11)
                userMobile.setError(activity.getString(R.string.validmobileNumber));
            return;
        } else if (!userMobile.getText().toString().isEmpty() && userMobile.getText().toString().length() < 11) {
            userMobile.setError(activity.getString(R.string.validmobileNumber));
            return;
        }

        ((MainActivity) activity).showProgress(R.string.sending_mail);

        if (!emailInput.getText().toString().isEmpty())
            sendEmailDialogPresenter.sendReceiptToEmail(emailInput.getText().toString().trim(), dateTransactions.getTransactionId(), dateTransactions.getTransactionChannel());

        if (!userMobile.getText().toString().isEmpty()) {
            sendEmailDialogPresenter.sendReceiptToMobile(userMobile.getText().toString().trim(), dateTransactions.getTransactionId());
        }
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
        focusView(emailInput);
    }

    private void focusView(final View view) {
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
    public void dismissProgress() {
        ((MainActivity) activity).dismissProgress();
        super.dismiss();
    }


    @Override
    public void dismiss() {
        sendEmailDialogPresenter.detachView();
        super.dismiss();
    }

    @Override
    public void showCompletedDialog(String receiptLink) {
        CompletedDialog completedDialog = new CompletedDialog(activity);
        completedDialog.SetData(receiptLink);
        if (!completedDialog.isShowing()) completedDialog.show();
        closeDialog();
    }

    @Override
    public void closeDialog() {
        sendEmailDialogPresenter.detachView();
        super.dismiss();
    }

}
