package com.eswar.finnova.ui.common;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import androidx.annotation.Nullable;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.eswar.finnova.R;

public final class UiUtils {

    private UiUtils() {
    }

    public static void hideKeyboard(Activity activity) {
        View focus = activity.getCurrentFocus();
        if (focus == null) return;
        InputMethodManager imm = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm != null) imm.hideSoftInputFromWindow(focus.getWindowToken(), 0);
    }

    public static void showErrorDialog(Context context, @Nullable String message) {
        new MaterialAlertDialogBuilder(context)
                .setMessage(message == null ? context.getString(R.string.something_went_wrong) : message)
                .setPositiveButton(R.string.ok, null)
                .show();
    }

    public static String text(EditText editText) {
        return editText.getText() == null ? "" : editText.getText().toString();
    }
}
