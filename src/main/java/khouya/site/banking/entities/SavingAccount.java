package khouya.site.banking.entities;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;

@Entity
@DiscriminatorValue("SA")
public class SavingAccount extends BankAccount {
    private double interestRate;
}
