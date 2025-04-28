package khouya.site.banking.controllers;

import khouya.site.banking.entities.BankAccount;
import khouya.site.banking.services.BankAccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/bank-accounts")
public class BankAccountController {
    @Autowired
    private BankAccountService bankAccountService;

    @PostMapping
    public BankAccount saveBankAccount(@RequestBody BankAccount bankAccount) {
        return bankAccountService.saveBankAccount(bankAccount);
    }

    @GetMapping
    public List<BankAccount> listBankAccounts() {
        return bankAccountService.listBankAccounts();
    }

    @GetMapping("/{id}")
    public BankAccount getBankAccount(@PathVariable String id) {
        return bankAccountService.getBankAccount(id);
    }

    @PutMapping("/{id}")
    public BankAccount updateBankAccount(@PathVariable String id, @RequestBody BankAccount bankAccount) {
        bankAccount.setId(id);
        return bankAccountService.updateBankAccount(bankAccount);
    }

    @DeleteMapping("/{id}")
    public void deleteBankAccount(@PathVariable String id) {
        bankAccountService.deleteBankAccount(id);
    }
} 