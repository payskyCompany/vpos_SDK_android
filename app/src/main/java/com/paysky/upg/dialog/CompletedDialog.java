package com.paysky.upg.dialog;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import com.paysky.upg.R;

import javax.annotation.Nullable;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.paysky.upg.data.network.model.request.InitiateOrderRequest;
import io.paysky.upg.data.network.model.response.InitiateOrderResponse;
import io.paysky.upg.util.DateTimeUtil;
import io.paysky.upg.util.SessionManager;

public class CompletedDialog extends Dialog {

    private Activity activity;
    private InitiateOrderRequest initiateOrderRequest;
    private InitiateOrderResponse initiateOrderResponse;


    @Nullable
    @BindView(R.id.ll_qr_scan)
    LinearLayout ll_qr_scan;

    @Nullable
    @BindView(R.id.iv_qr_scan)
    ImageView iv_qr_scan;

    @Nullable
    @BindView(R.id.ll_share_completed)
    LinearLayout ll_share_completed;


    @Nullable
    @BindView(R.id.tv_link_completed)
    TextView tv_link_completed;

    public CompletedDialog(final Activity activity) {
        super(activity);
        this.activity = activity;
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().getAttributes().windowAnimations = R.style.DialogTheme;

        setContentView(R.layout.dialog_completed);
        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        ButterKnife.bind(this);
        lp.copyFrom(getWindow().getAttributes());
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        lp.gravity = Gravity.CENTER;

        getWindow().setAttributes(lp);
        getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        Button close = (Button) findViewById(R.id.btn_close);

        close.setOnClickListener(v -> dismiss());
    }


    void SetData(InitiateOrderRequest initiateOrderRequest, InitiateOrderResponse initiateOrderResponse) {
        this.initiateOrderRequest = initiateOrderRequest;
        this.initiateOrderResponse = initiateOrderResponse;
        ShareLink();
        dismiss();
    }

    void SetData(String receiptLink) {
        if (ll_qr_scan != null) {
            ll_qr_scan.setVisibility(View.VISIBLE);
        }
        generateQr(receiptLink);
        if (ll_share_completed != null) {
            ll_share_completed.setVisibility(View.GONE);
        }
        if (tv_link_completed != null) {
            tv_link_completed.setText(activity.getString(R.string.upg_general_qr_link_title));
        }
    }

    void SetData(boolean fromQrLink, InitiateOrderResponse initiateOrderResponse) {
        this.initiateOrderResponse = initiateOrderResponse;
        if (ll_qr_scan != null) {
            ll_qr_scan.setVisibility(View.VISIBLE);
        }
        generateQr(initiateOrderResponse.getOrderurl());
        if (ll_share_completed != null) {
            ll_share_completed.setVisibility(View.GONE);
        }
        if (fromQrLink) {
            if (tv_link_completed != null) {
                tv_link_completed.setText(activity.getString(R.string.upg_general_qr_link_title));
            }
        }
    }


    private void generateQr(String orderUrl) {

        QRCodeWriter writer = new QRCodeWriter();
        try {
            BitMatrix bitMatrix = writer.encode(orderUrl, BarcodeFormat.QR_CODE, 512, 512);
            int width = bitMatrix.getWidth();
            int height = bitMatrix.getHeight();
            Bitmap bmp = Bitmap.createBitmap(width, height, Bitmap.Config.RGB_565);
            for (int x = 0; x < width; x++) {
                for (int y = 0; y < height; y++) {
                    bmp.setPixel(x, y, bitMatrix.get(x, y) ? Color.BLACK : Color.WHITE);
                }
            }
            if (iv_qr_scan != null) {
                iv_qr_scan.setImageBitmap(bmp);
            }

        } catch (WriterException e) {
            e.printStackTrace();
        }

    }

    private void ShareLink() {
        double amountTrxn = Double.parseDouble(initiateOrderRequest.getAmountTrxn()) / 100;
        String message = "";

        if (initiateOrderRequest.getMerchantReference().isEmpty()) {
            message = String.format("" +
                            activity.getResources().getString(R.string.sharePayLinkWithoutRef),
                    SessionManager.getInstance().getEmpData().getPrimaryMerchant().getMerchantName(),
                    initiateOrderRequest.getCurrencyName(),
                    amountTrxn,
                    initiateOrderResponse.getOrderurl(),
                    DateTimeUtil.getDateWithHoursFromString(initiateOrderRequest.getExpiryDateTime()));
        } else {
            message = String.format("" + activity.getResources().getString(R.string.sharePayLink),
                    SessionManager.getInstance().getEmpData().getPrimaryMerchant().getMerchantName(),
                    initiateOrderRequest.getCurrencyName(),
                    amountTrxn,
                    initiateOrderRequest.getMerchantReference(),
                    initiateOrderResponse.getOrderurl(),
                    DateTimeUtil.getDateWithHoursFromString(initiateOrderRequest.getExpiryDateTime()));
        }

        Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
        sharingIntent.setType("text/plain");
        sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT,
                activity.getResources().getString(R.string.upg_general_app_name));
        sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, message
        );

        Intent intent = Intent.createChooser(sharingIntent, activity.getResources()
                .getString(R.string.sharerecipt));
        activity.startActivity(intent);
    }

}
