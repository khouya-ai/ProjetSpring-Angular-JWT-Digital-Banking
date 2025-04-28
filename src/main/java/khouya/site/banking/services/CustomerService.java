package khouya.site.banking.services;

import khouya.site.banking.entities.Customer;
import java.util.List;

public interface CustomerService {
    Customer saveCustomer(Customer customer);
    List<Customer> listCustomers();
    Customer getCustomer(String id);
    Customer updateCustomer(Customer customer);
    void deleteCustomer(String id);
} 