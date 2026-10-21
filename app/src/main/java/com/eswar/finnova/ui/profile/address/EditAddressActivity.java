package com.eswar.finnova.ui.profile.address;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.Nullable;

import com.eswar.finnova.R;
import com.eswar.finnova.base.BaseActivity;
import com.eswar.finnova.data.model.Address;
import com.eswar.finnova.databinding.ActivityEditAddressBinding;
import com.eswar.finnova.di.Injection;
import com.eswar.finnova.ui.common.ErrorMessages;
import com.eswar.finnova.ui.common.SimpleTextWatcher;
import com.eswar.finnova.ui.common.UiUtils;
import com.eswar.finnova.validation.ValidationError;
import com.google.android.material.textfield.TextInputLayout;

public class EditAddressActivity extends BaseActivity implements EditAddressContract.View {

    private ActivityEditAddressBinding binding;
    private EditAddressPresenter presenter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityEditAddressBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        setupToolbar(binding.toolbar.getRoot(), R.string.row_address, true);

        binding.stateInput.setSimpleItems(getResources().getStringArray(R.array.us_states));

        presenter = new EditAddressPresenter(Injection.provideProfileRepository(), Injection.provideValidator());
        presenter.attachView(this);

        SimpleTextWatcher watcher = new SimpleTextWatcher(t -> presenter.onInputChanged(readForm()));
        binding.line1Input.addTextChangedListener(watcher);
        binding.line2Input.addTextChangedListener(watcher);
        binding.cityInput.addTextChangedListener(watcher);
        binding.stateInput.addTextChangedListener(watcher);
        binding.zipInput.addTextChangedListener(watcher);
        binding.saveButton.setOnClickListener(v -> {
            UiUtils.hideKeyboard(this);
            presenter.onSaveClicked(readForm());
        });

        presenter.start();
    }

    private AddressForm readForm() {
        return new AddressForm(
                UiUtils.text(binding.line1Input),
                UiUtils.text(binding.line2Input),
                UiUtils.text(binding.cityInput),
                UiUtils.text(binding.stateInput),
                UiUtils.text(binding.zipInput));
    }

    @Override
    protected void onDestroy() {
        presenter.detachView();
        super.onDestroy();
    }

    private TextInputLayout layoutFor(EditAddressContract.Field field) {
        switch (field) {
            case LINE1: return binding.line1Layout;
            case LINE2: return binding.line2Layout;
            case CITY: return binding.cityLayout;
            case STATE: return binding.stateLayout;
            default: return binding.zipLayout;
        }
    }

    @Override
    public void showAddress(Address a) {
        binding.line1Input.setText(a.line1);
        binding.line2Input.setText(a.line2);
        binding.cityInput.setText(a.city);
        binding.stateInput.setText(a.state, false);   // false = don't open/filter the dropdown
        binding.zipInput.setText(a.zip);
    }

    @Override
    public void showFieldError(EditAddressContract.Field field, @Nullable ValidationError error) {
        ErrorMessages.apply(layoutFor(field), error);
    }

    @Override
    public void focusField(EditAddressContract.Field field) {
        TextInputLayout layout = layoutFor(field);
        // requestFocus() also makes the NestedScrollView scroll the field into view.
        if (layout.getEditText() != null) layout.getEditText().requestFocus();
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
