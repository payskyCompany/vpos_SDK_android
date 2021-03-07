package com.paysky.upg.fragment;


import android.annotation.TargetApi;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.CookieManager;
import android.webkit.ValueCallback;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.paysky.upg.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Set;

import io.paysky.upg.data.network.model.response.DateTransactions;
import io.paysky.upg.mvp.webpaymentfragment.WebPaymentFragmentPresenter;
import io.paysky.upg.mvp.webpaymentfragment.WebPaymentFragmentView;
import io.paysky.upg.util.SessionManager;
import io.paysky.upg.util.ToastUtil;


/**
 * A simple {@link Fragment} subclass.
 */
public class WebPaymentFragment extends BaseFragment implements WebPaymentFragmentView {

    //GUI
    private WebView webView;
    //Variables.

    private String url;

    //Objects,
    private WebPaymentFragmentPresenter presenter = new WebPaymentFragmentPresenter();

    public WebPaymentFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        extractBundleData();
    }


    boolean timeout;
    Thread thread;

    private void extractBundleData() {
        Bundle arguments = getArguments();
        url = arguments.getString("url");
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragmentu
        super.onCreateView(inflater, container, savedInstanceState);
        if (isViewHidden) {
            return null;
        }
        presenter.attachView(this);
        return inflater.inflate(R.layout.fragment_web_payment, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if (isViewHidden) {
            return;
        }
        initView(view);
        load3dTransactionWebView();
    }

    private void initView(View view) {
        webView = view.findViewById(R.id.webView);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.setBackgroundColor(Color.TRANSPARENT);
        CookieManager.getInstance().setAcceptCookie(true);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            CookieManager.getInstance().setAcceptThirdPartyCookies(webView, true);
        }
        webView.setLayerType(WebView.LAYER_TYPE_SOFTWARE, null);
        progress.setLabel(getString(R.string.loading));
    }


    @Override
    public void showReceiptTransactionFragment(DateTransactions dateTransactions) {
        addNewFragmentNullBackStack(ReceiptWithTransFragment.newInstance(dateTransactions), "ReceiptWithTransFragment");
    }

    @Override
    public void showAnimatedVideoTransactionFragment(DateTransactions dateTransactions) {
        addNewFragmentNullBackStack(AnimationVideoTransactionsFragment.newInstance(dateTransactions), "AnimationVideoTransactionsFragment");
    }

    boolean transactionComplete = false;

    public void load3dTransactionWebView() {
        progress.show();
        webView.loadUrl(url);
        WebViewClient webViewClient = new WebViewClient() {

            @Override
            public void onPageStarted(final WebView view, final String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
                if (isViewHidden) {
                    return;
                }
                progress.show();

                if (url.startsWith(SessionManager.getInstance().getAppEnvironment()) &&
                        !transactionComplete && url.contains("Success")) {
                    transactionComplete = true;
                    // call server.
                    progress.show();
                    try {
                        webView.stopLoading();
                        webView.setVisibility(View.GONE);
                        webView.setWebViewClient(null);
                        webView = null;
                        Uri uri = Uri.parse(url);
                        Set<String> names = uri.getQueryParameterNames();
                        JSONObject jsonObject = new JSONObject();
                        for (String key : names) {
                            jsonObject.put(key, uri.getQueryParameter(key));
                        }

                        if (jsonObject.getBoolean("Success")) {
                            presenter.filterTransactionTahweel(jsonObject.getString("NetworkReference"));

                        } else {

                            Toast.makeText(getActivity(), jsonObject.getString("Message"), Toast.LENGTH_LONG).show();

                            addNewFragmentNullBackStack(ManualPaymentFragment.newInstance(WebPaymentFragment.class.getName()),
                                    ManualPaymentFragment.class.getSimpleName());

                            if (thread != null) {
                                thread.interrupt();
                                thread = null;
                            }

                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }


                if (thread != null) {
                    thread.interrupt();
                    thread = null;
                }

                thread = new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            Thread.sleep(10000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        if (timeout) {
                            if (isViewHidden) {
                                return;
                            }


                            webView.setWebViewClient(null);
                            //   getActivity().onBackPressed();
                            progress.dismiss();
                            Toast.makeText(getActivity(), R.string.error_try_again, Toast.LENGTH_LONG).show();
                            addNewFragmentNullBackStack(ManualPaymentFragment.newInstance(WebPaymentFragment.class.getName()),
                                    ManualPaymentFragment.class.getSimpleName());
                            // do what you want
                        }
                    }
                });

                thread.start();

            }

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                if (isViewHidden) {
                    return;
                }
                timeout = false;
                if (thread != null) {
                    thread.interrupt();
                    thread = null;
                }
                if (url.contains(SessionManager.getInstance().getAppEnvironment())) {
                    progress.show();
                } else {
                    progress.dismiss();

                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                        webView.evaluateJavascript(
                                "document.documentElement.outerHTML;",
                                new ValueCallback<String>() {
                                    @Override
                                    public void onReceiveValue(String html) {
                                        if (html.contains("HTTP Status - 400")) {
                                            try {
                                                html = html.replaceAll("<.*?>", "");
                                                html = html.replaceAll("Chtml>", "");
                                                html = html.substring(html.indexOf("E5000:") + 7);
                                                html = html.substring(0, html.indexOf("\\u003C"));
                                                if (!html.isEmpty())
                                                    Toast.makeText(getActivity(), html, Toast.LENGTH_LONG).show();
                                            } catch (Exception e) {
                                                e.printStackTrace();
                                                Toast.makeText(getActivity(), R.string.error_try_again, Toast.LENGTH_LONG).show();
                                            }
                                            webView.setWebViewClient(null);
                                            //   getActivity().onBackPressed();
                                            progress.dismiss();
                                            Toast.makeText(getActivity(), R.string.error_try_again, Toast.LENGTH_LONG).show();
                                            addNewFragmentNullBackStack(ManualPaymentFragment.newInstance(WebPaymentFragment.class.getName()),
                                                    ManualPaymentFragment.class.getSimpleName());

                                        }
                                    }
                                });
                    }
                }
            }

            @TargetApi(Build.VERSION_CODES.M)
            @Override
            public void onReceivedError(WebView view, WebResourceRequest req, WebResourceError rerr) {
                onReceivedError(view, rerr.getErrorCode(), rerr.getDescription().toString(), req.getUrl().toString());
                if (isViewHidden) return;
                //-2 address not reachable , -14 error in server.
                if (rerr.getErrorCode() != -6) {
                    // error in server.
                    if (progress.isShowing())
                        progress.dismiss();
                    if (isViewHidden) return;
                    Toast.makeText(getActivity(), R.string.error_try_again, Toast.LENGTH_LONG).show();
                    webView.setWebViewClient(null);
                    addNewFragmentNullBackStack(ManualPaymentFragment.newInstance(WebPaymentFragment.class.getName()),
                            ManualPaymentFragment.class.getSimpleName());

                } else {
                    //progress.dismiss();

                }

            }

            @SuppressWarnings("deprecation")
            public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
                if (isViewHidden) return;
                //-2 address not reachable , -14 error in server.
                if (errorCode != -6) {
                    // error in server.
                    progress.dismiss();
                    Toast.makeText(getActivity(), R.string.error_try_again, Toast.LENGTH_LONG).show();
                    webView.setWebViewClient(null);
                    addNewFragmentNullBackStack(ManualPaymentFragment.newInstance(WebPaymentFragment.class.getName()),
                            ManualPaymentFragment.class.getSimpleName());

                } else {
                    //  progress.dismiss();

                }
            }

        };
        webView.setWebViewClient(webViewClient);

        webView.setWebChromeClient(new WebChromeClient() {
            public void onProgressChanged(WebView view, int progressnumber) {
                if (isViewHidden) {
                    return;
                }
                if (progressnumber != 100) {
                    if (!progress.isShowing())
                        progress.show();
                } else if (!transactionComplete) {
                    progress.dismiss();

                }

            }
        });

    }


    @Override
    public void callBack() {
        addNewFragmentNullBackStack(ManualPaymentFragment.newInstance(WebPaymentFragment.class.getName()),
                ManualPaymentFragment.class.getSimpleName());
    }

    public void loadReportFromServerUsingExternal(String TahweeltransactionId) {
        if (isViewHidden) {
            return;
        }
        progress.setLabel(getString(R.string.retrieving_transaction_report));
        if (TahweeltransactionId == null || TahweeltransactionId.isEmpty() ||
                SessionManager.getInstance().getEmpData() == null) {
            ToastUtil.showShortToast(getActivity(), R.string.transaction_not_Found);
            return;
        }

        progress.show();
        presenter.filterTransaction(TahweeltransactionId);

    }


    @Override
    public void onDestroyView() {
        presenter.detachView();
        if (webView != null)
            webView.setWebViewClient(null);
        if (thread != null)
            thread.interrupt();
        thread = null;
        super.onDestroyView();
    }
}
