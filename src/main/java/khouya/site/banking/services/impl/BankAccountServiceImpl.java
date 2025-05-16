package khouya.site.banking.services.impl;


import khouya.site.banking.dtos.BankAccountDTO;
import khouya.site.banking.dtos.CurrentBankAccountDTO;
import khouya.site.banking.dtos.CustomerDTO;
import khouya.site.banking.dtos.SavingBankAccountDTO;
import khouya.site.banking.entities.*;
import khouya.site.banking.enums.AccountStatus;
import khouya.site.banking.enums.OperationType;
import khouya.site.banking.exceptions.BalanceNotSufficientException;
import khouya.site.banking.exceptions.BankAccountNotFoundException;
import khouya.site.banking.exceptions.CustomerNotFoundException;
import khouya.site.banking.mappers.BankAccountMapperImpl;
import khouya.site.banking.repositories.AccountOperationRepository;
import khouya.site.banking.repositories.BankAccountRepository;
import khouya.site.banking.repositories.CustomerRepository;
import khouya.site.banking.services.BankAccountService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@Transactional
@Slf4j
public class BankAccountServiceImpl implements BankAccountService {
    private final BankAccountRepository bankAccountRepository;
    private final CustomerRepository customerRepository;
    private final AccountOperationRepository accountOperationRepository;
    private final BankAccountMapperImpl dtoMapper;
    private final BankAccountMapperImpl bankAccountMapperImpl;

    public BankAccountServiceImpl(BankAccountRepository bankAccountRepository,
                                  CustomerRepository customerRepository,
                                  AccountOperationRepository accountOperationRepository, BankAccountMapperImpl dtoMapper, BankAccountMapperImpl bankAccountMapperImpl) {
        this.bankAccountRepository = bankAccountRepository;
        this.customerRepository = customerRepository;
        this.accountOperationRepository = accountOperationRepository;
        this.dtoMapper = dtoMapper;
        this.bankAccountMapperImpl = bankAccountMapperImpl;
    }


    @Override
    public CustomerDTO saveCustomer(CustomerDTO customerDTO) {
        log.info("Saving new Customer");
        Customer customer = dtoMapper.fromCustomerDTO(customerDTO);
        Customer savedCustomer = customerRepository.save(customer);
        return dtoMapper.fromCustomer(savedCustomer);
    }

    @Override
    public CurrentBankAccountDTO saveCurrentBankAccount(double initialBalance, double overDraft, String customerId) throws CustomerNotFoundException {
        Customer customer = customerRepository.findById(customerId).orElse(null);
        if (customer == null) {
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
        return dtoMapper.fromCurrentBankAccount(currentAccountSaved);
    }

    @Override
    public SavingBankAccountDTO saveSavingBankAccount(double initialBalance, double interestRate, String customerId) throws CustomerNotFoundException {
        Customer customer = customerRepository.findById(customerId).orElse(null);
        if (customer == null) {
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
        return dtoMapper.fromSavingBankAccount(savingAccountSaved);
    }

    @Override
    public BankAccountDTO updateBankAccount(String accountId, AccountStatus accountStatus) {
        return null;
    }

    @Override
    public List<CustomerDTO> listCustomer() {
        List<Customer> customers = customerRepository.findAll();
        List<CustomerDTO> customerDTOS = customers.stream()
                .map(customer -> dtoMapper.fromCustomer(customer)).collect(Collectors.toList());
        return customerDTOS;
    }

    @Override
    public BankAccount getBankAccountById(String id) throws BankAccountNotFoundException {
        BankAccount bankAccount = bankAccountRepository.findById(id)
                .orElseThrow(() -> new BankAccountNotFoundException("No Bank Account with this id: " + id));
        if (bankAccount instanceof SavingAccount savingAccount) {
            return savingAccount;
        } else {
            CurrentAccount currentAccount = (CurrentAccount) bankAccount;
            return currentAccount;
        }
    }


    @Override
    public void debit(String accountId, double amount, String description) throws BalanceNotSufficientException, BankAccountNotFoundException {
        BankAccount bankAccount = getBankAccountById(accountId);

        if (bankAccount.getBalance() < amount) {
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
        debit(accountIdSource, amount, "description");
        AccountOperation accountDestOperationDTO = new AccountOperation();
        accountDestOperationDTO.setAmount(amount);
        credit(accountIdDestination, amount, "description");
    }

    @Override
    public List<BankAccount> getListBankAccounts() {
        return bankAccountRepository.findAll();
    }

    @Override
    public CustomerDTO getCustomer(String customerId) throws CustomerNotFoundException {
        Customer customer = customerRepository.findById(customerId).orElseThrow(() -> new CustomerNotFoundException("Customer not found with this id : " + customerId));
        return bankAccountMapperImpl.fromCustomer(customer);
    }

    @Override
    public CustomerDTO updateCustomer(CustomerDTO customerDTO) {
        log.info("Updating a Customer");
        Customer customer = dtoMapper.fromCustomerDTO(customerDTO);
        Customer savedCustomer = customerRepository.save(customer);
        return dtoMapper.fromCustomer(savedCustomer);
    }

    @Override
    public void deleteCustomer(String customerId) throws CustomerNotFoundException {
        customerRepository.findById(customerId).orElseThrow(
                () -> new CustomerNotFoundException("Customer not found with this id: " + customerId));
        customerRepository.deleteById(customerId);
    }

    @Override
    public List<Customer> searchCustomers(String keyword) {
        return List.of();
    }


}