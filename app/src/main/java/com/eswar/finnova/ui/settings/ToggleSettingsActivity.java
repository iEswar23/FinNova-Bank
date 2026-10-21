package com.eswar.finnova.ui.settings;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.annotation.StringRes;

import com.eswar.finnova.R;
import com.eswar.finnova.base.BaseActivity;
import com.eswar.finnova.databinding.ActivityTogglesBinding;
import com.google.android.material.materialswitch.MaterialSwitch;

/**
 * "Login options" and "Account alerts" – simple on/off settings saved in SharedPreferences.
 * Practice task: convert this screen to MVP (Contract + Presenter + SettingsRepository) and unit test it.
 */
public class ToggleSettingsActivity extends BaseActivity {

    public static final int MODE_LOGIN_OPTIONS = 1;
    public static final int MODE_ACCOUNT_ALERTS = 2;
    private static final String EXTRA_MODE = "extra_mode";
    private static final String PREFS = "finnova_settings";

    public static Intent newIntent(Context context, int mode) {
        return new Intent(context, ToggleSettingsActivity.class).putExtra(EXTRA_MODE, mode);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityTogglesBinding binding = ActivityTogglesBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        boolean loginOptions = getIntent().getIntExtra(EXTRA_MODE, MODE_LOGIN_OPTIONS) == MODE_LOGIN_OPTIONS;
        setupToolbar(binding.toolbar.getRoot(),
                loginOptions ? R.string.row_login_options : R.string.row_account_alerts, true);

        SharedPreferences prefs = getSharedPreferences(PREFS, MODE_PRIVATE);
        if (loginOptions) {
            addToggle(binding.toggleContainer, prefs, "biometric_login", R.string.biometric_login, false);
            addToggle(binding.toggleContainer, prefs, "remember_user_id", R.string.remember_user_id, true);
        } else {
            addToggle(binding.toggleContainer, prefs, "alert_low_balance", R.string.alert_low_balance, true);
            addToggle(binding.toggleContainer, prefs, "alert_large_purchase", R.string.alert_large_purchase, true);
            addToggle(binding.toggleContainer, prefs, "alert_payment_due", R.string.alert_payment_due, true);
            addToggle(binding.toggleContainer, prefs, "alert_deposit", R.string.alert_deposit, false);
        }
    }

    private void addToggle(ViewGroup parent, SharedPreferences prefs, String key, @StringRes int label, boolean def) {
        MaterialSwitch toggle = new MaterialSwitch(this);
        toggle.setText(label);
        toggle.setTextSize(16);
        toggle.setMinHeight(getResources().getDimensionPixelSize(R.dimen.row_height));
        toggle.setChecked(prefs.getBoolean(key, def));
        toggle.setOnCheckedChangeListener((b, checked) -> prefs.edit().putBoolean(key, checked).apply());
        parent.addView(toggle, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT));
    }
}
