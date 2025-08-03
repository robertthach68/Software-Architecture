package esb;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.integration.annotation.Router;
import org.springframework.stereotype.Component;

@Component
public class ShippingRouter {

    @Autowired
    private NextDayShippingActivator nextDayShippingActivator;

    @Autowired
    private NormalShippingActivator normalShippingActivator;

    @Router(inputChannel = "shippingchannel")
    public String routeOrder(Order order) {
        if (order.getAmount() > 175.0) {
            System.out.println("Routing order " + order.getOrderNumber() + " to Next Day Shipping (amount: $"
                    + order.getAmount() + ")");
            nextDayShippingActivator.ship(order);
            return "nextDayShippingChannel";
        } else {
            System.out.println("Routing order " + order.getOrderNumber() + " to Normal Shipping (amount: $"
                    + order.getAmount() + ")");
            normalShippingActivator.ship(order);
            return "normalShippingChannel";
        }
    }
}