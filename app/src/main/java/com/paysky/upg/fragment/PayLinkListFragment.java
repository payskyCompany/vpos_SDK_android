package com.paysky.upg.fragment;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.jcodecraeer.xrecyclerview.XRecyclerView;
import com.paysky.upg.R;
import com.paysky.upg.adapter.PayLinkListAdapter;
import com.paysky.upg.customviews.PayLinkFilter;

import java.util.ArrayList;
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
public class PayLinkListFragment extends BaseFragment implements
        PayLinkListView, PayLinkListAdapter.ItemClickListener {


    @BindView(R.id.list_resturant)
    XRecyclerView listResturant;
    Unbinder unbinder;
    PayLinkListPresenter payLinkListPresenter = new PayLinkListPresenter();
    List<OrdersListBean> ordersListBeans = new ArrayList<>();
    PayLinkListAdapter adapter;
    PayLinkFilter payLinkFilter;
    OrdersListBean selectedOrder;

    public PayLinkListFragment() {
        // Required empty public constructor
    }


    public static PayLinkListFragment newInstance(String fromWhere) {
        PayLinkListFragment fragment = new PayLinkListFragment();
        Bundle args = new Bundle();
        args.putSerializable(TransactionsLoadingFragment.FROM_WHERE, fromWhere);
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container,
                             Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_pay_link_list, container, false);
        unbinder = ButterKnife.bind(this, view);
        payLinkListPresenter.attachView(this);
        payLinkFilter = new PayLinkFilter(getActivity(), payLinkListPresenter);
        setData();
        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
        payLinkListPresenter.detachView();
    }

    public void searchWithClear() {
        payLinkFilter.pageNumber = 1;
        ordersListBeans = new ArrayList<>();
        adapter.updateList(ordersListBeans);
        payLinkFilter.search();

    }


    public void setData() {


        initDataRecycler();


    }


    public void initDataRecycler() {
        listResturant.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new PayLinkListAdapter(ordersListBeans, this);
        listResturant.setAdapter(adapter);
        listResturant.addHeaderView(payLinkFilter.getHeader());


        listResturant.setLoadingListener(new XRecyclerView.LoadingListener() {
            @Override
            public void onRefresh() {
                searchWithClear();

            }

            @Override
            public void onLoadMore() {

                payLinkFilter.search();
            }
        });


        listResturant.setFootViewText(getString(R.string.loading), getString(R.string.no_data));
    }


    @Override
    public void payLinkTransactionList(SearchOrdersResponse ordersListBeans) {
        if (isViewHidden) {
            return;
        }


        if (payLinkFilter.pageNumber == 1) {
            this.ordersListBeans = new ArrayList<>();
            listResturant.refreshComplete();

            if (ordersListBeans != null)
                if (ordersListBeans.getOrdersList() == null || ordersListBeans.getOrdersList().size() == 0) {
                    adapter.updateList(new ArrayList<OrdersListBean>());
                    payLinkFilter.setDataAnalytic(ordersListBeans);
                }
        }
        if (ordersListBeans != null)
            if (ordersListBeans.getOrdersList() == null || ordersListBeans.getOrdersList().size() == 0) {
                listResturant.setNoMore(true);
                return;
            }

        if (ordersListBeans != null) {
            this.ordersListBeans.addAll(ordersListBeans.getOrdersList());
        }
        adapter.updateList(this.ordersListBeans);
        listResturant.loadMoreComplete();
        payLinkFilter.pageNumber = payLinkFilter.pageNumber + 1;
        if (ordersListBeans != null) {
            payLinkFilter.setDataAnalytic(ordersListBeans);
        }

    }


    @Override
    public void payLinkPaymentList(List<OrderPaymentsList> ordersListBeans) {
        addNewFragment(PayLinkListDetailsFragment.newPayLinkListDetails(ordersListBeans, selectedOrder), PayLinkListDetailsFragment.class.getName());
        dismissProgress();
    }

    @Override
    public void showReceiptWithTransactionFragment(DateTransactions transactionsEntitie) {
        addNewFragment(ReceiptWithTransFragment.newInstance(transactionsEntitie), "ReceiptWithTransFragment");
        dismissProgress();

    }


    @Override
    public void onItemClick(View view, OrdersListBean object, int position) {
        selectedOrder = object;
        payLinkListPresenter.detailsPayLink(this.ordersListBeans.get(position).getId());

    }

    @Override
    public void dismissProgress() {
        super.dismissProgress();
    }
}
