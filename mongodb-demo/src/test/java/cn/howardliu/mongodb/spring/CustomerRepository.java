package cn.howardliu.mongodb.spring;

import org.springframework.data.repository.Repository;

import java.math.BigInteger;
import java.util.List;

/**
 * <br>created at 17-2-27
 *
 * @author liuxh
 * @since 1.0.0
 */
public interface CustomerRepository extends Repository<Customer, BigInteger> {
    Customer findOne(BigInteger id);

    List<Customer> findAll();

    Customer save(Customer customer);

    Customer findByEmailAddress(EmailAddress emailAddress);
}
