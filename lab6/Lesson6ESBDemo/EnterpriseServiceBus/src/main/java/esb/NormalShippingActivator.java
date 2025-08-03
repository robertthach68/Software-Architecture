package esb;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.client.RestTemplate;

public class NormalShippingActivator {
    @Autowired
    RestTemplate restTemplate;

    public void ship(Order order) {
        System.out.println("Normal Shipping: " + order.toString());
        restTemplate.postForLocation("http://localhost:8083/orders", order);
    }
}