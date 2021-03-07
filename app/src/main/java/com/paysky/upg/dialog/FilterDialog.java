package com.paysky.upg.dialog;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Toast;

import com.paysky.upg.R;
import com.paysky.upg.fragment.DashBoardFragment;
import com.paysky.upg.utils.DateTimeDailogUtil;

import org.greenrobot.eventbus.EventBus;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.paysky.upg.data.network.model.request.ReportRequest;
import io.paysky.upg.util.DateTimeUtil;
import io.paysky.upg.util.SessionManager;
import io.paysky.upg.util.ViewUtil;


/**
 * Created by Amr Abd Elrhim on 15/07/2017.
 */

public class FilterDialog extends Dialog implements DatePickerDialog.OnDateSetListener {
    @BindView(R.id.amount)
    EditText amount;
    @BindView(R.id.dateFrom)
    Button dateFrom;
    @BindView(R.id.dataTo)
    Button dataTo;
    @BindView(R.id.CancelDialo)
    Button CancelDialo;
    @BindView(R.id.SubmitDialog)
    Button SubmitDialog;
    @BindView(R.id.TrimnalList)
    Spinner TrimnalList;
    @BindView(R.id.TransactionChanel)
    Spinner TransactionChanell;
    @BindView(R.id.TransactionChanelLL)
    LinearLayout TransactionChanelLL;
    @BindView(R.id.TrimnalListLL)
    LinearLayout TrimnalListLL;
    @BindView(R.id.MobileNumberLL)
    LinearLayout MobileNumberLL;
    @BindView(R.id.mobileNumber)
    EditText mobileNumber;

    private Activity activity;


    String StartDate = "";
    String EndtDate = "";
    String ClickDate = "";


