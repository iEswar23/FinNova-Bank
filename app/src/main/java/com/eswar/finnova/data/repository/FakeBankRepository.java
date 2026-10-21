package com.eswar.finnova.data.repository;

import com.eswar.finnova.data.model.Account;
import com.eswar.finnova.data.model.Address;
import com.eswar.finnova.data.model.Income;
import com.eswar.finnova.data.model.UserProfile;
import com.eswar.finnova.util.TaskRunner;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * In-memory "backend" so the app runs end-to-end without a server.
 *
 * It behaves like a real API: responses arrive after a delay, and some inputs
 * trigger SERVER-side validation errors so you can practise handling both
 * client-side and server-side errors:
 *   - email "taken@finnova.com"   -> EMAIL_IN_USE
 *   - user ID "takenuser"         -> USER_ID_TAKEN
 *   - wrong current password      -> WRONG_PASSWORD
 *
 * Demo login: user ID "demouser", password "Finnova@2026".
 */
public class FakeBankRepository implements ProfileRepository, AuthRepository, AccountRepository {

    static final long NETWORK_DELAY_MS = 700;

    private final TaskRunner runner;
    private UserProfile profile;
    private String password = "Finnova@2026";
    private final List<Account> accounts;

    public FakeBankRepository(TaskRunner runner) {
        this.runner = runner;
        this.profile = new UserProfile(
                "demouser",
                "Alex Johnson",
                "Alex",
                "alex.johnson@example.com",
                "4695550142",
                new Address("1200 Lakeside Blvd", "Apt 310", "Irving", "TX", "75038"),
                new Income("Employed full-time", 95_000, 1_850));
        this.accounts = Collections.unmodifiableList(new ArrayList<>(Arrays.asList(
                new Account("chk", "FinNova Everyday Checking", "4821", Account.Type.CHECKING, 482_315),
                new Account("sav", "FinNova High-Yield Savings", "7730", Account.Type.SAVINGS, 1_250_000),
                new Account("cc", "FinNova Rewards Card", "1094", Account.Type.CREDIT_CARD, -63_472))));
    }

    // ---------------------------------------------------------------- Auth

    @Override
    public void login(String userId, String pwd, DataCallback<Void> callback) {
        runner.run(() -> {
            if (profile.userId.equalsIgnoreCase(userId.trim()) && password.equals(pwd)) {
                callback.onSuccess(null);
            } else {
                callback.onError(new ServerException(ServerException.INVALID_CREDENTIALS,
                        "The User ID or password you entered is incorrect."));
            }
        }, NETWORK_DELAY_MS);
    }

    @Override
    public void updateCredentials(String currentPassword, String newUserId, String newPassword,
                                  DataCallback<Void> callback) {
        runner.run(() -> {
            if (!password.equals(currentPassword)) {
                callback.onError(new ServerException(ServerException.WRONG_PASSWORD,
                        "Your current password is incorrect."));
            } else if ("takenuser".equalsIgnoreCase(newUserId.trim())) {
                callback.onError(new ServerException(ServerException.USER_ID_TAKEN,
                        "That User ID is not available. Try another."));
            } else {
                profile = profile.withUserId(newUserId.trim());
                password = newPassword;
                callback.onSuccess(null);
            }
        }, NETWORK_DELAY_MS);
    }

    @Override
    public void logout() {
        // Nothing to clear in the fake. A real app clears tokens here.
    }

    // ---------------------------------------------------------------- Profile

    @Override
    public void getProfile(DataCallback<UserProfile> callback) {
        runner.run(() -> callback.onSuccess(profile), NETWORK_DELAY_MS / 2);
    }

    @Override
    public void updatePreferredFirstName(String name, DataCallback<UserProfile> callback) {
        runner.run(() -> {
            profile = profile.withPreferredFirstName(name);
            callback.onSuccess(profile);
        }, NETWORK_DELAY_MS);
    }

    @Override
    public void updateEmail(String email, DataCallback<UserProfile> callback) {
        runner.run(() -> {
            if ("taken@finnova.com".equalsIgnoreCase(email)) {
                callback.onError(new ServerException(ServerException.EMAIL_IN_USE,
                        "This email is already linked to another profile."));
                return;
            }
            profile = profile.withEmail(email);
            callback.onSuccess(profile);
        }, NETWORK_DELAY_MS);
    }

    @Override
    public void updateMobilePhone(String digits, DataCallback<UserProfile> callback) {
        runner.run(() -> {
            profile = profile.withMobilePhone(digits);
            callback.onSuccess(profile);
        }, NETWORK_DELAY_MS);
    }

    @Override
    public void updateAddress(Address address, DataCallback<UserProfile> callback) {
        runner.run(() -> {
            profile = profile.withAddress(address);
            callback.onSuccess(profile);
        }, NETWORK_DELAY_MS);
    }

    @Override
    public void updateIncome(Income income, DataCallback<UserProfile> callback) {
        runner.run(() -> {
            profile = profile.withIncome(income);
            callback.onSuccess(profile);
        }, NETWORK_DELAY_MS);
    }

    // ---------------------------------------------------------------- Accounts

    @Override
    public void getAccounts(DataCallback<List<Account>> callback) {
        runner.run(() -> callback.onSuccess(accounts), NETWORK_DELAY_MS);
    }
}
