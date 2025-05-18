import {Component, OnInit} from '@angular/core';
import {Customer} from '../model/customer.model';
import {ActivatedRoute, Router} from '@angular/router';
import {DatePipe, DecimalPipe, JsonPipe, NgForOf, NgIf, SlicePipe} from '@angular/common';
import {AccountService} from '../services/account.service';
import {Account, AccountDetails} from '../model/account.model';

@Component({
  selector: 'app-customer-accounts',
  imports: [
    NgIf,
    NgForOf,
    DecimalPipe,
    DatePipe
  ],
  templateUrl: './customer-accounts.component.html',
  styleUrl: './customer-accounts.component.css',
  standalone: true
})
export class CustomerAccountsComponent implements OnInit {
  customerId! : string ;
  customer! : Customer;
  accounts: Account[] = [];
  errorMessage!: string;

  constructor(
    private route : ActivatedRoute,
    private router :Router,
    private accountService: AccountService
  ) {
    this.customer=this.router.getCurrentNavigation()?.extras.state as Customer;
  }

  ngOnInit(): void {
    this.customerId = this.route.snapshot.params['id'];
    this.loadCustomerAccounts();
  }

  loadCustomerAccounts() {
    this.accountService.getCustomerAccounts(this.customerId).subscribe({
      next: (data) => {
        this.accounts = data;
      },
      error: (err) => {
        this.errorMessage = err.message;
      }
    });
  }
}
