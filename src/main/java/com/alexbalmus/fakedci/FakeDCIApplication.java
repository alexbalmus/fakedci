package com.alexbalmus.fakedci;

import com.alexbalmus.fakedci.dcibankaccounts.entities.Account;
import com.alexbalmus.fakedci.dcibankaccounts.repositories.AccountsRepository;
import com.alexbalmus.fakedci.dcibankaccounts.services.MoneyTransferService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Import;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.transaction.annotation.Transactional;

@SpringBootApplication
@Import({
	AppConfiguration.class
})
@EnableTransactionManagement
@Transactional
public class FakeDCIApplication implements CommandLineRunner
{
	@Autowired
	AccountsRepository accountsRepository;
	@Autowired
	MoneyTransferService moneyTransferService;


	public static void main(String[] args)
	{
		SpringApplication.run(FakeDCIApplication.class, args);
	}

	@Override
	public void run(String... args)
    {
		System.out.println("\nExecuting A to B money transfer scenario: \n");
		executeAToBMoneyTransferScenario();

		System.out.println("\nExecuting A to B to C money transfer scenario: \n");
		executeAToBToCMoneyTransferScenario();
	}

	public void executeAToBMoneyTransferScenario()
	{
		var source = new Account(100.0);
		accountsRepository.save(source);
		System.out.println("Source account: " + source.getBalance());

		var destination = new Account(200.0);
		accountsRepository.save(destination);
		System.out.println("Destination account: " + destination.getBalance());

		System.out.println("Transferring 50 from Source to Destination.");
		moneyTransferService.performMoneyTransfer(source.getId(), destination.getId(), 50.0);

		System.out.println("Source account: " + source.getBalance()); // 50.0
		System.out.println("Destination account: " + destination.getBalance()); // 250.0

		System.out.println("\n\n");
	}

	public void executeAToBToCMoneyTransferScenario()
	{
		var source = new Account(100.0);
		accountsRepository.save(source);
		System.out.println("Source account: " + source.getBalance());

		var intermediary = new Account(0.0);
		accountsRepository.save(intermediary);
		System.out.println("Intermediary account: " + intermediary.getBalance());

		var destination = new Account(200.0);
		accountsRepository.save(destination);
		System.out.println("Destination account: " + destination.getBalance());

		System.out.println("Transferring 50 from Source to Destination via Intermediary.");
		moneyTransferService
				.performABCMoneyTransfer(source.getId(), intermediary.getId(), destination.getId(), 50.0);

		System.out.println("Source account: " + source.getBalance()); // 50.0
		System.out.println("Intermediary account: " + intermediary.getBalance()); // 0.0
		System.out.println("Destination account: " + destination.getBalance()); // 250.0
	}
}
