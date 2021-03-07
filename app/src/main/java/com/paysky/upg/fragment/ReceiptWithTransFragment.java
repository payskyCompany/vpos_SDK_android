package com.paysky.upg.fragment;

import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.SystemClock;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDelegate;

import com.paysky.upg.R;
import com.paysky.upg.dialog.PasswordDialog;
import com.paysky.upg.dialog.ReFundCardDialog;
import com.paysky.upg.dialog.SendEmailDialog;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.paysky.upg.data.network.model.BaseResponse;
import io.paysky.upg.data.network.model.response.DateTransactions;
import io.paysky.upg.util.SessionManager;


public class ReceiptWithTransFragment extends BaseFragment {

    //GUI.
    @BindView(R.id.RejectResonLL)
    LinearLayout RejectResonLL;
    @BindView(R.id.RejectResonTV)
    TextView RejectResonTV;
    @BindView(R.id.MobileNumberLL)
    LinearLayout MobileNumberLL;
    @BindView(R.id.ReferenceLL)
    LinearLayout ReferenceLL;
    @BindView(R.id.tahweeltransLL)
    LinearLayout tahweeltransLL;
    @BindView(R.id.TransaImageStatus)
    ImageView TransaImageStatus;
    @BindView(R.id.TransStatus)
    TextView TransStatus;
    @BindView(R.id.AuthCode)
    TextView AuthCode;
    @BindView(R.id.TxnDateTime)
    TextView TxnDateTime;
    @BindView(R.id.TransType)
    TextView TransType;
    @BindView(R.id.MobileNumber)
    TextView MobileNumber;
    @BindView(R.id.ReferenceNumber)
    TextView ReferenceNumber;
    @BindView(R.id.Amnt)
    TextView Amnt;
    @BindView(R.id.tahweeltransid)
    TextView tahweeltransid;
    @BindView(R.id.RefundBtn)
    Button RefundBtn;
    @BindView(R.id.capture)
    Button capture;
    @BindView(R.id.SENRECET)
    Button SENRECET;


//    @Nullable
//    @BindView(R.id.btn_Email)
//    OmegaCenterIconButton btn_Email;
//
//    @Nullable
//    @BindView(R.id.btn_sms)
//    OmegaCenterIconButton btn_sms;

    @BindView(R.id.transid)
    TextView transid;
    @BindView(R.id.TipAmntLL)
    LinearLayout TipAmntLL;
    @BindView(R.id.TipAmnt)
    TextView TipAmnt;
    @BindView(R.id.CardNumberLL)
    LinearLayout CardNumberLL;
    @BindView(R.id.CardNumber)
    TextView CardNumber;
    @BindView(R.id.FeeAmntLL)
    LinearLayout FeeAmntLL;
    @BindView(R.id.FeeAmnt)
    TextView FeeAmnt;
    @BindView(R.id.priceLL)
    LinearLayout priceLL;
    @BindView(R.id.priceAmount)
    TextView priceAmount;
    @BindView(R.id.NameLL)
    LinearLayout NameLL;
    @BindView(R.id.Name)
    TextView Name;


    @BindView(R.id.IsRefund)
    LinearLayout IsRefund;
    @BindView(R.id.RefundReasonLL)
    LinearLayout RefundReasonLL;

    @BindView(R.id.OriginalTransactionId)
    TextView OriginalTransactionId;

    @BindView(R.id.OriginalTransactionIdLL)
    LinearLayout OriginalTransactionIdLL;

    @Nullable
    @BindView(R.id.receipt_operation_tv)
    TextView receipt_operation_tv;

    @BindView(R.id.RefundSourceLL)
    LinearLayout RefundSourceLL;
    @BindView(R.id.RefundUserCreatorLL)
    LinearLayout RefundUserCreatorLL;

    @BindView(R.id.RefundReason)
    TextView RefundReason;
    @BindView(R.id.RefundSource)
    TextView RefundSource;
    @BindView(R.id.RefundUserCreator)
    TextView RefundUserCreator;


    @BindView(R.id.close)
    ImageView close;


    //Variables.
    static String FROM_WHERE = "FROM_WHERE";
    //Objects.
    DateTransactions dateTransactions;

    String mobileNumber;
    private long lastClickTime = 0;

    public static ReceiptWithTransFragment newInstance() {
        return new ReceiptWithTransFragment();
    }


