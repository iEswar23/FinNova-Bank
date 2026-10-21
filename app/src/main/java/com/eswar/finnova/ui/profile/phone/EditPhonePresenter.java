package com.eswar.finnova.ui.profile.phone;

import com.eswar.finnova.base.BasePresenter;
import com.eswar.finnova.data.model.UserProfile;
import com.eswar.finnova.data.repository.DataCallback;
import com.eswar.finnova.data.repository.ProfileRepository;
import com.eswar.finnova.util.Formatters;
import com.eswar.finnova.validation.ProfileValidator;
import com.eswar.finnova.validation.ValidationResult;

public class EditPhonePresenter extends BasePresenter<EditPhoneContract.View>
        implements EditPhoneContract.Presenter {

    private final ProfileRepository repository;
    private final ProfileValidator validator;
    private String currentDigits = "";
    private boolean submitted;

    public EditPhonePresenter(ProfileRepository repository, ProfileValidator validator) {
        this.repository = repository;
        this.validator = validator;
    }

    @Override
    public void start() {
        repository.getProfile(new DataCallback<UserProfile>() {
            @Override
            public void onSuccess(UserProfile profile) {
                currentDigits = profile.mobilePhone;
                if (getView() != null) getView().showCurrentPhone(Formatters.maskPhone(currentDigits));
            }

            @Override
            public void onError(Throwable error) {
                if (getView() != null) getView().showServerError(error.getMessage());
            }
        });
    }

    @Override
    public void onPhoneChanged(String input) {
        if (getView() == null) return;
        getView().showPhoneError(submitted ? validator.validateMobilePhone(input, currentDigits).getError() : null);
    }

    @Override
    public void onSaveClicked(String input) {
        if (getView() == null) return;
        submitted = true;
        ValidationResult result = validator.validateMobilePhone(input, currentDigits);
        getView().showPhoneError(result.getError());
        if (!result.isValid()) return;

        getView().showLoading(true);
        // Always send the normalized form (10 digits) to the backend, never the formatted text.
        repository.updateMobilePhone(ProfileValidator.normalizePhone(input), new DataCallback<UserProfile>() {
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
