package com.paysky.upg.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.nfc.NfcAdapter;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.FragmentManager;

import com.paysky.upg.R;
import com.paysky.upg.fcm.NotificationData;
import com.paysky.upg.fragment.BaseFragment;
import com.paysky.upg.fragment.DashBoardFragment;
import com.paysky.upg.fragment.ManualPaymentFragment;
import com.paysky.upg.fragment.NotificationFragment;
import com.paysky.upg.fragment.ReceiptWithTransFragment;
import com.paysky.upg.fragment.TransactionsFragment;
import com.paysky.upg.utils.NotificationCount;
import com.pro100svitlo.creditCardNfcReader.CardNfcAsyncTask;
import com.pro100svitlo.creditCardNfcReader.utils.CardNfcUtils;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.paysky.upg.data.network.model.LoginUpgMerchant;
import io.paysky.upg.data.network.model.response.DateTransactions;
import io.paysky.upg.data.network.model.response.NotificationCountResponse;
import io.paysky.upg.data.network.model.response.ReportResponse;
import io.paysky.upg.mvp.upgmain.MainPresenter;
import io.paysky.upg.mvp.upgmain.MainView;
import io.paysky.upg.util.DateTimeUtil;
import io.paysky.upg.util.FirebaseAnalyticsUtilty;
import io.paysky.upg.util.LocaleUtil;
import io.paysky.upg.util.SessionManager;
import io.paysky.upg.util.ToastUtil;


public class MainActivity extends BaseActivity implements MainView, CardNfcAsyncTask.CardNfcInterface {


    public static LinearLayout notificationIcon;
    @BindView(R.id.tv_total)
    TextView tvTotal;
    @BindView(R.id.tv_trans)
    TextView tvTrans;
    @BindView(R.id.merchantName)
    TextView merchantName;
    @BindView(R.id.trimnalId)
    TextView trimnalId;
    @BindView(R.id.notiLL)
    LinearLayout notiLL;


    //Variables
    public static String StartDate;
    public static String MobileNumber = "";
    public static String EndDate;
    public static String TahweeltransactionId;
    public static String ResultTerminalidString;
    public static String ResultTypeString = "";


    //Objects.
    NotificationData notificationDataSelected;
    Toast toastNotification;
    TextView FromOrTo;
    private ClickedFragment clickedFragment;
    public static Bitmap staticMerchantQr = null;
    private MainPresenter mainPresenter;


    private String mDoNotMoveCardMessage;
    private String mUnknownEmvCardMessage;
    private String mCardWithLockedNfcMessage;
    private boolean mIsScanNow;
    private boolean mIntentFromCreate;
    private CardNfcUtils mCardNfcUtils;
    public static boolean CanMakeNFC = false;
    private NfcAdapter mNfcAdapter;
    private AlertDialog mTurnNfcDialog;
    private CardNfcAsyncTask mCardNfcAsyncTask;

    public static boolean showOnlyNfcInPaymentFragment = false;
    private long lastClickTime = 0;

    // Location Data
    private boolean isContinue = false;
    private LoginUpgMerchant empData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        SessionManager sessionManager = SessionManager.getInstance();
        empData = sessionManager.getEmpData();
        setContentView(R.layout.activity_upg_main);
        ButterKnife.bind(this);


        if (sessionManager.getEmpData() != null)
            FirebaseAnalyticsUtilty.getInstance().setKeyVale(sessionManager.getEmpData().getUserId(), sessionManager.getEmpData().getPrimaryMerchant().getMerchantId());


        if (empData.isIsPrimaryMerchant() && ResultTerminalidString == null) {
            ResultTerminalidString = "";
        } else if (!empData.isIsPrimaryMerchant()) {
            ResultTerminalidString = null;
        }

        if (StartDate == null) {
            StartDate = DateTimeUtil.getDateTimeFromMonthVpos();
        }
        if (EndDate == null) {
            EndDate = DateTimeUtil.getDateTimeNow();
        }


        TahweeltransactionId = "";

        mainPresenter = new MainPresenter();
        mainPresenter.attachView(this);
        backbtn = findViewById(R.id.backbtn);
        notificationIcon = findViewById(R.id.notiIcon);
        extractBundleData();
        setupToast();
        extractEmployeeData();
        setMerchantData();
        mNfcAdapter = NfcAdapter.getDefaultAdapter(this);
        if (mNfcAdapter == null) {
            CanMakeNFC = false;
        } else {
            mCardNfcUtils = new CardNfcUtils(this);
            CanMakeNFC = true;
            initNfcMessages();
            mIntentFromCreate = true;
            onNewIntent(getIntent());
        }

