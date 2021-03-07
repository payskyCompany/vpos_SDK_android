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

import com.alihafizji.library.CreditCardEditText;
import com.paysky.upg.R;
import com.paysky.upg.activity.MainActivity;
import com.paysky.upg.customviews.CardsValidation;
import com.paysky.upg.customviews.CreditCardEditTextPattern;
import com.paysky.upg.customviews.ExpiryDate;

import java.text.ParseException;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import faranjit.currency.edittext.CurrencyEditText;
import io.paysky.upg.data.network.model.BaseResponse;
import io.paysky.upg.data.network.model.response.DateTransactions;
import io.paysky.upg.mvp.refundcarddialog.RefundCardDialogPresenter;
import io.paysky.upg.mvp.refundcarddialog.RefundCardDialogView;
import io.paysky.upg.util.ValidateUtil;
import io.paysky.upg.util.ViewUtil;


/**
 * Created by Amr Abd Elrhim on 15/07/2017.
 */

public class ReFundCardDialog extends BaseDialog implements RefundCardDialogView {
    //GUI.
    private Activity activity;
    @BindView(R.id.amount)
    CurrencyEditText amount;

    @BindView(R.id.refundReson)
    EditText refundReson;

    @BindView(R.id.PAN)
    CreditCardEditText PAN;

    @BindView(R.id.DateExpiration)
    ExpiryDate DateExpiration;

    @BindView(R.id.refundResonLL)
    LinearLayout refundResonLL;

    @BindView(R.id.card_holderLL)
    LinearLayout card_holderLL;

    @BindView(R.id.CVV2)
    EditText CVV2;

    @BindView(R.id.card_holder)
    EditText card_holder;

    @BindView(R.id.login)
    LinearLayout login;

    @BindView(R.id.DELL)
    LinearLayout DELL;

    @BindView(R.id.cancel)
    LinearLayout cancel;

    @BindView(R.id.LLCCV)
    LinearLayout LLCCV;

    @BindView(R.id.LLPAN)
    LinearLayout LLPAN;

    @BindView(R.id.refundLabel)
    TextView refundLabel;

    @BindView(R.id.hedertitle)
    TextView headerTitle;

    @BindView(R.id.iv_username)
    ImageView iv_username;
    @BindView(R.id.iv_calendar_visa)
    ImageView iv_calendar_visa;
    @BindView(R.id.iv_visa)
    ImageView iv_visa;
    @BindView(R.id.iv_password)
    ImageView iv_password;
    @BindView(R.id.iv_amount)
    ImageView iv_amount;
    @BindView(R.id.iv_reason)
    ImageView iv_reason;

    //Objects.
    private RefundCardDialogPresenter refundCardDialogPresenter;
    private DateTransactions dateTransactions;


    String expireDate = "";
    String PANValue = "";
    boolean refund;

    public ReFundCardDialog(Activity activity, DateTransactions dateTransactions, boolean refund) {
        super(activity);
        this.activity = activity;
        this.dateTransactions = dateTransactions;
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().getAttributes().windowAnimations = R.style.DialogTheme;
        setContentView(R.layout.refund_card_dilog);
        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(getWindow().getAttributes());
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        lp.gravity = Gravity.CENTER;
        getWindow().setAttributes(lp);
        getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        //Button Cancel = (Button) findViewById(R.id.Cancel);
        ButterKnife.bind(this);
        refundCardDialogPresenter = new RefundCardDialogPresenter();
        refundCardDialogPresenter.attachView(this);


        ViewUtil.preventCopyAndPaste(amount, refundReson, PAN, DateExpiration, CVV2);

        PAN.setCreditCardEditTextListener(new CreditCardEditTextPattern(activity));

        if (!dateTransactions.isISForceSendCVCForRefund()) {
            LLCCV.setVisibility(View.GONE);
        }

        this.refund = refund;

        if (dateTransactions.getRefundButton() == 1) {
            refundReson.setHint(activity.getString(R.string.VoidReason));
            refundLabel.setText(activity.getString(R.string.Void));
            headerTitle.setText(activity.getString(R.string.voiddetail));
        } else {
            refundReson.setHint(activity.getString(R.string.upg_receipt_Refund_Reason_label));
            refundLabel.setText(activity.getString(R.string.upg_general_Refund));
            headerTitle.setText(activity.getString(R.string.upg_dialog_refund_card_title_label));
        }


        if (dateTransactions.isHasToken()) {
            LLCCV.setVisibility(View.GONE);
            DELL.setVisibility(View.GONE);
            LLPAN.setVisibility(View.GONE);
        }

        if (!refund) {
            headerTitle.setText(activity.getString(R.string.capturedetail));
            refundLabel.setText(activity.getString(R.string.upg_general_capture));
            refundResonLL.setVisibility(View.GONE);
            if (!dateTransactions.isHasToken()) {
                card_holderLL.setVisibility(View.VISIBLE);
            }


        }
    }


