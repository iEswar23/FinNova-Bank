package com.eswar.finnova.ui.accounts;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.eswar.finnova.R;
import com.eswar.finnova.data.model.Account;
import com.eswar.finnova.databinding.FragmentAccountsBinding;
import com.eswar.finnova.di.Injection;
import com.eswar.finnova.ui.common.UiUtils;

import java.util.List;

public class AccountsFragment extends Fragment implements AccountsContract.View {

    private FragmentAccountsBinding binding;
    private AccountsPresenter presenter;
    private final AccountsAdapter adapter = new AccountsAdapter();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = FragmentAccountsBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        binding.accountsList.setLayoutManager(new LinearLayoutManager(requireContext()));
        binding.accountsList.setAdapter(adapter);
        presenter = new AccountsPresenter(Injection.provideAccountRepository(), Injection.provideProfileRepository());
        presenter.attachView(this);
        presenter.load();
    }

    @Override
    public void onDestroyView() {
        presenter.detachView();
        binding = null;          // Fragments outlive their views; avoid leaking the binding
        super.onDestroyView();
    }

    @Override
    public void showGreeting(String firstName) {
        binding.greeting.setText(getString(R.string.greeting, firstName));
    }

    @Override
    public void showAccounts(List<Account> accounts) {
        adapter.submit(accounts);
    }

    @Override
    public void showError(String message) {
        UiUtils.showErrorDialog(requireContext(), message);
    }

    @Override
    public void showLoading(boolean show) {
        binding.progress.setVisibility(show ? View.VISIBLE : View.GONE);
    }
}
