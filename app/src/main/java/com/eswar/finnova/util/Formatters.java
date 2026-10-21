package com.eswar.finnova.util;

import androidx.annotation.Nullable;

import java.text.NumberFormat;
import java.util.Locale;

/** Display helpers. Pure Java so they can be unit tested. */
public final class Formatters {

    private Formatters() {
    }

    /** "4695550142" -> "(469) 555-0142". Returns the input unchanged if it isn't 10 digits. */
    public static String formatPhone(@Nullable String digits) {
        if (digits == null || !digits.matches("\\d{10}")) return digits == null ? "" : digits;
        return "(" + digits.substring(0, 3) + ") " + digits.substring(3, 6) + "-" + digits.substring(6);
    }

    /** Masks all but the last 4 digits: "4695550142" -> "(•••) •••-0142". */
    public static String maskPhone(@Nullable String digits) {
        if (digits == null || digits.length() < 4) return "";
        return "(•••) •••-" + digits.substring(digits.length() - 4);
    }

    /** "alex.johnson@example.com" -> "a••••••••••n@example.com". */
    public static String maskEmail(@Nullable String email) {
        if (email == null) return "";
        int at = email.indexOf('@');
        if (at <= 0) return email;
        String local = email.substring(0, at);
        String domain = email.substring(at);
        if (local.length() <= 2) return local.charAt(0) + "•" + domain;
        StringBuilder sb = new StringBuilder().append(local.charAt(0));
        for (int i = 1; i < local.length() - 1; i++) sb.append('•');
        return sb.append(local.charAt(local.length() - 1)).append(domain).toString();
    }

    /** 482315 cents -> "$4,823.15"; negative -> "-$634.72". */
    public static String formatCents(long cents) {
        NumberFormat f = NumberFormat.getCurrencyInstance(Locale.US);
        return f.format(cents / 100.0);
    }

    /** 95000 -> "$95,000". */
    public static String formatDollars(long dollars) {
        NumberFormat f = NumberFormat.getCurrencyInstance(Locale.US);
        f.setMaximumFractionDigits(0);
        return f.format(dollars);
    }
}
