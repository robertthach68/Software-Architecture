package contacts.integration;

import com.fasterxml.jackson.databind.ObjectMapper;
import contacts.domain.BookMessage;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;

@Component
public class BookJmsListener {

    @JmsListener(destination = "bookQueue")
    public void receiveBookMessage(String bookMessageString) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            BookMessage bookMessage = objectMapper.readValue(bookMessageString, BookMessage.class);

            System.out.println("Received Book Message:");
            System.out.println("Operation: " + bookMessage.getOperation());
            System.out.println("Book: " + bookMessage.getBook());
            System.out.println("----------------------------------------");

        } catch (Exception e) {
            System.err.println("Error processing book message: " + e.getMessage());
            e.printStackTrace();
        }
    }
}