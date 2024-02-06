package com.alexbalmus.fakedci.dcibankaccounts.usecases.moneytransfer;

import com.alexbalmus.fakedci.dcibankaccounts.entities.Account;
import com.alexbalmus.fakedci.dcibankaccounts.stereotypes.DciContext;
import lombok.RequiredArgsConstructor;

@DciContext
@RequiredArgsConstructor
public class MoneyTransferContext<A extends Account>
{
    private final Account_SourceRole<A> accountSourceRole;

    public void execute(final A source, final A destination, final Double amount)
    {
        accountSourceRole.transfer(source, destination, amount);
    }
}
