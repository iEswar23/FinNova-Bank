package com.eswar.finnova.ui.settings.credentials;

import androidx.annotation.Nullable;

import com.eswar.finnova.base.BaseView;
import com.eswar.finnova.validation.ValidationError;

public interface UpdateCredentialsContract {

    interface View extends BaseView {
        void showCurrentUserId(String userId);

        void showCurrentPasswordError(@Nullable ValidationError error);

        void showUserIdError(@Nullable ValidationError error);

        void showNewPasswordError(@Nullable ValidationError error);

        void showConfirmPasswordError(@Nullable ValidationError error);

        /** Live checklist under the password field. */
        void updatePasswordChecklist(boolean length, boolean upper, boolean lower, boolean digit, boolean special);

        void showWrongCurrentPassword(String message);

        void showUserIdTaken(String message);

        void showServerError(String message);

        void showSaved();
    }

    interface Presenter {
        void start();

        void onNewPasswordChanged(String password);

        void onInputChanged(CredentialsForm form);

        void onSaveClicked(CredentialsForm form);
    }
}
