package khouya.site.banking.repositories;

import khouya.site.banking.entities.BankAccount;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BankAccountRepository extends JpaRepository<BankAccount, String> {
    List<BankAccount> getBankAccountByCustomer_Id(Long customerId);
} 