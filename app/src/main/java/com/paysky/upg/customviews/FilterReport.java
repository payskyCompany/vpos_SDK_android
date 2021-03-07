package com.paysky.upg.customviews;

import android.animation.Animator;
import android.app.Activity;
import android.graphics.drawable.Drawable;
import android.os.SystemClock;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.paysky.upg.R;
import com.paysky.upg.dialog.FilterDialog;
import com.paysky.upg.fragment.DashBoardFragment;

import butterknife.BindDrawable;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.paysky.upg.data.network.model.request.ReportRequest;
import io.paysky.upg.data.network.model.response.ReportResponse;
import io.paysky.upg.mvp.reportdetailsservice.ReportDetailsServiceFragmentPresenter;
import io.paysky.upg.util.DateTimeUtil;
import io.paysky.upg.util.SessionManager;

public class FilterReport {


    @BindView(R.id.ResultNo)
    TextView ResultNo;
    @BindView(R.id.ResultFrom)
    TextView ResultFrom;
    @BindView(R.id.tv_total)
    TextView tv_total;
    @BindView(R.id.tv_total_tip)
    TextView tv_total_tip;
    @BindView(R.id.fillter_icon)
    ImageView fillter_icon;
    @BindView(R.id.ResultTo)
    TextView ResultTo;
    @BindView(R.id.ResultType)
    TextView ResultType;
    @BindView(R.id.ResultTerminalid)
    TextView ResultTerminalid;
    @BindDrawable(R.drawable.ic_expand_less_black_24dp)
    Drawable MoreDrawable;
    @BindDrawable(R.drawable.ic_expand_more_black_24dp)
    Drawable LessDrawable;
    @BindView(R.id.TerminalLL)
    LinearLayout TerminalLL;
    @BindView(R.id.FillterTransaction)
    LinearLayout FillterTransaction;

    @BindView(R.id.filterTitle)
    TextView filterTitle;

    @BindView(R.id.ll_total_tips)
    LinearLayout totalTipsLinearLayout;

    ReportRequest reportRequest;
    ReportResponse reportResponse;

    View header;
    public int displayNumber = 0;
    private long lastClickTime = 0;

    public View getHeader() {
        return header;
    }

    ReportDetailsServiceFragmentPresenter presenter;
    Activity context;
    boolean isViewHidden;

    public FilterReport(Activity context, ReportDetailsServiceFragmentPresenter presenter,
                        ReportRequest reportRequest,
                        ReportResponse reportResponse,
                        boolean isViewHidden) {
        this.context = context;
        this.presenter = presenter;
        this.isViewHidden = isViewHidden;
        header = LayoutInflater.from(context).inflate(R.layout.report_header_content,
                (ViewGroup) context.findViewById(R.id.header), false);

        this.reportRequest = reportRequest;
        this.reportResponse = reportResponse;
        ButterKnife.bind(this, header);


        totalTipsLinearLayout.setVisibility(View.GONE);
        setData();
    }


    public void setData() {
        ResultFrom.setText(reportRequest.getDateFromString());
        ResultTo.setText(reportRequest.getDateToString());
        tv_total.setText(reportRequest.getTotalAmountAllTransaction());
        ResultType.setText(context.getString(R.string.upg_general_Channel) + context.getString(R.string.upg_general_All));

        ResultNo.setText(context.getString(R.string.upg_dashboard_transaction_count_label) + reportResponse.getTotalCountAllTransaction());
        tv_total_tip.setText(reportRequest.getTotalAmountTipsTransaction());

        if (reportRequest.getChannel().equals("")) {
            ResultType.setText(context.getString(R.string.upg_general_Channel) + context.getString(R.string.upg_general_All));
        } else {
            ResultType.setText(context.getString(R.string.upg_general_Channel) +
                    SessionManager.getInstance().getEmpData().getChannelsName(reportRequest.getChannel()));
        }

        if (reportRequest.getFilterTerminalId() == null) {
            TerminalLL.setVisibility(View.GONE);
        } else {
            //  TerminalLL.setVisibility(View.VISIBLE);
            if (reportRequest.getFilterTerminalId().equals("")) {
                ResultTerminalid.setText(context.getString(R.string.upg_general_Terminal) + context.getString(R.string.upg_general_All));
            } else {
                ResultTerminalid.setText(context.getString(R.string.upg_general_Terminal) + reportRequest.getFilterTerminalId());
            }
        }


    }


