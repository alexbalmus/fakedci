package com.alexbalmus.fakedci.dcibankaccounts.usecases.moneytransfer;

import com.alexbalmus.fakedci.dcibankaccounts.entities.Account;
import com.alexbalmus.fakedci.dcibankaccounts.stereotypes.DciRole;

@DciRole
public class Account_DestinationRole<A extends Account>
{
    public void receive(A destination, final Double amount)
    {
        destination.increaseBalanceBy(amount);
    }
}
