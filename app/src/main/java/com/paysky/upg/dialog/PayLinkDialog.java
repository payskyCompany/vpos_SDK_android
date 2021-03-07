package com.paysky.upg.dialog;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.blankj.utilcode.util.ActivityUtils;
import com.paysky.upg.R;

import javax.annotation.Nullable;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Optional;
import io.paysky.upg.data.network.model.request.InitiateOrderRequest;
import io.paysky.upg.data.network.model.response.InitiateOrderResponse;
import io.paysky.upg.mvp.paylinkdialog.PayLinkDialogPresenter;
import io.paysky.upg.mvp.paylinkdialog.PayLinkDialogView;
import io.paysky.upg.util.ValidateUtil;


/**
 * Created by Amr Abd Elrhim on 15/07/2017.
 */

public class PayLinkDialog extends BaseDialog implements PayLinkDialogView {
    private Activity activity;

    TextView describtion;
    TextView headertitle;
    ImageView shareitem;

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
    @BindView(R.id.cancel_pay_link_ll)
    LinearLayout cancel_pay_link_ll;
    @Nullable
    @BindView(R.id.send_pay_link_ll)
    LinearLayout send_pay_link_ll;
    @Nullable
    @BindView(R.id.userName)
    EditText userName;
    @Nullable
    @BindView(R.id.userMobile)
    EditText userMobile;
    @Nullable
    @BindView(R.id.iv_close)
    ImageView iv_close;

    @Nullable
    @BindView(R.id.qrLink_ll)
    LinearLayout qrLink_ll;


    private String emailOrMobile = "";

    //Objects.
    private PayLinkDialogPresenter payLinkDialogPresenter;
    private String NotificationMethod;

    private InitiateOrderRequest initiateOrderRequest;
    private InitiateOrderResponse initiateOrderResponse;

