package com.eswar.finnova.data.model;

/** A bank account or card shown on the Accounts tab. Balance is kept in cents to avoid double rounding errors. */
public final class Account {
    public enum Type { CHECKING, SAVINGS, CREDIT_CARD }

    public final String id;
    public final String name;
    public final String lastFour;
    public final Type type;
    public final long balanceCents;

    public Account(String id, String name, String lastFour, Type type, long balanceCents) {
        this.id = id;
        this.name = name;
        this.lastFour = lastFour;
        this.type = type;
        this.balanceCents = balanceCents;
    }
}
