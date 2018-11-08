package com.huawei.cloudsec.customer.service;

import com.huawei.cloudsec.customer.elasticsearch.document.Customer;
import com.huawei.cloudsec.customer.vo.CustomerVO;
import org.elasticsearch.index.query.QueryBuilder;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface ICustomerService {
    void saveCustomer(Customer info);

    void deleteAllCustomers();

    List<Customer> findAllCustomers();

    Customer findByFirstName(String firstName);

    List<Customer> findByLastName(String lastName);

    Page<Customer> findCustomersByPage(CustomerVO vo, Pageable pageable);
}

