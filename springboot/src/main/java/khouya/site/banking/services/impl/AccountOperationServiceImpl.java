package khouya.site.banking.services.impl;

import khouya.site.banking.entities.AccountOperation;
import khouya.site.banking.repositories.AccountOperationRepository;
import khouya.site.banking.services.AccountOperationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AccountOperationServiceImpl implements AccountOperationService {
    @Autowired
    private AccountOperationRepository accountOperationRepository;

    @Override
    public AccountOperation saveOperation(AccountOperation operation) {
        return accountOperationRepository.save(operation);
    }

    @Override
    public List<AccountOperation> listOperations() {
        return accountOperationRepository.findAll();
    }

    @Override
    public AccountOperation getOperation(Long id) {
        return accountOperationRepository.findById(id).orElse(null);
    }

    @Override
    public AccountOperation updateOperation(AccountOperation operation) {
        return accountOperationRepository.save(operation);
    }

    @Override
    public void deleteOperation(Long id) {
        accountOperationRepository.deleteById(id);
    }
} 