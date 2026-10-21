package com.eswar.finnova.ui.main;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.eswar.finnova.R;
import com.eswar.finnova.base.BaseActivity;
import com.eswar.finnova.databinding.ActivityMainBinding;
import com.eswar.finnova.ui.accounts.AccountsFragment;
import com.eswar.finnova.ui.benefits.InfoFragment;
import com.eswar.finnova.ui.more.MoreFragment;

/** Home screen with the bottom navigation: Accounts | Benefits | Offers | More. */
public class MainActivity extends BaseActivity {

    private ActivityMainBinding binding;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        setupToolbar(binding.toolbar.getRoot(), R.string.app_name, false);

        binding.bottomNav.setOnItemSelectedListener(item -> {
            int id = item.getItemId();
            if (id == R.id.nav_accounts) {
                show(new AccountsFragment(), R.string.tab_accounts);
            } else if (id == R.id.nav_benefits) {
                show(InfoFragment.newInstance(R.string.benefits_title, R.string.benefits_body), R.string.tab_benefits);
            } else if (id == R.id.nav_offers) {
                show(InfoFragment.newInstance(R.string.offers_title, R.string.offers_body), R.string.tab_offers);
            } else {
                show(new MoreFragment(), R.string.tab_more);
            }
            return true;
        });

        if (savedInstanceState == null) {
            binding.bottomNav.setSelectedItemId(R.id.nav_accounts);
        }
    }

    private void show(Fragment fragment, int title) {
        if (getSupportActionBar() != null) getSupportActionBar().setTitle(title);
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragmentContainer, fragment)
                .commit();
    }
}
