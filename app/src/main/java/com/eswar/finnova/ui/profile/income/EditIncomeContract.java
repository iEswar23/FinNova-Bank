package com.eswar.finnova.ui.profile.income;

import androidx.annotation.Nullable;

import com.eswar.finnova.base.BaseView;
import com.eswar.finnova.data.model.Income;
import com.eswar.finnova.validation.ValidationError;

public interface EditIncomeContract {

    interface View extends BaseView {
        void showIncome(Income income);

        void showEmploymentError(@Nullable ValidationError error);

        void showIncomeError(@Nullable ValidationError error);

        void showHousingError(@Nullable ValidationError error);

        void showServerError(String message);

        void showSaved();
    }

    interface Presenter {
        void start();

        void onInputChanged(IncomeForm form);

        void onSaveClicked(IncomeForm form);
    }
}
