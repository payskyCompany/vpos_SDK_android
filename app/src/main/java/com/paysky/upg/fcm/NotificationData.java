package com.paysky.upg.fcm;


import java.io.Serializable;

import io.paysky.upg.data.network.model.response.TransactionDetailsEntities;

public class NotificationData implements Serializable {

    public static final String TEXT = "TEXT";
    private String title;
    private String TransactionTypeName;
    private String referenceNumber;


    public String getReferenceNumber() {
        return referenceNumber;
    }

    public void setReferenceNumber(String referenceNumber) {
        this.referenceNumber = referenceNumber;
    }

    public String getTransactionTypeName() {
        return TransactionTypeName;
    }

    public void setTransactionTypeName(String transactionTypeName) {
        this.TransactionTypeName = transactionTypeName;
    }

    private String textMessage;
    private TransactionDetailsEntities notiEntity;

    long id = 0;

    public static String getTEXT() {
        return TEXT;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getTextMessage() {
        return textMessage;
    }

    public void setTextMessage(String textMessage) {
        this.textMessage = textMessage;
    }

    public TransactionDetailsEntities getNotiEntity() {
        return notiEntity;
    }

    public void setNotiEntity(TransactionDetailsEntities notiEntity) {
        this.notiEntity = notiEntity;
    }
}
