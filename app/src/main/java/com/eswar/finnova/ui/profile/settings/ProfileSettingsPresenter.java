package com.eswar.finnova.ui.profile.settings;

import com.eswar.finnova.base.BasePresenter;
import com.eswar.finnova.data.model.UserProfile;
import com.eswar.finnova.data.repository.DataCallback;
import com.eswar.finnova.data.repository.ProfileRepository;
import com.eswar.finnova.util.Formatters;

public class ProfileSettingsPresenter extends BasePresenter<ProfileSettingsContract.View>
        implements ProfileSettingsContract.Presenter {

    private final ProfileRepository repository;

    public ProfileSettingsPresenter(ProfileRepository repository) {
        this.repository = repository;
    }

    @Override
    public void loadProfile() {
        if (getView() != null) getView().showLoading(true);
        repository.getProfile(new DataCallback<UserProfile>() {
            @Override
            public void onSuccess(UserProfile p) {
                ProfileSettingsContract.View v = getView();
                if (v == null) return;          // screen was closed while loading
                v.showLoading(false);
                v.showProfileSummary(
                        p.preferredFirstName,
                        p.address.singleLine(),
                        Formatters.maskEmail(p.email),
                        Formatters.maskPhone(p.mobilePhone),
                        Formatters.formatDollars(p.income.annualIncome) + " / year");
            }

            @Override
            public void onError(Throwable error) {
                ProfileSettingsContract.View v = getView();
                if (v == null) return;
                v.showLoading(false);
                v.showLoadError(error.getMessage());
            }
        });
    }

    @Override
    public void onRowClicked(ProfileRow row) {
        // A real app would also log an analytics event here.
        if (getView() != null) getView().navigateTo(row);
    }
}
