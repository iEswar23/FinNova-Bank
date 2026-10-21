package com.eswar.finnova.ui.accounts;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.eswar.finnova.R;
import com.eswar.finnova.data.model.Account;
import com.eswar.finnova.databinding.ItemAccountBinding;
import com.eswar.finnova.util.Formatters;

import java.util.ArrayList;
import java.util.List;

public class AccountsAdapter extends RecyclerView.Adapter<AccountsAdapter.Holder> {

    private final List<Account> items = new ArrayList<>();

    public void submit(List<Account> accounts) {
        items.clear();
        items.addAll(accounts);
        notifyDataSetChanged();   // Practice: switch to ListAdapter + DiffUtil
    }

    @NonNull
    @Override
    public Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new Holder(ItemAccountBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull Holder holder, int position) {
        holder.bind(items.get(position));
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    static class Holder extends RecyclerView.ViewHolder {
        private final ItemAccountBinding b;

        Holder(ItemAccountBinding binding) {
            super(binding.getRoot());
            this.b = binding;
        }

        void bind(Account a) {
            b.accountName.setText(a.name);
            b.accountNumber.setText(b.getRoot().getContext().getString(R.string.account_ending, a.lastFour));
            boolean card = a.type == Account.Type.CREDIT_CARD;
            b.balance.setText(Formatters.formatCents(Math.abs(a.balanceCents)));
            b.balanceLabel.setText(card ? R.string.current_balance : R.string.available_balance);
        }
    }
}
