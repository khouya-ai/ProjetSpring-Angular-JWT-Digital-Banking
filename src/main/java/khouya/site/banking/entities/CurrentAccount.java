package khouya.site.banking.entities;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;

@Entity
@DiscriminatorValue("CA")
public class CurrentAccount extends BankAccount {
    private double overDraft;
}
