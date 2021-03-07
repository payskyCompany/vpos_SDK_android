package com.paysky.upg.fragment;

import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.drawable.DrawableCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;

import com.paysky.upg.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.paysky.upg.data.network.model.request.QrGeneratorRequest;
import io.paysky.upg.data.network.model.request.ReportRequest;
import io.paysky.upg.data.network.model.response.DateTransactions;
import io.paysky.upg.data.network.model.response.ReportResponse;
import io.paysky.upg.mvp.transactionloadingfragment.TransactionLoadingFragmentPresenter;
import io.paysky.upg.mvp.transactionloadingfragment.TransactionLoadingFragmentView;
import io.paysky.upg.util.ToastUtil;

public class TransactionsLoadingFragment extends BaseFragment implements TransactionLoadingFragmentView {
    //GUI
    @BindView(R.id.ProgressImage)
    ImageView ProgressImage;
    @BindView(R.id.TextLoadding)
    TextView TextLoading;
    @BindView(R.id.CountDown)
    TextView CountDown;
    //Objects.
    private TransactionLoadingFragmentPresenter presenter = new TransactionLoadingFragmentPresenter();
    // constants.
    public static String FROM_WHERE_SALE = "RESPONSE_TRANSACTION";
    public static String FROM_WHERE_REPORT = "FROM_WHERE_REPORT";
    public static String FROM_WHERE = "FROM_WHERE";
    //Variables.
    String fromwhere = "";

    public static TransactionsLoadingFragment newInstance() {
        return new TransactionsLoadingFragment();
    }


    public static Fragment newInstance(String fromwhere) {
        TransactionsLoadingFragment fragment = new TransactionsLoadingFragment();
        Bundle args = new Bundle();
        args.putString(FROM_WHERE, fromwhere);
        fragment.setArguments(args);
        return fragment;

    }

    public static Fragment newInstance(QrGeneratorRequest saleQrModel) {

        TransactionsLoadingFragment fragment = new TransactionsLoadingFragment();
        Bundle args = new Bundle();
        args.putSerializable(FROM_WHERE_SALE, saleQrModel);
        fragment.setArguments(args);
        return fragment;
    }

    public static Fragment newInstance(ReportRequest saleQrModel) {
        TransactionsLoadingFragment fragment = new TransactionsLoadingFragment();
        Bundle args = new Bundle();
        args.putSerializable(FROM_WHERE_REPORT, saleQrModel);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle arguments = getArguments();

        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true);

        if (arguments != null) {
            if (arguments.getSerializable(FROM_WHERE_SALE) != null) {
                QrGeneratorRequest saleQrModel = (QrGeneratorRequest) arguments.getSerializable(FROM_WHERE_SALE);
                presenter.setSalesQrModel(saleQrModel);
            }

            if (arguments.getSerializable(FROM_WHERE_REPORT) != null) {
                ReportRequest reportRequest = (ReportRequest) arguments.getSerializable(FROM_WHERE_REPORT);
                presenter.setReportRequest(reportRequest);
            }

            if (arguments.getString(FROM_WHERE) != null) {
                fromwhere = arguments.getString(FROM_WHERE);
            }
        }
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View root = inflater.inflate(R.layout.fragment_loadding_transactions, container, false);
        ButterKnife.bind(this, root);
        presenter.attachView(this);


        ProgressImage.setBackgroundResource(R.drawable.server_progress_animation);
        DrawableCompat.setTint(ProgressImage.getBackground(), ContextCompat.getColor(getContext(), R.color.primaryBlue));

        ProgressImage.setColorFilter(R.color.primaryBlue);
        AnimationDrawable serverAnimation = (AnimationDrawable) ProgressImage
                .getBackground();

        serverAnimation.start();

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                getUserRouter();
            }
        }, 2000);

        if (presenter.getReportRequest() != null) {
            TextLoading.setText(getString(R.string.upg_retrieving_transactions_message));

        } else if (presenter.getSalesQrModel() != null) {
            TextLoading.setText(getString(R.string.GeneratingDynamicQR));
        }

        TextLoading.setTextColor(getResources().getColor(R.color.primaryBlue));

        new CountDownTimer(30000, 1000) {

            public void onTick(long millisUntilFinished) {
                CountDown.setText(String.format("%d", (millisUntilFinished / 1000)));
                //here you can have your logic to set text to edittext
            }

            public void onFinish() {
                if (isViewHidden) {
                    return;
                }
                ToastUtil.showShortToast(getActivity(), R.string.no_internet_connectiity);
                addNewFragmentNullBackStack(DashBoardFragment.newInstance(), "DashBoardFragment");
                removeStack();
            }

        }.start();

        return root;
    }

    public void getUserRouter() {
        if (presenter.getSalesQrModel() != null) {
            voucherQrSale();
        } else if (fromwhere.equals("ReciptFragment")) {
            addNewFragmentNullBackStack(ReceiptWithTransFragment.newInstance(), "ReceiptWithTransFragment");
        } else if (presenter.getReportRequest() != null) {
            reportService();
        }
    }


    public void reportService() {
        presenter.loadReports();
    }


    public void voucherQrSale() {
        presenter.voucherOrSale();
    }

    @Override
    public void showReportDetailsFragment(ReportResponse body, ReportRequest reportRequest) {
        addNewFragmentNullBackStack(ReportDetails.newInstance(body, reportRequest), "ReportDetails");
    }

    @Override
    public void showReceiptWithTransactionFragment(DateTransactions dateTransactions) {
        addNewFragmentNullBackStack(ReceiptWithTransFragment.newInstance(dateTransactions), "ReceiptWithTransFragment");
    }

    @Override
    public void showReportsFragment() {
        addNewFragmentNullBackStack(DashBoardFragment.newInstance(), "DashBoardFragment");
    }

    QrGeneratorRequest salesQrModel;

    @Override
    public void showStaticQrFragment(QrGeneratorRequest salesQrModel) {
        this.salesQrModel = salesQrModel;

    }

    @Override
    public void callBack() {
        FragmentActivity activity = getActivity();
        if (activity != null) {
            activity.onBackPressed();
        }
    }
}
