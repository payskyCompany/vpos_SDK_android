package com.paysky.upg.model;

import java.io.Serializable;
import java.util.List;

/**
 * Created by amrel on 20/03/2018.
 */

public class TransactionModelBundle implements Serializable {
    List<TransactionModel> transactionModels;

    public TransactionModelBundle(List<TransactionModel> transactionModels) {
        this.transactionModels = transactionModels;
    }

    public List<TransactionModel> getTransactionModels() {
        return transactionModels;
    }

    public void setTransactionModels(List<TransactionModel> transactionModels) {
        this.transactionModels = transactionModels;
    }
}
