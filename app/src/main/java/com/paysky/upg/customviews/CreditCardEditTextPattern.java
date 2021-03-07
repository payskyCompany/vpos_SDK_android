package com.paysky.upg.customviews;

import android.content.Context;

import com.alihafizji.library.CreditCardEditText;
import com.paysky.upg.R;

import java.util.ArrayList;
import java.util.List;

public class CreditCardEditTextPattern implements CreditCardEditText.CreditCartEditTextInterface {


    private Context mContext;

    public CreditCardEditTextPattern(Context context) {
        mContext = context;
    }

    @Override
    public List<CreditCardEditText.CreditCard> mapOfRegexStringAndImageResourceForCreditCardEditText(CreditCardEditText creditCardEditText) {


        List<CreditCardEditText.CreditCard> listOfPatterns = new ArrayList<CreditCardEditText.CreditCard>();

        CreditCardEditText.CreditCard Visa = new CreditCardEditText.CreditCard(
                CardPattern.VISA, mContext.getResources().getDrawable(R.drawable.visa),
                "Visa");

        listOfPatterns.add(Visa);

        CreditCardEditText.CreditCard Visa2 = new CreditCardEditText.CreditCard(
                CardPattern.VISA_VALID, mContext.getResources().getDrawable(R.drawable.visa),
                "valid");

        listOfPatterns.add(Visa2);
        CreditCardEditText.CreditCard VISA_SHOET = new CreditCardEditText.CreditCard(
                CardPattern.VISA_SHOET, mContext.getResources().getDrawable(R.drawable.visa),
                "Visa");

        listOfPatterns.add(VISA_SHOET);


        CreditCardEditText.CreditCard MasterCard = new CreditCardEditText.CreditCard(
                CardPattern.MASTERCARD_SHORTER, mContext.getResources().getDrawable(R.drawable.mastercard),
                "MasterCard");


        listOfPatterns.add(MasterCard);


        CreditCardEditText.CreditCard MasterCard4 = new CreditCardEditText.CreditCard(
                CardPattern.MASTERCARD_VALID, mContext.getResources().getDrawable(R.drawable.mastercard),
                "valid");


        listOfPatterns.add(MasterCard4);


        CreditCardEditText.CreditCard MasterCard6 = new CreditCardEditText.CreditCard(
                CardPattern.AMERICAN_EXPRESS_VALID, mContext.getResources().getDrawable(R.drawable.am),
                "valid");


        listOfPatterns.add(MasterCard6);


        CreditCardEditText.CreditCard MasterCard7 = new CreditCardEditText.CreditCard(
                CardPattern.DISCOVER_VALID, mContext.getResources().getDrawable(R.drawable.ds),
                "valid");


        listOfPatterns.add(MasterCard7);


        CreditCardEditText.CreditCard MasterCard8 = new CreditCardEditText.CreditCard(
                CardPattern.JCB_VALID, mContext.getResources().getDrawable(R.drawable.jcb),
                "valid");


        listOfPatterns.add(MasterCard8);


        CreditCardEditText.CreditCard MasterCard9 = new CreditCardEditText.CreditCard(
                CardPattern.DINERS_CLUB_VALID, mContext.getResources().getDrawable(R.drawable.dc),
                "valid");


        listOfPatterns.add(MasterCard9);


        CreditCardEditText.CreditCard MasterCard2 = new CreditCardEditText.CreditCard(
                CardPattern.MASTERCARD, mContext.getResources().getDrawable(R.drawable.mastercard),
                "MasterCard");

        listOfPatterns.add(MasterCard2);


        CreditCardEditText.CreditCard MasterCard3 = new CreditCardEditText.CreditCard(
                CardPattern.MASTERCARD_SHORT, mContext.getResources().getDrawable(R.drawable.mastercard),
                "MasterCard");


        listOfPatterns.add(MasterCard3);


        CreditCardEditText.CreditCard AMERICAN_EXPRESS = new CreditCardEditText.CreditCard(
                CardPattern.AMERICAN_EXPRESS, mContext.getResources().getDrawable(R.drawable.amex),
                "AMERICAN_EXPRESS");


        listOfPatterns.add(AMERICAN_EXPRESS);


        CreditCardEditText.CreditCard DISCOVER_SHORT = new CreditCardEditText.CreditCard(
                CardPattern.DISCOVER_SHORT, mContext.getResources().getDrawable(R.drawable.ds),
                "DISCOVER_SHORT");


        listOfPatterns.add(DISCOVER_SHORT);


        CreditCardEditText.CreditCard DISCOVER = new CreditCardEditText.CreditCard(
                CardPattern.DISCOVER, mContext.getResources().getDrawable(R.drawable.ds),
                "DISCOVER");


        listOfPatterns.add(DISCOVER);


        CreditCardEditText.CreditCard jcb = new CreditCardEditText.CreditCard(
                CardPattern.JCB_SHORT, mContext.getResources().getDrawable(R.drawable.jcb),
                "jcb");


        listOfPatterns.add(jcb);


        CreditCardEditText.CreditCard JCB = new CreditCardEditText.CreditCard(
                CardPattern.JCB, mContext.getResources().getDrawable(R.drawable.jcb),
                "jcb");


        listOfPatterns.add(JCB);


        CreditCardEditText.CreditCard Diners_Club = new CreditCardEditText.CreditCard(
                CardPattern.DINERS_CLUB, mContext.getResources().getDrawable(R.drawable.dc),
                "Diners_Club");


        listOfPatterns.add(Diners_Club);


        CreditCardEditText.CreditCard DINERS_CLUB_SHORT = new CreditCardEditText.CreditCard(
                CardPattern.DINERS_CLUB_SHORT, mContext.getResources().getDrawable(R.drawable.dc),
                "DINERS_CLUB_SHORT");


        listOfPatterns.add(DINERS_CLUB_SHORT);


        CreditCardEditText.CreditCard MEZA_VALID = new CreditCardEditText.CreditCard(
                CardPattern.MEZA_VALID, mContext.getResources().getDrawable(R.drawable.ic_meeza),
                "valid");

        listOfPatterns.add(MEZA_VALID);


        CreditCardEditText.CreditCard MASTER_MEZA_VALID = new CreditCardEditText.CreditCard(
                CardPattern.MASTER_MEZA_VALID, mContext.getResources().getDrawable(R.drawable.ic_meeza),
                "valid");


        listOfPatterns.add(MASTER_MEZA_VALID);


        CreditCardEditText.CreditCard MASTER_MEZA_VALID_SHORT = new CreditCardEditText.CreditCard(
                CardPattern.SHORT_MEZA_VALID, mContext.getResources().getDrawable(R.drawable.ic_meeza),
                "MASTER_MEZA_VALID");


        listOfPatterns.add(MASTER_MEZA_VALID_SHORT);


        CreditCardEditText.CreditCard MASTER_MEZA_VALID_SHORT2 = new CreditCardEditText.CreditCard(
                CardPattern.SHORT_MASTER_MEZA_VALID, mContext.getResources().getDrawable(R.drawable.ic_meeza),
                "MASTER_MEZA_VALID");


        listOfPatterns.add(MASTER_MEZA_VALID_SHORT2);


        return listOfPatterns;

    }
}
