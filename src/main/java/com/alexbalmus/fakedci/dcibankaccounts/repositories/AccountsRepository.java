package com.alexbalmus.fakedci.dcibankaccounts.repositories;

import com.alexbalmus.fakedci.dcibankaccounts.entities.Account;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AccountsRepository extends JpaRepository<Account, Long>
{
}
