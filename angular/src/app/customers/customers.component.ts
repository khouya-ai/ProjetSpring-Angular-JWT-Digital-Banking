import {Component, OnInit} from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {catchError, Observable} from 'rxjs';
import {Customer} from '../model/customer.model';
import {AsyncPipe, NgForOf, NgIf} from '@angular/common';
import {CustomerService} from '../services/customer.service';

@Component({
  selector: 'app-customers',
  imports: [
    AsyncPipe,
    NgForOf,
    NgIf
  ],
  templateUrl: './customers.component.html',
  styleUrl: './customers.component.css'
})
export class CustomersComponent implements OnInit {
  customers! : Observable<Array<Customer>>;

  constructor(private customerService:CustomerService) { }
  ngOnInit(): void {
    this.customers=this.customerService.getCustomers();
  }


  handleDeleteCustomer(c:any) {

  }

  handleCustomerAccounts(c:any) {

  }
}
