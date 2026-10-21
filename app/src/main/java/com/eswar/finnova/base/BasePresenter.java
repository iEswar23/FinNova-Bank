package com.eswar.finnova.base;

import androidx.annotation.Nullable;

/**
 * Base class for all Presenters.
 *
 * The Presenter holds NO Android framework classes (no Context, no View, no R.string)
 * so that it can be tested with plain JUnit on the JVM.
 *
 * Lifecycle: the View calls attachView() in onCreate/onViewCreated and
 * detachView() in onDestroy/onDestroyView. Always null-check getView()
 * before using it, because an async callback can arrive after the screen closed.
 */
public abstract class BasePresenter<V extends BaseView> {

    @Nullable
    private V view;

    public void attachView(V view) {
        this.view = view;
    }

    public void detachView() {
        this.view = null;
    }

    @Nullable
    protected V getView() {
        return view;
    }

    protected boolean isViewAttached() {
        return view != null;
    }
}
