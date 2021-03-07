package com.paysky.upg.utils;


import android.app.DatePickerDialog;
import android.content.Context;
import android.view.ContextThemeWrapper;

import com.paysky.upg.R;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import io.paysky.upg.util.DateTimeUtil;

/**
 * Created by PaySky-3 on 8/9/2017.
 */

public class DateTimeDailogUtil {
    public static DatePickerDialog createDialogWithoutDateField(String selectedate, Context context,
                                                                DatePickerDialog.OnDateSetListener onDateSetListener) {

        Calendar calendar = DateTimeUtil.toCalendar(selectedate);
        int arg1 = calendar.get(Calendar.YEAR);
        int arg2 = calendar.get(Calendar.MONTH);
        int arg3 = calendar.get(Calendar.DAY_OF_MONTH);

        final Context contextThemeWrapper = new ContextThemeWrapper(context, R.style.AppTheme2);

        DatePickerDialog dpd = new DatePickerDialog(contextThemeWrapper, onDateSetListener, arg1, arg2, arg3);

        dpd.getDatePicker().setMaxDate(new Date().getTime());

        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.MONTH, -6);

        try {
            dpd.getDatePicker().setMinDate(cal.getTime().getTime());

        } catch (Exception e) {

        }

        return dpd;
    }


    public static DatePickerDialog createExpirationDateDialog(String selectedDate, Context context,
                                                              DatePickerDialog.OnDateSetListener onDateSetListener) {
        Calendar calendar = convertDateToCalendar(selectedDate);
        int arg1 = calendar.get(Calendar.YEAR);
        int arg2 = calendar.get(Calendar.MONTH);
        int arg3 = calendar.get(Calendar.DAY_OF_MONTH);
        final Context contextThemeWrapper = new ContextThemeWrapper(context, R.style.AppTheme2);
        long now = Calendar.getInstance().getTimeInMillis();
        DatePickerDialog dpd = new DatePickerDialog(contextThemeWrapper, onDateSetListener, arg1, arg2, arg3);
        // set min date for expire date of dialog to today,
        dpd.getDatePicker().setMinDate(0);


        dpd.getDatePicker().setMinDate(now);
        return dpd;
    }


    private static Calendar convertDateToCalendar(String DateFrom) {
        SimpleDateFormat input = new SimpleDateFormat("dd/MM/yyyy", Locale.ENGLISH);

        if (DateFrom.isEmpty()) {
            return Calendar.getInstance();
        }
        Calendar cal = Calendar.getInstance();
        try {
            cal.setTime(input.parse(DateFrom));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return cal;
    }

}
