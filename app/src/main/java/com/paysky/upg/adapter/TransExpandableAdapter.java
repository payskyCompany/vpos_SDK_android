package com.paysky.upg.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.paysky.upg.R;

import java.util.ArrayList;
import java.util.List;

import io.paysky.upg.data.network.model.response.TransactionsEntitie;
import io.paysky.upg.util.SessionManager;


/**
 * Created by Amr Abd Elrhim on 10/12/2016.
 */

public class TransExpandableAdapter extends RecyclerView.Adapter<TransExpandableAdapter.MyViewHolder> {
    String TAG = "OrderPaddingAdapter";
    private Context mContext;
    private List<TransactionsEntitie> transactionsModels;


    public TransExpandableAdapter(Context mContext, List<TransactionsEntitie> transactionsModels) {
        this.mContext = mContext;
        this.transactionsModels = transactionsModels;


    }


    public void Update(List<TransactionsEntitie> transactionsModels) {
        if (transactionsModels != null && transactionsModels.size() > 0) {
            this.transactionsModels = transactionsModels;
        } else {
            this.transactionsModels = new ArrayList<>();
        }
        notifyDataSetChanged();
    }


    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.transaction_report_parent_item, parent, false);

        return new MyViewHolder(itemView);
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
    public void onBindViewHolder(final MyViewHolder holder, int position) {

        final TransactionsEntitie transactionsModel = transactionsModels.get(position);
        holder.title_item.setText(transactionsModel.getDate());
        if (SessionManager.getInstance().getEmpData() == null) {
            return;
        }

        holder.TotalAmount.setText(mContext.getString(R.string.SalesFees)

                + SessionManager.getInstance().getEmpData().CurrencyName

                + " " + transactionsModel.getDateTotalAmount());
        TransChildExpandableAdapter ChildExpandableAdapter = new TransChildExpandableAdapter(mContext,
                transactionsModel.getDateTransactions(), position, transactionsModels);
        LinearLayoutManager llm = new LinearLayoutManager(mContext);
        holder.subtrans.setLayoutManager(llm);
        holder.subtrans.setAdapter(ChildExpandableAdapter);


    }


    @Override
    public int getItemCount() {
        return transactionsModels.size();
    }


    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView title_item, TotalAmount, trxnTypeTitle, timeTitle, refTitle, amountTitle;
        public RecyclerView subtrans;

        public MyViewHolder(View view) {
            super(view);
            title_item = (TextView) view.findViewById(R.id.date_transaction_tv);
            TotalAmount = (TextView) view.findViewById(R.id.TotalAmount);
            subtrans = (RecyclerView) view.findViewById(R.id.subtrans);

            trxnTypeTitle = (TextView) view.findViewById(R.id.trxnTypeTitle);
            timeTitle = (TextView) view.findViewById(R.id.timeTitle);
            refTitle = (TextView) view.findViewById(R.id.refTitle);
            amountTitle = (TextView) view.findViewById(R.id.amountTitle);


            refTitle.setText(mContext.getResources().getText(R.string.TrxID));
        }
    }


}
