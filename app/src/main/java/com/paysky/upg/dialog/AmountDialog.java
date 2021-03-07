package com.paysky.upg.dialog;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Handler;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.paysky.upg.R;

import java.util.Objects;

import faranjit.currency.edittext.CurrencyEditText;

/**
 * Created by PaySky-3 on 10/23/2017.
 */

public class AmountDialog extends Dialog {
    //Objects.
    private Activity activity;
    private AmountDialogAction amountDialogAction;
    //GUi.
    private final CurrencyEditText currencyEditText;

    public static void hideKeyboard(Activity activity) {
        InputMethodManager imm = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
        //Find the currently focused view, so we can grab the correct window token from it.
        View view = activity.getCurrentFocus();
        //If no view currently has focus, create a new one, just so we can grab a window token from it
        if (view == null) {
            view = new View(activity);
        }
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    boolean multiples = false;

    public AmountDialog(final Activity activity, final AmountDialogAction lissner) {
        super(activity);
        this.activity = activity;
        this.amountDialogAction = lissner;
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        Objects.requireNonNull(getWindow()).getAttributes().windowAnimations = R.style.DialogTheme;
        setContentView(R.layout.dialog_amount);
        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(getWindow().getAttributes());
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        lp.gravity = Gravity.CENTER;
        this.multiples = multiples;

        getWindow().setAttributes(lp);
        getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        currencyEditText = findViewById(R.id.amount);

        ImageView iv_icon_amount = findViewById(R.id.iv_icon_amount);
        TextView amountTitleTextView = findViewById(R.id.tv_amount_title);

        LinearLayout confirmBtn = findViewById(R.id.confirmBtn);
        LinearLayout Cancel = findViewById(R.id.Cancel);

        Cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                hideKeyboard(activity);

                AmountDialog.this.dismiss();

                amountDialogAction.Error();

            }
        });


        confirmBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    if (currencyEditText.getCurrencyText().equals("") ||
                            currencyEditText.getCurrencyText().equals("0.00")) {
                        currencyEditText.setError(getContext().getString(R.string.upg_general_required));

                        return;
                    }


                    hideKeyboard(activity);


                    amountDialogAction.ConfirmBtnAction("" + currencyEditText.getCurrencyDouble());
                    dismiss();
                } catch (Exception e) {
                    e.printStackTrace();
                    currencyEditText.setError(getContext().getString(R.string.upg_general_required));


                }

            }
        });


    }


    @Override
    public void show() {
        super.show();
        currencyEditText.requestFocus();

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                InputMethodManager inputMethodManager =
                        (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
                Objects.requireNonNull(inputMethodManager).toggleSoftInputFromWindow(
                        currencyEditText.getApplicationWindowToken(),
                        InputMethodManager.SHOW_FORCED, 0);

            }
        }, 500);
    }


    @Override
    public void setOnCancelListener(@Nullable OnCancelListener listener) {
        super.setOnCancelListener(listener);
        amountDialogAction.Error();
    }

    @Override
    public void setOnDismissListener(@Nullable OnDismissListener listener) {
        super.setOnDismissListener(listener);
        amountDialogAction.Error();
    }

    public interface AmountDialogAction {

        void ConfirmBtnAction(String amount);

        void Error();

    }

}
