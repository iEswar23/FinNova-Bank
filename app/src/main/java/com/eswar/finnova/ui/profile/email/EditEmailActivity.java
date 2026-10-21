package com.eswar.finnova.ui.profile.email;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.Nullable;

import com.eswar.finnova.R;
import com.eswar.finnova.base.BaseActivity;
import com.eswar.finnova.databinding.ActivityEditEmailBinding;
import com.eswar.finnova.di.Injection;
import com.eswar.finnova.ui.common.ErrorMessages;
import com.eswar.finnova.ui.common.SimpleTextWatcher;
import com.eswar.finnova.ui.common.UiUtils;
import com.eswar.finnova.validation.ValidationError;

public class EditEmailActivity extends BaseActivity implements EditEmailContract.View {

    private ActivityEditEmailBinding binding;
    private EditEmailPresenter presenter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityEditEmailBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        setupToolbar(binding.toolbar.getRoot(), R.string.row_email, true);

        presenter = new EditEmailPresenter(Injection.provideProfileRepository(), Injection.provideValidator());
        presenter.attachView(this);

        SimpleTextWatcher watcher = new SimpleTextWatcher(text ->
                presenter.onInputChanged(UiUtils.text(binding.emailInput), UiUtils.text(binding.confirmEmailInput)));
        binding.emailInput.addTextChangedListener(watcher);
        binding.confirmEmailInput.addTextChangedListener(watcher);
        binding.saveButton.setOnClickListener(v -> {
            UiUtils.hideKeyboard(this);
            presenter.onSaveClicked(UiUtils.text(binding.emailInput), UiUtils.text(binding.confirmEmailInput));
        });

        presenter.start();
    }

    @Override
    protected void onDestroy() {
        presenter.detachView();
        super.onDestroy();
    }

    @Override
    public void showCurrentEmail(String maskedEmail) {
        binding.currentValue.setText(getString(R.string.current_value, maskedEmail));
    }

    @Override
    public void showEmailError(@Nullable ValidationError error) {
        ErrorMessages.apply(binding.emailLayout, error);
    }

    @Override
    public void showConfirmEmailError(@Nullable ValidationError error) {
        ErrorMessages.apply(binding.confirmEmailLayout, error);
    }

    @Override
    public void showEmailInUse(String message) {
        binding.emailLayout.setError(message);
        binding.emailInput.requestFocus();
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
