package com.eswar.finnova.ui.profile.email;

import com.eswar.finnova.base.BasePresenter;
import com.eswar.finnova.data.model.UserProfile;
import com.eswar.finnova.data.repository.DataCallback;
import com.eswar.finnova.data.repository.ProfileRepository;
import com.eswar.finnova.data.repository.ServerException;
import com.eswar.finnova.util.Formatters;
import com.eswar.finnova.validation.ProfileValidator;
import com.eswar.finnova.validation.ValidationResult;

public class EditEmailPresenter extends BasePresenter<EditEmailContract.View>
        implements EditEmailContract.Presenter {

    private final ProfileRepository repository;
    private final ProfileValidator validator;
    private String currentEmail = "";
    private boolean submitted;

    public EditEmailPresenter(ProfileRepository repository, ProfileValidator validator) {
        this.repository = repository;
        this.validator = validator;
    }

    @Override
    public void start() {
        repository.getProfile(new DataCallback<UserProfile>() {
            @Override
            public void onSuccess(UserProfile profile) {
                currentEmail = profile.email;
                if (getView() != null) getView().showCurrentEmail(Formatters.maskEmail(currentEmail));
            }

            @Override
            public void onError(Throwable error) {
                if (getView() != null) getView().showServerError(error.getMessage());
            }
        });
    }

    @Override
    public void onInputChanged(String email, String confirmEmail) {
        if (getView() == null) return;
        if (submitted) {
            validate(email, confirmEmail);
        } else {
            getView().showEmailError(null);
            getView().showConfirmEmailError(null);
        }
    }

    /** Validates both fields, shows/clears both errors, returns true when everything is valid. */
    private boolean validate(String email, String confirmEmail) {
        ValidationResult emailResult = validator.validateEmail(email, currentEmail);
        ValidationResult confirmResult = validator.validateConfirmEmail(email, confirmEmail);
        getView().showEmailError(emailResult.getError());
        getView().showConfirmEmailError(confirmResult.getError());
        return emailResult.isValid() && confirmResult.isValid();
    }

    @Override
    public void onSaveClicked(String email, String confirmEmail) {
        if (getView() == null) return;
        submitted = true;
        if (!validate(email, confirmEmail)) return;

        getView().showLoading(true);
        repository.updateEmail(email.trim(), new DataCallback<UserProfile>() {
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
                if (error instanceof ServerException
                        && ServerException.EMAIL_IN_USE.equals(((ServerException) error).getCode())) {
                    getView().showEmailInUse(error.getMessage());
                } else {
                    getView().showServerError(error.getMessage());
                }
            }
        });
    }
}
