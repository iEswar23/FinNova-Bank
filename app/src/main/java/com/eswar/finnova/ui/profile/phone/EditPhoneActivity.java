package com.eswar.finnova.ui.profile.phone;

import android.os.Bundle;
import android.telephony.PhoneNumberFormattingTextWatcher;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.Nullable;

import com.eswar.finnova.R;
import com.eswar.finnova.base.BaseActivity;
import com.eswar.finnova.databinding.ActivityEditPhoneBinding;
import com.eswar.finnova.di.Injection;
import com.eswar.finnova.ui.common.ErrorMessages;
import com.eswar.finnova.ui.common.SimpleTextWatcher;
import com.eswar.finnova.ui.common.UiUtils;
import com.eswar.finnova.validation.ValidationError;

public class EditPhoneActivity extends BaseActivity implements EditPhoneContract.View {

    private ActivityEditPhoneBinding binding;
    private EditPhonePresenter presenter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityEditPhoneBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        setupToolbar(binding.toolbar.getRoot(), R.string.row_mobile_phone, true);

        presenter = new EditPhonePresenter(Injection.provideProfileRepository(), Injection.provideValidator());
        presenter.attachView(this);

        // Formats as the user types: 4695550142 -> (469) 555-0142
        binding.phoneInput.addTextChangedListener(new PhoneNumberFormattingTextWatcher("US"));
        binding.phoneInput.addTextChangedListener(new SimpleTextWatcher(presenter::onPhoneChanged));
        binding.saveButton.setOnClickListener(v -> {
            UiUtils.hideKeyboard(this);
            presenter.onSaveClicked(UiUtils.text(binding.phoneInput));
        });

        presenter.start();
    }

    @Override
    protected void onDestroy() {
        presenter.detachView();
        super.onDestroy();
    }

    @Override
    public void showCurrentPhone(String formattedPhone) {
        binding.currentValue.setText(getString(R.string.current_value, formattedPhone));
    }

    @Override
    public void showPhoneError(@Nullable ValidationError error) {
        ErrorMessages.apply(binding.phoneLayout, error);
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
