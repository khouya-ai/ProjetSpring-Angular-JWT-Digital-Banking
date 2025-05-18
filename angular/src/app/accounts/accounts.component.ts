import {Component, OnInit} from '@angular/core';
import {FormBuilder, FormGroup, ReactiveFormsModule} from "@angular/forms";
import {catchError, Observable, throwError} from "rxjs";
import {AccountDetails} from '../model/account.model';
import {AsyncPipe, DatePipe, DecimalPipe, NgClass, NgForOf, NgIf} from '@angular/common';
import {AccountService} from '../services/account.service';



@Component({
  selector: 'app-accounts',
  imports: [
    ReactiveFormsModule,
    AsyncPipe,
    DecimalPipe,
    DatePipe,
    NgClass,
    NgIf,
    NgForOf
  ],
  templateUrl: './accounts.component.html',
  styleUrl: './accounts.component.css'
})
export class AccountsComponent implements OnInit {
  accountFormGroup!: FormGroup;
  currentPage: number = 0;
  pageSize: number = 5;
  accountObservable!: Observable<AccountDetails>
  operationFromGroup!: FormGroup;
  errorMessage!: string;

  constructor(private fb: FormBuilder, private accountService: AccountService) {
  }

  ngOnInit(): void {
    this.accountFormGroup = this.fb.group({
      accountId: this.fb.control('')
    });
    this.operationFromGroup = this.fb.group({
      operationType: this.fb.control(null),
      amount: this.fb.control(0),
      description: this.fb.control(null),
      accountDestination: this.fb.control(null)
    })
  }

  handleSearchAccount() {
    let accountId: string = this.accountFormGroup.value.accountId;
    this.accountObservable = this.accountService.getAccount(accountId, this.currentPage, this.pageSize).pipe(
      catchError(err => {
        this.errorMessage = err.message;
        return throwError(err);
      })
    );
  }

  gotoPage(page: number) {
    this.currentPage = page;
    this.handleSearchAccount();
  }


}
