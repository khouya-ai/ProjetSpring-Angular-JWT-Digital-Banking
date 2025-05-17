package khouya.site.banking.controllers;

import khouya.site.banking.entities.AccountOperation;
import khouya.site.banking.services.AccountOperationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/operations")
public class AccountOperationController {
    @Autowired
    private AccountOperationService accountOperationService;

    @PostMapping
    public AccountOperation saveOperation(@RequestBody AccountOperation operation) {
        return accountOperationService.saveOperation(operation);
    }

    @GetMapping
    public List<AccountOperation> listOperations() {
        return accountOperationService.listOperations();
    }

    @GetMapping("/{id}")
    public AccountOperation getOperation(@PathVariable Long id) {
        return accountOperationService.getOperation(id);
    }

    @PutMapping("/{id}")
    public AccountOperation updateOperation(@PathVariable Long id, @RequestBody AccountOperation operation) {
        operation.setId(id);
        return accountOperationService.updateOperation(operation);
    }

    @DeleteMapping("/{id}")
    public void deleteOperation(@PathVariable Long id) {
        accountOperationService.deleteOperation(id);
    }
} 