package com.alexbalmus.fakedci.dcibankaccounts.usecases.moneytransfer;

import com.alexbalmus.fakedci.dcibankaccounts.entities.Account;
import com.alexbalmus.fakedci.dcibankaccounts.stereotypes.DciRole;
import lombok.RequiredArgsConstructor;

@DciRole
@RequiredArgsConstructor
public class Account_SourceRole<A extends Account>
{
    private final Account_DestinationRole<A> accountDestinationRole;
    String INSUFFICIENT_FUNDS = "Insufficient funds.";

    void transfer(final A source, final A destination, final Double amount)
    {
        if (source.getBalance() < amount)
        {
            throw new BalanceException(INSUFFICIENT_FUNDS); // Rollback.
        }
        source.decreaseBalanceBy(amount);
        accountDestinationRole.receive(destination, amount);
    }
}
