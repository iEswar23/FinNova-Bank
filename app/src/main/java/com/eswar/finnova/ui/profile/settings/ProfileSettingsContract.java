package com.eswar.finnova.ui.profile.settings;

import com.eswar.finnova.base.BaseView;

/**
 * MVP contract for the Profile & Settings screen.
 * Reading a Contract tells you everything the screen can do without reading the Activity.
 */
public interface ProfileSettingsContract {

    interface View extends BaseView {
        /** All values arrive already formatted / masked; the View just displays them. */
        void showProfileSummary(String preferredName, String address, String maskedEmail,
                                String maskedPhone, String income);

        void showLoadError(String message);

        void navigateTo(ProfileRow row);
    }

    interface Presenter {
        void loadProfile();

        void onRowClicked(ProfileRow row);
    }
}
