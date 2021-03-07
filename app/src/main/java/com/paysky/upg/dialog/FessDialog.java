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

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Amr Abd Elrhim on 15/07/2017.
 */

public class FessDialog extends Dialog {
    @BindView(R.id.to)
    TextView to;
    @BindView(R.id.amount)
    TextView amount;
    @BindView(R.id.fess)
    TextView fess;
    @BindView(R.id.total)
    TextView total;
    @BindView(R.id.cancel_action)
    LinearLayout cancelAction;
    @BindView(R.id.comfirm)
    LinearLayout comfirm;


    public FessDialog(Activity activity) {
        super(activity);
        Activity activity1 = activity;
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().getAttributes().windowAnimations = R.style.DialogTheme;
        setContentView(R.layout.fess_dialog);
        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(getWindow().getAttributes());
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        lp.gravity = Gravity.CENTER;
        getWindow().setAttributes(lp);
        getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        ButterKnife.bind(this);


    }


    @OnClick({R.id.cancel_action, R.id.comfirm})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.cancel_action:
                break;
            case R.id.comfirm:
                break;
        }
    }
}
