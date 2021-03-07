package com.paysky.upg.utils;

import android.content.Context;

import androidx.annotation.StringRes;

import com.kaopiz.kprogresshud.KProgressHUD;

/**
 * Created by ahmed on 29/11/17.
 */

public class DialogProgressUtils {


    public static KProgressHUD kProgressHUD;

    private static KProgressHUD createProgressDialog(Context context,
                                                     String label, String labelDetails, boolean isCancelable) {


        if (context == null) {
            return null;
        }

        if (labelDetails != null && kProgressHUD != null) {
            kProgressHUD.setDetailsLabel(labelDetails);
            if (kProgressHUD.isShowing()) {
                kProgressHUD.dismiss();
            }

            return kProgressHUD;
        }


        kProgressHUD = KProgressHUD.create(context)
                .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE);
        if (label != null && !label.isEmpty()) {
            kProgressHUD.setLabel(label);
        }
        if (labelDetails != null) {
            kProgressHUD.setDetailsLabel(labelDetails);
        }


        kProgressHUD.setCancellable(false)
                .setAnimationSpeed(2)
                .setDimAmount(0.5f);
        if (kProgressHUD != null) {
            return kProgressHUD;

        }


        return kProgressHUD;
    }

    public static KProgressHUD createNotCancelableProgressDialog(Context context) {
        return createProgressDialog(context, null, null, false);
    }

    public static KProgressHUD createNotCancelableProgressDialog(Context context, @StringRes int label) {
        return createProgressDialog(context, context.getString(label), null, false);
    }

    public static KProgressHUD createCancelableProgressDialog(Context context, String label) {
        return createProgressDialog(context, label, null, false);
    }

}
