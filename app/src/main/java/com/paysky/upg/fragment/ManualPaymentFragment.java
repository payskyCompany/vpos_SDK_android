package com.paysky.upg.fragment;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.os.Handler;
import android.text.InputFilter;
import android.text.InputType;
import android.util.Log;
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
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.paysky.upg.R;
import com.paysky.upg.activity.MainActivity;
import com.paysky.upg.customviews.CardEditText;
import com.paysky.upg.customviews.CardsValidation;
import com.paysky.upg.utils.MonthYearPickerDialog;
import com.threatmetrix.TrustDefender.Config;
import com.threatmetrix.TrustDefender.EndNotifier;
import com.threatmetrix.TrustDefender.Profile;
import com.threatmetrix.TrustDefender.ProfilingOptions;
import com.threatmetrix.TrustDefender.TrustDefender;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Random;
import java.util.concurrent.TimeUnit;

import cards.pay.paycardsrecognizer.sdk.Card;
import cards.pay.paycardsrecognizer.sdk.ScanCardIntent;
import faranjit.currency.edittext.CurrencyEditText;
import io.paysky.upg.data.network.model.LoginUpgMerchant;
import io.paysky.upg.data.network.model.response.DateTransactions;
import io.paysky.upg.mvp.manualpaymentfragment.ManualPaymentFragmentPresenter;
import io.paysky.upg.mvp.manualpaymentfragment.ManualPaymentFragmentView;
import io.paysky.upg.util.DateTimeUtil;
import io.paysky.upg.util.NumberUtil;
import io.paysky.upg.util.SessionManager;
import io.paysky.upg.util.ToastUtil;
import io.paysky.upg.util.ViewUtil;

import static android.text.TextUtils.isEmpty;
import static com.paysky.upg.activity.MainActivity.showOnlyNfcInPaymentFragment;

public class ManualPaymentFragment extends BaseFragment implements ManualPaymentFragmentView {


    //GUI.
    public CardEditText cardNumberEditText;
    private EditText CardHolderName;
    public EditText expireDateEditText;
    private EditText ccvEditText;
    private CurrencyEditText amountEditText;
    private String cardNumber;
    private String expireDate;
    private String ccv;
    EditText emailInput;
    public static String selectedTerminal;

    private TextView currencyTextView;


    LinearLayout mainTab, manualLL, contactLessLL;
    ImageView imageManual, imageContactLess;
    TextView tvManual, tvContactLess;

    private LinearLayout payByCardLayout;
    private LinearLayout tabCardLayout;
    Button proceedButton;
    ImageView scanCardImageView;

    View rootView;

    Spinner NotiType;
    private String generateRandomNumberFingerPrint = "";

    // Objects
    private ManualPaymentFragmentPresenter presenter = new ManualPaymentFragmentPresenter();
    private String invoiceNo;
    private boolean IsICNotify;
    private double totalAmount;
    private boolean isAllowPartialPayment;

    static final int REQUEST_CODE_SCAN_CARD = 1;
    private Handler handler = new Handler();


    private InputFilter filter = (source, start, end, dest, dstart, dend) -> {
        String blockCharacterSet = "~#^|$%&*!.";
        if (source != null && blockCharacterSet.contains(("" + source))) {
            return "";
        }
        return null;
    };

    public ManualPaymentFragment() {
        // Required empty public constructor
    }


    Spinner ButtonSpinner;
    public static String CardHolderNameString;


    @Override
    public void onPause() {
        super.onPause();
        IsICNotify = false;
    }

    public static ManualPaymentFragment newInstance() {
        ManualPaymentFragment fragment = new ManualPaymentFragment();
        return fragment;
    }

