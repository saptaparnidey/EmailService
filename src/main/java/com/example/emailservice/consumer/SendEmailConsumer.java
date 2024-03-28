package com.example.emailservice.consumer;

import com.example.emailservice.dto.SendEmailMessageDto;
import com.example.emailservice.utils.EmailUtil;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;
import java.util.Properties;

import javax.mail.Authenticator;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;

@Service
public class SendEmailConsumer {

    @Autowired
    private ObjectMapper objectMapper;

    @KafkaListener(topics = "sendEmail", groupId = "emailService")
    public void handleSendEmail(String message){
        try {
            SendEmailMessageDto sendEmailMessageDto = objectMapper.readValue(message, SendEmailMessageDto.class);

            Properties props = new Properties();
            props.put("mail.smtp.host", "smtp.gmail.com"); //SMTP Host
            props.put("mail.smtp.port", "587"); //TLS Port
            props.put("mail.smtp.auth", "true"); //enable authentication
            props.put("mail.smtp.starttls.enable", "true"); //enable STARTTLS

            //create Authenticator object to pass in Session.getInstance argument
            Authenticator auth = new Authenticator() {
                //override the getPasswordAuthentication method
                protected PasswordAuthentication getPasswordAuthentication() {
                    return new PasswordAuthentication("saptaparnidey15@gmail.com", "bkmg dbjj bvtv uimy");
                }
            };
            Session session = Session.getInstance(props, auth);

            EmailUtil.sendEmail(session, sendEmailMessageDto.getTo(), sendEmailMessageDto.getSubject(), sendEmailMessageDto.getBody());

        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }
}
