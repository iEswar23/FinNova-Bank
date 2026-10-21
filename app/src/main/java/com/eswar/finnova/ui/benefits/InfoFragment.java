package com.eswar.finnova.ui.benefits;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.StringRes;
import androidx.fragment.app.Fragment;

import com.eswar.finnova.databinding.FragmentInfoBinding;

/** Static content tab, used for Benefits and Offers. Practice: load offers from a repository with MVP. */
public class InfoFragment extends Fragment {

    private static final String ARG_TITLE = "title";
    private static final String ARG_BODY = "body";

    public static InfoFragment newInstance(@StringRes int title, @StringRes int body) {
        Bundle args = new Bundle();
        args.putInt(ARG_TITLE, title);
        args.putInt(ARG_BODY, body);
        InfoFragment f = new InfoFragment();
        f.setArguments(args);
        return f;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        FragmentInfoBinding b = FragmentInfoBinding.inflate(inflater, container, false);
        Bundle args = requireArguments();
        b.infoTitle.setText(args.getInt(ARG_TITLE));
        b.infoBody.setText(args.getInt(ARG_BODY));
        return b.getRoot();
    }
}
