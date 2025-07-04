package contacts.integration;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import contacts.domain.Contact;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Component;

@Component
public class JmsSender {
    @Autowired
    JmsTemplate jmsTemplate;

    public void sendMessage(Contact contact) {
        try {
            // convert person to JSON string
            ObjectMapper objectMapper = new ObjectMapper();
            String contactString = objectMapper.writeValueAsString(contact);
            System.out.println("Sending a JMS message:" + contactString);
            jmsTemplate.convertAndSend("bookQueue", contactString);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
    }
}
