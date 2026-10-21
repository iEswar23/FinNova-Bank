package com.eswar.finnova.ui.settings.comingsoon;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.annotation.StringRes;

import com.eswar.finnova.R;
import com.eswar.finnova.base.BaseActivity;
import com.eswar.finnova.databinding.ActivityComingSoonBinding;

/** Placeholder for screens left as practice tasks (Link accounts, Snapshot, Connected Apps, Help). */
public class ComingSoonActivity extends BaseActivity {

    private static final String EXTRA_TITLE = "extra_title";

    public static Intent newIntent(Context context, @StringRes int title) {
        return new Intent(context, ComingSoonActivity.class).putExtra(EXTRA_TITLE, title);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityComingSoonBinding binding = ActivityComingSoonBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        setupToolbar(binding.toolbar.getRoot(), getIntent().getIntExtra(EXTRA_TITLE, R.string.app_name), true);
    }
}
