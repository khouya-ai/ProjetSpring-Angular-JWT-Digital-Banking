package khouya.site.banking.services;

import khouya.site.banking.entities.*;
import khouya.site.banking.enums.AccountStatus;
import khouya.site.banking.exceptions.BalanceNotSufficientException;
import khouya.site.banking.exceptions.BankAccountNotFoundException;
import khouya.site.banking.exceptions.CustomerNotFoundException;

import java.util.List;

public interface BankAccountService {
    Customer saveCustomer(Customer customerDTO);

    CurrentAccount saveCurrentBankAccount(double initialBalance, double overDraft, String customerId) throws CustomerNotFoundException;
    SavingAccount saveSavingBankAccount(double initialBalance, double interestRate, String customerId) throws CustomerNotFoundException;

    BankAccount updateBankAccount(String accountId, AccountStatus accountStatus) ;

    List<Customer> listCustomer();

    BankAccount getBankAccountById(String id) throws BankAccountNotFoundException;

    void debit(String accountId, double amount, String description) throws BalanceNotSufficientException, BankAccountNotFoundException;
    void credit(String accountId, double amount, String description) throws BankAccountNotFoundException;

    void transfer(String accountIdSource, String accountIdDestination, double amount) throws BankAccountNotFoundException, BalanceNotSufficientException;

    List<BankAccount> getListBankAccounts();
//
    Customer getCustomer(Long customerId) ;
//
    Customer updateCustomer(Customer customerDTO);

    void deleteCustomer(Long customerId) ;
//
//    List<AccountOperation> getAccountHistoryByList(String accountId);
//
//    List<BankAccount> getBankAccountsByCustomerId(Long customerId);
//
//    AccountHistoryDTO getAccountHistoryByPage(String accountId, int page, int size) ;
//
    List<Customer> searchCustomers(String keyword);



} 