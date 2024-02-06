package com.alexbalmus.fakedci.dcibankaccounts.entities;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name="account")
public class Account
{
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    @Column(name = "balance")
    private Double balance;

    public Account(final Double balance)
    {
        this.balance = balance;
    }

    protected Account()
    {
    }

    public void increaseBalanceBy(final Double amount)
    {
        balance += amount;
    }

    public void decreaseBalanceBy(final Double amount)
    {
        balance -= amount;
    }


// Implementing equals and hashCode for JPA & Hibernate entities:
//
// https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
// https://thorben-janssen.com/ultimate-guide-to-implementing-equals-and-hashcode-with-hibernate/
// https://jpa-buddy.com/blog/hopefully-the-final-article-about-equals-and-hashcode-for-jpa-entities-with-db-generated-ids/

    @Override
    public boolean equals(Object o)
    {
        if (this == o)
        {
            return true;
        }

        if (o == null || getClass() != o.getClass())
        {
            return false;
        }

        Account other = (Account) o;
        return id != null && id.equals(other.getId());
    }

    @Override
    public int hashCode()
    {
        return getClass().hashCode();
    }
}

