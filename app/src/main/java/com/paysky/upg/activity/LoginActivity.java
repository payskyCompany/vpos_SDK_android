package com.paysky.upg.activity;

import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.firebase.iid.FirebaseInstanceId;
import com.paysky.upg.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.paysky.upg.mvp.login.LoginPresenter;
import io.paysky.upg.mvp.login.LoginView;
import io.paysky.upg.util.ActivityUtil;
import io.paysky.upg.util.ValidateUtil;
import io.paysky.upg.util.ViewUtil;

public class LoginActivity extends BaseActivity implements LoginView {

    @BindView(R.id.userName)
    EditText userName;
    @BindView(R.id.password)
    EditText passwordInput;
    @BindView(R.id.login_layout)
    LinearLayout loginLinearLayout;
    LoginPresenter loginPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);
        ViewUtil.alignRightIfAppRtl(passwordInput);
        ViewUtil.preventCopyAndPaste(userName, passwordInput);

        passwordInput.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {

                if (actionId == 0) {
                    loginUser();
                    return true;
                }

                return false;
            }
        });

        loginPresenter = new LoginPresenter();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @OnClick(R.id.login_layout)
    public void loginClick(View view) {
        loginUser();
    }

    private void loginUser() {
        userName.setError(null);
        passwordInput.setError(null);
        boolean cancel = false;
        View focusView = null;
        if (userName.getText().toString().trim().isEmpty()) {
            userName.setError(getString(R.string.error_field_required));
            focusView = userName;
            cancel = true;
        } else if (passwordInput.getText().toString().trim().isEmpty()) {
            passwordInput.setError(getString(R.string.error_field_required));
            focusView = passwordInput;
            cancel = true;
        } else if (!ValidateUtil.validPassword(passwordInput.getText().toString().trim())) {
            passwordInput.setError(getString(R.string.error_invalid_password));
            focusView = passwordInput;
            cancel = true;
        }
        if (cancel) {
            focusView.requestFocus();
        } else {
            ViewUtil.hideKeyboard(this);
            loginLinearLayout.setEnabled(false);
            FirebaseInstanceId.getInstance().getInstanceId().addOnSuccessListener(instanceIdResult -> {
                String deviceToken = instanceIdResult.getToken();

                loginPresenter.login(getText(userName), getText(passwordInput), deviceToken, R.string.ConnectingtoUPG);
            });
            FirebaseInstanceId.getInstance().getInstanceId().addOnFailureListener(e -> loginPresenter.login(getText(userName), getText(passwordInput),
                    "", R.string.ConnectingtoUPG));
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        loginPresenter.attachView(this);
    }

    @Override
    protected void onStop() {
        loginPresenter.detachView();
        super.onStop();
    }

    @Override
    protected void onPause() {
        super.onPause();
        passwordInput.setText("");
    }

    @Override
    public void openUpgMainActivity() {
        loginLinearLayout.setEnabled(true);
        ActivityUtil.openActivity(this, MainActivity.class, true);
        finish();
    }

    @Override
    public void wrongPassword() {
        loginLinearLayout.setEnabled(true);
    }

    @Override
    public void onBackPressed() {
        onBackRealyPressed();
    }

}
