package esb;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class DomesticOrderRouter {

    @Autowired
    private NextDayShippingActivator nextDayShippingActivator;

    @Autowired
    private NormalShippingActivator normalShippingActivator;

    public void routeDomesticOrder(Order order) {
        if (order.getAmount() > 175.0) {
            System.out.println("Routing domestic order " + order.getOrderNumber() +
                    " to Next Day Shipping (amount: $" + order.getAmount() + " > $175)");
            nextDayShippingActivator.ship(order);
        } else {
            System.out.println("Routing domestic order " + order.getOrderNumber() +
                    " to Normal Shipping (amount: $" + order.getAmount() + " ≤ $175)");
            normalShippingActivator.ship(order);
        }
    }
}