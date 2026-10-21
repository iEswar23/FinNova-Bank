package com.eswar.finnova.ui.login;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.EditorInfo;

import androidx.annotation.Nullable;

import com.eswar.finnova.base.BaseActivity;
import com.eswar.finnova.databinding.ActivityLoginBinding;
import com.eswar.finnova.di.Injection;
import com.eswar.finnova.ui.common.ErrorMessages;
import com.eswar.finnova.ui.common.UiUtils;
import com.eswar.finnova.ui.main.MainActivity;
import com.eswar.finnova.validation.ValidationError;

public class LoginActivity extends BaseActivity implements LoginContract.View {

    private ActivityLoginBinding binding;
    private LoginPresenter presenter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityLoginBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        presenter = new LoginPresenter(Injection.provideAuthRepository());
        presenter.attachView(this);

        binding.signInButton.setOnClickListener(v -> signIn());
        binding.passwordInput.setOnEditorActionListener((v, actionId, e) -> {
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                signIn();
                return true;
            }
            return false;
        });
    }

    private void signIn() {
        UiUtils.hideKeyboard(this);
        presenter.onLoginClicked(UiUtils.text(binding.userIdInput), UiUtils.text(binding.passwordInput));
    }

    @Override
    protected void onDestroy() {
        presenter.detachView();
        super.onDestroy();
    }

    @Override
    public void showUserIdError(@Nullable ValidationError error) {
        ErrorMessages.apply(binding.userIdLayout, error);
    }

    @Override
    public void showPasswordError(@Nullable ValidationError error) {
        ErrorMessages.apply(binding.passwordLayout, error);
    }

    @Override
    public void showLoginFailed(String message) {
        binding.passwordInput.setText(null);   // never keep a wrong password on screen
        UiUtils.showErrorDialog(this, message);
    }

    @Override
    public void navigateToHome() {
        startActivity(new Intent(this, MainActivity.class));
        finish();
    }

    @Override
    public void showLoading(boolean show) {
        binding.progress.setVisibility(show ? View.VISIBLE : View.GONE);
        binding.signInButton.setEnabled(!show);
    }
}
