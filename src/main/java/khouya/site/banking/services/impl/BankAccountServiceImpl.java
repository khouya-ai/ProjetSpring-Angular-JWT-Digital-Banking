package khouya.site.banking.services.impl;


import khouya.site.banking.entities.*;
import khouya.site.banking.enums.AccountStatus;
import khouya.site.banking.enums.OperationType;
import khouya.site.banking.exceptions.BalanceNotSufficientException;
import khouya.site.banking.exceptions.BankAccountNotFoundException;
import khouya.site.banking.exceptions.CustomerNotFoundException;
import khouya.site.banking.repositories.AccountOperationRepository;
import khouya.site.banking.repositories.BankAccountRepository;
import khouya.site.banking.repositories.CustomerRepository;
import khouya.site.banking.services.BankAccountService;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.UUID;

@Service
@Transactional
@Slf4j
public class BankAccountServiceImpl implements BankAccountService {
    private BankAccountRepository bankAccountRepository;
    private CustomerRepository customerRepository;
    private AccountOperationRepository accountOperationRepository;

    public BankAccountServiceImpl(BankAccountRepository bankAccountRepository,
                                  CustomerRepository customerRepository,
                                  AccountOperationRepository accountOperationRepository) {
        this.bankAccountRepository = bankAccountRepository;
        this.customerRepository = customerRepository;
        this.accountOperationRepository = accountOperationRepository;
    }



    @Override
    public Customer saveCustomer(Customer customer) {
        log.info("Saving new Customer");
        Customer savedCustomer = customerRepository.save(customer);
        return savedCustomer;
    }

    @Override
    public CurrentAccount saveCurrentBankAccount(double initialBalance, double overDraft, String customerId) throws CustomerNotFoundException {
        Customer customer = customerRepository.findById(customerId).orElse(null);
        if(customer == null) {
            throw new CustomerNotFoundException("Customer not found");
        }
        CurrentAccount currentAccount = new CurrentAccount();

        currentAccount.setId(UUID.randomUUID().toString());
        currentAccount.setCreatedAt(new Date());
        currentAccount.setBalance(initialBalance);
        currentAccount.setCustomer(customer);
        currentAccount.setOverDraft(overDraft);
        currentAccount.setStatus(AccountStatus.CREATED);
        CurrentAccount currentAccountSaved = bankAccountRepository.save(currentAccount);
        return currentAccountSaved;
    }

    @Override
    public SavingAccount saveSavingBankAccount(double initialBalance, double interestRate, String customerId) throws CustomerNotFoundException {
        Customer customer = customerRepository.findById(customerId).orElse(null);
        if(customer == null) {
            throw new CustomerNotFoundException("Customer not found");
        }
        SavingAccount savingAccount = new SavingAccount();

        savingAccount.setId(UUID.randomUUID().toString());
        savingAccount.setCreatedAt(new Date());
        savingAccount.setBalance(initialBalance);
        savingAccount.setCustomer(customer);
        savingAccount.setInterestRate(interestRate);
        savingAccount.setStatus(AccountStatus.CREATED);
        SavingAccount savingAccountSaved = bankAccountRepository.save(savingAccount);
        return savingAccountSaved;
    }

    @Override
    public BankAccount updateBankAccount(String accountId, AccountStatus accountStatus) {
        return null;
    }

    @Override
    public List<Customer> listCustomer() {
        return customerRepository.findAll();
    }

    @Override
    public BankAccount getBankAccountById(String id) throws BankAccountNotFoundException {
        BankAccount bankAccount = bankAccountRepository.findById(id)
                .orElseThrow(() -> new BankAccountNotFoundException("No Bank Account with this id: " + id ));
        if(bankAccount instanceof SavingAccount) {
            SavingAccount savingAccount = (SavingAccount) bankAccount;
            return savingAccount;
        } else {
            CurrentAccount currentAccount = (CurrentAccount) bankAccount;
            return currentAccount;
        }
    }



    @Override
    public void debit(String accountId, double amount, String description) throws BalanceNotSufficientException, BankAccountNotFoundException {
        BankAccount bankAccount = getBankAccountById(accountId);

        if(bankAccount.getBalance() < amount) {
            throw new BalanceNotSufficientException("Balance not sufficient");
        }
        AccountOperation debitAccountOperation = new AccountOperation();
        debitAccountOperation.setType(OperationType.DEBIT);
        debitAccountOperation.setAmount(amount);
        debitAccountOperation.setDate(new Date());
        debitAccountOperation.setBankAccount(bankAccount);
        debitAccountOperation.setDescription(description);
        accountOperationRepository.save(debitAccountOperation);

        bankAccount.setBalance(bankAccount.getBalance() - amount);

        bankAccountRepository.save(bankAccount);
    }

    @Override
    public void credit(String accountId, double amount, String description) throws BankAccountNotFoundException {
        BankAccount bankAccount = getBankAccountById(accountId);

        AccountOperation creditAccountOperation = new AccountOperation();
        creditAccountOperation.setType(OperationType.CREDIT);
        creditAccountOperation.setAmount(amount);
        creditAccountOperation.setDate(new Date());
        creditAccountOperation.setBankAccount(bankAccount);
        creditAccountOperation.setDescription(description);
        accountOperationRepository.save(creditAccountOperation);

        bankAccount.setBalance(bankAccount.getBalance() + amount);

    }

    @Override
    public void transfer(String accountIdSource, String accountIdDestination, double amount) throws BankAccountNotFoundException, BalanceNotSufficientException {
        AccountOperation accountSourceOperationDTO = new AccountOperation();
        accountSourceOperationDTO.setAmount(amount);
        debit(accountIdSource,amount,"description");
        AccountOperation accountDestOperationDTO = new AccountOperation();
        accountDestOperationDTO.setAmount(amount);
        credit(accountIdDestination,amount,"description");
    }

    @Override
    public List<BankAccount> getListBankAccounts() {
        return bankAccountRepository.findAll();
    }

    @Override
    public Customer getCustomer(Long customerId) {
        return null;
    }

    @Override
    public Customer updateCustomer(Customer customerDTO) {
        return null;
    }

    @Override
    public void deleteCustomer(Long customerId) {

    }

    @Override
    public List<Customer> searchCustomers(String keyword) {
        return List.of();
    }


}