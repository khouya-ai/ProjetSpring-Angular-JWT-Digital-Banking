package khouya.site.banking.dtos;

import khouya.site.banking.enums.OperationType;
import lombok.Data;

import java.util.Date;

@Data
public class AccountOperationDTO {

    private Long id;
    private Date operationDate;
    private double amount;
    private OperationType operationType;
    private String accountId;
    private String description;
}