    boolean blocBtn = false;

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(BaseResponse x) {
        dateTransactions.setRefundEnabled(false);
        // TransactionChannel.setText(dateTransactions.getTransactionChannel() + "refund ");
        Drawable mDrawable = getResources().getDrawable(R.drawable.gray_with_redius);
        RefundBtn.setBackground(mDrawable);
        capture.setBackground(mDrawable);
        capture.setVisibility(View.GONE);
        RefundBtn.setVisibility(View.GONE);
        blocBtn = true;
    }


    public static ReceiptWithTransFragment newInstance(DateTransactions dateTransactions) {
        ReceiptWithTransFragment fragment = new ReceiptWithTransFragment();
        Bundle args = new Bundle();
        args.putSerializable(FROM_WHERE, dateTransactions);
        fragment.setArguments(args);
        return fragment;
    }


    public static ReceiptWithTransFragment newInstance(String transaNumber) {
        return new ReceiptWithTransFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View root = inflater.inflate(R.layout.fragment_recipt_with_transaction, container, false);
        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true);
        ButterKnife.bind(this, root);
        if (getArguments() != null && getArguments().getSerializable(FROM_WHERE) != null) {
            dateTransactions = (DateTransactions) getArguments().getSerializable(FROM_WHERE);
        }

        if (dateTransactions != null && dateTransactions.isRefund()) {
            if (dateTransactions.getRefundReason() == null || dateTransactions.getRefundReason().isEmpty()) {
                RefundReasonLL.setVisibility(View.GONE);
            } else {
                RefundReason.setText(dateTransactions.getRefundReason());
                RefundReasonLL.setVisibility(View.VISIBLE);
            }


            if (dateTransactions.getRefundSource() == null || dateTransactions.getRefundSource().isEmpty()) {
                RefundSourceLL.setVisibility(View.GONE);
            } else {
                RefundSource.setText(dateTransactions.getRefundSource());
                RefundSourceLL.setVisibility(View.VISIBLE);
            }


            if (dateTransactions.getRefundUserCreator() == null || dateTransactions.getRefundUserCreator().isEmpty()) {
                RefundUserCreatorLL.setVisibility(View.GONE);
            } else {
                RefundUserCreator.setText(dateTransactions.getRefundUserCreator());
                RefundUserCreatorLL.setVisibility(View.VISIBLE);
            }

        } else {
            RefundUserCreatorLL.setVisibility(View.GONE);
            RefundSourceLL.setVisibility(View.GONE);
            RefundReasonLL.setVisibility(View.GONE);

        }


        if (dateTransactions != null) {
            if (dateTransactions.getStatus().equals("Approved")) {
                TransaImageStatus.setImageDrawable(getResources().getDrawable(R.drawable.complete_transaction));
                TransStatus.setText(getString(R.string.TransactionApprove));
            } else {
                TransaImageStatus.setImageDrawable(getResources().getDrawable(R.drawable.transaction_declined));
                TransStatus.setText(getString(R.string.TransactionReject));
                RefundBtn.setVisibility(View.GONE);
                if (dateTransactions.getResCodeDesc() == null || dateTransactions.getResCodeDesc().isEmpty()) {
                    RejectResonLL.setVisibility(View.GONE);
                } else {
                    RejectResonTV.setText(dateTransactions.getResCodeDesc());
                    RejectResonLL.setVisibility(View.VISIBLE);
                }
            }
        }


        if (dateTransactions != null && !dateTransactions.getOriginalTxnId().equals("") && !dateTransactions.getOriginalTxnId().isEmpty()) {
            OriginalTransactionIdLL.setVisibility(View.VISIBLE);
            OriginalTransactionId.setText(dateTransactions.getOriginalTxnId());
        }
        if (dateTransactions != null)
            if (receipt_operation_tv != null) {
                receipt_operation_tv.setText(String.format(" - %s", dateTransactions.getTransType()));
            }

        if (dateTransactions != null) {
            TxnDateTime.setText(dateTransactions.getFullTxnDateTime());
        }
        if (dateTransactions != null) {
            TransType.setText(dateTransactions.getTransType());
        }
        //       TransactionChannel.setText(dateTransactions.getTransactionChannel());
        if (dateTransactions != null) {
            if (dateTransactions.getMobileNumber() == null || dateTransactions.getMobileNumber().isEmpty()) {
                MobileNumberLL.setVisibility(View.GONE);
            } else {
                mobileNumber = dateTransactions.getMobileNumber();
                MobileNumber.setText(mobileNumber);
                MobileNumberLL.setVisibility(View.VISIBLE);
            }
        }

        if (dateTransactions != null) {
            if (dateTransactions.getCardNo() == null || dateTransactions.getCardNo().isEmpty()) {
                CardNumberLL.setVisibility(View.GONE);
            } else {
                CardNumber.setText(dateTransactions.getCardNo());
                CardNumberLL.setVisibility(View.VISIBLE);
            }
        }

