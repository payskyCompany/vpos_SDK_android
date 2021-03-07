package com.paysky.upg.dialog;

import android.app.Activity;
import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.paysky.upg.R;

import io.paysky.upg.data.network.model.LoginUpgMerchant;
import io.paysky.upg.util.SessionManager;

/**
 * Created by Amr Abd Elrhim on 15/07/2017.
 */

public class CashOutDialog extends Dialog {
    private Activity activity;

    TextView describtion;
    TextView headertitle;

    TextView meezaDigitalMerchantId;

    TextView meezaDigitalMerchantIdTitle;

    public CashOutDialog(Activity activity, final View.OnClickListener onClickListener) {
        super(activity);
        this.activity = activity;
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().getAttributes().windowAnimations = R.style.DialogTheme;

        setContentView(R.layout.cash_out_dilog);

        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(getWindow().getAttributes());
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        lp.gravity = Gravity.CENTER;

        getWindow().setAttributes(lp);
        getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        LinearLayout Cancel = findViewById(R.id.cancel_action);
        headertitle = findViewById(R.id.headertitle);
        describtion = findViewById(R.id.code);
        meezaDigitalMerchantId = findViewById(R.id.tv_meezaDigitalMerchantId);
        meezaDigitalMerchantIdTitle = findViewById(R.id.title_tv_meezaDigitalMerchantId);

        LoginUpgMerchant empData = SessionManager.getInstance().getEmpData();

        if (empData.isIsPrimaryMerchant()) {
            meezaDigitalMerchantId.setText(empData.getPrimaryMerchant().getTahweelParentMobile());

        } else {
            meezaDigitalMerchantId.setText(empData.getDigQrTerminal().getTahweelChildMerchantId());
        }

        if (meezaDigitalMerchantId.getText().toString().isEmpty() || !empData.isIsTahweel()) {
            meezaDigitalMerchantId.setVisibility(View.GONE);
            meezaDigitalMerchantIdTitle.setVisibility(View.GONE);
        }

        Cancel.setOnClickListener(onClickListener);


    }

    public void SetTitle(String x) {
        describtion.setText(x);

    }


}
