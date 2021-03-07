package com.paysky.upg.customviews;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.paysky.upg.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

/**
 * Created by Abdulrahman Rudwan on 20/02/2018.
 */

public class CustomKeyBoardToWithDraw extends LinearLayout {
    public View rootView;
    Unbinder unbinder;

    @BindView(R.id.text_1)
    public TextView txt_1;

    @BindView(R.id.text_2)
    public TextView txt_2;

    @BindView(R.id.text_3)
    public TextView txt_3;

    @BindView(R.id.text_4)
    public TextView txt_4;

    @BindView(R.id.text_5)
    public TextView txt_5;

    @BindView(R.id.text_6)
    public TextView txt_6;

    @BindView(R.id.text_7)
    public TextView txt_7;

    @BindView(R.id.text_8)
    public TextView txt_8;


    @BindView(R.id.text_9)
    public TextView txt_9;


    @BindView(R.id.text_0)
    public TextView txt_0;


    public String val;
    public String value = "";
    public int lenght = 3;

    ItemClickListener itemClickListener;

    public CustomKeyBoardToWithDraw(Context context) {
        super(context);
        init();
    }

    public void SetitemClickListener(ItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
    }

    public CustomKeyBoardToWithDraw(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public CustomKeyBoardToWithDraw(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    void init() {
        LayoutInflater inflater = LayoutInflater.from(getContext());
        rootView = inflater.inflate(R.layout.custom_keyboard_withdrawl, this);
        unbinder = ButterKnife.bind(this, rootView);

    }


    @OnClick({R.id.text_1, R.id.text_2, R.id.text_3,
            R.id.text_4, R.id.text_5, R.id.text_6, R.id.text_7, R.id.text_8, R.id.text_9, R.id.text_0})
    void clicked(View view) {
        if (value.length() >= lenght) {
            return;
        }

        switch (view.getId()) {

            case R.id.text_1:
                val = "1";
                value = value + "" + val;
                TextInput(value);


                break;
            case R.id.text_2:
                val = "2";
                value = value + "" + val;
                TextInput(value);


                break;
            case R.id.text_3:
                val = "3";
                value = value + "" + val;

                TextInput(value);

                break;
            case R.id.text_4:
                val = "4";
                value = value + "" + val;

                TextInput(value);

                break;
            case R.id.text_5:
                val = "5";
                value = value + "" + val;

                TextInput(value);

                break;
            case R.id.text_6:
                val = "6";
                value = value + "" + val;

                TextInput(value);

                break;
            case R.id.text_7:
                val = "7";
                value = value + "" + val;

                TextInput(value);

                break;
            case R.id.text_8:
                val = "8";
                value = value + "" + val;

                TextInput(value);

                break;
            case R.id.text_9:
                val = "9";
                value = value + "" + val;

                TextInput(value);

                break;
            case R.id.text_0:
                val = "0";
                value = value + "" + val;

                TextInput(value);

                break;


        }
    }


    @OnClick(R.id.next)
    public void Next(View view) {
        itemClickListener.setNext();

    }

//
//    @OnClick(R.id.back)
//    public void Back(View view) {
//        itemClickListener.setBack();
//
//    }

    public void TextInput(String view) {
        try {
            itemClickListener.setPriceInEditText(view);

        } catch (Exception e) {

        }
    }

    @OnClick(R.id.RemoveLast)
    public void RemoveLast(View view) {
        String str = value;
        if (str != null && str.length() > 0) {
            str = str.substring(0, str.length() - 1);
        }
        value = str;
        TextInput(str);

    }

    public interface ItemClickListener {
        void setPriceInEditText(String s);

        void setNext();

        void setBack();


    }
}
