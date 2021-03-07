package com.paysky.upg.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.paysky.upg.R;
import com.paysky.upg.activity.BaseActivity;
import com.paysky.upg.adapter.NotificationAdapter;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.paysky.upg.data.network.model.LoginUpgMerchant;
import io.paysky.upg.data.network.model.response.DateTransactions;
import io.paysky.upg.data.network.model.response.NotiEntity;
import io.paysky.upg.data.network.model.response.ParseISOQResponse;
import io.paysky.upg.mvp.notificationfragment.NotificationFragmentPresenter;
import io.paysky.upg.mvp.notificationfragment.NotificationFragmentView;


public class NotificationFragment extends BaseFragment implements NotificationAdapter.ItemClickListener, NotificationFragmentView {

    //GUI.
    @BindView(R.id.RCnoti)
    RecyclerView notificationList;


    @BindView(R.id.swiperefresh)
    SwipeRefreshLayout swiperefresh;
    NotificationAdapter notificationAdapter;
    @BindView(R.id.NotMessage)
    TextView NotMessage;
    //Variables.
    int display = 0;
    //Objects.
    private NotificationFragmentPresenter presenter = new NotificationFragmentPresenter();
    List<NotiEntity> notifications = new ArrayList<>();


    public static NotificationFragment newInstance() {
        return new NotificationFragment();
    }


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        // inflate views.
        View root = inflater.inflate(R.layout.fragment_notification, container, false);
        ButterKnife.bind(this, root);
        notifications = new ArrayList<>();
        notificationAdapter = new NotificationAdapter(getContext(), notifications, this);
        LinearLayoutManager llm = new LinearLayoutManager(getContext());
        notificationList.setLayoutManager(llm);
        notificationList.setAdapter(notificationAdapter);

//        if (BuildUtil.isADCBApp())
//        notificationList.addItemDecoration(new DividerItemDecoration(notificationList.getContext(), DividerItemDecoration.VERTICAL));


        swiperefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                notifications = new ArrayList<>();
                display = 0;
                notificationAdapter.loadMoreData = false;
                presenter.loadNotificationData(display);

            }
        });
        return root;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        presenter.attachView(this);
        notifications = new ArrayList<>();
        display = 0;
        notificationAdapter.loadMoreData = false;
        presenter.loadNotificationData(display);
    }


    @Override
    public void notificationDetails(NotiEntity notiEntity) {
        HandleData(notiEntity);
    }

    @Override
    public void pay(NotiEntity notiEntity) {
        HandleData(notiEntity);
    }

    public void HandleData(NotiEntity notiEntity) {
        if (!notiEntity.getTransaction().getTransactionNo().equals("0")) {
            ReportService(notiEntity.getTransaction().getTransactionNo());

        } else {
            if (!notiEntity.isBtnPay())
                presenter.loadQRData(notiEntity.getId());

        }

        if (!notiEntity.getSeen()) {
            seenAction(notiEntity);
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void endScroll(LoginUpgMerchant request) {
        if (notificationAdapter.loadMoreData) {
            notificationAdapter.loadMoreData = false;
            presenter.loadMoreNotifications(display);
        }
    }

    public void ReportService(String tahweelTransactionId) {


        presenter.loadReportService(tahweelTransactionId);
    }


    public void seenAction(NotiEntity notiEntity) {
        presenter.seenAction(notiEntity);
    }

    @Override
    public void onDestroyView() {
        presenter.detachView();
        super.onDestroyView();
    }


    @Override
    public void showProgress(int message) {
        swiperefresh.setRefreshing(true);
    }


    @Override
    public void dismissProgress() {
        swiperefresh.setRefreshing(false);
        super.dismissProgress();
    }

    @Override
    public void showNotifications(List<NotiEntity> notifications) {

        this.notifications.addAll(notifications);
        display = display + 1;
        notificationAdapter.loadMoreData = true;
        notificationAdapter.UpdateList(this.notifications);
        NotMessage.setVisibility(View.GONE);
        notificationList.setVisibility(View.VISIBLE);
    }

    @Override
    public void showMoreNotifications(List<NotiEntity> notifications) {
        this.notifications.addAll(notifications);
        display = display + 1;
        notificationAdapter.UpdateList(this.notifications);
        notificationAdapter.loadMoreData = true;
    }

    @Override
    public void showReceiptWithTransactionFragment(DateTransactions dateTransactions) {


        ((BaseActivity) getActivity()).addNewFragment(
                ReceiptWithTransFragment.newInstance(dateTransactions), "ReceiptWithTransFragment");


    }

    @Override
    public void notifyAdapterDataChanged() {
        notificationAdapter.notifyDataSetChanged();
    }

    @Override
    public void decreaseNotificationCount() {
    }


    @Override
    public void completeM2M(ParseISOQResponse versionReponse) {
        if (isViewHidden) {
            return;
        }
    }
}
