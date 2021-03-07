package com.paysky.upg.customviews;

import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * Created by Philipp Jahoda on 14/09/15.
 */
public class DaysXAxisValueFormatter implements IAxisValueFormatter {

    String stringValue;
    List<Date> xAxisArrayList;
    SimpleDateFormat mFormat = new SimpleDateFormat("dd/MM", Locale.US);

    public DaysXAxisValueFormatter(List<Date> DatesArr) {
        this.xAxisArrayList = DatesArr;
    }

    @Override
    public String getFormattedValue(float value, AxisBase axis) {
        if (value >= 0) {
            if (value < xAxisArrayList.size()) {
                stringValue = mFormat.format((xAxisArrayList.get((int) value)));
            } else {
                stringValue = "";
            }
        } else {
            stringValue = "";
        }

        return stringValue;
    }


}
