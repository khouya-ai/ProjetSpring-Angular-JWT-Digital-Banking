package khouya.site.banking;

import khouya.site.banking.entities.*;
import khouya.site.banking.repositories.BankAccountRepository;
import khouya.site.banking.repositories.CustomerRepository;
import khouya.site.banking.repositories.AccountOperationRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.util.Date;
import java.util.UUID;
import java.util.stream.Stream;

@SpringBootApplication
public class BankingApplication {

	public static void main(String[] args) {
		SpringApplication.run(BankingApplication.class, args);
	}

	@Bean
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
//				ba.getAccountOperations().forEach(op -> {
//					System.out.println("Operation ID: " + op.getId());
//					System.out.println("Operation Date: " + op.getDate());
//					System.out.println("Operation Amount: " + op.getAmount());
//					System.out.println("Operation Type: " + op.getType());
//				});
		};
	}

}
