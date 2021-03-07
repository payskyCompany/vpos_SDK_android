package com.paysky.upg.fragment;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.jcodecraeer.xrecyclerview.XRecyclerView;
import com.paysky.upg.R;
import com.paysky.upg.adapter.PayLinkListDetailsAdapter;

import java.io.Serializable;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import io.paysky.upg.data.network.model.response.DateTransactions;
import io.paysky.upg.data.network.model.response.OrderPaymentsList;
import io.paysky.upg.data.network.model.response.OrdersListBean;
import io.paysky.upg.data.network.model.response.SearchOrdersResponse;
import io.paysky.upg.mvp.paylinklist.PayLinkListPresenter;
import io.paysky.upg.mvp.paylinklist.PayLinkListView;


/**
 * A simple {@link Fragment} subclass.
 */
public class PayLinkListDetailsFragment extends BaseFragment implements PayLinkListDetailsAdapter.ItemClickListener,
        PayLinkListView {


    @BindView(R.id.list_resturant)
    XRecyclerView listResturant;
    Unbinder unbinder;


    PayLinkListPresenter payLinkListPresenter = new PayLinkListPresenter();

    public PayLinkListDetailsFragment() {
        // Required empty public constructor
    }


    public static PayLinkListDetailsFragment newPayLinkListDetails(List<OrderPaymentsList> paymentList,
                                                                   OrdersListBean orderList) {
        Bundle bundle = new Bundle();
        bundle.putSerializable("paymentList", (Serializable) paymentList);
        bundle.putSerializable("orderList", orderList);
        PayLinkListDetailsFragment servicesFragment = new PayLinkListDetailsFragment();
        servicesFragment.setArguments(bundle);
        return servicesFragment;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    PayLinkListDetailsAdapter adapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_pay_link_list_details, container, false);
        unbinder = ButterKnife.bind(this, view);
        loadDataBasedOnBundle(getArguments());

        payLinkListPresenter.attachView(this);

        listResturant.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new PayLinkListDetailsAdapter(paymentList, this);
        listResturant.setAdapter(adapter);


        listResturant.setNoMore(true);
        listResturant.setLoadingListener(new XRecyclerView.LoadingListener() {
            @Override
            public void onRefresh() {
                searchWithClear();

            }

            @Override
            public void onLoadMore() {

            }
        });

        return view;
    }

    List<OrderPaymentsList> paymentList;
    OrdersListBean orderList;

    private void loadDataBasedOnBundle(Bundle arguments) {
        if (arguments.containsKey("paymentList")) {
            paymentList = (List<OrderPaymentsList>) arguments.getSerializable("paymentList");
        }
        if (arguments.containsKey("orderList")) {
            orderList = (OrdersListBean) arguments.getSerializable("orderList");

        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }


    @Override
    public void onItemClick(View view, OrderPaymentsList object, int position) {
        progress.show();
        payLinkListPresenter.loadReportService(paymentList.get(position).getNetworkreferencenumber());
    }


    @Override
    public void payLinkTransactionList(SearchOrdersResponse ordersListBeans) {

    }

    @Override
    public void payLinkPaymentList(List<OrderPaymentsList> ordersListBeans) {
        progress.dismiss();
        adapter.updateList(ordersListBeans);
        listResturant.refreshComplete();
        listResturant.setNoMore(true);

    }

    @Override
    public void showReceiptWithTransactionFragment(DateTransactions transactionsEntitie) {
        progress.dismiss();
        if (isViewHidden) {
            return;
        }
        addNewFragment(ReceiptWithTransFragment.newInstance(transactionsEntitie), "ReceiptWithTransFragment");
    }

    @Override
    public void searchWithClear() {
        payLinkListPresenter.detailsPayLink(orderList.getId());

    }

}
