package khouya.site.banking.services;

import khouya.site.banking.entities.AccountOperation;
import java.util.List;

public interface AccountOperationService {
    AccountOperation saveOperation(AccountOperation operation);
    List<AccountOperation> listOperations();
    AccountOperation getOperation(Long id);
    AccountOperation updateOperation(AccountOperation operation);
    void deleteOperation(Long id);
} 