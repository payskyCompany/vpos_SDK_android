package com.paysky.upg.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.paysky.upg.R;
import com.paysky.upg.model.TransactionModel;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Amr Abd Elrhim on 09/07/2016.
 */
public class TransactionAdapter extends RecyclerView.Adapter<TransactionAdapter.MyViewHolder> {


    List<TransactionModel> transactionModels = new ArrayList<>();
    ItemClickListener itemClickListener;


    public TransactionAdapter(List<TransactionModel> transactionModels, ItemClickListener mClickListener) {
        this.transactionModels = transactionModels;
        this.itemClickListener = mClickListener;
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
                .inflate(R.layout.transaction_item, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        final TransactionModel transactionModel = transactionModels.get(position);
        holder.TransactionsImage.setImageResource(transactionModel.getDrawable());
        holder.TransactionsName.setText(transactionModel.getTransactionName());
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

        @BindView(R.id.TransactionsImage)
        ImageView TransactionsImage;

        @BindView(R.id.TransactionsName)
        TextView TransactionsName;

        public MyViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);

        }
    }

    public interface ItemClickListener {
        void onItemClick(View view, TransactionModel object, int position);
    }

}