    @Override
    public void show() {
        super.show();
        focusView(PAN);
    }

    private void focusView(final View view) {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                InputMethodManager imm = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.showSoftInput(view, InputMethodManager.SHOW_IMPLICIT);
                view.dispatchTouchEvent(MotionEvent.obtain(SystemClock.uptimeMillis(), SystemClock.uptimeMillis(), MotionEvent.ACTION_DOWN, 0, 0, 0));
                view.dispatchTouchEvent(MotionEvent.obtain(SystemClock.uptimeMillis(), SystemClock.uptimeMillis(), MotionEvent.ACTION_UP, 0, 0, 0));
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        if (PAN.getText().toString().length() > 0) {
                            PAN.setSelection(PAN.getText().toString().length());
                        }
                    }
                }, 100);
            }
        }, 500);
    }


    @OnClick(R.id.login)
    public void Login() {
        boolean hasError = false;
        if (amount.getText().toString().isEmpty() || amount.getText().toString().equals("0.00")) {
            amount.setError(activity.getString(R.string.error_field_required));
            hasError = true;
        }

        if (!dateTransactions.isHasToken()) {

            if (card_holderLL.getVisibility() == View.VISIBLE) {
                if (card_holder.getText().toString().isEmpty()) {
                    card_holder.setError(activity.getString(R.string.error_field_required));
                    hasError = true;
                }
            }

            if (dateTransactions.isISForceSendCVCForRefund()) {

                if (CVV2.getText().toString().isEmpty()) {
                    CVV2.setError(activity.getString(R.string.error_field_required));
                    hasError = true;
                }

                if (!CVV2.getText().toString().isEmpty() && CVV2.getText().toString().length() < 3) {
                    CVV2.setError(activity.getString(R.string.invalid_ccv));
                    hasError = true;
                }

            }
            if (DateExpiration.getText().toString().isEmpty()) {
                DateExpiration.setError(activity.getString(R.string.error_field_required));
                hasError = true;
            }


            try {

                if (!ValidateUtil.isAfterToday(DateExpiration.getYear().toInt(), DateExpiration.getMonth().toInt(), 1)) {
                    DateExpiration.setError(activity.getString(R.string.date_error));
                    hasError = true;
                }
                expireDate = DateExpiration.getYear().getSameDate() + "" + DateExpiration.getMonth();

            } catch (Exception e) {

            }


            if (PAN.getText().toString().isEmpty()) {
                PAN.setError(activity.getString(R.string.error_field_required));
                hasError = true;
            }


            PANValue = PAN.getCreditCardNumber();


            if (!CardsValidation.luhnCheck(PANValue)) {
                amount.setError(activity.getString(R.string.invalid_card_number_length));
                hasError = true;
            }

        }
        try {
            if (amount.getCurrencyDouble() > Double.parseDouble(dateTransactions.getAmntNumber())) {
                amount.setError(activity.getString(R.string.Therefundamount));
                hasError = true;

            }
        } catch (ParseException e) {
            e.printStackTrace();
        }

        String reason = refundReson.getText().toString().trim();

        if (reason.isEmpty() && refundResonLL.getVisibility() == View.VISIBLE) {
            refundReson.setError(activity.getString(R.string.error_field_required));
            hasError = true;
        }


        if (hasError) {
            return;
        }


        try {
            refundCardDialogPresenter.refund("", "", amount.getCurrencyDouble(),
                    CVV2.getText().toString().trim(), expireDate, PANValue,
                    refundReson.getText().toString().trim(), dateTransactions, refund,
                    card_holder.getText().toString());

        } catch (ParseException e) {
            e.printStackTrace();
        }

//        amount.setText("");
//        CVV2.setText("");
//        PAN.setText("");
//        DateExpiration.setText("");
//        refundReson.setText("");
//        card_holder.setText("");
//        refundLabel.setText("");
    }

    @OnClick(R.id.cancel)
    public void cancel() {
        dismiss();
    }


    @Override
    public void showProgress(int message) {
        ((MainActivity) activity).showProgress(message);
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
    public void dismiss() {
        refundCardDialogPresenter.detachView();
        super.dismiss();
    }

    @Override
    public void completeRequest(BaseResponse baseResponse) {
        dismiss();

    }
}