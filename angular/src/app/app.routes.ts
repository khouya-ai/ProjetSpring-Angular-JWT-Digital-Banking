import {Routes} from '@angular/router';
import {CustomersComponent} from './customers/customers.component';
import {AccountsComponent} from './accounts/accounts.component';
import {NewCustomerComponent} from './new-customer/new-customer.component';
import {CustomerAccountsComponent} from './customer-accounts/customer-accounts.component';
import {LoginComponent} from './login/login.component';
import {AdminTemplateComponent} from './admin-template/admin-template.component';
import {authorizationGuard} from './guards/authorization.guard';
import {authenticationGuard} from './guards/authentication.guard';
import {NoAuthorizedComponent} from './no-authorized/no-authorized.component';

export const routes: Routes = [
  { path :"", pathMatch:"full", redirectTo:"login" },
  { path :"login", component: LoginComponent },
  {path : "admin", component: AdminTemplateComponent, canActivate: [authenticationGuard],
    children: [
      {path: "customers", component: CustomersComponent},
      {path: "accounts", component: AccountsComponent},
      {path: "new-customer", component: NewCustomerComponent, canActivate:[authorizationGuard],data:{roles: ["ADMIN"]}},
      {path: "customer-accounts/:id", component: CustomerAccountsComponent},
      {path: "notAuthorized", component: NoAuthorizedComponent}
    ]},
];
