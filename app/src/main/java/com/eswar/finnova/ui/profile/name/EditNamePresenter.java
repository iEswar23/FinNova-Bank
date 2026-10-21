package com.eswar.finnova.ui.profile.name;

import com.eswar.finnova.base.BasePresenter;
import com.eswar.finnova.data.model.UserProfile;
import com.eswar.finnova.data.repository.DataCallback;
import com.eswar.finnova.data.repository.ProfileRepository;
import com.eswar.finnova.validation.ProfileValidator;
import com.eswar.finnova.validation.ValidationResult;

/**
 * Validation UX used on every edit screen:
 *  - Before the first Save tap, typing never shows errors (don't shout at the user while they type).
 *  - On Save, validate everything and show errors.
 *  - After that first Save, re-validate live as they type so errors disappear as soon as they're fixed.
 */
public class EditNamePresenter extends BasePresenter<EditNameContract.View>
        implements EditNameContract.Presenter {

    private final ProfileRepository repository;
    private final ProfileValidator validator;
    private String currentName = "";
    private boolean submitted;

    public EditNamePresenter(ProfileRepository repository, ProfileValidator validator) {
        this.repository = repository;
        this.validator = validator;
    }

    @Override
    public void start() {
        repository.getProfile(new DataCallback<UserProfile>() {
            @Override
            public void onSuccess(UserProfile profile) {
                currentName = profile.preferredFirstName;
                if (getView() != null) getView().showCurrentName(currentName);
            }

            @Override
            public void onError(Throwable error) {
                if (getView() != null) getView().showServerError(error.getMessage());
            }
        });
    }

    @Override
    public void onNameChanged(String input) {
        if (getView() == null) return;
        getView().showNameError(submitted ? validator.validatePreferredName(input, currentName).getError() : null);
    }

    @Override
    public void onSaveClicked(String input) {
        EditNameContract.View view = getView();
        if (view == null) return;
        submitted = true;

        ValidationResult result = validator.validatePreferredName(input, currentName);
        view.showNameError(result.getError());
        if (!result.isValid()) return;

        view.showLoading(true);
        repository.updatePreferredFirstName(input.trim(), new DataCallback<UserProfile>() {
            @Override
            public void onSuccess(UserProfile profile) {
                if (getView() == null) return;
                getView().showLoading(false);
                getView().showSaved();
            }

            @Override
            public void onError(Throwable error) {
                if (getView() == null) return;
                getView().showLoading(false);
                getView().showServerError(error.getMessage());
            }
        });
    }
}
