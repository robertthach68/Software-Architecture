package esb;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.integration.annotation.Router;
import org.springframework.stereotype.Component;

@Component
public class OrderTypeRouter {

    @Autowired
    private InternationalShippingActivator internationalShippingActivator;

    @Autowired
    private DomesticOrderRouter domesticOrderRouter;

    @Router(inputChannel = "shippingchannel")
    public String routeByOrderType(Order order) {
        if ("international".equalsIgnoreCase(order.getOrderType())) {
            System.out.println("Routing international order " + order.getOrderNumber() +
                    " to International Shipping Service");
            internationalShippingActivator.ship(order);
            return "internationalShippingChannel";
        } else {
            System.out.println("Routing domestic order " + order.getOrderNumber() +
                    " to Domestic Order Router");
            domesticOrderRouter.routeDomesticOrder(order);
            return "domesticOrderChannel";
        }
    }
}