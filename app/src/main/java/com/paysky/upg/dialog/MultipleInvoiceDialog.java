package com.paysky.upg.dialog;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;

import com.paysky.upg.R;

public class MultipleInvoiceDialog extends Dialog {

    MultipleInvoiceAction multipleInvoiceAction;
    Activity activity;

    public MultipleInvoiceDialog(final Activity activity, MultipleInvoiceAction multipleInvoiceAction) {
        super(activity);
        this.activity = activity;
        this.multipleInvoiceAction = multipleInvoiceAction;
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().getAttributes().windowAnimations = R.style.DialogTheme;

        setContentView(R.layout.multiple_invoice_dialog);
        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(getWindow().getAttributes());
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        lp.gravity = Gravity.CENTER;

        getWindow().setAttributes(lp);
        getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        LinearLayout Cancel = (LinearLayout) findViewById(R.id.cancel_action);
        @SuppressLint("WrongViewCast") LinearLayout comfirm = (LinearLayout) findViewById(R.id.comfirm);
        Cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                multipleInvoiceAction.cancelBtnAction(true);
                dismiss();
            }
        });
        comfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
                multipleInvoiceAction.cancelBtnAction(false);
            }
        });
    }


    public interface MultipleInvoiceAction {
        void cancelBtnAction(boolean cancel);
    }
}