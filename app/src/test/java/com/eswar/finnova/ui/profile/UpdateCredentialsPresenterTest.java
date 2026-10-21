package com.eswar.finnova.ui.profile;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

import com.eswar.finnova.data.repository.FakeBankRepository;
import com.eswar.finnova.ui.settings.credentials.CredentialsForm;
import com.eswar.finnova.ui.settings.credentials.UpdateCredentialsContract;
import com.eswar.finnova.ui.settings.credentials.UpdateCredentialsPresenter;
import com.eswar.finnova.util.TaskRunner;
import com.eswar.finnova.validation.ProfileValidator;
import com.eswar.finnova.validation.ValidationError;

import org.junit.Before;
import org.junit.Test;

public class UpdateCredentialsPresenterTest {

    private UpdateCredentialsContract.View view;
    private UpdateCredentialsPresenter presenter;

    @Before
    public void setUp() {
        view = mock(UpdateCredentialsContract.View.class);
        FakeBankRepository repo = new FakeBankRepository(TaskRunner.IMMEDIATE);
        presenter = new UpdateCredentialsPresenter(repo, repo, new ProfileValidator());
        presenter.attachView(view);
    }

    @Test
    public void checklist_updatesAsUserTypes() {
        presenter.onNewPasswordChanged("Bank@2026");
        verify(view).updatePasswordChecklist(true, true, true, true, true);
    }

    @Test
    public void weakPassword_showsFirstFailingRule() {
        presenter.onSaveClicked(new CredentialsForm("Finnova@2026", "demouser", "bankbank1!", "bankbank1!"));
        verify(view).showNewPasswordError(ValidationError.PASSWORD_MISSING_UPPERCASE);
        verify(view, never()).showLoading(true);
    }

    @Test
    public void wrongCurrentPassword_fromServer() {
        presenter.onSaveClicked(new CredentialsForm("Wrong@123", "demouser", "Bank@2026x", "Bank@2026x"));
        verify(view).showWrongCurrentPassword("Your current password is incorrect.");
    }

    @Test
    public void validChange_saves() {
        presenter.onSaveClicked(new CredentialsForm("Finnova@2026", "eswar_23", "Bank@2026x", "Bank@2026x"));
        verify(view).showSaved();
    }
}
