package com.paysky.upg.customviews;

import android.app.Activity;
import android.content.res.Configuration;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.borax12.materialdaterangepicker.date.DatePickerDialog;
import com.paysky.upg.R;
import com.zcw.togglebutton.ToggleButton;

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
import io.paysky.upg.data.network.model.response.SearchOrdersResponse;
import io.paysky.upg.mvp.paylinklist.PayLinkListPresenter;
import io.paysky.upg.util.DateTimeUtil;
import io.paysky.upg.util.LocaleUtil;
import io.paysky.upg.util.SessionManager;

public class PayLinkFilter implements DatePickerDialog.OnDateSetListener {
    @BindView(R.id.TotalDueAmount)
    TextView TotalDueAmount;
    @BindView(R.id.TotalDueCount)
    TextView TotalDueCount;
    @BindView(R.id.TotalPaidAmount)
    TextView TotalPaidAmount;
    @BindView(R.id.TotalPaidCount)
    TextView TotalPaidCount;
    @BindView(R.id.TotalCount)
    TextView TotalCount;
    @BindView(R.id.PayOnDeliveryTB)
    ToggleButton PayOnDeliveryTB;


    View header;

    public View getHeader() {
        return header;
    }


    public int pageNumber = 1;

    @BindView(R.id.MerchantReferance)
    EditText MerchantReferance;


    @BindView(R.id.LLAnaltisis)
    LinearLayout LLAnaltisis;


    @BindView(R.id.ResultFrom)
    TextView ResultFrom;
    @BindView(R.id.ResultTo)
    TextView ResultTo;
    @BindView(R.id.PaymerntStatusSpinner)
    Spinner PaymerntStatusSpinner;
    @BindView(R.id.TerminalSpinner)
    Spinner TerminalSpinner;


    public String selectedTerminal;
    public int selectedPaymerntStatus;
    public boolean payOnDelivery = false;
    public String StartDate;
    public String EndtDate;

    @BindView(R.id.TerminalLL)
    LinearLayout TerminalLL;
    @BindView(R.id.PaymerntStatusLL)
    LinearLayout PaymerntStatusLL;
    Activity context;
    PayLinkListPresenter payLinkListPresenter;
    boolean IncludeAll = true;


    public static String SelectedTerminal;
    public static int SelectedPaymerntStatus;
    public static String SelectedResultFrom;
    public static String SelectedResultTo;
    public static String SelectedMerchantReferance;