    public FilterDialog(Activity activity) {
        super(activity);
        this.activity = activity;
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().getAttributes().windowAnimations = R.style.DialogTheme;

        setContentView(R.layout.filter_dialog);
        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(getWindow().getAttributes());
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        lp.gravity = Gravity.CENTER;

        getWindow().setAttributes(lp);
        getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        //Button Cancel = (Button) findViewById(R.id.Cancel);
        ButterKnife.bind(this);

        // TODO: 12/10/2018 handle api interface
        //     apiService = ApiClient.getClient(ApiClient.WALLET_URL).create(ApiInterface.class);


        dateFrom.setText(
                DateTimeUtil.getDateFromString(DashBoardFragment.StartDate)
        );

        dataTo.setText(
                DateTimeUtil.getDateFromString(DashBoardFragment.EndDate)
        );
        StartDate = DashBoardFragment.StartDate;
        EndtDate = DashBoardFragment.EndDate;
        mobileNumber.setText(DashBoardFragment.MobileNumber);


        ViewUtil.preventCopyAndPaste(amount);

        if (SessionManager.getInstance().getEmpData().isIsPrimaryMerchant()) {
            List<String> strings = new ArrayList<>();
//            strings.add(activity.getString(R.string.Pleaseselecterminalnumber));
            strings.add(activity.getString(R.string.upg_general_Terminal) + activity.getString(R.string.upg_general_All));
            strings.addAll(SessionManager.getInstance().getEmpData().getTerminals());
            ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(getContext(), R.layout.spinner_item, strings);
            spinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            TrimnalList.setAdapter(spinnerArrayAdapter);

//            TrimnalList.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
//                @Override
//                public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
//                }
//
//                @Override
//                public void onNothingSelected(AdapterView<?> adapterView) {
//
//                }
//            });
//            if (TransactionsFragment.ResultTerminalidString != null && TransactionsFragment.ResultTerminalidString.isEmpty()) {
//                TrimnalList.setSelection(0);
//            } else {
//                for (int x = 0; x < SessionManager.getInstance().getEmpData().getTerminalsORGINAL().size(); x++) {
//                    if (TransactionsFragment.ResultTerminalidString.
//                            equals(SessionManager.getInstance().getEmpData().getTerminalsORGINAL().get(x))) {
//                        TrimnalList.setSelection((x + 1));
//                    }
//                }
//            }
//
//            TrimnalListLL.setVisibility(View.GONE);

        }
        final List<String> stringsChannel = new ArrayList<>();

//        stringsChannel.add(activity.getString(R.string.Pleaseselectchanneltype));
        stringsChannel.add(activity.getString(R.string.upg_general_Channel) + activity.getString(R.string.upg_general_All));
        stringsChannel.addAll(SessionManager.getInstance().getEmpData().getChannelList());
        ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(getContext(), R.layout.spinner_item, stringsChannel);
        spinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        TransactionChanell.setAdapter(spinnerArrayAdapter);


        TransactionChanell.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position != 0) {
                    if (SessionManager.getInstance().getEmpData().getChannels().get((position - 1)).getValue().equals("2")) {
                        MobileNumberLL.setVisibility(View.VISIBLE);
                    } else {
                        MobileNumberLL.setVisibility(View.GONE);
                        DashBoardFragment.MobileNumber = "";
                        mobileNumber.setText("");
                    }

                }


            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


    }

    public void SubmitDialog() {
        if (StartDate.isEmpty() && !EndtDate.isEmpty()) {
            Toast.makeText(getContext(), activity.getString(R.string.pleaseselectstartdate), Toast.LENGTH_SHORT).show();
            return;

        }

        if (!StartDate.isEmpty() && EndtDate.isEmpty()) {
            Toast.makeText(getContext(), activity.getString(R.string.pleaseselectenddate), Toast.LENGTH_SHORT).show();
            return;
        }


        if (TransactionChanell.getSelectedItemPosition() != 0) {

            if (!SessionManager.getInstance().getEmpData().getChannels().
                    get((TransactionChanell.getSelectedItemPosition() - 1)).getValue().equals("2")) {
                mobileNumber.setText("");
            }


            if (SessionManager.getInstance().getEmpData().
                    getChannels().get((TransactionChanell.getSelectedItemPosition() - 1)).getValue().equals("2")
                    && !mobileNumber.getText().toString().isEmpty() && mobileNumber.getText().toString().length() != 11
            ) {

                mobileNumber.setError(activity.getString(R.string.validmobileNumber));

                return;

            }
        }


        if (StartDate.isEmpty() && EndtDate.isEmpty() && amount.getText().toString().isEmpty()) {
            Toast.makeText(getContext(), activity.getString(R.string.Invalidsearchdata), Toast.LENGTH_SHORT).show();
            return;
        }
        try {
            SimpleDateFormat input = new SimpleDateFormat("yyyyMMdd", Locale.ENGLISH);
            Date dateStartString = input.parse(StartDate);
            Date dateFromString = input.parse(EndtDate);

            if (dateFromString != null && dateFromString.before(dateStartString)) {

                Toast.makeText(getContext(), activity.getString(R.string.DateFrommustbehigherthanDato), Toast.LENGTH_SHORT).show();
                return;
                //Do Something
            }

        } catch (ParseException e) {
            e.printStackTrace();
        }

        DashBoardFragment.StartDate = StartDate;
        DashBoardFragment.EndDate = EndtDate;
        DashBoardFragment.TahweeltransactionId = amount.getText().toString();
        DashBoardFragment.MobileNumber = mobileNumber.getText().toString();
        if (TrimnalListLL.getVisibility() == View.VISIBLE) {

            if (TrimnalList.getSelectedItemPosition() == 0) {
                DashBoardFragment.ResultTerminalidString = "";

            } else {
                DashBoardFragment.ResultTerminalidString =
                        SessionManager.getInstance().getEmpData().getTerminalsORGINAL().get
                                ((TrimnalList.getSelectedItemPosition() - 1));
            }
        }

        if (TransactionChanelLL.getVisibility() == View.VISIBLE) {


            if (TransactionChanell.getSelectedItemPosition() == 0) {
                DashBoardFragment.ResultTypeString = "";
            } else {
                DashBoardFragment.ResultTypeString =
                        SessionManager.getInstance().getEmpData().getChannels().get
                                ((TransactionChanell.getSelectedItemPosition() - 1)).getValue();
            }
        }


        EventBus.getDefault().post(new ReportRequest());
        dismiss();
    }

    @OnClick({R.id.CancelDialo, R.id.SubmitDialog, R.id.dateFrom, R.id.dateFromImage, R.id.dataTo, R.id.dateToImage})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.CancelDialo:
                dismiss();
                break;
            case R.id.SubmitDialog:
                SubmitDialog();
                break;

            case R.id.dateFrom:
            case R.id.dateFromImage:
                DateTimeDailogUtil.createDialogWithoutDateField(StartDate, getContext(), this).show();
                ClickDate = "dateFrom";
                break;

            case R.id.dataTo:
            case R.id.dateToImage:

                DateTimeDailogUtil.createDialogWithoutDateField(EndtDate, getContext(), this).show();
                ClickDate = "dataTo";

                break;
        }
    }


    @Override
    public void show() {
        super.show();

    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.MONTH, month);
        calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
        calendar.set(Calendar.YEAR, year);


        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.MONTH, -6);

        if (calendar.getTime().getTime() < cal.getTime().getTime()) {
            return;
        }


        month = month + 1;
        if (ClickDate.equals("dateFrom")) {
            StartDate = "" + new StringBuilder().append(year).append("")
                    .append((month < 10 ? "0" + month : month))
                    .append("").append((dayOfMonth < 10 ? "0" + dayOfMonth : dayOfMonth));


            dateFrom.setText(DateTimeUtil.getDateFromString(StartDate));


        } else {


            EndtDate = "" + new StringBuilder().append(year).append("")
                    .append((month < 10 ? "0" + month : month))
                    .append("").append((dayOfMonth < 10 ? "0" + dayOfMonth : dayOfMonth));

            dataTo.setText(DateTimeUtil.getDateFromString(EndtDate));

        }


    }


}
