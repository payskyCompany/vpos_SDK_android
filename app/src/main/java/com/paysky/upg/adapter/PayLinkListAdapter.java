package com.paysky.upg.adapter;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.blankj.utilcode.util.ActivityUtils;
import com.paysky.upg.R;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.paysky.upg.data.network.model.response.OrdersListBean;

/**
 * Created by Amr Abd Elrhim on 09/07/2016.
 */
public class PayLinkListAdapter extends RecyclerView.Adapter<PayLinkListAdapter.MyViewHolder> {

    private List<OrdersListBean> transactionModels = new ArrayList<>();
    private ItemClickListener itemClickListener;

    public PayLinkListAdapter(List<OrdersListBean> transactionModels,
                              ItemClickListener mClickListener
    ) {
        this.transactionModels = transactionModels;
        this.itemClickListener = mClickListener;
    }

    public void updateList(List<OrdersListBean> transactionModels) {
        this.transactionModels = transactionModels;
        notifyDataSetChanged();

    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.paylink_item, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        final OrdersListBean transactionModel = transactionModels.get(position);
        holder.OrderID.setText("" + transactionModel.getId());
        holder.terminalId.setText(transactionModel.getTerminalId());
        holder.Status.setText(transactionModel.getOrderStatus());
        holder.Date.setText(transactionModel.getInsertionDateTime());
        holder.Ref.setText(transactionModel.getBillerRefNumber());
        holder.amount.setText("" + transactionModel.getFormatedAmount());

        if (transactionModel.getBillerRefNumber() == null || transactionModel.getBillerRefNumber().isEmpty()) {
            holder.MerchantReferanceLL.setVisibility(View.GONE);
        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                itemClickListener.onItemClick(v, transactionModel, position);
            }
        });

        //  if (transactionModel.getNumberOfPayment() > 1) {

        //  } else {

        //   }


        if (transactionModel.getMaxNumberOfPayment() > 1) {
            holder.numberpaymentLL.setVisibility(View.VISIBLE);
            holder.numberpayment.setText("" + transactionModel.getNumberOfPayment());
            holder.MaxNumberOfPaymentLL.setVisibility(View.VISIBLE);
            holder.MaxNumberOfPayment.setText("" + transactionModel.getMaxNumberOfPayment());
        } else {
            holder.numberpaymentLL.setVisibility(View.GONE);

            holder.MaxNumberOfPaymentLL.setVisibility(View.GONE);

        }


        if (transactionModel.getFileName() == null || transactionModel.getFileName().isEmpty()) {
            holder.FileLL.setVisibility(View.GONE);
        } else {
            holder.FileLL.setVisibility(View.VISIBLE);
            holder.FileName.setText(transactionModel.getFileName());
            holder.FileName.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    openUrl(transactionModel.getFilePath());
                }
            });

        }
    }


    @Override
    public int getItemCount() {
        return transactionModels.size();
    }


    public class MyViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.Order_ID)
        TextView OrderID;
        @BindView(R.id.MaxNumberOfPayment)
        TextView MaxNumberOfPayment;
        @BindView(R.id.amount)
        TextView amount;
        @BindView(R.id.terminal_id)
        TextView terminalId;
        @BindView(R.id.Status)
        TextView Status;
        @BindView(R.id.Date)
        TextView Date;
        @BindView(R.id.Ref)
        TextView Ref;
        @BindView(R.id.MerchantReferanceLL)
        LinearLayout MerchantReferanceLL;
        @BindView(R.id.numberpaymentLL)
        LinearLayout numberpaymentLL;
        @BindView(R.id.MaxNumberOfPaymentLL)
        LinearLayout MaxNumberOfPaymentLL;
        @BindView(R.id.FileLL)
        LinearLayout FileLL;
        @BindView(R.id.numberpayment)
        TextView numberpayment;
        @BindView(R.id.FileName)
        TextView FileName;

        public MyViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);

        }
    }

    public interface ItemClickListener {
        void onItemClick(View view, OrdersListBean object, int position);
    }


    public void openUrl(String Url) {
        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(Url));
        PackageManager manager = ActivityUtils.getTopActivity().getPackageManager();
        List<ResolveInfo> infos = manager.queryIntentActivities(browserIntent, 0);
        if (infos.size() > 0) {
            ActivityUtils.startActivity(browserIntent);
        } else {
        }

        return;
    }

}
