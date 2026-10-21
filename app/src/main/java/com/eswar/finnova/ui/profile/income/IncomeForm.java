package com.eswar.finnova.ui.profile.income;

/** Raw text from the income form, exactly as typed. */
public final class IncomeForm {
    public final String employmentStatus;
    public final String annualIncome;
    public final String monthlyHousing;

    public IncomeForm(String employmentStatus, String annualIncome, String monthlyHousing) {
        this.employmentStatus = employmentStatus;
        this.annualIncome = annualIncome;
        this.monthlyHousing = monthlyHousing;
    }
}
