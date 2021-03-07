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

import java.text.ParseException;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import faranjit.currency.edittext.CurrencyEditText;
import io.paysky.upg.data.network.model.response.DateTransactions;
import io.paysky.upg.mvp.refunddialog.RefundDialogPresenter;
import io.paysky.upg.mvp.refunddialog.RefundDialogView;
import io.paysky.upg.util.ViewUtil;

/**
 * Created by Amr Abd Elrhim on 15/07/2017.
 */

public class ReFundDialog extends BaseDialog implements RefundDialogView {
    //GUI
    private Activity activity;
    @BindView(R.id.amount)
    CurrencyEditText amount;
    @BindView(R.id.refundReson)
    EditText refundReson;
    @BindView(R.id.login)
    LinearLayout login;
    @BindView(R.id.cancel)
    LinearLayout cancel;
    @BindView(R.id.refundLabel)
    TextView refundLabel;
    @BindView(R.id.hedertitle)
    TextView hedertitle;
    @BindView(R.id.iv_reason)
    ImageView reasonImageView;
    @BindView(R.id.iv_amount)
    ImageView amountImageView;
    //Objects.
    private RefundDialogPresenter refundDialogPresenter = new RefundDialogPresenter();
    private DateTransactions dateTransactions;

    @Override
    public void show() {
        super.show();

        focusView(amount);
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
                        if (amount.getText().toString().length() > 0) {
                            amount.setSelection(amount.getText().toString().length());
                        }
                    }
                }, 100);
            }
        }, 500);
    }


    public ReFundDialog(Activity activity, DateTransactions dateTransactions) {
        super(activity);
        refundDialogPresenter.attachView(this);
        this.activity = activity;
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().getAttributes().windowAnimations = R.style.DialogTheme;
        setContentView(R.layout.refund_dilog);
        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(getWindow().getAttributes());
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        lp.gravity = Gravity.CENTER;
        getWindow().setAttributes(lp);
        getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        //Button Cancel = (Button) findViewById(R.id.Cancel);
        ButterKnife.bind(this);
        this.dateTransactions = dateTransactions;
        ViewUtil.preventCopyAndPaste(amount, refundReson);
        if (dateTransactions.getRefundButton() == 1) {
            refundReson.setHint(activity.getString(R.string.VoidReason));
            refundLabel.setText(activity.getString(R.string.Void));
            hedertitle.setText(activity.getString(R.string.voiddetail));
        } else {
            refundReson.setHint(activity.getString(R.string.upg_receipt_Refund_Reason_label));
            refundLabel.setText(activity.getString(R.string.upg_general_Refund));
            hedertitle.setText(activity.getString(R.string.upg_dialog_refund_card_title_label));
        }
    }


    @OnClick(R.id.login)
    public void login() {
        boolean hasError = false;


        if (amount.getText().toString().isEmpty() || amount.getText().toString().equals("0.00")) {
            amount.setError(activity.getString(R.string.error_field_required));
            hasError = true;

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

        if (reason.isEmpty()) {
            refundReson.setError(activity.getString(R.string.error_field_required));
            hasError = true;
        }
        if (hasError) {
            return;
        }

        try {
            double amountValue = amount.getCurrencyDouble();


            refundDialogPresenter.refund(amountValue, reason, dateTransactions);
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    @OnClick(R.id.cancel)

    public void cancel() {
        dismiss();
    }

    @Override
    public void dismiss() {
        refundDialogPresenter.detachView();
        super.dismiss();
    }


}
