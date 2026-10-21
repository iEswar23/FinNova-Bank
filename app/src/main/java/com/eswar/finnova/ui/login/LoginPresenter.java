package com.eswar.finnova.ui.login;

import com.eswar.finnova.base.BasePresenter;
import com.eswar.finnova.data.repository.AuthRepository;
import com.eswar.finnova.data.repository.DataCallback;
import com.eswar.finnova.validation.ValidationError;

public class LoginPresenter extends BasePresenter<LoginContract.View> implements LoginContract.Presenter {

    private final AuthRepository authRepository;

    public LoginPresenter(AuthRepository authRepository) {
        this.authRepository = authRepository;
    }

    @Override
    public void onLoginClicked(String userId, String password) {
        LoginContract.View view = getView();
        if (view == null) return;

        // On the login screen we only check "required". Detailed rules would leak hints to attackers.
        boolean userIdMissing = userId == null || userId.trim().isEmpty();
        boolean passwordMissing = password == null || password.isEmpty();
        view.showUserIdError(userIdMissing ? ValidationError.USER_ID_REQUIRED : null);
        view.showPasswordError(passwordMissing ? ValidationError.PASSWORD_REQUIRED : null);
        if (userIdMissing || passwordMissing) return;

        view.showLoading(true);
        authRepository.login(userId, password, new DataCallback<Void>() {
            @Override
            public void onSuccess(Void data) {
                if (getView() == null) return;
                getView().showLoading(false);
                getView().navigateToHome();
            }

            @Override
            public void onError(Throwable error) {
                if (getView() == null) return;
                getView().showLoading(false);
                getView().showLoginFailed(error.getMessage());
            }
        });
    }
}