        showServicesTab();
        getBuildReport();
    }

    public void getBuildReport() {
        mainPresenter.buildTransactionChart(ResultTypeString, StartDate, EndDate, ResultTerminalidString, MobileNumber
        );
    }


    private void initNfcMessages() {
        mDoNotMoveCardMessage = getString(R.string.snack_doNotMoveCard);
        mCardWithLockedNfcMessage = getString(R.string.snack_lockedNfcCard);
        mUnknownEmvCardMessage = getString(R.string.snack_unknownEmv);
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    public void setMerchantData() {
        SessionManager sessionManager = SessionManager.getInstance();
        if (sessionManager != null && sessionManager.getEmpData() != null && merchantName != null)
            if (sessionManager.getEmpData().isPrimaryMerchant()) {
                merchantName.setText(sessionManager.getEmpData().getPrimaryMerchant().getMerchantName());
            } else {
                merchantName.setText(sessionManager.getEmpData().getDigQrTerminal().getName());
            }

        if (trimnalId != null) {
            if (sessionManager != null && sessionManager.getEmpData() != null && sessionManager.getEmpData().isPrimaryMerchant()) {
                trimnalId.setText(String.format("%s %s", getString(R.string.MerchantID),
                        sessionManager.getEmpData().getPrimaryMerchant().getMerchantId()));
            } else {
                if (sessionManager != null && sessionManager.getEmpData() != null && sessionManager.getEmpData().getDigQrTerminal() != null && sessionManager.getEmpData().getDigQrTerminal().getTerminalId() != null) {
                    trimnalId.setText(String.format("%s %s", getString(R.string.UPGTID),
                            sessionManager.getEmpData().getDigQrTerminal().getTerminalId()));
                }
            }

        }

        if (notiLL != null) {
            notiLL.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (getVisibleFragment() != null && getVisibleFragment().getTag() != null &&
                            (getVisibleFragment().getTag().equals("NotificationFragment"))) {
                        return;
                    }
                    addNewFragmentPush(NotificationFragment.newInstance(), "NotificationFragment");
                }
            });
        }
    }


    @Override
    protected void onStop() {
        mainPresenter.detachView();
        super.onStop();
    }

    private void extractEmployeeData() {
        LoginUpgMerchant employeeData = session.getEmpData();
        if (employeeData != null) {
            if (employeeData.isIsPrimaryMerchant()) {
                ResultTerminalidString = "";
            }
        }
    }

    private void extractBundleData() {
        if (session.isLogin() && getIntent() != null && getIntent().getExtras() != null && getIntent().getExtras().getSerializable("notification") != null) {
            NotificationData notificationData = (NotificationData) getIntent().getExtras().getSerializable("notification");
            reportService(notificationData.getReferenceNumber());
        }
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        if (mNfcAdapter != null && mNfcAdapter.isEnabled()) {
            mCardNfcAsyncTask = new CardNfcAsyncTask.Builder(this, intent, mIntentFromCreate)
                    .build();
        }

    }

    public void setupToast() {
        LayoutInflater inflater = getLayoutInflater();
        View layout = inflater.inflate(R.layout.noti_items,
                (ViewGroup) findViewById(R.id.toast_layout_root));
        FromOrTo = (TextView) layout.findViewById(R.id.FromOrTo);
        TextView trxid = (TextView) layout.findViewById(R.id.trxid);
        TextView Amount = (TextView) layout.findViewById(R.id.Amount);
        TextView dataTime = (TextView) layout.findViewById(R.id.dataTime);
        LinearLayout notiitem = (LinearLayout) layout.findViewById(R.id.toast_layout_root);
        Amount.setVisibility(View.GONE);
        dataTime.setVisibility(View.GONE);
        trxid.setText(getString(R.string.Notification));
        notiitem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                reportService(notificationDataSelected.getReferenceNumber());

            }
        });
        toastNotification = new Toast(getApplicationContext());
        toastNotification.setGravity(Gravity.BOTTOM, 0, 200);
        toastNotification.setDuration(Toast.LENGTH_SHORT);
        toastNotification.setView(layout);
    }


    @Subscribe(threadMode = ThreadMode.MAIN)
    public void generateCustomToast(final NotificationData notificationData) {
        mainPresenter.getNotificationCount();

        if (notificationData.getReferenceNumber() != null) {
            notificationDataSelected = notificationData;
            FromOrTo.setText(notificationDataSelected.getTextMessage());
            if (getVisibleFragment() != null && getVisibleFragment().getTag() != null && (getVisibleFragment().getTag().equals("StaticQrFragment") || getVisibleFragment().getTag().equals("StaticQr")
            )) {
                return;
            }

            if (getVisibleFragment() == null) {
                return;
            }
            toastNotification.show();
        }

    }

    public void reportService(String transactionId) {
        mainPresenter.filterTransaction(transactionId);
    }


    private void showServicesTab() {
        removeStack();
        addNewFragment(TransactionsFragment.newInstance(),
                TransactionsFragment.class.getSimpleName());
        backbtn.setVisibility(View.GONE);

    }


    @Override
    protected void onResume() {
        super.onResume();
        getNotificationCount();
        if (!session.isLogin()) {
            EventBus.getDefault().post("FINISH");
        }

        mIntentFromCreate = false;
        if (mNfcAdapter != null && !mNfcAdapter.isEnabled()) {
        } else if (mNfcAdapter != null) {
            mCardNfcUtils.enableDispatch();
        }
    }

    public void openNfcIFNeedCheck() {
        mIntentFromCreate = false;
        if (mNfcAdapter != null && !mNfcAdapter.isEnabled()) {
            showTurnOnNfcDialog();
        } else if (mNfcAdapter != null) {
            //   ToastUtil.showLongToast(getContext(), getString(R.string.tv_tapYourCard));
            mCardNfcUtils.enableDispatch();
        }
    }

    public void showTurnOnNfcDialog() {
        progress.dismiss();
        if (mTurnNfcDialog == null) {
            String title = getString(R.string.ad_nfcTurnOn_title);
            String mess = getString(R.string.ad_nfcTurnOn_message);
            String pos = getString(R.string.ad_nfcTurnOn_pos);
            String neg = getString(R.string.ad_nfcTurnOn_neg);
            mTurnNfcDialog = new AlertDialog.Builder(this)
                    .setTitle(title)
                    .setMessage(mess)
                    .setPositiveButton(pos, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            // Send the user to the settings page and hope they turn it on
                            if (android.os.Build.VERSION.SDK_INT >= 16) {
                                startActivity(new Intent(android.provider.Settings.ACTION_NFC_SETTINGS));
                            } else {
                                startActivity(new Intent(android.provider.Settings.ACTION_WIRELESS_SETTINGS));
                            }
                        }
                    })
                    .setNegativeButton(neg, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            onBackPressed();
                        }
                    }).create();
        }

        if (!mTurnNfcDialog.isShowing())
            mTurnNfcDialog.show();
    }


    public void removeStack() {
        FragmentManager fm = getSupportFragmentManager();
        for (int i = 0; i < fm.getBackStackEntryCount(); ++i) {
            fm.popBackStack();
        }
        backbtn.setVisibility(View.GONE);
    }


    public void setLocale() {
        String lang = SessionManager.getInstance().getLang();
        if (lang.equals("ar")) {
            lang = "en";
            LocaleUtil.changeAppLanguage(this, lang);
        } else {
            lang = "ar";
            LocaleUtil.changeAppLanguage(this, lang);
        }

        recreate();
        NotificationCount.RefreshCount();
//        Intent refresh = new Intent(this, SplashActivity.class);
//        startActivity(refresh);
//        NotificationCount.RefreshCount();
//        finish();
        // TODO: 12/5/2018  what is need of this
 /*       new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                NotificationCount.RefreshCount();
            }
        }, 1000);*/
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onNotification(NotificationCountResponse reportRequest) {
        DashBoardFragment.MerChantbalance = reportRequest.getMerchantBalance();
        NotificationCount.setCount(reportRequest.getNotificationCountX());
    }

    public void getNotificationCount() {
        mainPresenter.getNotificationCount();
    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }

    @Override
    protected void onPause() {
        super.onPause();
        if (mNfcAdapter != null) {
            mCardNfcUtils.disableDispatch();
        }
    }

    @Override
    public void showTransactionWithReceiptFragment(DateTransactions dateTransactions) {
        progress.dismiss();

        ((BaseFragment) getVisibleFragment()).progress.dismiss();

        addNewFragment(ReceiptWithTransFragment.newInstance(dateTransactions), "ReceiptWithTransFragment");
        backbtn.setVisibility(View.VISIBLE);
    }


    @Override
    public void showData(ReportResponse body) {
        tvTrans.setText(String.format("%s %s", getString(R.string.upg_dashboard_transaction_count_label), body.getTotalCountAllTransaction()));
        tvTotal.setText(String.format("%s%s",
                " " + SessionManager.getInstance().getEmpData().CurrencyName + " ", body.getTotalAmountAllTransaction()));
    }

    @Override
    public void startNfcReadCard() {
        if (!showOnlyNfcInPaymentFragment)
            return;
        mIsScanNow = true;
        progress.show();

    }

    @Override
    public void cardIsReadyToRead() {
        progress.dismiss();
        if (!showOnlyNfcInPaymentFragment) {
            return;
        }
        String cardPAN = mCardNfcAsyncTask.getCardNumber();
        String expiredDate = mCardNfcAsyncTask.getCardExpireDate();

        ManualPaymentFragment manualPaymentFragment = (ManualPaymentFragment) getSupportFragmentManager().findFragmentById(R.id.fragment_container);
        if (manualPaymentFragment != null)
            manualPaymentFragment.payByCardContactLess(cardPAN, expiredDate);

    }

    @Override
    public void doNotMoveCardSoFast() {
        progress.dismiss();
        // ToastUtil.showLongToast(getContext(), mDoNotMoveCardMessage);
    }

    @Override
    public void unknownEmvCard() {
        progress.dismiss();
        ToastUtil.showLongToast(getContext(), mUnknownEmvCardMessage);
    }

    @Override
    public void cardWithLockedNfc() {
        progress.dismiss();
        ToastUtil.showLongToast(getContext(), mCardWithLockedNfcMessage);
    }

    @Override
    public void finishNfcReadCard() {
        mCardNfcAsyncTask = null;
        mIsScanNow = false;
    }


    private enum ClickedFragment {
        DASHBOARD, QR, SERVICES, TRANSACTIONS, HELP
    }


}
