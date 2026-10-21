package com.eswar.finnova.ui.accounts;

import com.eswar.finnova.base.BaseView;
import com.eswar.finnova.data.model.Account;

import java.util.List;

public interface AccountsContract {

    interface View extends BaseView {
        void showGreeting(String firstName);

        void showAccounts(List<Account> accounts);

        void showError(String message);
    }

    interface Presenter {
        void load();
    }
}
