package com.paysky.upg.fragment;


import android.Manifest;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.PorterDuff;
import android.net.Uri;
import android.os.Bundle;
import android.text.InputFilter;
import android.text.InputType;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TableRow;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.gun0912.tedpermission.PermissionListener;
import com.gun0912.tedpermission.TedPermission;
import com.paysky.upg.R;
import com.paysky.upg.dialog.PayLinkDialog;
import com.paysky.upg.model.FragmentMapper;
import com.paysky.upg.utils.DateTimeDailogUtil;
import com.paysky.upg.utils.FileUtils;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import faranjit.currency.edittext.CurrencyEditText;
import io.paysky.upg.data.network.model.LoginUpgMerchant;
import io.paysky.upg.data.network.model.request.InitiateOrderRequest;
import io.paysky.upg.data.network.model.response.InitiateOrderResponse;
import io.paysky.upg.mvp.paylinkfragment.PayLinkFragmentPresenter;
import io.paysky.upg.mvp.paylinkfragment.PayLinkFragmentView;
import io.paysky.upg.util.SessionManager;

import static io.paysky.upg.util.DateTimeUtil.getDateTimeExpirePayLinkPlusOneDay;


/**
 * A simple {@link Fragment} subclass.
 */
public class PayLinkFragment extends BaseFragment implements DatePickerDialog.OnDateSetListener
        , PayLinkFragmentView {

    //GUI.
    @BindView(R.id.TerminalSpinner)
    Spinner TerminalSpinner;
    @BindView(R.id.refnumber)
    EditText refnumber;
    @BindView(R.id.PayerName)
    EditText PayerName;
    @BindView(R.id.NumberPayment)
    EditText NumberPayment;


    @BindView(R.id.CurrncyType)
    Spinner CurrncyType;
    @BindView(R.id.amount_editText)
    CurrencyEditText amountEditText;
    @BindView(R.id.dataExp)
    Button dataExp;
    @BindView(R.id.NotiType)
    Spinner NotiType;
    @BindView(R.id.typeLL)
    LinearLayout typeLL;

    @BindView(R.id.tv_currency)
    TextView currencyTextView;


    @BindView(R.id.TypeSpinner)
    Spinner TypeSpinner;


    @BindView(R.id.notification)
    EditText notification;
    @BindView(R.id.genratePayLink)
    LinearLayout genratePayLink;
    Unbinder unbinder;
    @BindView(R.id.TerminalLL)
    TableRow TerminalLL;
    @BindView(R.id.ReferenceNumberLL)
    TableRow ReferenceNumberLL;
    @BindView(R.id.AmoutLL)
    TableRow AmoutLL;
    @BindView(R.id.dataExpLL)
    TableRow dataExpLL;
    @BindView(R.id.LLnoti)
    TableRow LLnoti;
    @BindView(R.id.generate_history_link)
    LinearLayout generateHistoryLink;

    @Nullable
    @BindView(R.id.messageEditText)
    EditText messageEditText;

    //Objects.
    private LoginUpgMerchant empData;
    private PayLinkFragmentPresenter presenter = new PayLinkFragmentPresenter();
    public String selectedTerminal;
    public String selectedCurrancy;
    public int selectedTypePayLink;
    public String NotificationMethod;
    String linkExpireDate;

    private InputFilter filter = (source, start, end, dest, dstart, dend) -> {
        String blockCharacterSet = "~#^|$%&*!.";
        if (source != null && blockCharacterSet.contains(("" + source))) {
            return "";
        }
        return null;
    };


    public PayLinkFragment() {
        // Required empty public constructor
    }

    public static PayLinkFragment newInstance(String fromWhere) {
        PayLinkFragment fragment = new PayLinkFragment();
        Bundle args = new Bundle();
        args.putSerializable(TransactionsLoadingFragment.FROM_WHERE, fromWhere);
        fragment.setArguments(args);
        return fragment;
    }


    public static PayLinkFragment newInstance() {
        return new PayLinkFragment();
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (SessionManager.getInstance() != null && SessionManager.getInstance().getEmpData() != null)
            empData = SessionManager.getInstance().getEmpData();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_pay_link, container, false);
        unbinder = ButterKnife.bind(this, view);
        initData();

        setDefaultLinkExpireDateADCB();

        generateHistoryLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addNewFragment(FragmentMapper.getFragmentByName(PayLinkListFragment.class.getSimpleName()), "PayLinkListFragment");
            }
        });

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        presenter.attachView(this);
    }

    @Override
    public void onDestroyView() {
        presenter.detachView();
        super.onDestroyView();
        unbinder.unbind();
    }

    public void initData() {
        ArrayList<String> strings = new ArrayList<>();
        if (empData != null) {
            strings.add(empData.CurrencyName);
            currencyTextView.setText(empData.CurrencyName);
        }
        ArrayAdapter<String> spinnerArrayAdapter3 = new ArrayAdapter<String>(getContext(), R.layout.spinner_item_paylink, strings);
        spinnerArrayAdapter3.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        CurrncyType.setAdapter(spinnerArrayAdapter3);

        CurrncyType.getBackground().setColorFilter(getResources().getColor(R.color.colorPrimary), PorterDuff.Mode.SRC_ATOP);

        CurrncyType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                if (selectedItemView != null)

                    ((TextView) selectedItemView).setTextColor(getResources().getColor(R.color.colorPrimary));

                if (empData != null) selectedCurrancy = empData.CurrencyCode;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                ((TextView) parentView.getItemAtPosition(0)).setTextColor(getResources().getColor(R.color.colorPrimary));
                if (empData != null)
                    selectedTerminal = empData.getterminalTypesModelString().get(0);
            }

        });


        ArrayAdapter<String> spinnerArrayAdapter4 = new ArrayAdapter<String>(getContext(), R.layout.spinner_item_paylink,
                getResources().getStringArray(R.array.pay_link_type));
        spinnerArrayAdapter4.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        TypeSpinner.setAdapter(spinnerArrayAdapter4);

        TypeSpinner.getBackground().setColorFilter(getResources().getColor(R.color.colorPrimary), PorterDuff.Mode.SRC_ATOP);

        TypeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                if (selectedItemView != null) {
                    ((TextView) selectedItemView).setTextColor(getResources().getColor(R.color.colorPrimary));

                }
                selectedTypePayLink = position;
                if (position == 1) {
                    typeLL.setVisibility(View.VISIBLE);
                } else {
                    typeLL.setVisibility(View.GONE);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                selectedTypePayLink = 0;
            }
        });

        ArrayAdapter<String> spinnerArrayAdapter2 = new ArrayAdapter<String>(getContext(), R.layout.spinner_item_paylink,
                getResources().getStringArray(R.array.notitype)); //selected item will look like a spinner set from XML
        spinnerArrayAdapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        NotiType.setAdapter(spinnerArrayAdapter2);

        NotiType.getBackground().setColorFilter(getResources().getColor(R.color.colorPrimary), PorterDuff.Mode.SRC_ATOP);

        NotiType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                if (selectedItemView != null) {

                    ((TextView) selectedItemView).setTextColor(getResources().getColor(R.color.colorPrimary));

                }
                if (position == 0) {
                    notification.setHint(getResources().getString(R.string.upg_new_merchant_label_email));
                    notification.setInputType(1);
                    notification.setInputType(InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS | InputType.TYPE_TEXT_VARIATION_EMAIL_SUBJECT);
                    NotificationMethod = "0";

                } else {
                    notification.setHint(getResources().getString(R.string.upg_new_merchant_phone_hint));
                    notification.setInputType(InputType.TYPE_CLASS_PHONE);
                    NotificationMethod = "1";
                    if (SessionManager.getInstance().getLang().equals("ar")) {
                        notification.setTextAlignment(View.TEXT_ALIGNMENT_VIEW_START);
                    }
                }
                notification.setText("");
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                if (empData != null)
                    selectedTerminal = empData.getterminalTypesModelString().get(0);
            }

        });

        if (empData != null) {
            ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(getContext(), R.layout.spinner_item_paylink,
                    empData.getterminalTypesModelWithTypeString()); //selected item will look like a spinner set from XML
            spinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            TerminalSpinner.setAdapter(spinnerArrayAdapter);
        }


        TerminalSpinner.getBackground().setColorFilter(getResources().getColor(R.color.colorPrimary), PorterDuff.Mode.SRC_ATOP);

        if (empData.getterminalTypesModelString().size() > 1) {
            TerminalSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                    if (selectedItemView != null) {

                        ((TextView) selectedItemView).setTextColor(getResources().getColor(R.color.colorPrimary));

                    }
                    if (empData != null)
                        selectedTerminal = empData.getterminalTypesModelString().get(position);
                }

                @Override
                public void onNothingSelected(AdapterView<?> parentView) {
                    if (empData != null)
                        selectedTerminal = empData.getterminalTypesModelString().get(0);
                }
            });


        } else {
            if (empData != null) selectedTerminal = empData.getterminalTypesModelString().get(0);
            TerminalLL.setVisibility(View.GONE);
        }
        amountEditText.setFilters(new InputFilter[]{filter});
    }

    @OnClick({R.id.genratePayLink, R.id.btnG})
    public void onViewClicked() {
        boolean Haserro = false;

        try {
            if (amountEditText.getText().toString().isEmpty() || amountEditText.getText().toString().equals("0.00")) {
                amountEditText.setError(getResources().getString(R.string.error_field_required));
                Haserro = true;
            }
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }

//            if (notification.getText().toString().isEmpty()) {
//                notification.setError(getResources().getString(R.string.error_field_required));
//                Haserro = true;
//            } else {
//
//                if (NotificationMethod.equals("0")) {
//                    if (!ValidateUtil.validMail(notification.getText().toString())) {
//                        notification.setError(getResources().getString(R.string.invalid_mail));
//                        Haserro = true;
//                    }
//                }
//
//                if (NotificationMethod.equals("1")) {
//                    if (notification.getText().toString().isEmpty()) {
//                        notification.setError(getResources().getString(R.string.invalid_phone));
//                        Haserro = true;
//                    }
//                }
//            }

        if (Haserro) {
            return;
        }

        String numberOfpaymentValue = NumberPayment.getText().toString();

        if (selectedTypePayLink == 0) {
            numberOfpaymentValue = "1";
        } else {
            if (NumberPayment.getText().toString().isEmpty() || Integer.parseInt(NumberPayment.getText().toString()) == 0) {
                NumberPayment.setError(getResources().getString(R.string.error_field_required));
                return;
            }
        }

        try {
            presenter.initiateOrder(selectedTerminal, empData.CurrencyCode, empData.CurrencyName,
                    linkExpireDate, PayerName.getText().toString(),
                    NotificationMethod, notification.getText().toString(),
                    refnumber.getText().toString(), "",
                    ((int) (amountEditText.getCurrencyDouble() * 100)), numberOfpaymentValue, imageBase64Value, messageEditText.getText().toString());
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    @OnClick(R.id.dataExp)
    public void SelectDate() {

        DateTimeDailogUtil.createExpirationDateDialog(linkExpireDate, getContext(), this).show();

    }

    private void setDefaultLinkExpireDate() {

        final Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.MONTH, 1);
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        month = month + 1;
        linkExpireDate = String.format("%d/%d/%d", day, month, year);
        dataExp.setText(linkExpireDate);
        dataExp.setTextColor(getResources().getColor(R.color.fieldscolor));
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int minute = calendar.get(Calendar.MINUTE);

        linkExpireDate = "" + new StringBuilder().append(year).append("")
                .append((month < 10 ? "0" + month : month)).append("").append((day < 10 ? "0" + day : day))
                .append((hour < 10 ? "0" + hour : hour)).append((minute < 10 ? "0" + minute : minute));
    }


    private void setDefaultLinkExpireDateADCB() {
        linkExpireDate = getDateTimeExpirePayLinkPlusOneDay();
//        dataExp.setText(getDateTimeExpirePayLinkPlusOneDay());
//        dataExp.setTextColor(getResources().getColor(R.color.fieldscolor));
    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        month = month + 1;
        linkExpireDate = dayOfMonth + "/" + month + "/" + year;
        dataExp.setText(linkExpireDate);
        dataExp.setTextColor(getResources().getColor(R.color.fieldscolor));

        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.MONTH, month);
        calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
        calendar.set(Calendar.YEAR, year);

        int arg1 = calendar.get(Calendar.YEAR);
        int arg3 = calendar.get(Calendar.DAY_OF_MONTH);
        int arg4 = calendar.get(Calendar.HOUR_OF_DAY);
        int arg5 = calendar.get(Calendar.MINUTE);


        linkExpireDate = "" + new StringBuilder().append(arg1).append("")
                .append((month < 10 ? "0" + month : month)).append("").append((arg3 < 10 ? "0" + arg3 : arg3))
                .append((arg4 < 10 ? "0" + arg4 : arg4)).append((arg5 < 10 ? "0" + arg5 : arg5));

        dataExp.setTextColor(getResources().getColor(R.color.fieldscolor));

    }


    @Override
    public void showPayLinkDialog(InitiateOrderRequest InitiateOrder, InitiateOrderResponse body) {
        PayLinkDialog payLinkDialog = new PayLinkDialog(getActivity());
        if (body != null) payLinkDialog.SetTitle(body.getOrderurl());
        payLinkDialog.SetData(InitiateOrder, body);
        if (!payLinkDialog.isShowing()) payLinkDialog.show();

        notification.setText("");
        amountEditText.setText("");
        PayerName.setText("");
        refnumber.setText("");
        NumberPayment.setText("");
        TypeSpinner.setSelection(0);
        if (messageEditText != null) {
            messageEditText.setText("");
        }
    }


    @OnClick({R.id.succesimage})
    public void deleteImage
            (final View view) {
        succesimage.setVisibility(View.GONE);
        tvSelectedimage.setText(getString(R.string.upg_general_choose));
        imageBase64Value = "";
    }


    @OnClick({R.id.select_image_LL})
    public void onClickImageSelected
            (final View view) {
        TedPermission.with(getContext()).setPermissions(Manifest.permission.READ_EXTERNAL_STORAGE)
                .setPermissionListener(new PermissionListener() {
                    @Override
                    public void onPermissionGranted() {
                        Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
                        intent.addCategory(Intent.CATEGORY_OPENABLE);
                        intent.setType("image/*");
                        int requestCode = 50000;
                        getActivity().startActivityForResult(intent, requestCode);
                    }

                    @Override
                    public void onPermissionDenied(List<String> deniedPermissions) {

                    }
                }).check();
    }

    @BindView(R.id.tv_selectedimage)
    TextView tvSelectedimage;
    @BindView(R.id.succesimage)
    ImageView succesimage;
    @BindView(R.id.select_image_LL)
    LinearLayout selectImageLL;

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent imageReturnedIntent) {
        super.onActivityResult(requestCode, resultCode, imageReturnedIntent);
        switch (requestCode) {

            case 50000:

                if (imageReturnedIntent == null) {
                    return;
                }
                Uri uri = imageReturnedIntent.getData();
                succesimage.setVisibility(View.VISIBLE);

                uploadFile(uri, requestCode);
                break;
        }

    }

    String imageBase64Value = "";

    private void uploadFile(Uri filepath, int index) {
        File file = null;
        String url = "";
        url = FileUtils.getPath(getActivity(), filepath);
        file = new File(url);
        imageBase64Value = encodeImage(file);
        String taxFileName = file.getName();

        tvSelectedimage.setText(taxFileName);
    }


    private String encodeImage(File imagefile) {
        FileInputStream fis = null;
        try {
            fis = new FileInputStream(imagefile);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        Bitmap bm = BitmapFactory.decodeStream(fis);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bm.compress(Bitmap.CompressFormat.JPEG, 50, baos);
        byte[] b = baos.toByteArray();
        String encImage = Base64.encodeToString(b, Base64.DEFAULT);
        //Base64.de
        return encImage;

    }

}
