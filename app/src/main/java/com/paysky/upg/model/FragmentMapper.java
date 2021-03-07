package com.paysky.upg.model;

import androidx.fragment.app.Fragment;

import com.paysky.upg.fragment.ManualPaymentFragment;
import com.paysky.upg.fragment.PayLinkFragment;
import com.paysky.upg.fragment.PayLinkListFragment;
import com.paysky.upg.fragment.TransactionsFragment;

/**
 * Created by amrel on 27/03/2018.
 */


public class FragmentMapper {
    public static Fragment getFragmentByName(String FragmentName) {
        Fragment fragment;

        if (FragmentName == null) {
            return null;
        } else if (FragmentName.equals("getUPGService1")) {
            return TransactionsFragment.newInstance();
        } else if (FragmentName.equals("getUPGService2")) {
            return TransactionsFragment.newInstance();
        } else if (FragmentName.equals("getUPGService3")) {
            return TransactionsFragment.newInstance();
        } else if (FragmentName.equals("getUPGService4")) {
            return TransactionsFragment.newInstance();
        } else if (FragmentName.equals(ManualPaymentFragment.class.getSimpleName())) {


//            if ( Build.DISPLAY.contains("WPOS")){
//                return AmountFragment.newInstance("ENVAmount");
//
//            }else{
            return ManualPaymentFragment.newInstance("ManualPayment");

            //       }
            //   return CardPosFragment.newInstance();
        } else if (FragmentName.equals(PayLinkFragment.class.getSimpleName())) {
            return PayLinkFragment.newInstance("PayLinkFragment");
        } else if (FragmentName.equals(PayLinkListFragment.class.getSimpleName())) {
            return PayLinkListFragment.newInstance("PayLinkListFragment");
        } else if (FragmentName.equals("accountService")) {
            return TransactionsFragment.newInstance();
        }

        return null;
    }
}
