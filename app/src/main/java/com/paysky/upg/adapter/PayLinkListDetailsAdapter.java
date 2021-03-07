package com.paysky.upg.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.paysky.upg.R;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.paysky.upg.data.network.model.response.OrderPaymentsList;

/**
 * Created by Amr Abd Elrhim on 09/07/2016.
 */
public class PayLinkListDetailsAdapter extends RecyclerView.Adapter<PayLinkListDetailsAdapter.MyViewHolder> {


    List<OrderPaymentsList> transactionModels = new ArrayList<>();
    ItemClickListener itemClickListener;

    public PayLinkListDetailsAdapter(List<OrderPaymentsList> transactionModels,
                                     ItemClickListener mClickListener
    ) {

        this.transactionModels = transactionModels;
        this.itemClickListener = mClickListener;
    }

    public void updateList(List<OrderPaymentsList> transactionModels) {
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
                .inflate(R.layout.paylink_details_item, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        final OrderPaymentsList transactionModel = transactionModels.get(position);
        holder.OrderID.setText(transactionModel.getSystemreferencenumber());
        holder.Status.setText(transactionModel.getPaymentmethod());
        holder.amount.setText("" + transactionModel.getAmount());
        holder.Ref.setText(transactionModel.getNetworkreferencenumber());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                itemClickListener.onItemClick(v, transactionModel, position);
            }
        });
    }


    @Override
    public int getItemCount() {
        return transactionModels.size();
    }


    public class MyViewHolder extends RecyclerView.ViewHolder {


        @BindView(R.id.Order_ID)
        TextView OrderID;
        @BindView(R.id.Status)
        TextView Status;
        @BindView(R.id.Ref)
        TextView Ref;
        @BindView(R.id.amount)
        TextView amount;

        public MyViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);

        }
    }

    public interface ItemClickListener {
        void onItemClick(View view, OrderPaymentsList object, int position);
    }

}
