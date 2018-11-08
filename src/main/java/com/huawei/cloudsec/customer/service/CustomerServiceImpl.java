package com.huawei.cloudsec.customer.service;

import com.huawei.cloudsec.customer.dao.ICustomerDao;
import com.huawei.cloudsec.customer.elasticsearch.document.Customer;
import com.huawei.cloudsec.customer.vo.CustomerVO;
import org.elasticsearch.common.util.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 客户信息
 *
 * @author Ankong
 */
@Component
public class CustomerServiceImpl implements ICustomerService {
    @Autowired
    private ICustomerDao customerDao;

    @Override
    public void saveCustomer(Customer info) {
        customerDao.save(info);
    }

    @Override
    public void deleteAllCustomers() {
        this.customerDao.deleteAll();
    }

    @Override
    public List<Customer> findAllCustomers() {
        return CollectionUtils.iterableAsArrayList(this.customerDao.findAll());
    }

    @Override
    public Customer findByFirstName(String firstName) {
        return this.customerDao.findByFirstName(firstName);
    }

    @Override
    public List<Customer> findByLastName(String lastName) {
        return this.customerDao.findByLastName(lastName);
    }

    @Override
    public Page<Customer> findCustomersByPage(CustomerVO vo, Pageable pageable) {
        return this.customerDao.search(vo, pageable);
    }
}
