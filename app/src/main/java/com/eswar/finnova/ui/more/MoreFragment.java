package com.eswar.finnova.ui.more;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.eswar.finnova.R;
import com.eswar.finnova.databinding.FragmentMoreBinding;
import com.eswar.finnova.di.Injection;
import com.eswar.finnova.ui.login.LoginActivity;
import com.eswar.finnova.ui.profile.settings.ProfileSettingsActivity;
import com.eswar.finnova.ui.settings.comingsoon.ComingSoonActivity;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;

public class MoreFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        FragmentMoreBinding b = FragmentMoreBinding.inflate(inflater, container, false);

        b.rowProfileSettings.rowTitle.setText(R.string.more_profile_settings);
        b.rowProfileSettings.getRoot().setOnClickListener(v ->
                startActivity(new Intent(requireContext(), ProfileSettingsActivity.class)));

        b.rowHelp.rowTitle.setText(R.string.more_help);
        b.rowHelp.getRoot().setOnClickListener(v ->
                startActivity(ComingSoonActivity.newIntent(requireContext(), R.string.more_help)));

        b.rowSignOut.rowTitle.setText(R.string.more_sign_out);
        b.rowSignOut.getRoot().setOnClickListener(v -> confirmSignOut());
        return b.getRoot();
    }

    private void confirmSignOut() {
        new MaterialAlertDialogBuilder(requireContext())
                .setMessage(R.string.sign_out_confirm)
                .setNegativeButton(R.string.cancel, null)
                .setPositiveButton(R.string.more_sign_out, (d, w) -> {
                    Injection.provideAuthRepository().logout();
                    Intent intent = new Intent(requireContext(), LoginActivity.class)
                            .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                })
                .show();
    }
}
