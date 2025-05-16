package khouya.site.banking.services;

import khouya.site.banking.dtos.BankAccountDTO;
import khouya.site.banking.dtos.CurrentBankAccountDTO;
import khouya.site.banking.dtos.CustomerDTO;
import khouya.site.banking.dtos.SavingBankAccountDTO;
import khouya.site.banking.entities.*;
import khouya.site.banking.enums.AccountStatus;
import khouya.site.banking.exceptions.BalanceNotSufficientException;
import khouya.site.banking.exceptions.BankAccountNotFoundException;
import khouya.site.banking.exceptions.CustomerNotFoundException;

import java.util.List;

public interface BankAccountService {
    CustomerDTO saveCustomer(CustomerDTO customerDTO);

    CurrentBankAccountDTO saveCurrentBankAccount(double initialBalance, double overDraft, String customerId) throws CustomerNotFoundException;
    SavingBankAccountDTO saveSavingBankAccount(double initialBalance, double interestRate, String customerId) throws CustomerNotFoundException;

    BankAccountDTO updateBankAccount(String accountId, AccountStatus accountStatus) ;

    List<CustomerDTO> listCustomer();

    BankAccount getBankAccountById(String id) throws BankAccountNotFoundException;

    void debit(String accountId, double amount, String description) throws BalanceNotSufficientException, BankAccountNotFoundException;
    void credit(String accountId, double amount, String description) throws BankAccountNotFoundException;


    void transfer(String accountIdSource, String accountIdDestination, double amount) throws BankAccountNotFoundException, BalanceNotSufficientException;

    List<BankAccount> getListBankAccounts();
//
    CustomerDTO getCustomer(String customerId) throws CustomerNotFoundException;
//
    CustomerDTO updateCustomer(CustomerDTO customerDTO);

    void deleteCustomer(String customerId) throws CustomerNotFoundException;
//
//    List<AccountOperation> getAccountHistoryByList(String accountId);
//
//    List<BankAccount> getBankAccountsByCustomerId(Long customerId);
//
//    AccountHistoryDTO getAccountHistoryByPage(String accountId, int page, int size) ;
//
    List<Customer> searchCustomers(String keyword);



} 