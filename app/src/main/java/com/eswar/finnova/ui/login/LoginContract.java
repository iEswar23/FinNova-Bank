package com.eswar.finnova.ui.login;

import androidx.annotation.Nullable;

import com.eswar.finnova.base.BaseView;
import com.eswar.finnova.validation.ValidationError;

public interface LoginContract {

    interface View extends BaseView {
        void showUserIdError(@Nullable ValidationError error);

        void showPasswordError(@Nullable ValidationError error);

        void showLoginFailed(String message);

        void navigateToHome();
    }

    interface Presenter {
        void onLoginClicked(String userId, String password);
    }
}
