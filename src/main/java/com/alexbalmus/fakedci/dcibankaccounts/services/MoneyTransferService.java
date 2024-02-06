package com.alexbalmus.fakedci.dcibankaccounts.services;

import com.alexbalmus.fakedci.dcibankaccounts.entities.Account;
import com.alexbalmus.fakedci.dcibankaccounts.repositories.AccountsRepository;
import com.alexbalmus.fakedci.dcibankaccounts.usecases.moneytransfer.MoneyTransferContext;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class MoneyTransferService
{
    private final AccountsRepository accountsRepository;
    private final MoneyTransferContext<Account> moneyTransferContext;

    public void performMoneyTransfer(final Long sourceId, final Long destinationId,
        final Double amount)
    {
        var source = accountsRepository.findById(sourceId).orElseThrow();
        var destination = accountsRepository.findById(destinationId).orElseThrow();
        moneyTransferContext.execute(source, destination, amount);
    }

    public void performABCMoneyTransfer(final Long sourceId, final Long intermediaryId, final Long destinationId,
        final Double amount)
    {
        var source = accountsRepository.findById(sourceId).orElseThrow();
        var intermediary = accountsRepository.findById(intermediaryId).orElseThrow();
        var destination = accountsRepository.findById(destinationId).orElseThrow();
        moneyTransferContext.execute(source, intermediary, amount);
        moneyTransferContext.execute(intermediary, destination, amount);
    }
}
