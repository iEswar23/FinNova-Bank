package com.eswar.finnova.ui.settings.credentials;

import android.content.res.ColorStateList;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;

import com.eswar.finnova.R;
import com.eswar.finnova.base.BaseActivity;
import com.eswar.finnova.databinding.ActivityUpdateCredentialsBinding;
import com.eswar.finnova.di.Injection;
import com.eswar.finnova.ui.common.ErrorMessages;
import com.eswar.finnova.ui.common.SimpleTextWatcher;
import com.eswar.finnova.ui.common.UiUtils;
import com.eswar.finnova.validation.ValidationError;

public class UpdateCredentialsActivity extends BaseActivity implements UpdateCredentialsContract.View {

    private ActivityUpdateCredentialsBinding binding;
    private UpdateCredentialsPresenter presenter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityUpdateCredentialsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        setupToolbar(binding.toolbar.getRoot(), R.string.row_credentials, true);

        presenter = new UpdateCredentialsPresenter(Injection.provideProfileRepository(),
                Injection.provideAuthRepository(), Injection.provideValidator());
        presenter.attachView(this);

        SimpleTextWatcher watcher = new SimpleTextWatcher(t -> presenter.onInputChanged(readForm()));
        binding.currentPasswordInput.addTextChangedListener(watcher);
        binding.newUserIdInput.addTextChangedListener(watcher);
        binding.newPasswordInput.addTextChangedListener(watcher);
        binding.confirmPasswordInput.addTextChangedListener(watcher);
        binding.newPasswordInput.addTextChangedListener(new SimpleTextWatcher(presenter::onNewPasswordChanged));
        binding.saveButton.setOnClickListener(v -> {
            UiUtils.hideKeyboard(this);
            presenter.onSaveClicked(readForm());
        });

        presenter.start();
    }

    private CredentialsForm readForm() {
        return new CredentialsForm(
                UiUtils.text(binding.currentPasswordInput),
                UiUtils.text(binding.newUserIdInput),
                UiUtils.text(binding.newPasswordInput),
                UiUtils.text(binding.confirmPasswordInput));
    }

    @Override
    protected void onDestroy() {
        presenter.detachView();
        super.onDestroy();
    }

    @Override
    public void showCurrentUserId(String userId) {
        binding.currentValue.setText(getString(R.string.current_user_id, userId));
        binding.newUserIdInput.setText(userId);
    }

    @Override
    public void showCurrentPasswordError(@Nullable ValidationError error) {
        ErrorMessages.apply(binding.currentPasswordLayout, error);
    }

    @Override
    public void showUserIdError(@Nullable ValidationError error) {
        ErrorMessages.apply(binding.newUserIdLayout, error);
    }

    @Override
    public void showNewPasswordError(@Nullable ValidationError error) {
        ErrorMessages.apply(binding.newPasswordLayout, error);
    }

    @Override
    public void showConfirmPasswordError(@Nullable ValidationError error) {
        ErrorMessages.apply(binding.confirmPasswordLayout, error);
    }

    @Override
    public void updatePasswordChecklist(boolean length, boolean upper, boolean lower, boolean digit, boolean special) {
        setRule(binding.ruleLength, length);
        setRule(binding.ruleUpper, upper);
        setRule(binding.ruleLower, lower);
        setRule(binding.ruleDigit, digit);
        setRule(binding.ruleSpecial, special);
    }

    private void setRule(TextView view, boolean met) {
        int color = ContextCompat.getColor(this, met ? R.color.success : R.color.text_secondary);
        view.setCompoundDrawablesRelativeWithIntrinsicBounds(
                met ? R.drawable.ic_check_circle : R.drawable.ic_circle_outline, 0, 0, 0);
        view.setCompoundDrawableTintList(ColorStateList.valueOf(color));
        view.setTextColor(color);
    }

    @Override
    public void showWrongCurrentPassword(String message) {
        binding.currentPasswordLayout.setError(message);
        binding.currentPasswordInput.requestFocus();
    }

    @Override
    public void showUserIdTaken(String message) {
        binding.newUserIdLayout.setError(message);
        binding.newUserIdInput.requestFocus();
    }

    @Override
    public void showLoading(boolean show) {
        binding.progress.setVisibility(show ? View.VISIBLE : View.GONE);
        binding.saveButton.setEnabled(!show);
    }

    @Override
    public void showServerError(String message) {
        UiUtils.showErrorDialog(this, message);
    }

    @Override
    public void showSaved() {
        Toast.makeText(this, R.string.saved_successfully, Toast.LENGTH_SHORT).show();
        setResult(RESULT_OK);
        finish();
    }
}
