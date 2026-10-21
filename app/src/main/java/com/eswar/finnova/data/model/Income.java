package com.eswar.finnova.data.model;

/** Income details the bank asks for (annual income + monthly housing payment). */
public final class Income {
    public final String employmentStatus;
    public final long annualIncome;
    public final long monthlyHousingPayment;

    public Income(String employmentStatus, long annualIncome, long monthlyHousingPayment) {
        this.employmentStatus = employmentStatus;
        this.annualIncome = annualIncome;
        this.monthlyHousingPayment = monthlyHousingPayment;
    }
}
