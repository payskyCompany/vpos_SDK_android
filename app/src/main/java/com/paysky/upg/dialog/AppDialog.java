package com.paysky.upg.dialog;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.StringRes;

import com.paysky.upg.R;


public class AppDialog {

    // Variables.
    private String title;
    private String text;
    private String okText;

    public String getOkText() {
        return okText;
    }

    public void setOkText(String okText) {
        this.okText = okText;
    }

    private boolean showPositiveButton = true, showNegativeButton = true;
    //Objects.
    private PositiveButtonClickListener positiveButtonClickListener;
    private NegativeButtonClickListener negativeButtonClickListener;
    private Context context;
    public Dialog dialog;
    private boolean isCancelable = true;

    public AppDialog(Context context) {
        this.context = context;
    }


    public AppDialog setTitle(@StringRes int title) {
        this.title = context.getString(title);
        return this;
    }


    public AppDialog setText(@StringRes int text) {
        this.text = context.getString(text);
        return this;
    }


    public AppDialog setTitle(String title) {
        this.title = title;
        return this;
    }

    public AppDialog setText(String text) {
        this.text = text;
        return this;
    }

    public AppDialog showPositiveButton(boolean showPositiveButton) {
        this.showPositiveButton = showPositiveButton;
        return this;
    }

    public AppDialog showNegativeButton(boolean showNegativeButton) {
        this.showNegativeButton = showNegativeButton;
        return this;
    }

    public AppDialog setPositiveButtonClick(PositiveButtonClickListener buttonClick) {
        this.positiveButtonClickListener = buttonClick;
        return this;
    }


    public AppDialog setNegativeButtonClick(NegativeButtonClickListener buttonClick) {
        this.negativeButtonClickListener = buttonClick;
        return this;
    }

    public AppDialog setCancelable(boolean isCancelable) {
        this.isCancelable = isCancelable;
        return this;
    }


    public void show() {
        View dialogView = LayoutInflater.from(context).inflate(R.layout.dialog_confirm_info_layout, null);
        TextView dialogTitle = (TextView) dialogView.findViewById(R.id.dialog_title);
        TextView dialogText = (TextView) dialogView.findViewById(R.id.dialog_text);
        TextView comfirmTV = (TextView) dialogView.findViewById(R.id.comfirmTV);
        LinearLayout confirmImageView = (LinearLayout) dialogView.findViewById(R.id.comfirm);
        LinearLayout rejectImageView = (LinearLayout) dialogView.findViewById(R.id.cancel_action);


        dialogTitle.setText(title);
        dialogText.setText(text);
        if (showPositiveButton) {
            confirmImageView.setVisibility(View.VISIBLE);
            confirmImageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (positiveButtonClickListener != null) {
                        positiveButtonClickListener.positiveButtonClick(dialog);
                    } else {
                        dialog.dismiss();
                    }
                }
            });


            if (getOkText() != null) {
                comfirmTV.setText(getOkText());

            }
        } else {
            confirmImageView.setVisibility(View.GONE);
        }


        if (showNegativeButton) {
            rejectImageView.setVisibility(View.VISIBLE);
            rejectImageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (negativeButtonClickListener != null) {
                        negativeButtonClickListener.negativeButtonClick(dialog);
                    } else {
                        dialog.dismiss();
                    }
                }
            });


        } else {
            rejectImageView.setVisibility(View.GONE);
        }


        dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(dialogView);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialog.setCancelable(isCancelable);
        dialog.show();
    }


    public interface PositiveButtonClickListener {
        void positiveButtonClick(Dialog alertDialog);
    }

    public interface PositiveButtonClickListenerWithValue {
        void positiveButtonClickWithValue(DialogInterface dialogInterface, Object result);
    }

    public interface NegativeButtonClickListener {
        void negativeButtonClick(Dialog alertDialog);
    }


}
