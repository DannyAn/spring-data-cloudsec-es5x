package com.huawei.cloudsec.customer.dao;

import com.huawei.cloudsec.customer.elasticsearch.document.Customer;
import com.huawei.cloudsec.customer.vo.CustomerVO;
import org.elasticsearch.index.query.QueryBuilder;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface ICustomerDao {
    Iterable<Customer> findAll();
    void deleteAll();
    void save(Customer info);
    Customer findByFirstName(String firstName);
    List<Customer> findByLastName(String lastName);
    Page<Customer> search(CustomerVO vo, Pageable pageable);
    long count();
}
