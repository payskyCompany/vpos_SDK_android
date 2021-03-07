package com.paysky.upg.fragment;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.jcodecraeer.xrecyclerview.XRecyclerView;
import com.paysky.upg.R;
import com.paysky.upg.adapter.TransExpandableAdapter;
import com.paysky.upg.customviews.FilterReport;
import com.paysky.upg.fcm.NotificationData;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import io.paysky.upg.data.network.model.request.ReportRequest;
import io.paysky.upg.data.network.model.response.DateTransactions;
import io.paysky.upg.data.network.model.response.ReportResponse;
import io.paysky.upg.data.network.model.response.TransactionsEntitie;
import io.paysky.upg.mvp.reportdetailsservice.ReportDetailsServiceFragmentPresenter;
import io.paysky.upg.mvp.reportdetailsservice.ReportDetailsServiceFragmentView;

/**
 * A simple {@link Fragment} subclass.
 */
public class ReportDetails extends BaseFragment implements ReportDetailsServiceFragmentView {

    //GUI.
    @BindView(R.id.transaction)
    XRecyclerView rCtransactions;


    View rootView;
    Unbinder unbinder;

    FilterReport filterReport;
    //Variables.
    static String RESPONSE_TRANSACTION = "RESPONSE_TRANSACTION";
    static String REQUEST_TRANSACTION = "REQUEST_TRANSACTION";
    //Objects
    private TransExpandableAdapter transChildExpandableAdapter;
    List<TransactionsEntitie> transactionsEntities = new ArrayList<>();
    private ReportDetailsServiceFragmentPresenter presenter = new ReportDetailsServiceFragmentPresenter();
    ReportResponse reportResponse;
    ReportRequest reportRequest;


    public static ReportDetails newInstance(ReportResponse reportResponse, ReportRequest reportRequest) {
        ReportDetails fragment = new ReportDetails();
        Bundle args = new Bundle();
        args.putSerializable(RESPONSE_TRANSACTION, reportResponse);
        args.putSerializable(REQUEST_TRANSACTION, reportRequest);
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        rootView = inflater.inflate(R.layout.fragment_report_details, container, false);
        unbinder = ButterKnife.bind(this, rootView);
        presenter.attachView(this);

        if (getArguments() != null && getArguments().getSerializable(RESPONSE_TRANSACTION) != null) {
            reportResponse = (ReportResponse) getArguments().getSerializable(RESPONSE_TRANSACTION);
        }

        if (getArguments() != null && getArguments().getSerializable(REQUEST_TRANSACTION) != null) {
            reportRequest = (ReportRequest) getArguments().getSerializable(REQUEST_TRANSACTION);
        }

        transactionsEntities = reportResponse.getTransactions();

        transChildExpandableAdapter = new TransExpandableAdapter(getContext(), transactionsEntities);
        LinearLayoutManager llm = new LinearLayoutManager(getContext());
        rCtransactions.setLayoutManager(llm);
        rCtransactions.setAdapter(transChildExpandableAdapter);
        filterReport = new FilterReport(getActivity(), presenter, reportRequest, reportResponse, isViewHidden);
        initDataRecycler();
        return rootView;
    }


    public void initDataRecycler() {
        filterReport.displayNumber = 20;
        rCtransactions.setLayoutManager(new LinearLayoutManager(getContext()));
        transChildExpandableAdapter = new TransExpandableAdapter(getContext(), transactionsEntities);
        rCtransactions.setAdapter(transChildExpandableAdapter);
        rCtransactions.addHeaderView(filterReport.getHeader());
        rCtransactions.setLoadingListener(new XRecyclerView.LoadingListener() {
            @Override
            public void onRefresh() {
                if (isViewHidden) {
                    return;
                }
                filterReport.reportService(reportRequest);

            }

            @Override
            public void onLoadMore() {


                presenter.loadMoreFilterTransaction(reportRequest, filterReport.displayNumber);


            }
        });
        rCtransactions.setFootViewText(getString(R.string.loading), getString(R.string.no_data));

//        filterReport.reportService(reportRequest);
//        rCtransactions.setLoadingMoreEnabled(true);

    }


    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(DateTransactions x) {
        if (isViewHidden) {
            return;
        }
        addNewFragment(ReceiptWithTransFragment.newInstance(x), "ReceiptWithTransFragment");
    }

    @Override
    public void showTransactionEntities(List<TransactionsEntitie> transactions, String totalCount) {
        this.transactionsEntities = new ArrayList<>();
        rCtransactions.refreshComplete();

        transactionsEntities = transactions;
        transChildExpandableAdapter.Update(transactionsEntities);
        filterReport.showTransactionEntities(totalCount);
        filterReport.displayNumber = 20;


        reportResponse.setTransactions(transactionsEntities);


        getArguments().putSerializable(RESPONSE_TRANSACTION, reportResponse);
    }

    @Override
    public void showReportsService(ReportResponse body) {
        filterReport.showReportsService(body);
    }


    @Subscribe(threadMode = ThreadMode.MAIN)
    public void generateCustomToast(final NotificationData notificationData) {
        filterReport.reportService(reportRequest);
    }


    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(ReportRequest reportRequest) {
        transactionsEntities.clear();
        transChildExpandableAdapter.Update(new ArrayList<TransactionsEntitie>());
        transactionsEntities = new ArrayList<>();
        filterReport.generateReport();
    }

    @Override
    public void onResume() {
        super.onResume();


        if (isViewHidden) {
            return;
        }
        filterReport.displayNumber = 0;
        filterReport.reportService(reportRequest);

    }

    @Override
    public void onDestroyView() {
        presenter.detachView();
        super.onDestroyView();
    }


    @Override
    public void showMoreTransactions(List<TransactionsEntitie> transactions) {
        if (isViewHidden) {
            return;
        }

        if (transactions == null || transactions.size() == 0) {
            rCtransactions.setNoMore(true);
            return;
        }
        rCtransactions.loadMoreComplete();
        transactionsEntities.addAll(transactions);
        transChildExpandableAdapter.Update(transactionsEntities);
        filterReport.displayNumber = filterReport.displayNumber + 20;


        reportResponse.setTransactions(transactionsEntities);


        getArguments().putSerializable(RESPONSE_TRANSACTION, reportResponse);
    }
}
