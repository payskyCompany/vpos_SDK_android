package com.paysky.upg.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;

import com.paysky.upg.R;
import com.paysky.upg.adapter.TransactionAdapter;
import com.paysky.upg.model.TransactionModel;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.paysky.upg.data.network.model.request.ReportRequest;
import io.paysky.upg.mvp.reportservice.DashBoardPresenter;
import io.paysky.upg.util.DateTimeUtil;

import static com.paysky.upg.fragment.DashBoardFragment.EndDate;
import static com.paysky.upg.fragment.DashBoardFragment.MobileNumber;
import static com.paysky.upg.fragment.DashBoardFragment.ResultTerminalidString;
import static com.paysky.upg.fragment.DashBoardFragment.ResultTypeString;
import static com.paysky.upg.fragment.DashBoardFragment.StartDate;
import static com.paysky.upg.fragment.DashBoardFragment.TahweeltransactionId;


public class TransactionsFragment extends BaseFragment implements TransactionAdapter.ItemClickListener {

    @BindView(R.id.ll_pay_by_card)
    LinearLayout payByCardLinearLayout;
    @BindView(R.id.ll_pay_by_link)
    LinearLayout payByLinkLinearLayout;
    @BindView(R.id.ll_pay_by_qr)
    LinearLayout payByQRLinearLayout;
    ReportRequest reportRequestForNextPage = new ReportRequest();


    private DashBoardPresenter presenter = new DashBoardPresenter();
    private long lastClickTime = 0;


    public static TransactionsFragment newInstance() {
        return new TransactionsFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View root = inflater.inflate(R.layout.fragment_bank_transactions, container, false);
        ButterKnife.bind(this, root);

        payByCardLinearLayout.setOnClickListener(view -> addNewFragment(ManualPaymentFragment.newInstance(),
                ManualPaymentFragment.class.getSimpleName()));

        payByLinkLinearLayout.setOnClickListener(view -> addNewFragment(PayLinkFragment.newInstance(),
                ManualPaymentFragment.class.getSimpleName()));

        payByQRLinearLayout.setOnClickListener(view -> showTransactions());

        if (StartDate == null) {
            StartDate = DateTimeUtil.getDateTimeFromMonthVpos();
        }
        if (EndDate == null) {
            EndDate = DateTimeUtil.getDateTimeNow();
        }

        TahweeltransactionId = "";


        return root;
    }

    @Override
    public void onItemClick(View view, TransactionModel object, int position) {
    }

    public void showTransactions() {
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

}
