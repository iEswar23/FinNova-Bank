package com.eswar.finnova.base;

/**
 * Marker interface for every MVP "View" (usually an Activity or Fragment).
 * Each feature's Contract.View extends this.
 */
public interface BaseView {
    void showLoading(boolean show);
}
