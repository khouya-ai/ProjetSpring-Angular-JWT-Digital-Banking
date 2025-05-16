package khouya.site.banking;

import khouya.site.banking.dtos.CustomerDTO;
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
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.stream.Stream;

@SpringBootApplication
public class BankingApplication {

    public static void main(String[] args) {
        SpringApplication.run(BankingApplication.class, args);
    }

    @Bean
    CommandLineRunner start(BankAccountService bankAccountService) {
        return args -> {


            Stream.of("Soulaimane", "Ismail", "Sara", "Youssef").forEach(name -> {
                CustomerDTO customerDTO = new CustomerDTO();
                customerDTO.setId(UUID.randomUUID().toString());
                customerDTO.setName(name);
                customerDTO.setEmail(name + "@gmail.com");
                bankAccountService.saveCustomer(customerDTO);
            });
            bankAccountService.listCustomer().forEach(customer -> {
                try {
                    bankAccountService.saveCurrentBankAccount(Math.random() * 90000, 9000, customer.getId());
                    bankAccountService.saveSavingBankAccount(Math.random() * 85000, 3.2, customer.getId());
                } catch (CustomerNotFoundException e) {
                    e.printStackTrace();
                }
            });

            try {
                List<BankAccount> bankAccountList = bankAccountService.getListBankAccounts();
                for (BankAccount bankAccount : bankAccountList) {
                    for (int i = 0; i < 10; i++) {
                        String accountId = bankAccount.getId();

                        bankAccountService.credit(
                                accountId,
                                10000 + Math.random() * 120000,
                                "Credit");

                        bankAccountService.debit(
                                accountId,
                                1000 + Math.random() * 9000,
                                "Debit");

                    }
                }
            } catch (BalanceNotSufficientException | BankAccountNotFoundException e) {
                e.printStackTrace();
            }
        };
    }

    //    @Bean
    CommandLineRunner start(CustomerRepository customerRepository,
                            BankAccountRepository bankAccountRepository,
                            AccountOperationRepository accountOperationRepository) {
        return args -> {
            Stream.of("Hassan", "Yassine", "Aicha").forEach(name -> {
                Customer customer = new Customer();
                customer.setId(UUID.randomUUID().toString());
                customer.setName(name);
                customer.setEmail(name + "@gmail.com");
                customerRepository.save(customer);
            });

            customerRepository.findAll().forEach(customer -> {
                CurrentAccount currentAccount = new CurrentAccount();
                currentAccount.setId(UUID.randomUUID().toString());
                currentAccount.setBalance(Math.random() * 90000);
                currentAccount.setCreatedAt(new Date());
                currentAccount.setStatus(AccountStatus.CREATED);
                currentAccount.setCustomer(customer);
                currentAccount.setOverDraft(9000);
                bankAccountRepository.save(currentAccount);

                SavingAccount savingAccount = new SavingAccount();
                savingAccount.setId(UUID.randomUUID().toString());
                savingAccount.setBalance(Math.random() * 120000);
                savingAccount.setCreatedAt(new Date());
                savingAccount.setStatus(AccountStatus.CREATED);
                savingAccount.setCustomer(customer);
                savingAccount.setInterestRate(5.5);
                bankAccountRepository.save(savingAccount);
            });

            bankAccountRepository.findAll().forEach(acc -> {
                for (int i = 0; i < 5; i++) {
                    AccountOperation accountOperation = new AccountOperation();
                    accountOperation.setDate(new Date());
                    accountOperation.setAmount(Math.random() * 12000);
                    accountOperation.setType(Math.random() > 0.5 ? OperationType.DEBIT : OperationType.CREDIT);
                    accountOperation.setBankAccount(acc);
                    accountOperationRepository.save(accountOperation);
                }
            });

            BankAccount ba = bankAccountRepository.findAll().getFirst();
            System.out.println("***************************************");
            System.out.println("Bank Account ID: " + ba.getId());
            System.out.println("Bank Account Balance: " + ba.getBalance());
            System.out.println("Bank Account Created At: " + ba.getCreatedAt());
            System.out.println("Bank Account Status: " + ba.getStatus());
            System.out.println("Bank Account Customer: " + ba.getCustomer().getName());
            if (ba instanceof SavingAccount) {
                System.out.println("Saving Account Interest Rate: " + ((SavingAccount) ba).getInterestRate());
            } else if (ba instanceof CurrentAccount) {
                System.out.println("Current Account Overdraft: " + ((CurrentAccount) ba).getOverDraft());
            }
            System.out.println("Operations: ");

            ba.getAccountOperations().forEach(op ->
                    System.out.printf("%d %s  %.2f %s%n", op.getId(), op.getDate(), op.getAmount(), op.getType())
            );

        };
    }

}
