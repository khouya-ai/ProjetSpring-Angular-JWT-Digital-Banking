export interface AccountDetails {
  accountId: string;
  balance: number;
  currentPage: number;
  totalPages: number;
  pageSize: number;
  accountOperationDTOList: AccountOperation[];
}

export interface AccountOperation {
  id: number;
  operationDate: Date;
  amount: number;
  type: string;
  description: string;
}

export interface Account {
  type: 'CurrentAccount' | 'SavingAccount';
  id: string;
  balance: number;
  createdDate: string;
  status: string;
  interestRate?: number;  // Only for SavingAccount
  overDraft?: number;     // Only for CurrentAccount
}
