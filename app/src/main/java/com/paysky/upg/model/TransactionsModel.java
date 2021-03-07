package com.paysky.upg.model;

import java.util.ArrayList;
import java.util.List;

import io.paysky.upg.data.network.model.response.TransactionDetailsEntities;

/**
 * Created by Amr Abd Elrhim on 18/07/2017.
 */

public class TransactionsModel {
    List<TransactionDetailsEntities> transactionDetailsEntities;


    String date;

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public List<TransactionDetailsEntities> getTransactionDetailsEntities() {
        return transactionDetailsEntities;
    }

    public void setTransactionDetailsEntities(List<TransactionDetailsEntities> transactionDetailsEntities) {
        this.transactionDetailsEntities = transactionDetailsEntities;
    }

    public static List<TransactionsModel> getListTransactions() {
        List<TransactionsModel> transactionDetailsEntities = new ArrayList<>();

        List<TransactionDetailsEntities> models = new ArrayList<>();

        TransactionsModel transactionsModel = new TransactionsModel();
        transactionsModel.setDate("17 March");
        TransactionDetailsEntities transactionDetailsEntities1 = new TransactionDetailsEntities();


        models.add(transactionDetailsEntities1);
        models.add(transactionDetailsEntities1);
        transactionsModel.setTransactionDetailsEntities(models);
        transactionDetailsEntities.add(transactionsModel);
        transactionDetailsEntities.add(transactionsModel);


        return transactionDetailsEntities;
    }
}