        if (dateTransactions != null) {
            if (dateTransactions.getRRN() == null) {
                ReferenceLL.setVisibility(View.GONE);
            } else {
                if (dateTransactions.getRRN() != null && !dateTransactions.getRRN().equals("0"))
                    ReferenceNumber.setText(dateTransactions.getRRN());
            }
        }


        if (dateTransactions != null && dateTransactions.getCurrency() != null) {
            Amnt.setText(String.format("%s %s", dateTransactions.getCurrency(), dateTransactions.getTotal()));
            priceAmount.setText(String.format("%s %s", dateTransactions.getCurrency(), dateTransactions.getAmnt()));
        }

        if (dateTransactions != null) {
            if (dateTransactions.getExternalTxnId() != null) {
                tahweeltransid.setText(dateTransactions.getExternalTxnId());
            } else {
                tahweeltransLL.setVisibility(View.GONE);
            }
        }

        if (dateTransactions != null) {
            if (dateTransactions.getSenderName() != null && !dateTransactions.getSenderName().isEmpty()) {
                Name.setText(dateTransactions.getSenderName());
            } else {
                NameLL.setVisibility(View.GONE);
            }
        }

        if (dateTransactions != null) {
            transid.setText(dateTransactions.getTransactionId());
        }
        TransStatus.setPaintFlags(TransStatus.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);

        if (dateTransactions.getFeeAmnt() == null) {
            FeeAmntLL.setVisibility(View.GONE);
        } else {
            FeeAmnt.setText(String.format("%s %s", dateTransactions.getCurrency(), dateTransactions.getFeeAmnt()));
            FeeAmntLL.setVisibility(View.VISIBLE);
        }

        if (dateTransactions.getTipAmnt() == null) {
            TipAmntLL.setVisibility(View.GONE);
        } else {
            TipAmnt.setText(String.format("%s %s", dateTransactions.getCurrency(), dateTransactions.getTipAmnt()));
            TipAmntLL.setVisibility(View.VISIBLE);
        }

        if (!SessionManager.getInstance().getEmpData().isIsPrimaryMerchant()
                || !dateTransactions.isRefundEnabled() || (!SessionManager.getInstance().getEmpData().isRefundEnabled() && dateTransactions.getRefundButton() != 5)) {
            RefundBtn.setVisibility(View.GONE);
        }

        if (dateTransactions.getRefundButton() == 0) {
            RefundBtn.setText(getString(R.string.upg_general_Refund));

        } else if (dateTransactions.getRefundButton() == 1) {
            RefundBtn.setText(getString(R.string.Void));

        } else if (dateTransactions.getRefundButton() == 5) {
            capture.setVisibility(View.VISIBLE);
            RefundBtn.setText(getString(R.string.Void));
        }


        return root;
    }


    @OnClick({R.id.RefundBtn, R.id.SENRECET, R.id.close, R.id.capture})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.RefundBtn:
                if (SystemClock.elapsedRealtime() - lastClickTime < 1000) {
                    return;
                }
                lastClickTime = SystemClock.elapsedRealtime();
                if (blocBtn) return;
                if (!dateTransactions.isRefundEnabled() || !SessionManager.getInstance().getEmpData().isRefundEnabled())
                    return;
                PasswordDialog passwordDialog = new PasswordDialog(getActivity(), dateTransactions);
                if (!passwordDialog.isShowing()) passwordDialog.show();
                break;
            case R.id.SENRECET:
                if (SystemClock.elapsedRealtime() - lastClickTime < 1000) {
                    return;
                }
                lastClickTime = SystemClock.elapsedRealtime();
                SendEmailDialog sendEmailDialog = new SendEmailDialog(getActivity(), dateTransactions);
                if (!sendEmailDialog.isShowing()) sendEmailDialog.show();
                break;
            case R.id.close:
                getActivity().onBackPressed();
                break;
            case R.id.capture:
                if (SystemClock.elapsedRealtime() - lastClickTime < 1000) {
                    return;
                }
                lastClickTime = SystemClock.elapsedRealtime();
                if (blocBtn)
                    return;
                ReFundCardDialog reFundCardDialog = new ReFundCardDialog(getActivity(), dateTransactions, false);
                if (!reFundCardDialog.isShowing()) reFundCardDialog.show();
                break;
//            case R.id.OriginalTransactionId:
//              //  (UpgMainActivity).reportService
//                break;
        }
    }

}