    public PayLinkDialog(final Activity activity) {
        super(activity);
        this.activity = activity;
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().getAttributes().windowAnimations = R.style.DialogTheme;
        payLinkDialogPresenter = new PayLinkDialogPresenter();
        payLinkDialogPresenter.attachView(this);

        setContentView(R.layout.pay_link_dialog);
        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        ButterKnife.bind(this);
        lp.copyFrom(getWindow().getAttributes());
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        lp.gravity = Gravity.CENTER;

        getWindow().setAttributes(lp);
        getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        LinearLayout Cancel = (LinearLayout) findViewById(R.id.cancel_action);
        headertitle = (TextView) findViewById(R.id.headertitle);
        describtion = (TextView) findViewById(R.id.describtion);
        shareitem = (ImageView) findViewById(R.id.shareitem);
        headertitle.setText(activity.getString(R.string.upg_generalshare_by_your_link));

        Cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });


        describtion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(initiateOrderResponse.getOrderurl()));
                ActivityUtils.startActivity(browserIntent);
            }
        });

    }


    public void SetData(InitiateOrderRequest initiateOrderRequest,
                        InitiateOrderResponse initiateOrderResponse) {
        this.initiateOrderRequest = initiateOrderRequest;
        this.initiateOrderResponse = initiateOrderResponse;
    }


    public void SetTitle(String x) {
        describtion.setText(x);
    }

    @Optional
    @OnClick({R.id.sms_ll, R.id.email_ll, R.id.share_ll, R.id.cancel_pay_link_ll, R.id.send_pay_link_ll, R.id.iv_close, R.id.qrLink_ll})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.sms_ll:
                showMobileLayout();
                break;
            case R.id.email_ll:
                showEmailLayout();
                break;
            case R.id.share_ll:
                generatePayLinkSocial("Yalla.Paylink@PaySky.io");
                break;
            case R.id.send_pay_link_ll:
                initiatePayLinkOrder();
                break;
            case R.id.cancel_pay_link_ll:
                doCancelBusiness();
                break;
            case R.id.iv_close:
                dismiss();
                break;
            case R.id.qrLink_ll:
                generatePayLinkQR();
                break;
        }
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
        NotificationMethod = "0";
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

        if (userName != null) {
            userName.setError(null);
        }
    }

    private void showMobileLayout() {
        NotificationMethod = "1";

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
        if (userMobile != null) {
            userMobile.setError(null);
        }
    }

    private void initiatePayLinkOrder() {
        if (userName.getText().toString().isEmpty() && userMobile.getText().toString().isEmpty()) {
            userName.setError(activity.getString(R.string.error_field_required));
            userMobile.setError(activity.getString(R.string.error_field_required));
            return;
        } else if (!ValidateUtil.validMail(userName.getText().toString()) && !userName.getText().toString().isEmpty()) {
            userName.setError(activity.getString(R.string.error_email));
            if (!userMobile.getText().toString().isEmpty() && userMobile.getText().toString().length() < 11)
                userMobile.setError(activity.getString(R.string.validmobileNumber));
            return;
        } else if (!userMobile.getText().toString().isEmpty() && userMobile.getText().toString().length() < 11) {
            userMobile.setError(activity.getString(R.string.validmobileNumber));
            return;
        }

        if (!userName.getText().toString().isEmpty() && !userName.getText().toString().equals("")) {
            emailOrMobile = userName.getText().toString();
        }

        if (!userMobile.getText().toString().isEmpty() && !userMobile.getText().toString().equals("")) {
            emailOrMobile = userMobile.getText().toString();
        }

        payLinkDialogPresenter.initiateOrder(false, false, initiateOrderRequest.getTerminalId(), initiateOrderRequest.getCurrency(), initiateOrderRequest.getCurrencyName(),
                initiateOrderRequest.getExpiryDateTime(), initiateOrderRequest.getPayerName(), NotificationMethod,
                emailOrMobile, initiateOrderRequest.getMerchantReference(), initiateOrderRequest.getAmount(),
                Double.parseDouble(initiateOrderRequest.getAmountTrxn()), initiateOrderRequest.getMaxNumberOfPayment(), "", initiateOrderRequest.getMessage());
    }

    private void generatePayLinkSocial(String emailAddress) {
        payLinkDialogPresenter.initiateOrder(true, false, initiateOrderRequest.getTerminalId(), initiateOrderRequest.getCurrency(), initiateOrderRequest.getCurrencyName(),
                initiateOrderRequest.getExpiryDateTime(), initiateOrderRequest.getPayerName(), "0",
                emailAddress, initiateOrderRequest.getMerchantReference(), initiateOrderRequest.getAmount(),
                Double.parseDouble(initiateOrderRequest.getAmountTrxn()), initiateOrderRequest.getMaxNumberOfPayment(), "", initiateOrderRequest.getMessage());
    }

    private void generatePayLinkQR() {
        payLinkDialogPresenter.initiateOrder(false, true, initiateOrderRequest.getTerminalId(), initiateOrderRequest.getCurrency(), initiateOrderRequest.getCurrencyName(),
                initiateOrderRequest.getExpiryDateTime(), initiateOrderRequest.getPayerName(), "0",
                "Yalla.Paylink@PaySky.io", initiateOrderRequest.getMerchantReference(), initiateOrderRequest.getAmount(),
                Double.parseDouble(initiateOrderRequest.getAmountTrxn()), initiateOrderRequest.getMaxNumberOfPayment(), "", initiateOrderRequest.getMessage());
    }


    @Override
    public void closeDialog() {
        payLinkDialogPresenter.detachView();
        super.dismiss();
    }


    @Override
    public void showCompletedDialog(boolean fromShare, boolean fromScanQrCode, InitiateOrderRequest InitiateOrder, InitiateOrderResponse body) {
        CompletedDialog completedDialog = new CompletedDialog(activity);
        if (!completedDialog.isShowing()) completedDialog.show();
        if (fromShare) {
            completedDialog.SetData(InitiateOrder, body);
        }
        if (fromScanQrCode)
            completedDialog.SetData(fromScanQrCode, body);
        closeDialog();
    }

}