    @OnClick({R.id.filter, R.id.fillter_icon})
    public void showFilter() {
        if (fillter_icon.getDrawable() == MoreDrawable) {
            fillter_icon.setImageDrawable(LessDrawable);
            YoYo.with(Techniques.SlideInDown)
                    .duration(500)
                    .onStart(new YoYo.AnimatorCallback() {
                        @Override
                        public void call(Animator animator) {
                            FillterTransaction.setVisibility(View.VISIBLE);
                        }
                    })
                    .playOn(FillterTransaction);

        } else {
            fillter_icon.setImageDrawable(MoreDrawable);
            YoYo.with(Techniques.SlideOutUp)
                    .duration(500)
                    .onEnd(new YoYo.AnimatorCallback() {
                        @Override
                        public void call(Animator animator) {
                            FillterTransaction.setVisibility(View.GONE);
                        }
                    })
                    .playOn(FillterTransaction);
        }
    }


    public void showTransactionEntities(String totalCount) {

        displayNumber = 20;
        ResultNo.setText(context.getString(R.string.upg_dashboard_transaction_count_label) + totalCount);
    }

    public void showReportsService(ReportResponse body) {
        ResultNo.setText(String.format("%s%s", context.getString(io.paysky.upg.R.string.upg_dashboard_transaction_count_label), body.getTotalCountAllTransaction()));
        tv_total.setText(String.format("%s%s", body.getCurrency(context), body.getTotalAmountAllTransaction()));
        tv_total_tip.setText(String.format("%s%s", body.getCurrency(context), body.getTotalAmountTipsTransaction()));
        reportService(reportRequest);
    }


    public void getBuildReport() {
        if (isViewHidden) {
            return;
        }
        presenter.buildTransactionChart(DashBoardFragment.StartDate, DashBoardFragment.EndDate,

                DashBoardFragment.ResultTypeString, DashBoardFragment.ResultTerminalidString,
                DashBoardFragment.MobileNumber
        );
    }


    public void generateReport() {
        if (DashBoardFragment.ResultTerminalidString == null) {
            ResultTerminalid.setVisibility(View.GONE);
        } else {
            ResultTerminalid.setVisibility(View.VISIBLE);

            if (DashBoardFragment.ResultTerminalidString.equals("") || DashBoardFragment.ResultTerminalidString == null) {
                ResultTerminalid.setText(context.getString(R.string.upg_general_Terminal) + context.getString(R.string.upg_general_All));
            } else {
                ResultTerminalid.setText(context.getString(R.string.upg_general_Terminal) + DashBoardFragment.ResultTerminalidString);

            }
        }
        if (DashBoardFragment.ResultTypeString.equals("")) {
            ResultType.setText(context.getString(R.string.upg_general_Channel) + context.getString(R.string.upg_general_All));
        } else {
            ResultType.setText(context.getString(R.string.upg_general_Channel) +
                    SessionManager.getInstance().getEmpData().getChannelsName(DashBoardFragment.ResultTypeString));
        }

        ResultFrom.setText(DateTimeUtil.getDateFromString(DashBoardFragment.StartDate));
        ResultTo.setText(DateTimeUtil.getDateFromString(DashBoardFragment.EndDate));
        getBuildReport();
    }


    public void reportService(ReportRequest reportRequest) {
        if (isViewHidden) {
            return;
        }
        if (reportRequest != null) {
            this.reportRequest = reportRequest;
            this.reportRequest.setDisplayLength("20");
            this.reportRequest.setDisplayStart("0");
            this.reportRequest.setChannel(DashBoardFragment.ResultTypeString);
            this.reportRequest.setTahweeltransactionId(DashBoardFragment.TahweeltransactionId);
            this.reportRequest.setDateFrom(DashBoardFragment.StartDate);
            this.reportRequest.setDateTo(DashBoardFragment.EndDate);
            this.reportRequest.setConsumerMobile(DashBoardFragment.MobileNumber);
            this.reportRequest.setFilterTerminalId(DashBoardFragment.ResultTerminalidString);
        }

        displayNumber = 0;


        presenter.filterTransaction(reportRequest, displayNumber);
    }


    @OnClick(R.id.FillterTransaction)
    public void filterTransactionDialog() {
        if (SystemClock.elapsedRealtime() - lastClickTime < 1000) {
            return;
        }
        lastClickTime = SystemClock.elapsedRealtime();
        FilterDialog filterDialog = new FilterDialog(context);
        if (!filterDialog.isShowing()) filterDialog.show();
    }

}
