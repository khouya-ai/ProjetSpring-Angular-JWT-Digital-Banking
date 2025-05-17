package khouya.site.banking.entities;
import jakarta.persistence.*;
import khouya.site.banking.enums.AccountStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;

@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "TYPE", length = 4)
@Data @AllArgsConstructor
@NoArgsConstructor
public abstract class BankAccount {
    @Id
    private String id;
    private Date createdDate;
    private double balance;
    private String currency;

    @Enumerated(EnumType.STRING)
    private AccountStatus status;

    @ManyToOne
    private Customer customer;

    @OneToMany(mappedBy = "bankAccount", fetch = FetchType.EAGER)
    private List<AccountOperation> accountOperations;
}
