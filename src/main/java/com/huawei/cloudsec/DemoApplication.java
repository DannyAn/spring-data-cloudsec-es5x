package com.huawei.cloudsec;

import com.huawei.cloudsec.customer.elasticsearch.document.Customer;
import com.huawei.cloudsec.customer.elasticsearch.repository.CustomerRepository;
import com.huawei.cloudsec.customer.service.ICustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class DemoApplication implements CommandLineRunner {

    //	public static void main(String[] args) {
//		SpringApplication.run(DemoApplication.class, args);
//	}
    @Autowired
    private CustomerRepository repository;

    @Autowired
    private ICustomerService customerService;

    @Override
    public void run(String... args) throws Exception {
//        this.customerService.deleteAllCustomers();
//        saveCustomers();
//        fetchAllCustomers();
//        fetchIndividualCustomers();
    }

    private void saveCustomers() {
    }

    private void fetchAllCustomers() {
        System.out.println("Customers found with findAll():");
        System.out.println("-------------------------------");
        for (Customer customer : this.customerService.findAllCustomers()) {
            System.out.println(customer);
        }
        System.out.println();
    }

    private void fetchIndividualCustomers() {
        System.out.println("Customer found with findByFirstName('Alice'):");
        System.out.println("--------------------------------");
        System.out.println(this.repository.findByFirstName("Alice"));

        System.out.println("Customers found with findByLastName('Smith'):");
        System.out.println("--------------------------------");
        for (Customer customer : this.repository.findByLastName("Smith")) {
            System.out.println(customer);
        }
    }

    public static void main(String[] args) {
        SpringApplication.run(DemoApplication.class, "--debug").close();
    }
}
