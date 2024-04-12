package com.alexbalmus.fakedci.dcibankaccounts.usecases.moneytransfer;

class BalanceException extends RuntimeException
{
    public BalanceException(final String message)
    {
        super(message);
    }
}
