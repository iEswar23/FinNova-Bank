package com.eswar.finnova.di;

import com.eswar.finnova.data.repository.AccountRepository;
import com.eswar.finnova.data.repository.AuthRepository;
import com.eswar.finnova.data.repository.FakeBankRepository;
import com.eswar.finnova.data.repository.ProfileRepository;
import com.eswar.finnova.util.MainThreadTaskRunner;
import com.eswar.finnova.validation.ProfileValidator;

/**
 * Manual dependency injection (a tiny "service locator").
 * Real projects often use Dagger/Hilt; the idea is the same: screens ask for
 * interfaces, and one place decides which implementation they get.
 */
public final class Injection {

    private static FakeBankRepository bankRepository;
    private static final ProfileValidator VALIDATOR = new ProfileValidator();

    private Injection() {
    }

    private static synchronized FakeBankRepository bank() {
        if (bankRepository == null) {
            bankRepository = new FakeBankRepository(new MainThreadTaskRunner());
        }
        return bankRepository;
    }

    public static ProfileRepository provideProfileRepository() {
        return bank();
    }

    public static AuthRepository provideAuthRepository() {
        return bank();
    }

    public static AccountRepository provideAccountRepository() {
        return bank();
    }

    public static ProfileValidator provideValidator() {
        return VALIDATOR;
    }
}
