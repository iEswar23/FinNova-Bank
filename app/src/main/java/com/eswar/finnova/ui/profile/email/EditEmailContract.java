package com.eswar.finnova.ui.profile.email;

import androidx.annotation.Nullable;

import com.eswar.finnova.base.BaseView;
import com.eswar.finnova.validation.ValidationError;

public interface EditEmailContract {

    interface View extends BaseView {
        void showCurrentEmail(String maskedEmail);

        void showEmailError(@Nullable ValidationError error);

        void showConfirmEmailError(@Nullable ValidationError error);

        /** Server-side validation: email belongs to another customer. */
        void showEmailInUse(String message);

        void showServerError(String message);

        void showSaved();
    }

    interface Presenter {
        void start();

        void onInputChanged(String email, String confirmEmail);

        void onSaveClicked(String email, String confirmEmail);
    }
}
