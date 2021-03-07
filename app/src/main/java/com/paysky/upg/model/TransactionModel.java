package com.paysky.upg.model;

import com.paysky.upg.R;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by amrel on 12/03/2018.
 */

public class TransactionModel implements Serializable {

    int drawable;
    String TransactionName;
    String activityName;

    public int getDrawable() {
        return drawable;
    }

    public void setDrawable(int drawable) {
        this.drawable = drawable;
    }

    public String getTransactionName() {
        return TransactionName;
    }

    public void setTransactionName(String transactionName) {
        TransactionName = transactionName;
    }

    public String getActivityName() {
        return activityName;
    }

    public void setActivityName(String activityName) {
        this.activityName = activityName;
    }

    public TransactionModel(int drawable, String transactionName, String activityName, boolean backstack) {
        this.drawable = drawable;
        TransactionName = transactionName;
        this.activityName = activityName;
        this.backstack = backstack;
    }


    public TransactionModel(int drawable, String transactionName, String activityName) {
        this.drawable = drawable;
        TransactionName = transactionName;
        this.activityName = activityName;
    }

    boolean backstack = true;

    public boolean isBackstack() {
        return backstack;
    }

    public void setBackstack(boolean backstack) {
        this.backstack = backstack;
    }

    public static TransactionModelBundle getTransacton() {
        return getMerchantTransacton();
    }
//
//    public static TransactionModelBundle getService() {
//        return getBankService();
//    }
//
//
//
//    public static TransactionModelBundle getBankService() {
//        List<TransactionModel> transactionModels = new ArrayList<>();
//        transactionModels.add(new TransactionModel(R.drawable.transfer, "Transfer", "AmountFragment"));
//        transactionModels.add(new TransactionModel(R.drawable.balance, "Balance Inquiry", "AmountFragment"));
//        transactionModels.add(new TransactionModel(R.drawable.ministatement, "Mini-Statement ", "AmountFragment"));
//        transactionModels.add(new TransactionModel(R.drawable.requestchequebook, "Request cheque book", "AmountFragment"));
//        transactionModels.add(new TransactionModel(R.drawable.verifysign, "Verify signature", "AmountFragment"));
//        transactionModels.add(new TransactionModel(R.drawable.exchangerates, "Exchange rates", "AmountFragment"));
//        transactionModels.add(new TransactionModel(R.drawable.support, " Support", "AmountFragment"));
//
//
//        return new TransactionModelBundle(transactionModels);
//    }
//
//
//    public static List<TransactionModel> getBankPayType() {
//        List<TransactionModel> transactionModels = new ArrayList<>();
//        transactionModels.add(new TransactionModel(R.drawable.card, "Card", "AmountFragment"));
//        transactionModels.add(new TransactionModel(R.drawable.wallet, "Wallet", "AmountFragment"));
//        transactionModels.add(new TransactionModel(R.drawable.account, "Account", "AmountFragment"));
//
//
//        return transactionModels;
//    }
//
//    public static List<TransactionModel> getReportList() {
//        List<TransactionModel> transactionModels = new ArrayList<>();
//        transactionModels.add(new TransactionModel(0, "ALL", "AmountFragment"));
//        transactionModels.add(new TransactionModel(R.drawable.card, "Card", "AmountFragment"));
//        transactionModels.add(new TransactionModel(R.drawable.wallet, "Wallet", "AmountFragment"));
//        transactionModels.add(new TransactionModel(R.drawable.cash, "Cash", "AmountFragment"));
//
//
//        return transactionModels;
//    }


    public static TransactionModelBundle getMerchantTransacton() {
        List<TransactionModel> transactionModels = new ArrayList<>();
        transactionModels.add(new TransactionModel(R.drawable.sale, "Sale", "AmountFragment"));
        transactionModels.add(new TransactionModel(R.drawable.void_transaction, "Void", "AmountFragment"));
        transactionModels.add(new TransactionModel(R.drawable.refund, "Refund", "AmountFragment"));
        transactionModels.add(new TransactionModel(R.drawable.installments, "Installments", "AmountFragment"));
        transactionModels.add(new TransactionModel(R.drawable.settlement, "Settlement", "AmountFragment"));


        return new TransactionModelBundle(transactionModels);
    }


    public static List<TransactionModel> getMerchantPayType() {
        List<TransactionModel> transactionModels = new ArrayList<>();
        transactionModels.add(new TransactionModel(R.drawable.card, "Card", "AmountFragment"));
        transactionModels.add(new TransactionModel(R.drawable.wallet, "Wallet", "AmountFragment"));
        transactionModels.add(new TransactionModel(R.drawable.qrcode, "QR Code", "AmountFragment"));
        transactionModels.add(new TransactionModel(R.drawable.cash, "Cash", "AmountFragment"));


        return transactionModels;
    }


