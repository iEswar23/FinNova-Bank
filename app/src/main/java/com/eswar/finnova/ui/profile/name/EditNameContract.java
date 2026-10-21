package com.eswar.finnova.ui.profile.name;

import androidx.annotation.Nullable;

import com.eswar.finnova.base.BaseView;
import com.eswar.finnova.validation.ValidationError;

public interface EditNameContract {

    interface View extends BaseView {
        void showCurrentName(String name);

        /** Pass null to clear the error. */
        void showNameError(@Nullable ValidationError error);

        void showServerError(String message);

        void showSaved();
    }

    interface Presenter {
        void start();

        void onNameChanged(String input);

        void onSaveClicked(String input);
    }
}
