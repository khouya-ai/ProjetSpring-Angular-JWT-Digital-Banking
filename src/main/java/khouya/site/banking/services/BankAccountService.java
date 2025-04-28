package khouya.site.banking.services;

import khouya.site.banking.entities.BankAccount;
import java.util.List;

public interface BankAccountService {
    BankAccount saveBankAccount(BankAccount bankAccount);
    List<BankAccount> listBankAccounts();
    BankAccount getBankAccount(String id);
    BankAccount updateBankAccount(BankAccount bankAccount);
    void deleteBankAccount(String id);
} 