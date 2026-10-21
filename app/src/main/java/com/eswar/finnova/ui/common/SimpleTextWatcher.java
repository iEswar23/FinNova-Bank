package com.eswar.finnova.ui.common;

import android.text.Editable;
import android.text.TextWatcher;

/** TextWatcher that only cares about the final text. Usage: input.addTextChangedListener(new SimpleTextWatcher(presenter::onNameChanged)); */
public class SimpleTextWatcher implements TextWatcher {

    public interface Listener {
        void onTextChanged(String text);
    }

    private final Listener listener;

    public SimpleTextWatcher(Listener listener) {
        this.listener = listener;
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
    }

    @Override
    public void afterTextChanged(Editable s) {
        listener.onTextChanged(s.toString());
    }
}
