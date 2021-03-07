package com.paysky.upg.dialog;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;

import androidx.annotation.StringRes;

import com.paysky.upg.R;

import java.util.ArrayList;
import java.util.List;

import io.paysky.upg.util.SessionManager;

/**
 * Created by Ahmed AL Agamy on 8/16/2017.
 */

public class DialogUtils {


    public static ProgressDialog createProgressDialog(Context context, @StringRes int message) {
        return createProgressDialog(context, context.getString(message));
    }

    public static ProgressDialog createProgressDialog(Context context, String message) {
        ProgressDialog progressDialog = new ProgressDialog(context);
        progressDialog.setMessage(message);
        progressDialog.setCancelable(false);
        return progressDialog;
    }


    public static void showAppPasswordDialog(Context context, final OnPasswordDialogButtonClick buttonClick, String hint
            , String text) {


        final Dialog dialog = new Dialog(context);
        dialog.setContentView(R.layout.dialog_app_password);

        final EditText passwordInput = (EditText) dialog.findViewById(R.id.app_password_input);
        final LinearLayout comfirm = (LinearLayout) dialog.findViewById(R.id.comfirm);
        final LinearLayout cancel_action = (LinearLayout) dialog.findViewById(R.id.cancel_action);
        final ImageView iv_dialog_password = (ImageView) dialog.findViewById(R.id.iv_dialog_password);


        if (SessionManager.getInstance().getLang().equals("ar")) {
            passwordInput.setTextAlignment(View.TEXT_ALIGNMENT_VIEW_START);
        }

        comfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                buttonClick.passwordButtonClick(passwordInput.getText().toString(), dialog);

            }
        });
        cancel_action.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();

            }
        });

        dialog.show();

    }


    public static void showAppSettingDialog(final Context context, final OnSetingDone buttonClick, String hint
            , String text) {

        final Dialog dialogView = new Dialog(context);
        dialogView.setContentView(R.layout.dialog_app_setting);


        final Spinner httpURLLISt = (Spinner) dialogView.findViewById(R.id.httpURLLISt);
        final List<String> strings = new ArrayList<>();

        final LinearLayout comfirm = (LinearLayout) dialogView.findViewById(R.id.comfirm);
        final LinearLayout cancel_action = (LinearLayout) dialogView.findViewById(R.id.cancel_action);


        ArrayAdapter<String> adapter = new ArrayAdapter<String>(context,
                android.R.layout.simple_spinner_dropdown_item, strings);
        httpURLLISt.setAdapter(adapter);


        comfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogView.dismiss();
                buttonClick.SetringButtonClick(httpURLLISt.getSelectedItem().toString());

            }
        });
        cancel_action.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogView.dismiss();

            }
        });

        dialogView.show();


    }


    public interface OnPasswordDialogButtonClick {

        void passwordButtonClick(String password, DialogInterface alertDialog);

    }

    public interface OnSetingDone {

        void SetringButtonClick(String password);

    }
}

