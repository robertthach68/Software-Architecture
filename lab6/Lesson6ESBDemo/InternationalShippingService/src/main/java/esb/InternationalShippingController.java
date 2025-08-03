package esb;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class InternationalShippingController {

    @PostMapping("/orders")
    public ResponseEntity<?> receiveOrder(@RequestBody Order order) {
        System.out.println("International Shipping Service receiving order: " + order);
        System.out.println("Processing international shipping for order: " + order.getOrderNumber() +
                " with amount: $" + order.getAmount() + " and type: " + order.getOrderType());
        return new ResponseEntity<Order>(order, HttpStatus.OK);
    }
}