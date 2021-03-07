package com.paysky.upg.fragment;


import android.content.res.Configuration;
import android.os.Bundle;
import android.os.SystemClock;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.github.mikephil.charting.charts.LineChart;
import com.paysky.upg.R;
import com.paysky.upg.dialog.FilterDialog;
import com.paysky.upg.fcm.MyFirebaseMessagingService;
import com.paysky.upg.fcm.NotificationData;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import io.paysky.upg.data.network.model.LoginUpgMerchant;
import io.paysky.upg.data.network.model.request.ReportRequest;
import io.paysky.upg.data.network.model.response.NotificationCountResponse;
import io.paysky.upg.data.network.model.response.ReportResponse;
import io.paysky.upg.mvp.reportservice.DashBoardPresenter;
import io.paysky.upg.mvp.reportservice.DashBoardView;
import io.paysky.upg.util.DateTimeUtil;
import io.paysky.upg.util.LocaleUtil;
import io.paysky.upg.util.SessionManager;


/**
 * A simple {@link Fragment} subclass.
 */
public class DashBoardFragment extends BaseFragment implements DashBoardView {

    //GUI
    @BindView(R.id.line_charts)
    LineChart lineChart;
    View rootView;
    Unbinder unbinder;
    @BindView(R.id.tv_total)
    TextView tvTotal;
    @BindView(R.id.tv_trans)
    TextView tvTrans;
    @BindView(R.id.ResultTo)
    TextView ResultTo;
    @BindView(R.id.ResultFrom)
    TextView ResultFrom;
    @BindView(R.id.ResultTerminalid)
    TextView ResultTerminalid;
    @BindView(R.id.WalletTrans)
    TextView WalletTrans;
    @BindView(R.id.mVisaTrans)
    TextView mVisaTrans;
    @BindView(R.id.CardTrans)
    TextView CardTrans;
    @BindView(R.id.CashTrans)
    TextView CashTrans;
    @BindView(R.id.tv_total_tip)
    TextView tv_total_tip;
    @BindView(R.id.CashLL)
    LinearLayout CashLL;
    @BindView(R.id.CardLL)
    LinearLayout CardLL;
    @BindView(R.id.mVisaTransLL)
    LinearLayout mVisaTransLL;
    @BindView(R.id.WalletTransLL)
    LinearLayout WalletTransLL;

    @BindView(R.id.mVisaView)
    View mVisaView;
    @BindView(R.id.cashView)
    View cashView;
    @BindView(R.id.walletView)
    View walletView;
    @BindView(R.id.cardLLView)
    View cardLLView;

    @BindView(R.id.balanceLL)
    LinearLayout balanceLL;


    @BindView(R.id.balance)
    TextView balance;

    boolean fromFilterDialog;


    @BindView(R.id.swiperefresh)
    SwipeRefreshLayout swiperefresh;


    //Variables
    public static String StartDate;
    public static String MobileNumber = "";
    public static String EndDate;
    public static String TahweeltransactionId;
    public static String ResultTerminalidString;
    public static String ResultTypeString = "";
    //Objects.
    private LoginUpgMerchant empData;
    ReportRequest reportRequestForNextPage = new ReportRequest();
    private DashBoardPresenter presenter = new DashBoardPresenter();
    private long lastClickTime = 0;


    public DashBoardFragment() {
        // Required empty public constructor
    }

