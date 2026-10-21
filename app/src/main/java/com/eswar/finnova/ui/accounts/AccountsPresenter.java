package com.eswar.finnova.ui.accounts;

import com.eswar.finnova.base.BasePresenter;
import com.eswar.finnova.data.model.Account;
import com.eswar.finnova.data.model.UserProfile;
import com.eswar.finnova.data.repository.AccountRepository;
import com.eswar.finnova.data.repository.DataCallback;
import com.eswar.finnova.data.repository.ProfileRepository;

import java.util.List;

public class AccountsPresenter extends BasePresenter<AccountsContract.View> implements AccountsContract.Presenter {

    private final AccountRepository accountRepository;
    private final ProfileRepository profileRepository;

    public AccountsPresenter(AccountRepository accountRepository, ProfileRepository profileRepository) {
        this.accountRepository = accountRepository;
        this.profileRepository = profileRepository;
    }

    @Override
    public void load() {
        if (getView() == null) return;
        getView().showLoading(true);
        profileRepository.getProfile(new DataCallback<UserProfile>() {
            @Override
            public void onSuccess(UserProfile profile) {
                if (getView() != null) getView().showGreeting(profile.preferredFirstName);
            }

            @Override
            public void onError(Throwable error) {
                // Greeting is optional; ignore.
            }
        });
        accountRepository.getAccounts(new DataCallback<List<Account>>() {
            @Override
            public void onSuccess(List<Account> accounts) {
                if (getView() == null) return;
                getView().showLoading(false);
                getView().showAccounts(accounts);
            }

            @Override
            public void onError(Throwable error) {
                if (getView() == null) return;
                getView().showLoading(false);
                getView().showError(error.getMessage());
            }
        });
    }
}
