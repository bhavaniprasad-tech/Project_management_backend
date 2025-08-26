package com.bhavani.service;

import jakarta.mail.MessagingException;
import org.springframework.stereotype.Service;

public interface EmailService {

     default void sendEmailWithToken(String userEmail, String link) throws MessagingException {

    }
}
