package com.eswar.finnova.ui.profile.name;

import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Toast;

import androidx.annotation.Nullable;

import com.eswar.finnova.R;
import com.eswar.finnova.base.BaseActivity;
import com.eswar.finnova.databinding.ActivityEditNameBinding;
import com.eswar.finnova.di.Injection;
import com.eswar.finnova.ui.common.ErrorMessages;
import com.eswar.finnova.ui.common.SimpleTextWatcher;
import com.eswar.finnova.ui.common.UiUtils;
import com.eswar.finnova.validation.ValidationError;

/**
 * VIEW layer for "Preferred first name". It only:
 *   1. forwards user events to the Presenter, and
 *   2. renders whatever the Presenter tells it to.
 * No validation logic lives here — that's in ProfileValidator, called by the Presenter.
 */
public class EditNameActivity extends BaseActivity implements EditNameContract.View {

    private ActivityEditNameBinding binding;
    private EditNamePresenter presenter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityEditNameBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        setupToolbar(binding.toolbar.getRoot(), R.string.row_preferred_name, true);

        presenter = new EditNamePresenter(Injection.provideProfileRepository(), Injection.provideValidator());
        presenter.attachView(this);

        binding.nameInput.addTextChangedListener(new SimpleTextWatcher(presenter::onNameChanged));
        binding.nameInput.setOnEditorActionListener((v, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                save();
                return true;
            }
            return false;
        });
        binding.saveButton.setOnClickListener(v -> save());

        presenter.start();
    }

    private void save() {
        UiUtils.hideKeyboard(this);
        presenter.onSaveClicked(UiUtils.text(binding.nameInput));
    }

    @Override
    protected void onDestroy() {
        presenter.detachView();   // avoid leaking the Activity if a callback arrives later
        super.onDestroy();
    }

    // ------------------------------------------------ EditNameContract.View

    @Override
    public void showCurrentName(String name) {
        binding.currentValue.setText(getString(R.string.current_value, name));
        binding.nameInput.setText(name);
        binding.nameInput.setSelection(name.length());
    }

    @Override
    public void showNameError(@Nullable ValidationError error) {
        ErrorMessages.apply(binding.nameLayout, error);
    }

    @Override
    public void showLoading(boolean show) {
        binding.progress.setVisibility(show ? View.VISIBLE : View.GONE);
        binding.saveButton.setEnabled(!show);
        binding.nameInput.setEnabled(!show);
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
