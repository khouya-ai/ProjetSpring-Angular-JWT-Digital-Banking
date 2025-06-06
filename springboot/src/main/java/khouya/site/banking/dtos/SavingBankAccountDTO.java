package khouya.site.banking.dtos;

import khouya.site.banking.enums.AccountStatus;
import lombok.Data;

import java.util.Date;

@Data
public class SavingBankAccountDTO extends BankAccountDTO{

    private String id;
    private double balance;
    private Date createdDate;
    private AccountStatus status;
    private CustomerDTO customerDTO;
    private double interestRate;
}