    public PayLinkFilter(Activity context,
                         final PayLinkListPresenter payLinkListPresenter) {
        this.context = context;
        this.payLinkListPresenter = payLinkListPresenter;
        header = LayoutInflater.from(context).inflate(R.layout.paylink_header_content,
                (ViewGroup) context.findViewById(R.id.header), false);


        ButterKnife.bind(this, header);

        initDataPaymerntStatus();
        initDataTerminal();
        initDataDatePicker();


        MerchantReferance.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    search(null);
                    return true;
                }
                return false;
            }
        });


        PayOnDeliveryTB.setOnToggleChanged(new ToggleButton.OnToggleChanged() {
            @Override
            public void onToggle(boolean on) {
                payOnDelivery = on;
                pageNumber = 1;

                search();
            }
        });

        setSavedValues();
    }

    public void initDataPaymerntStatus() {
        ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(context,
                R.layout.spinner_item,
                context.getResources().getStringArray(R.array.upg_paylink_filter_statustypes)); //selected item will look like a spinner set from XML
        spinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        PaymerntStatusSpinner.setAdapter(spinnerArrayAdapter);
        PaymerntStatusSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                selectedPaymerntStatus = position;
                payLinkListPresenter.searchWithClear();

            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                selectedPaymerntStatus = 0;
            }

        });


    }

    final List<String> strings = new ArrayList<>();
    final List<String> stringsList = new ArrayList<>();

    public void initDataTerminal() {


        if (SessionManager.getInstance().getEmpData().getterminalTypesModelString().size() > 1) {


            strings.add(context.getResources().getString(R.string.AllTerminal));
            stringsList.add(context.getResources().getString(R.string.AllTerminal));
            strings.addAll(SessionManager.getInstance().getEmpData().getterminalTypesModelString());
            stringsList.addAll(SessionManager.getInstance().getEmpData().getterminalTypesModelWithTypeString());

            ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(context,
                    R.layout.spinner_item,
                    stringsList
            ); //selected item will look like a spinner set from XML
            spinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            TerminalSpinner.setAdapter(spinnerArrayAdapter);


            TerminalSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {


                    if (position == 0) {
                        IncludeAll = true;
                        selectedTerminal = "";

                        if (pageNumber == 1) {

                            return;
                        }
                        pageNumber = 1;


                        return;
                    } else {
                        IncludeAll = false;
                        selectedTerminal = strings.get(position);
                        pageNumber = 1;

                    }

                    search();
                }

                @Override
                public void onNothingSelected(AdapterView<?> parentView) {
                    selectedTerminal = "";
                }

            });


        } else {
            IncludeAll = true;
            selectedTerminal = SessionManager.getInstance().getEmpData().getterminalTypesModelString().get(0);
            TerminalLL.setVisibility(View.GONE);

        }
    }

    public void setDataAnalytic(SearchOrdersResponse dataAnalytic) {
        if (dataAnalytic.getTotalCount() == 0) {
            LLAnaltisis.setVisibility(View.GONE);

            return;
        } else {
            LLAnaltisis.setVisibility(View.VISIBLE);

        }
        TotalDueAmount.setText("" + dataAnalytic.getTotalDueAmount());
        TotalDueCount.setText("" + dataAnalytic.getTotalDueCount());
        TotalPaidAmount.setText("" + dataAnalytic.getTotalPaidAmount());
        TotalPaidCount.setText("" + dataAnalytic.getTotalPaidCount());
        TotalCount.setText("" + dataAnalytic.getTotalCount());
    }


    public void setSavedValues() {
        if (SelectedMerchantReferance != null) {
            MerchantReferance.setText(SelectedMerchantReferance);
        }

        if (SelectedResultTo != null) {
            StartDate = SelectedResultTo;
            EndtDate = SelectedResultFrom;


            ResultFrom.setText(DateTimeUtil.getDateFromString(SelectedResultTo));
            ResultTo.setText(DateTimeUtil.getDateFromString(SelectedResultFrom));
        }

        if (SelectedTerminal != null && !SelectedTerminal.isEmpty()) {

            selectedTerminal = SelectedTerminal;


            TerminalSpinner.setSelection(strings.indexOf(selectedTerminal));
        }


        selectedPaymerntStatus = SelectedPaymerntStatus;
        PaymerntStatusSpinner.setSelection(selectedPaymerntStatus);


    }

    public void search() {
        SelectedTerminal = selectedTerminal;
        SelectedPaymerntStatus = selectedPaymerntStatus;
        SelectedMerchantReferance = MerchantReferance.getText().toString();
        SelectedResultTo = StartDate;
        SelectedResultFrom = EndtDate;

        payLinkListPresenter.searchPayLink(IncludeAll, selectedTerminal,
                selectedPaymerntStatus, MerchantReferance.getText().toString(),
                StartDate, EndtDate, pageNumber, payOnDelivery);
    }


    public void initDataDatePicker() {


        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DAY_OF_MONTH, -1);


        int yearStart = cal.get(Calendar.YEAR);
        int monthStart = cal.get(Calendar.MONTH);
        int dayOfMonthStart = cal.get(Calendar.DAY_OF_MONTH);

        monthStart = monthStart + 1;

        StartDate = "" + new StringBuilder().append(yearStart).append("")
                .append((monthStart < 10 ? "0" + monthStart : monthStart))
                .append("").append((dayOfMonthStart < 10 ? "0" + dayOfMonthStart : dayOfMonthStart));


        ResultFrom.setText(DateTimeUtil.getDateFromString(StartDate));


        int year = Calendar.getInstance().get(Calendar.YEAR);
        int month = Calendar.getInstance().get(Calendar.MONTH);
        int dayOfMonth = Calendar.getInstance().get(Calendar.DAY_OF_MONTH);

        month = month + 1;


        EndtDate = "" + new StringBuilder().append(year).append("")
                .append((month < 10 ? "0" + month : month))
                .append("").append((dayOfMonth < 10 ? "0" + dayOfMonth : dayOfMonth));


        ResultTo.setText(DateTimeUtil.getDateFromString(EndtDate));


    }


    @OnClick({R.id.searchIcon})
    public void search(View view) {


        pageNumber = 1;
        search();


    }

    @OnClick({R.id.TerminalIcon})
    public void openTerminalSpinner(View view) {
        TerminalSpinner.performClick();

    }


    @OnClick({R.id.PaymerntStatusIcon})
    public void openPaymerntStatuSpinner(View view) {
        PaymerntStatusSpinner.performClick();

    }


    @OnClick({R.id.DatePicker})
    public void onViewClicked(View view) {

        if (LocaleUtil.isAppLanguageAr(context)) {
            Locale locale = new Locale("ar");
            Locale.setDefault(locale);
            Configuration config =
                    context.getResources().getConfiguration();
            config.setLocale(locale);
            context.createConfigurationContext(config);
        }

        Calendar calendar1 = DateTimeUtil.toCalendar(StartDate);
        Calendar calendar2 = DateTimeUtil.toCalendar(EndtDate);
        DatePickerDialog dpd = DatePickerDialog.newInstance(
                this,
                calendar1.get(Calendar.YEAR),
                calendar1.get(Calendar.MONTH),
                calendar1.get(Calendar.DAY_OF_MONTH),
                calendar2.get(Calendar.YEAR),
                calendar2.get(Calendar.MONTH),
                calendar2.get(Calendar.DAY_OF_MONTH));

        dpd.setMaxDate(Calendar.getInstance());
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.MONTH, -6);
        dpd.setMinDate(cal);
        dpd.show(context.getFragmentManager(), "Datepickerdialog");
    }


    @Override
    public void onDateSet(DatePickerDialog view, int year, int monthOfYear, int dayOfMonth,
                          int yearEnd, int monthOfYearEnd, int dayOfMonthEnd) {


        monthOfYearEnd = monthOfYearEnd + 1;
        monthOfYear = monthOfYear + 1;
        StartDate = "" + new StringBuilder().append(year).append("")
                .append((monthOfYear < 10 ? "0" + monthOfYear : monthOfYear))
                .append("").append((dayOfMonth < 10 ? "0" + dayOfMonth : dayOfMonth));


        ResultFrom.setText(DateTimeUtil.getDateFromString(StartDate));

        EndtDate = "" + new StringBuilder().append(yearEnd).append("")
                .append((monthOfYearEnd < 10 ? "0" + monthOfYearEnd : monthOfYearEnd))
                .append("").append((dayOfMonthEnd < 10 ? "0" + dayOfMonthEnd : dayOfMonthEnd));


        ResultTo.setText(DateTimeUtil.getDateFromString(EndtDate));
        try {
            SimpleDateFormat input = new SimpleDateFormat("yyyyMMdd", Locale.ENGLISH);
            Date dateStartString = input.parse(StartDate);
            Date dateFromString = input.parse(EndtDate);
            if (dateFromString != null && dateFromString.before(dateStartString)) {
                Toast.makeText(context, context.getString(R.string.DateFrommustbehigherthanDato), Toast.LENGTH_SHORT).show();
                return;
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        payLinkListPresenter.searchWithClear();

    }
}