    public static ManualPaymentFragment newInstance(String fromwhere) {
        ManualPaymentFragment fragment = new ManualPaymentFragment();
        Bundle args = new Bundle();
        args.putSerializable(TransactionsLoadingFragment.FROM_WHERE, fromwhere);
        fragment.setArguments(args);

        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initFingerPrintSDK();
        if (getArguments() != null) {
            invoiceNo = (String) getArguments().getSerializable("invoiceNo");
            totalAmount = (double) getArguments().getDouble("totalAmount");
            isAllowPartialPayment = (boolean) getArguments().getBoolean("isAllowPartialPayment");
            IsICNotify = (boolean) getArguments().getBoolean("IsICNotify");
        }
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        rootView = inflater.inflate(R.layout.fragment_card_manual_payment, container, false);
        initView(rootView);
        showOnlyNfcInPaymentFragment = false;
        presenter.attachView(this);
        try {
            if (!String.valueOf(totalAmount).equals("0.0"))
                amountEditText.setText(String.valueOf(totalAmount));
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }

        if (!isAllowPartialPayment && !String.valueOf(totalAmount).equals("0.0")) {
            amountEditText.setEnabled(false);
        }
        return rootView;
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    String NotificationMethod = "0";

    private void initView(View view) {
        ImageView cardTypeImageView = view.findViewById(R.id.card_type_imageView);
        cardNumberEditText = view.findViewById(R.id.card_number_editText);
        cardNumberEditText.setCardTypeImage(cardTypeImageView);
        CardHolderName = view.findViewById(R.id.card_owner_name_editText);
        expireDateEditText = view.findViewById(R.id.expire_date_editText);
        ButtonSpinner = view.findViewById(R.id.ButtonSpinner);
        NotiType = view.findViewById(R.id.NotiType);
        emailInput = view.findViewById(R.id.userName);
        ccvEditText = view.findViewById(R.id.ccv_editText);
        amountEditText = view.findViewById(R.id.amount_editText);
        amountEditText.setFilters(new InputFilter[]{filter});
        payByCardLayout = view.findViewById(R.id.layout_pay_by_card);
        tabCardLayout = view.findViewById(R.id.tv_tab_card);
        proceedButton = view.findViewById(R.id.proceed_button);
        scanCardImageView = view.findViewById(R.id.scan_camera_imageView);
        currencyTextView = view.findViewById(R.id.tv_currency);
        manualLL = view.findViewById(R.id.manualLL);
        contactLessLL = view.findViewById(R.id.contactLessLL);
        imageManual = view.findViewById(R.id.imageManual);
        imageContactLess = view.findViewById(R.id.imageContactLess);
        tvManual = view.findViewById(R.id.tvManual);
        tvContactLess = view.findViewById(R.id.tvContactLess);
        mainTab = view.findViewById(R.id.MainTab);


        LinearLayout LLTerminalList = view.findViewById(R.id.LLTerminalList);

        ArrayAdapter<String> spinnerArrayAdapter2 = new ArrayAdapter<String>(getContext(),
                R.layout.spinner_item,
                getActivity().getResources().getStringArray(R.array.notitype)); //selected item will look like a spinner set from XML
        spinnerArrayAdapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        NotiType.setAdapter(spinnerArrayAdapter2);

        NotiType.getBackground().setColorFilter(getActivity().getResources().getColor(R.color.fieldscolor), PorterDuff.Mode.SRC_ATOP);

        NotiType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                if (selectedItemView != null)

                    ((TextView) selectedItemView).setTextColor(getResources().getColor(R.color.black));
                if (position == 0) {
                    emailInput.setHint(getActivity().getResources().getString(R.string.upg_new_merchant_label_email));
                    emailInput.setInputType(1);
                    emailInput.setInputType(InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS |
                            InputType.TYPE_TEXT_VARIATION_EMAIL_SUBJECT);

                    NotificationMethod = "0";

                } else {
                    emailInput.setHint(getActivity().getResources().getString(R.string.upg_new_merchant_phone_hint));
                    emailInput.setInputType(InputType.TYPE_CLASS_PHONE);
                    NotificationMethod = "1";
                    if (SessionManager.getInstance().getLang().equals("ar")) {

                        emailInput.setTextAlignment(View.TEXT_ALIGNMENT_VIEW_START);
                    }
                }

                emailInput.setText("");
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
            }

        });

        NotiType.setSelection(1);
        final LoginUpgMerchant empData = sessionManager.getEmpData();
        ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(getActivity(), R.layout.spinner_item,
                empData.getterminalTypesModelWithTypeString()); //selected item will look like a spinner set from XML
        spinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        ButtonSpinner.setAdapter(spinnerArrayAdapter);


        ButtonSpinner.getBackground().setColorFilter(getResources().getColor(R.color.fieldscolor), PorterDuff.Mode.SRC_ATOP);
        if (empData.getterminalTypesModelString().size() > 1) {
            ButtonSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                    selectedTerminal = empData.getterminalTypesModelString().get(position);
                }

                @Override
                public void onNothingSelected(AdapterView<?> parentView) {
                    selectedTerminal = empData.getterminalTypesModelString().get(0);
                }
            });

        } else {
            selectedTerminal = empData.getterminalTypesModelString().get(0);
            LLTerminalList.setVisibility(View.GONE);
        }

        ViewUtil.alignRightIfAppRtl(cardNumberEditText, expireDateEditText, ccvEditText, amountEditText);

        if (MainActivity.CanMakeNFC) {
            mainTab.setVisibility(View.VISIBLE);
            ((MainActivity) getActivity()).openNfcIFNeedCheck();
        }
        expireDateEditText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MonthYearPickerDialog pickerDialog = new MonthYearPickerDialog();
                pickerDialog.setListener(new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int year, int month, int i2) {
                        expireDate = String.valueOf(year).substring(2);
                        expireDateEditText.setText(String.format("%s/%s", String.format(Locale.ENGLISH, "%02d", month), expireDate));
                        expireDateEditText.setError(null);
                    }
                });
                pickerDialog.show(ManualPaymentFragment.this.getChildFragmentManager(), "MonthYearPickerDialog");
            }
        });

        showManual();

        manualLL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showManual();
            }
        });

        contactLessLL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showContactLess();
            }
        });

        proceedButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                payByCardManual();
            }
        });

        scanCardImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onScanCardCameraButtonClick();
            }
        });

        currencyTextView.setText(empData.CurrencyName);
    }

    private void initFingerPrintSDK() {
        LoginUpgMerchant empData = sessionManager.getEmpData();
        if (empData.getPrimaryMerchant().IsAllowCyberSource) {
            if (empData.getPrimaryMerchant().CyberSourceFingerPrintOrgId != null) {
                try {
                    Config config = new Config().setOrgId(empData.getPrimaryMerchant().CyberSourceFingerPrintOrgId).setFPServer("h.online-metrix.net").setContext(getContext()).setTimeout(10, TimeUnit.DAYS.SECONDS)
                            .setRegisterForLocationServices(true).setRegisterForPush(true);
                    TrustDefender.getInstance().init(config);
                    generateRandomNumberFingerPrint = DateTimeUtil.getDateTimeLocalTrxn() + String.valueOf(10000 + new Random(System.currentTimeMillis()).nextInt(20000));
                    ProfilingOptions options = new ProfilingOptions().setSessionID(empData.getPrimaryMerchant().CyberSourceMerchantId + generateRandomNumberFingerPrint);
                    TrustDefender.getInstance().doProfileRequest(options, new CompletionNotifier());
                } catch (Exception e) {
                    ToastUtil.showShortToast(getContext(), "Invalid format for OrgId, ");
                }
            }
        }
    }

    private void payByCardManual() {
        // get manual payment data.
        cardNumber = getText(cardNumberEditText).replaceAll(" ", "");
        String cardOwnerName = getText(CardHolderName);
        expireDate = getText(expireDateEditText).replaceAll("/", "");
        ccv = getText(ccvEditText);
        String amount = getText(amountEditText);
        boolean hasError = false;
        if (!isInputsValid(cardOwnerName, expireDate, ccv)) {
            hasError = true;
        }
        CardHolderNameString = cardOwnerName;

        if (amount.isEmpty() || amount.equals("0.00")) {
            amountEditText.setError(getString(R.string.upg_general_required));
            hasError = true;
        }
        if (!cardNumberEditText.getText().toString().isEmpty() && cardNumberEditText.getText().toString().length() < 16) {
            cardNumberEditText.setError(getString(R.string.invalid_card_number_length));
            hasError = true;
        }

        if (cardNumberEditText.getText().toString().isEmpty()) {
            cardNumberEditText.setError(getString(R.string.upg_general_required));
            hasError = true;
        }
        if (!cardNumberEditText.getText().toString().isEmpty()
                && !CardsValidation.luhnCheck(cardNumber)) {
            cardNumberEditText.setError(getString(R.string.invalid_card_number_length));
            hasError = true;
        }

        if (hasError) {
            return;
        }

        // replace expire date.
        String month = expireDate.substring(0, 2);
        String year = expireDate.substring(2);
        expireDate = year + month;

        ViewUtil.hideKeyboard(getActivity());
        int amountFormatted = 0;
        try {
            amountFormatted = NumberUtil.formatPaymentAmountToServer(amountEditText.getCurrencyText());
        } catch (ParseException e) {
            e.printStackTrace();
        }

        LoginUpgMerchant empData = sessionManager.getEmpData();

        presenter.payByCard(invoiceNo, IsICNotify, CardHolderName.getText().toString(), empData.getCurrencyCode(), amountFormatted + "", selectedTerminal, ccv, expireDate, cardNumber, NotificationMethod, emailInput.getText().toString(), generateRandomNumberFingerPrint);

        cardNumberEditText.setText("");
        amountEditText.setText("");
        expireDateEditText.setText("");
        ccvEditText.setText("");
        CardHolderName.setText("");
        NotiType.setSelection(0);
        ButtonSpinner.setSelection(0);
    }

    private class CompletionNotifier implements EndNotifier {

        @Override
        public void complete(Profile.Result result) {
            TrustDefender.getInstance().doPackageScan();
        }
    }

    private String getText(TextView view) {
        return view.getText().toString().trim();
    }


    private void onScanCardCameraButtonClick() {
        Intent intent = new ScanCardIntent.Builder(getActivity()).setSoundEnabled(false).build();
        startActivityForResult(intent, REQUEST_CODE_SCAN_CARD);

    }

    private boolean isInputsValid(String ownerName, String expireDate, String ccv) {
        boolean isValidInputs = true;

        if (isEmpty(ownerName)) {
            isValidInputs = false;
            CardHolderName.setError(getString(R.string.enter_valid_owner));
        }
        if (isEmpty(expireDate) || expireDate.length() < 4) {
            isValidInputs = false;
            expireDateEditText.setError(getString(R.string.invalid_expire_date));
        } else {
            // validate that expire date large than today.
            int enteredMonth = Integer.parseInt(expireDate.substring(0, 2));
            int enteredYear = Integer.parseInt(expireDate.substring(2));
            SimpleDateFormat sdf = new SimpleDateFormat("MM/yy", Locale.US);
            String date = sdf.format(new Date());
            String month = date.substring(0, date.indexOf("/"));
            String year = date.substring(date.indexOf("/") + 1);
            int monthNumber = Integer.valueOf(month);
            int yearNumber = Integer.valueOf(year);
            if (enteredYear < yearNumber) {
                // invalid year.
                isValidInputs = false;
                expireDateEditText.setError(getString(R.string.invalid_expire_date_date));
            } else if (enteredYear == yearNumber) {
                // validate month.
                if (enteredMonth < monthNumber) {
                    isValidInputs = false;
                    expireDateEditText.setError(getString(R.string.invalid_expire_date_date));
                }
            }
        }

        if (isEmpty(ccv)) {
            isValidInputs = false;
            ccvEditText.setError(getString(R.string.upg_general_required_field));
        }
        if (!isEmpty(ccv) && ccv.length() < 3) {
            isValidInputs = false;
            ccvEditText.setError(getString(R.string.invalid_ccv));
        }
        return isValidInputs;
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_CODE_SCAN_CARD) {
            if (resultCode == Activity.RESULT_OK) {
                Card card = data.getParcelableExtra(ScanCardIntent.RESULT_PAYCARDS_CARD);
                cardNumberEditText.setText(card.getCardNumber());
                expireDateEditText.setText(card.getExpirationDate());
                CardHolderName.setText(card.getCardHolderName());
            } else if (resultCode == Activity.RESULT_CANCELED) {
                Log.i("pay-cards", "Scan canceled");
            } else {
                Log.i("pay-cards", "Scan failed");
            }
        }
    }


    public void show3dpWebView(String url) {
        Bundle bundle = new Bundle();
        bundle.putString("url", url);
        WebPaymentFragment webPaymentFragment = new WebPaymentFragment();
        webPaymentFragment.setArguments(bundle);
        addNewFragmentNullBackStack(webPaymentFragment, WebPaymentFragment.class.getSimpleName());
    }


    public void loadReportFromServerUsingExternal(String TahweeltransactionId) {
        progress.setLabel(getString(R.string.retrieving_transaction_report));

        if (TahweeltransactionId == null || TahweeltransactionId.isEmpty() ||
                sessionManager.getEmpData() == null) {
            ToastUtil.showShortToast(getContext(), R.string.transaction_not_Found);
            return;
        }
        presenter.loadReportFromServerUsingExternal(TahweeltransactionId, selectedTerminal
        );
    }

    @Override
    public void showReceiptWithTransactionFragment(DateTransactions dateTransactions) {
        addNewFragmentNullBackStack(ReceiptWithTransFragment.newInstance(dateTransactions), "ReceiptWithTransFragment");
    }

    @Override
    public void showAnimatedVideoTransactionFragment(DateTransactions dateTransactions) {
        addNewFragmentNullBackStack(AnimationVideoTransactionsFragment.newInstance(dateTransactions), "AnimationVideoTransactionsFragment");
    }

    @Override
    public void onDestroyView() {
        presenter.detachView();
        super.onDestroyView();
        showOnlyNfcInPaymentFragment = false;
    }


    public void payByCardContactLess(String cardNo, String cardExpiry) {
        // Format expire date.
        String expireDate = cardExpiry.replaceAll("/", "");
        String month = expireDate.substring(0, 2);
        String year = expireDate.substring(2);
        expireDate = year + month;
        int amountFormatted = 0;
        boolean hasError = false;
        String amount = getText(amountEditText);
        if (amount.isEmpty() || amount.equals("0.00")) {
            amountEditText.setError(getString(R.string.upg_general_required));
            hasError = true;
        }
        if (hasError) return;
        try {
            amountFormatted = NumberUtil.formatPaymentAmountToServer(amountEditText.getCurrencyText());
        } catch (ParseException e) {
            e.printStackTrace();
        }
        LoginUpgMerchant empData = SessionManager.getInstance().getEmpData();
        selectedTerminal = empData.getterminalTypesModelString().get(0);
        presenter.payByCard(invoiceNo, IsICNotify, "", empData.getCurrencyCode(), amountFormatted + "", selectedTerminal, "", expireDate, cardNo, "0", "", generateRandomNumberFingerPrint);
    }


    private void showManual() {
        imageContactLess.setColorFilter(getResources().getColor(R.color.primaryBlue));
        tvContactLess.setTextColor(getResources().getColor(R.color.primaryBlue));
        contactLessLL.setBackground(getResources().getDrawable(R.drawable.white_radius_black_background));
        imageManual.setColorFilter(getResources().getColor(R.color.white));
        tvManual.setTextColor(getResources().getColor(R.color.white));
        manualLL.setBackground(getResources().getDrawable(R.drawable.blue_bg_with_redius));

        payByCardLayout.setVisibility(View.VISIBLE);
        tabCardLayout.setVisibility(View.GONE);
        showOnlyNfcInPaymentFragment = false;
    }

    private void showContactLess() {
        imageManual.setColorFilter(getResources().getColor(R.color.primaryBlue));
        tvManual.setTextColor(getResources().getColor(R.color.primaryBlue));
        manualLL.setBackground(getResources().getDrawable(R.drawable.white_radius_black_background));
        imageContactLess.setColorFilter(getResources().getColor(R.color.white));
        tvContactLess.setTextColor(getResources().getColor(R.color.white));
        contactLessLL.setBackground(getResources().getDrawable(R.drawable.blue_bg_with_redius));
        tabCardLayout.setVisibility(View.VISIBLE);
        payByCardLayout.setVisibility(View.GONE);
        showOnlyNfcInPaymentFragment = true;
    }

}