    public static DashBoardFragment newInstance() {
        return new DashBoardFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        empData = sessionManager.getEmpData();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        super.onCreateView(inflater, container, savedInstanceState);
        rootView = inflater.inflate(R.layout.fragment_dashboard, container, false);
        unbinder = ButterKnife.bind(this, rootView);

        if (empData.isIsPrimaryMerchant() && ResultTerminalidString == null) {
            ResultTerminalidString = "";
        } else if (!empData.isIsPrimaryMerchant()) {
            ResultTerminalidString = null;
        }

        if (StartDate == null) {
            StartDate = DateTimeUtil.getDateTimeFromMonthVpos();
        }
        if (EndDate == null) {
            EndDate = DateTimeUtil.getDateTimeNow();
        }


        TahweeltransactionId = "";

        if (ResultTerminalidString == null) {
            ResultTerminalid.setVisibility(View.GONE);
        } else {
            ResultTerminalid.setVisibility(View.VISIBLE);

            if (ResultTerminalidString.equals("")) {
                ResultTerminalid.setText(getString(R.string.upg_general_Terminal) + getString(R.string.upg_general_All));
            } else {
                ResultTerminalid.setText(getString(R.string.upg_general_Terminal) + ResultTerminalidString);

            }
        }


        ShowTransactionTypes();

        presenter.attachView(this);
        ResultFrom.setText(DateTimeUtil.getDateFromString(DashBoardFragment.StartDate));
        ResultTo.setText(DateTimeUtil.getDateFromString(DashBoardFragment.EndDate));
        TahweeltransactionId = "";
        lineChart.setNoDataTextColor(getResources().getColor(R.color.charColor));
        lineChart.setNoDataText(getString(R.string.nochartData));

        swiperefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getBuildReport();
            }
        });

        getBuildReport();
        tvTotal.setText(SessionManager.getInstance().getEmpData().CurrencyName);


        if (SessionManager.getInstance().getEmpData().isPrimaryMerchant()
                && SessionManager.getInstance().getEmpData().getPrimaryMerchant().getSVA()
                && !MerChantbalance.isEmpty()
        ) {
            balanceLL.setVisibility(View.VISIBLE);

            balance.setText(MerChantbalance);

        }


