package com.paysky.upg.model;

/**
 * Created by amrel on 13/12/2017.
 */

public class ComposeISOQRRequest {


    public boolean isStatic() {
        return IsStatic;
    }

    public void setStatic(boolean aStatic) {
        IsStatic = aStatic;
    }

    /**
     * Amount : 50.23
     * Message : i need money
     * MobileNumber : 01285898402
     * ReceiverCountryId : 1
     * To : 01028313753
     */

    private boolean IsStatic = false;
    private String Amount;
    private String Message;
    private String MobileNumber;
    private int ReceiverCountryId;
    private String To;

    public String getAmount() {
        return Amount;
    }

    public void setAmount(String Amount) {
        this.Amount = Amount;
    }

    public String getMessage() {
        return Message;
    }

    public void setMessage(String Message) {
        this.Message = Message;
    }

    public String getMobileNumber() {
        return MobileNumber;
    }

    public void setMobileNumber(String MobileNumber) {
        this.MobileNumber = MobileNumber;
    }

    public int getReceiverCountryId() {
        return ReceiverCountryId;
    }

    public void setReceiverCountryId(int ReceiverCountryId) {
        this.ReceiverCountryId = ReceiverCountryId;
    }

    public String getTo() {
        return To;
    }

    public void setTo(String To) {
        this.To = To;
    }
}
