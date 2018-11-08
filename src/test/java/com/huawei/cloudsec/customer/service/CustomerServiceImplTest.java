package com.huawei.cloudsec.customer.service;

import com.huawei.cloudsec.customer.elasticsearch.document.Customer;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest
public class CustomerServiceImplTest {

    @Autowired
    private ICustomerService customerService;

    @Test
    public void saveCustomer() {
        this.customerService.deleteAllCustomers();
        this.customerService.saveCustomer(new Customer("Alice", "Smith", "192.168.1.23"));
        this.customerService.saveCustomer(new Customer("Bob", "Smith", "192.168.2.34"));
    }

    @Test
    public void deleteAllCustomers() {
        this.customerService.deleteAllCustomers();
    }

    @Test
    public void findAllCustomers() {
        this.customerService.deleteAllCustomers();
        this.customerService.saveCustomer(new Customer("Alice", "Smith", "192.168.1.23"));
        this.customerService.saveCustomer(new Customer("Bob", "Smith", "192.168.1.23"));
        List<Customer> allCustomers = this.customerService.findAllCustomers();
        for (Customer customer : allCustomers) {
            System.out.println(customer);
        }
        //Iterable<Customer> itCustomers = this.customerService.findAllCustomers();
        //ArrayList<Customer> customers = CollectionUtils.iterableAsArrayList(itCustomers);
        System.out.println(allCustomers);
        System.out.println("Number:" + allCustomers.size());
    }

    @Test
    public void findByFirstName() {
        System.out.println("Customer found with findByFirstName('Alice'):");
        System.out.println("--------------------------------");
        Customer existCustomer = this.customerService.findByFirstName("Alice");
        System.out.println(existCustomer);
        assertNotNull(existCustomer);

        System.out.println("Customer found with findByFirstName('anonymous'):");
        System.out.println("--------------------------------");
        Customer notExitst = this.customerService.findByFirstName("anonymous");

        assertNull(notExitst);
        System.out.println(notExitst);
        System.out.println("--------------------------------end");
    }

    @Test
    public void findByLastName() {
        System.out.println("Customers found with findByLastName('Smith'):");
        System.out.println("--------------------------------");
        for (Customer customer : this.customerService.findByLastName("Smith")) {
            System.out.println(customer);
        }
    }
}