package com.eswar.finnova.ui.profile.income;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.Nullable;

import com.eswar.finnova.R;
import com.eswar.finnova.base.BaseActivity;
import com.eswar.finnova.data.model.Income;
import com.eswar.finnova.databinding.ActivityEditIncomeBinding;
import com.eswar.finnova.di.Injection;
import com.eswar.finnova.ui.common.ErrorMessages;
import com.eswar.finnova.ui.common.SimpleTextWatcher;
import com.eswar.finnova.ui.common.UiUtils;
import com.eswar.finnova.validation.ValidationError;

public class EditIncomeActivity extends BaseActivity implements EditIncomeContract.View {

    private ActivityEditIncomeBinding binding;
    private EditIncomePresenter presenter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityEditIncomeBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        setupToolbar(binding.toolbar.getRoot(), R.string.row_income, true);

        binding.employmentInput.setSimpleItems(getResources().getStringArray(R.array.employment_statuses));

        presenter = new EditIncomePresenter(Injection.provideProfileRepository(), Injection.provideValidator());
        presenter.attachView(this);

        SimpleTextWatcher watcher = new SimpleTextWatcher(t -> presenter.onInputChanged(readForm()));
        binding.employmentInput.addTextChangedListener(watcher);
        binding.annualIncomeInput.addTextChangedListener(watcher);
        binding.housingInput.addTextChangedListener(watcher);
        binding.saveButton.setOnClickListener(v -> {
            UiUtils.hideKeyboard(this);
            presenter.onSaveClicked(readForm());
        });

        presenter.start();
    }

    private IncomeForm readForm() {
        return new IncomeForm(
                UiUtils.text(binding.employmentInput),
                UiUtils.text(binding.annualIncomeInput),
                UiUtils.text(binding.housingInput));
    }

    @Override
    protected void onDestroy() {
        presenter.detachView();
        super.onDestroy();
    }

    @Override
    public void showIncome(Income income) {
        binding.employmentInput.setText(income.employmentStatus, false);
        binding.annualIncomeInput.setText(String.valueOf(income.annualIncome));
        binding.housingInput.setText(String.valueOf(income.monthlyHousingPayment));
    }

    @Override
    public void showEmploymentError(@Nullable ValidationError error) {
        ErrorMessages.apply(binding.employmentLayout, error);
    }

    @Override
    public void showIncomeError(@Nullable ValidationError error) {
        ErrorMessages.apply(binding.annualIncomeLayout, error);
    }

    @Override
    public void showHousingError(@Nullable ValidationError error) {
        ErrorMessages.apply(binding.housingLayout, error);
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
