package com.eswar.finnova.ui.profile.phone;

import androidx.annotation.Nullable;

import com.eswar.finnova.base.BaseView;
import com.eswar.finnova.validation.ValidationError;

public interface EditPhoneContract {

    interface View extends BaseView {
        void showCurrentPhone(String formattedPhone);

        void showPhoneError(@Nullable ValidationError error);

        void showServerError(String message);

        void showSaved();
    }

    interface Presenter {
        void start();

        void onPhoneChanged(String input);

        void onSaveClicked(String input);
    }
}
