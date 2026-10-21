package com.eswar.finnova.ui.profile.income;

import com.eswar.finnova.base.BasePresenter;
import com.eswar.finnova.data.model.Income;
import com.eswar.finnova.data.model.UserProfile;
import com.eswar.finnova.data.repository.DataCallback;
import com.eswar.finnova.data.repository.ProfileRepository;
import com.eswar.finnova.validation.ProfileValidator;
import com.eswar.finnova.validation.ValidationResult;

public class EditIncomePresenter extends BasePresenter<EditIncomeContract.View>
        implements EditIncomeContract.Presenter {

    private final ProfileRepository repository;
    private final ProfileValidator validator;
    private boolean submitted;

    public EditIncomePresenter(ProfileRepository repository, ProfileValidator validator) {
        this.repository = repository;
        this.validator = validator;
    }

    @Override
    public void start() {
        repository.getProfile(new DataCallback<UserProfile>() {
            @Override
            public void onSuccess(UserProfile profile) {
                if (getView() != null) getView().showIncome(profile.income);
            }

            @Override
            public void onError(Throwable error) {
                if (getView() != null) getView().showServerError(error.getMessage());
            }
        });
    }

    private boolean validate(IncomeForm f, boolean show) {
        ValidationResult status = validator.validateEmploymentStatus(f.employmentStatus);
        ValidationResult income = validator.validateAnnualIncome(f.annualIncome);
        ValidationResult housing = validator.validateHousingPayment(f.monthlyHousing, f.annualIncome);
        getView().showEmploymentError(show ? status.getError() : null);
        getView().showIncomeError(show ? income.getError() : null);
        getView().showHousingError(show ? housing.getError() : null);
        return status.isValid() && income.isValid() && housing.isValid();
    }

    @Override
    public void onInputChanged(IncomeForm form) {
        if (getView() != null) validate(form, submitted);
    }

    @Override
    public void onSaveClicked(IncomeForm form) {
        if (getView() == null) return;
        submitted = true;
        if (!validate(form, true)) return;

        Income income = new Income(form.employmentStatus.trim(),
                ProfileValidator.parseMoney(form.annualIncome),
                ProfileValidator.parseMoney(form.monthlyHousing));
        getView().showLoading(true);
        repository.updateIncome(income, new DataCallback<UserProfile>() {
            @Override
            public void onSuccess(UserProfile profile) {
                if (getView() == null) return;
                getView().showLoading(false);
                getView().showSaved();
            }

            @Override
            public void onError(Throwable error) {
                if (getView() == null) return;
                getView().showLoading(false);
                getView().showServerError(error.getMessage());
            }
        });
    }
}
