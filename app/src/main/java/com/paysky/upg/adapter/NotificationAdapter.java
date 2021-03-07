package com.paysky.upg.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.paysky.upg.R;

import org.greenrobot.eventbus.EventBus;

import java.util.List;

import io.paysky.upg.data.network.model.LoginUpgMerchant;
import io.paysky.upg.data.network.model.response.NotiEntity;


/**
 * Created by Amr Abd Elrhim on 10/12/2016.
 */

public class NotificationAdapter extends RecyclerView.Adapter<NotificationAdapter.MyViewHolder> {

    String TAG = "NotificationAdapter";
    private Context mContext;
    private List<NotiEntity> notiEntities;
    private ItemClickListener itemClickListener;

    public boolean loadMoreData = true;

    public NotificationAdapter(Context mContext, List<NotiEntity> notiEntities, ItemClickListener itemClickListener) {
        this.mContext = mContext;
        this.notiEntities = notiEntities;
        this.itemClickListener = itemClickListener;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.noti_items, parent, false);
        return new MyViewHolder(itemView);
    }

    public void UpdateList(List<NotiEntity> notiEntities) {
        this.notiEntities.clear();
        this.notiEntities.addAll(notiEntities);
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
    public void onBindViewHolder(final MyViewHolder holder, int position) {
        final NotiEntity data = notiEntities.get(position);
        holder.dataTime.setText(data.getDate() + " " + data.getTime());
        holder.Amount.setText(mContext.getString(R.string.AmountEGP) + data.getTransaction().getPrice());


        if (data.isSend()) {
            holder.FromOrTo.setText(data.getTransaction().getTransactionTypeName() + ": "
                    + data.getTransaction().getToName()
                    + " (" + data.getTransaction().getTo() + ")");

        } else {
            holder.FromOrTo.setText(data.getTransaction().getTransactionTypeName() + ": " +
                    data.getTransaction().getFromName() +
                    " (" + data.getTransaction().getFromNumber() + ")");

        }


        holder.trxid.setText(mContext.getString(R.string.TrxID) + ": " + data.getTransaction().getTransactionNo());


        if (position > notiEntities.size() - 3 && loadMoreData && notiEntities.size() > 8) {
            EventBus.getDefault().post(new LoginUpgMerchant());
        }


        holder.itemView.setTag(R.id.Amount, data);


        if (data.getTransaction().getTransactionNo().equals("0")) {
            holder.FromOrTo.setText(data.getMessage());
            holder.trxid.setVisibility(View.GONE);
            holder.Amount.setVisibility(View.GONE);
            holder.icon_image_pay.setVisibility(View.VISIBLE);
            holder.payLL.setVisibility(View.VISIBLE);
            holder.icon_image.setVisibility(View.GONE);
            holder.pay.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    itemClickListener.pay(data);
                }
            });

            if (data.isBtnPay()) {
                holder.pay.setText(holder.itemView.getContext().getString(R.string.upg_general_Paid));
            } else {
                holder.pay.setText(holder.itemView.getContext().getString(R.string.upg_general_pay));

            }
            holder.pay.setVisibility(View.VISIBLE);
        } else {
            holder.payLL.setVisibility(View.GONE);
            holder.icon_image_pay.setVisibility(View.GONE);
            holder.icon_image.setVisibility(View.VISIBLE);

        }


    }


    @Override
    public int getItemCount() {
        return notiEntities.size();
    }


    class MyViewHolder extends RecyclerView.ViewHolder {
        TextView dataTime, Amount, FromOrTo, trxid;
        CardView MasterRl;
        ImageView icon_image, icon_image_pay;
        LinearLayout payLL;

        View id_noti_item_view;
        Button pay;

        LinearLayout toast_layout_root;

        MyViewHolder(View view) {
            super(view);
            dataTime = view.findViewById(R.id.dataTime);
            pay = view.findViewById(R.id.pay);
            payLL = view.findViewById(R.id.payLL);
            icon_image = view.findViewById(R.id.icon_image);
            icon_image_pay = view.findViewById(R.id.icon_image_pay);
            Amount = view.findViewById(R.id.Amount);
            FromOrTo = view.findViewById(R.id.FromOrTo);
            trxid = view.findViewById(R.id.trxid);
            MasterRl = view.findViewById(R.id.cardview);

            id_noti_item_view = view.findViewById(R.id.id_noti_item_view);
            toast_layout_root = view.findViewById(R.id.toast_layout_root);
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    NotiEntity entity = (NotiEntity) v.getTag(R.id.Amount);
                    itemClickListener.notificationDetails(entity);
                }
            });
        }
    }

    public interface ItemClickListener {
        void notificationDetails(NotiEntity notiEntity);

        void pay(NotiEntity notiEntity);
    }
}