    public static TransactionModelBundle getSettingList() {
        List<TransactionModel> transactionModels = new ArrayList<>();
        transactionModels.add(new TransactionModel(R.drawable.networksetting, "Network settings", "AmountFragment"));
        transactionModels.add(new TransactionModel(R.drawable.lanuage, "Language", "AmountFragment"));
        transactionModels.add(new TransactionModel(R.drawable.defaultsettings, "Default settings", "AmountFragment"));


        return new TransactionModelBundle(transactionModels);
    }


    public static TransactionModelBundle getMerchantService() {
        List<TransactionModel> transactionModels = new ArrayList<>();
        transactionModels.add(new TransactionModel(R.drawable.transfer, "Store Cloud", "AmountFragment"));
        transactionModels.add(new TransactionModel(R.drawable.balance, "Invoice Cloud", "AmountFragment"));
        transactionModels.add(new TransactionModel(R.drawable.ministatement, "Fawateery ", "AmountFragment"));
        transactionModels.add(new TransactionModel(R.drawable.requestchequebook, "Support", "AmountFragment"));


        return new TransactionModelBundle(transactionModels);
    }


    public static TransactionModelBundle getUPGTransaction() {
        List<TransactionModel> transactionModels = new ArrayList<>();
        transactionModels.add(new TransactionModel(R.drawable.static_qr, "Show my PayCode", "StaticQrFragment"));
        transactionModels.add(new TransactionModel(R.drawable.salebyqr, "Sale by QR", "SaleByQRFragment"));
        transactionModels.add(new TransactionModel(R.drawable.salebycash, "Sale by cash ", "SaleByCash"));
        transactionModels.add(new TransactionModel(R.drawable.tahweelcashin, "Tahweel  - Cash In  ", "Ta7weelCashIn"));
        transactionModels.add(new TransactionModel(R.drawable.tahweelcashout, "Tahweel  - Cash Out  ", "Ta7weelCashOut"));


        return new TransactionModelBundle(transactionModels);
    }

    public static TransactionModelBundle getUPGService() {
        List<TransactionModel> transactionModels = new ArrayList<>();
//        transactionModels.add(new TransactionModel(R.drawable.hassal_service, "Hassal services", "getUPGService1"));
//        transactionModels.add(new TransactionModel(R.drawable.support, "Support", "SupportRequestFragment"));
        return new TransactionModelBundle(transactionModels);
    }

    public static TransactionModelBundle getUPGService1() {
        List<TransactionModel> transactionModels = new ArrayList<>();
//        transactionModels.add(new TransactionModel(R.drawable.telecom, "Telecom", "getUPGService2"));
//        transactionModels.add(new TransactionModel(R.drawable.utilities, "Utilities", "getUPGService2"));
//        transactionModels.add(new TransactionModel(R.drawable.government, "Government", "getUPGService2"));
//        transactionModels.add(new TransactionModel(R.drawable.education, "Education", "getUPGService2"));
//        transactionModels.add(new TransactionModel(R.drawable.charity, "Charity", "getUPGService2"));
        return new TransactionModelBundle(transactionModels);
    }


    public static TransactionModelBundle getUPGService2() {
        List<TransactionModel> transactionModels = new ArrayList<>();
//        transactionModels.add(new TransactionModel(R.drawable.vodafone, "Vodafone", "getUPGService3"));
//        transactionModels.add(new TransactionModel(R.drawable.orange, "Orange", "getUPGService3"));
//        transactionModels.add(new TransactionModel(R.drawable.etisalat, "Etisalat", "getUPGService3"));
//        transactionModels.add(new TransactionModel(R.drawable.we, "We", "getUPGService3"));
        return new TransactionModelBundle(transactionModels);
    }


    public static TransactionModelBundle getUPGService3() {
        List<TransactionModel> transactionModels = new ArrayList<>();
//        transactionModels.add(new TransactionModel(R.drawable.top_up, "Top up", ""));
//        transactionModels.add(new TransactionModel(R.drawable.monthly_bill, "Monthly bill   ", ""));
        return new TransactionModelBundle(transactionModels);
    }


    public static TransactionModelBundle getUPGService4() {
        List<TransactionModel> transactionModels = new ArrayList<>();
//        transactionModels.add(new TransactionModel(R.drawable.card, "EGP 10 Card", ""));
//        transactionModels.add(new TransactionModel(R.drawable.card, "EGP 25 Card", "getUPGService4"));
//        transactionModels.add(new TransactionModel(R.drawable.card, "EGP 50 Card", ""));
//        transactionModels.add(new TransactionModel(R.drawable.card, "EGP 100 Card", ""));
//        transactionModels.add(new TransactionModel(R.drawable.card, "EGP 200 Card", ""));

        return new TransactionModelBundle(transactionModels);
    }


}
