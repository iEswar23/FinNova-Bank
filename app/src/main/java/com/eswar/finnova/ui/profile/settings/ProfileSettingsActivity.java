package com.eswar.finnova.ui.profile.settings;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.annotation.StringRes;

import com.eswar.finnova.R;
import com.eswar.finnova.base.BaseActivity;
import com.eswar.finnova.databinding.ActivityProfileSettingsBinding;
import com.eswar.finnova.databinding.ViewSettingsRowBinding;
import com.eswar.finnova.di.Injection;
import com.eswar.finnova.ui.common.UiUtils;
import com.eswar.finnova.ui.profile.address.EditAddressActivity;
import com.eswar.finnova.ui.profile.email.EditEmailActivity;
import com.eswar.finnova.ui.profile.income.EditIncomeActivity;
import com.eswar.finnova.ui.profile.name.EditNameActivity;
import com.eswar.finnova.ui.profile.phone.EditPhoneActivity;
import com.eswar.finnova.ui.settings.ToggleSettingsActivity;
import com.eswar.finnova.ui.settings.comingsoon.ComingSoonActivity;
import com.eswar.finnova.ui.settings.credentials.UpdateCredentialsActivity;

/** The "Profile & Settings" screen (More tab → Profile & Settings). */
public class ProfileSettingsActivity extends BaseActivity implements ProfileSettingsContract.View {

    private ActivityProfileSettingsBinding binding;
    private ProfileSettingsPresenter presenter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityProfileSettingsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        setupToolbar(binding.toolbar.getRoot(), R.string.profile_settings_title, true);

        presenter = new ProfileSettingsPresenter(Injection.provideProfileRepository());
        presenter.attachView(this);

        bindRow(binding.rowPreferredName, R.string.row_preferred_name, ProfileRow.PREFERRED_NAME);
        bindRow(binding.rowAddress, R.string.row_address, ProfileRow.ADDRESS);
        bindRow(binding.rowEmail, R.string.row_email, ProfileRow.EMAIL);
        bindRow(binding.rowMobilePhone, R.string.row_mobile_phone, ProfileRow.MOBILE_PHONE);
        bindRow(binding.rowIncome, R.string.row_income, ProfileRow.INCOME);
        bindRow(binding.rowCredentials, R.string.row_credentials, ProfileRow.CREDENTIALS);
        bindRow(binding.rowLoginOptions, R.string.row_login_options, ProfileRow.LOGIN_OPTIONS);
        bindRow(binding.rowAccountAlerts, R.string.row_account_alerts, ProfileRow.ACCOUNT_ALERTS);
        bindRow(binding.rowLinkAccounts, R.string.row_link_accounts, ProfileRow.LINK_ACCOUNTS);
        bindRow(binding.rowSnapshot, R.string.row_snapshot, ProfileRow.SNAPSHOT);
        bindRow(binding.rowConnectedApps, R.string.row_connected_apps, ProfileRow.CONNECTED_APPS);
    }

    private void bindRow(ViewSettingsRowBinding row, @StringRes int title, ProfileRow target) {
        row.rowTitle.setText(title);
        row.getRoot().setOnClickListener(v -> presenter.onRowClicked(target));
    }

    @Override
    protected void onResume() {
        super.onResume();
        presenter.loadProfile();   // refresh after returning from an edit screen
    }

    @Override
    protected void onDestroy() {
        presenter.detachView();
        super.onDestroy();
    }

    private static void setSubtitle(ViewSettingsRowBinding row, String text) {
        row.rowSubtitle.setText(text);
        row.rowSubtitle.setVisibility(View.VISIBLE);
    }

    @Override
    public void showProfileSummary(String preferredName, String address, String maskedEmail,
                                   String maskedPhone, String income) {
        setSubtitle(binding.rowPreferredName, preferredName);
        setSubtitle(binding.rowAddress, address);
        setSubtitle(binding.rowEmail, maskedEmail);
        setSubtitle(binding.rowMobilePhone, maskedPhone);
        setSubtitle(binding.rowIncome, income);
    }

    @Override
    public void showLoading(boolean show) {
        binding.progress.setVisibility(show ? View.VISIBLE : View.GONE);
    }

    @Override
    public void showLoadError(String message) {
        UiUtils.showErrorDialog(this, message);
    }

    @Override
    public void navigateTo(ProfileRow row) {
        Intent intent;
        switch (row) {
            case PREFERRED_NAME: intent = new Intent(this, EditNameActivity.class); break;
            case ADDRESS: intent = new Intent(this, EditAddressActivity.class); break;
            case EMAIL: intent = new Intent(this, EditEmailActivity.class); break;
            case MOBILE_PHONE: intent = new Intent(this, EditPhoneActivity.class); break;
            case INCOME: intent = new Intent(this, EditIncomeActivity.class); break;
            case CREDENTIALS: intent = new Intent(this, UpdateCredentialsActivity.class); break;
            case LOGIN_OPTIONS:
                intent = ToggleSettingsActivity.newIntent(this, ToggleSettingsActivity.MODE_LOGIN_OPTIONS); break;
            case ACCOUNT_ALERTS:
                intent = ToggleSettingsActivity.newIntent(this, ToggleSettingsActivity.MODE_ACCOUNT_ALERTS); break;
            case LINK_ACCOUNTS: intent = ComingSoonActivity.newIntent(this, R.string.row_link_accounts); break;
            case SNAPSHOT: intent = ComingSoonActivity.newIntent(this, R.string.row_snapshot); break;
            default: intent = ComingSoonActivity.newIntent(this, R.string.row_connected_apps); break;
        }
        startActivity(intent);
    }
}
