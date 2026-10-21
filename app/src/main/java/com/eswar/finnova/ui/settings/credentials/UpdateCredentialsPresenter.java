package com.eswar.finnova.ui.settings.credentials;

import com.eswar.finnova.base.BasePresenter;
import com.eswar.finnova.data.model.UserProfile;
import com.eswar.finnova.data.repository.AuthRepository;
import com.eswar.finnova.data.repository.DataCallback;
import com.eswar.finnova.data.repository.ProfileRepository;
import com.eswar.finnova.data.repository.ServerException;
import com.eswar.finnova.validation.ProfileValidator;
import com.eswar.finnova.validation.ValidationResult;

public class UpdateCredentialsPresenter extends BasePresenter<UpdateCredentialsContract.View>
        implements UpdateCredentialsContract.Presenter {

    private final ProfileRepository profileRepository;
    private final AuthRepository authRepository;
    private final ProfileValidator validator;
    private boolean submitted;

    public UpdateCredentialsPresenter(ProfileRepository profileRepository, AuthRepository authRepository,
                                      ProfileValidator validator) {
        this.profileRepository = profileRepository;
        this.authRepository = authRepository;
        this.validator = validator;
    }

    @Override
    public void start() {
        profileRepository.getProfile(new DataCallback<UserProfile>() {
            @Override
            public void onSuccess(UserProfile profile) {
                if (getView() != null) getView().showCurrentUserId(profile.userId);
            }

            @Override
            public void onError(Throwable error) {
                if (getView() != null) getView().showServerError(error.getMessage());
            }
        });
        onNewPasswordChanged("");
    }

    @Override
    public void onNewPasswordChanged(String p) {
        if (getView() == null) return;
        getView().updatePasswordChecklist(
                ProfileValidator.hasValidPasswordLength(p),
                ProfileValidator.hasUppercase(p),
                ProfileValidator.hasLowercase(p),
                ProfileValidator.hasDigit(p),
                ProfileValidator.hasSpecial(p));
    }

    private boolean validate(CredentialsForm f, boolean show) {
        ValidationResult current = validator.validateCurrentPassword(f.currentPassword);
        ValidationResult userId = validator.validateUserId(f.newUserId);
        ValidationResult pwd = validator.validateNewPassword(f.newPassword, f.newUserId, f.currentPassword);
        ValidationResult confirm = validator.validateConfirmPassword(f.newPassword, f.confirmPassword);
        getView().showCurrentPasswordError(show ? current.getError() : null);
        getView().showUserIdError(show ? userId.getError() : null);
        getView().showNewPasswordError(show ? pwd.getError() : null);
        getView().showConfirmPasswordError(show ? confirm.getError() : null);
        return current.isValid() && userId.isValid() && pwd.isValid() && confirm.isValid();
    }

    @Override
    public void onInputChanged(CredentialsForm form) {
        if (getView() != null) validate(form, submitted);
    }

    @Override
    public void onSaveClicked(CredentialsForm form) {
        if (getView() == null) return;
        submitted = true;
        if (!validate(form, true)) return;

        getView().showLoading(true);
        authRepository.updateCredentials(form.currentPassword, form.newUserId.trim(), form.newPassword,
                new DataCallback<Void>() {
                    @Override
                    public void onSuccess(Void data) {
                        if (getView() == null) return;
                        getView().showLoading(false);
                        getView().showSaved();
                    }

                    @Override
                    public void onError(Throwable error) {
                        if (getView() == null) return;
                        getView().showLoading(false);
                        String code = error instanceof ServerException ? ((ServerException) error).getCode() : "";
                        if (ServerException.WRONG_PASSWORD.equals(code)) {
                            getView().showWrongCurrentPassword(error.getMessage());
                        } else if (ServerException.USER_ID_TAKEN.equals(code)) {
                            getView().showUserIdTaken(error.getMessage());
                        } else {
                            getView().showServerError(error.getMessage());
                        }
                    }
                });
    }
}
