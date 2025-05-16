package khouya.site.banking.controllers;

import khouya.site.banking.entities.BankAccount;
import khouya.site.banking.services.BankAccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/bank-accounts")
public class BankAccountController {
    @Autowired
    private BankAccountService bankAccountService;

} 