//        if (BuildUtil.isYallaVposApp()) {
//            mVisaView.setBackgroundColor(getResources().getColor(R.color.yellow));
//            walletView.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
//            cardLLView.setBackgroundColor(getResources().getColor(R.color.dashboardGrey));
//        }

        return rootView;
    }

    @Override
    public void showProgress(int message) {
        showProgress();
    }

    @Override
    public void showProgress() {
        MyFirebaseMessagingService.hasNewNotification = false;
        swiperefresh.setRefreshing(true);
        swiperefresh.setColorSchemeColors(getResources().getColor(R.color.colorPrimary));
    }

    @Override
    public void dismissProgress() {
        swiperefresh.setRefreshing(false);
    }

    @Override
    public void onResume() {
        super.onResume();
        if (MyFirebaseMessagingService.hasNewNotification) {
            getBuildReport();

        }

        if (SessionManager.getInstance().getEmpData().isPrimaryMerchant()
                && SessionManager.getInstance().getEmpData().getPrimaryMerchant().getSVA()
                && !MerChantbalance.isEmpty()
        ) {
            balanceLL.setVisibility(View.VISIBLE);

            balance.setText(MerChantbalance);

        }
    }

    public void ShowTransactionTypes() {

        if (empData.isIsCard()) {
            CardLL.setVisibility(View.VISIBLE);
        } else {
            CardLL.setVisibility(View.GONE);
        }


        if (empData.isIsCash()) {
            CashLL.setVisibility(View.VISIBLE);
        } else {
            CashLL.setVisibility(View.GONE);
        }


////        if (empData.getPrimaryMerchant().getSVA()) {
////            disputeLL.setVisibility(View.VISIBLE);
////        } else {
//            disputeLL.setVisibility(View.GONE);
//        }


        if (empData.isIsMVisa()) {
            mVisaTransLL.setVisibility(View.VISIBLE);
        } else {
            mVisaTransLL.setVisibility(View.GONE);
        }

        if (empData.isIsTahweel()) {
            WalletTransLL.setVisibility(View.VISIBLE);
        } else {
            WalletTransLL.setVisibility(View.GONE);
        }


    }


    public static String MerChantbalance = "";

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onNotification(NotificationCountResponse reportRequest) {

        if (SessionManager.getInstance().getEmpData().isPrimaryMerchant()
                && SessionManager.getInstance().getEmpData().getPrimaryMerchant().getSVA()
        ) {
            balanceLL.setVisibility(View.VISIBLE);
            MerChantbalance = reportRequest.getMerchantBalance();
            balance.setText(reportRequest.getMerchantBalance());

        } else {
            balanceLL.setVisibility(View.GONE);

        }

    }


    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(ReportRequest reportRequest) {
        if (ResultTerminalidString == null) {
            ResultTerminalid.setVisibility(View.GONE);
        } else {
            ResultTerminalid.setVisibility(View.VISIBLE);

            if (ResultTerminalidString.equals("") || ResultTerminalidString == null) {
                ResultTerminalid.setText(getString(R.string.upg_general_Terminal) + getString(R.string.upg_general_All));
            } else {
                ResultTerminalid.setText(getString(R.string.upg_general_Terminal) + ResultTerminalidString);
            }
        }


        ResultFrom.setText(DateTimeUtil.getDateFromString(DashBoardFragment.StartDate));
        ResultTo.setText(DateTimeUtil.getDateFromString(DashBoardFragment.EndDate));
        if (!TahweeltransactionId.equals("")) {
            showNextPage(null);
        } else {
            getBuildReport();
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void GerateToastCustom(final NotificationData notificationData) {


        getBuildReport();
    }

    public void getBuildReport() {
        presenter.buildTransactionChart(ResultTypeString, DashBoardFragment.StartDate,
                DashBoardFragment.EndDate, DashBoardFragment.ResultTerminalidString,
                DashBoardFragment.MobileNumber

        );
    }


    @OnClick({R.id.filter, R.id.dateSelector})
    public void filter() {
        if (SystemClock.elapsedRealtime() - lastClickTime < 1000) {
            return;
        }
        lastClickTime = SystemClock.elapsedRealtime();

        if (LocaleUtil.isAppLanguageAr(getContext())) {
            Locale locale = new Locale("ar");
            Locale.setDefault(locale);
            Configuration config =
                    getContext().getResources().getConfiguration();
            config.setLocale(locale);
            getContext().createConfigurationContext(config);
        }

        FilterDialog filterDialog = new FilterDialog(getActivity());
        if (!filterDialog.isShowing()) filterDialog.show();
    }

    @OnClick(R.id.ln_details)
    public void showNextPage(View view) {
        reportRequestForNextPage.setDateFrom(StartDate);
        reportRequestForNextPage.setDateTo(EndDate);
        reportRequestForNextPage.setDisplayStart("0");
        reportRequestForNextPage.setDisplayLength("20");
        reportRequestForNextPage.setTahweeltransactionId(TahweeltransactionId);
        reportRequestForNextPage.setChannel(ResultTypeString);
        reportRequestForNextPage.setFilterTerminalId(ResultTerminalidString);
        reportRequestForNextPage.setConsumerMobile(MobileNumber);
        addNewFragment(TransactionsLoadingFragment.newInstance(reportRequestForNextPage), "DashBoardFragment");
    }


    @Override
    public void onDestroyView() {
        presenter.detachView();

        super.onDestroyView();
    }


    @Override
    public void showData(ReportResponse body) {
        tvTrans.setText(String.format("%s %s", getString(R.string.upg_dashboard_transaction_count_label), body.getTotalCountAllTransaction()));
        tvTotal.setText(String.format("%s%s",
                " " + SessionManager.getInstance().getEmpData().CurrencyName + " "
                , body.getTotalAmountAllTransaction()));
    }

    @Override
    public void noChartData() {
        lineChart.invalidate();
        lineChart.clear();
        lineChart.setNoDataText(getString(R.string.nochartDataInternet));

        WalletTransLL.setVisibility(View.GONE);
        mVisaTransLL.setVisibility(View.GONE);
        CashLL.setVisibility(View.GONE);
        CardLL.setVisibility(View.GONE);


    }
}
