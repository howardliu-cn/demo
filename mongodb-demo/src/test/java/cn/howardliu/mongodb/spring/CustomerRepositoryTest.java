package cn.howardliu.mongodb.spring;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.math.BigInteger;
import java.util.List;

/**
 * <br>created at 17-2-27
 *
 * @author liuxh
 * @since 1.0.0
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath*:spring-mongoDB.xml")
public class CustomerRepositoryTest {
    @Autowired
    private CustomerRepository customerRepository;

    @Test
    public void testFindOne() throws Exception {
        Customer customer = customerRepository.findOne(new BigInteger("27452075600723659662595972552"));
        System.out.println(customer);
    }

    @Test
    public void testFindAll() throws Exception {
        List<Customer> all = customerRepository.findAll();
        System.out.println(all);
    }

    @Test
    public void testSave() throws Exception {
        customerRepository.save(new Customer("Howard", "Liu"));
    }

    @Test
    public void testFindByEmailAddress() throws Exception {
    }
}