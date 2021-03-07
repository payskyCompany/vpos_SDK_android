package com.paysky.upg.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.paysky.upg.R;

import org.greenrobot.eventbus.EventBus;

import java.util.List;

import io.paysky.upg.data.network.model.response.DateTransactions;
import io.paysky.upg.data.network.model.response.TransactionsEntitie;


/**
 * Created by Amr Abd Elrhim on 10/12/2016.
 */

public class TransChildExpandableAdapter extends RecyclerView.Adapter<TransChildExpandableAdapter.MyViewHolder> {

    String TAG = "OrderPaddingAdapter";
    private Context mContext;
    private List<DateTransactions> transactionDetailsEntities;

    List<TransactionsEntitie> transactionsModels;


    public TransChildExpandableAdapter(Context mContext,
                                       List<DateTransactions> transactionDetailsEntities,
                                       int position, List<TransactionsEntitie> transactionsModels) {
        this.mContext = mContext;
        this.transactionDetailsEntities = transactionDetailsEntities;
        this.transactionsModels = transactionsModels;

    }


    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.trans_list_item, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {
        final DateTransactions data = transactionDetailsEntities.get(position);
        holder.time.setText(data.getTxnDateTime());

        if (data.getExternalTxnId() == null) {
            holder.trxid.setText(data.getTransactionId());
        } else {
            holder.trxid.setText(data.getExternalTxnId());
        }

        holder.type.setText(data.getTransType());
        holder.amount.setText(data.getCurrency() + data.getAmnt());


        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EventBus.getDefault().post(data);
            }
        });


        /****
         * getTrxIcon = 0 declined
         * getTrxIcon = 1 approved
         * getTrxIcon = 2 approved
         * getTrxIcon = 3 approved
         * getTrxIcon = 0 declined
         */

        if (data.getTxnIcon() == 0) {
            holder.arrowtransactin.setImageDrawable(mContext.getResources().getDrawable(R.drawable.transaction_declined));
        }

        if (position == (transactionDetailsEntities.size() - 1)) {
            holder.line.setVisibility(View.GONE);
        } else {
            holder.line.setVisibility(View.VISIBLE);

        }

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
    public int getItemCount() {
        return transactionDetailsEntities.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView time, trxid, type, amount;

        public ImageView arrowtransactin;

        public View line;

        public MyViewHolder(View view) {
            super(view);
            time = view.findViewById(R.id.time);
            trxid = view.findViewById(R.id.trxid);
            type = view.findViewById(R.id.method);
            amount = view.findViewById(R.id.amount);
            line = view.findViewById(R.id.line);
            arrowtransactin = view.findViewById(R.id.arrowtransactin);


        }
    }

}
