package contacts.web;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class BookReceiverController {

    @GetMapping("/status")
    public String getStatus() {
        return "BookReceiver is running and listening for book messages on bookQueue";
    }
}