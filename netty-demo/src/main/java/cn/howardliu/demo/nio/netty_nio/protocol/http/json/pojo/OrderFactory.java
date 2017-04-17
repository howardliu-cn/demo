package cn.howardliu.demo.nio.netty_nio.protocol.http.json.pojo;

/**
 * <br>created at 17-4-17
 *
 * @author liuxh
 * @version 1.0.0
 * @since 1.0.0
 */
public class OrderFactory {
    public static Order create(long orderID) {
        Order order = new Order();
        order.setOrderNumber(orderID);
        order.setTotal(9999.999f);
        Address address = new Address();
        address.setCity("北京市");
        address.setCountry("中国");
        address.setPostCode("102600");
        address.setState("北京市");
        address.setStreet1("王府井大街");
        order.setBillTo(address);
        Customer customer = new Customer();
        customer.setCustomerNumber(orderID);
        customer.setFirstName("Howard");
        customer.setLastName("Liu");
        order.setCustomer(customer);
        order.setShipping(Shipping.INTERNATIONAL_MAIL);
        order.setShipTo(address);
        return order;
    }
}
