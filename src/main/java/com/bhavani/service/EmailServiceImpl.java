package com.bhavani.service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailException;
import org.springframework.mail.MailSendException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@Service
public class EmailServiceImpl implements EmailService {

    @Autowired
    private JavaMailSender mailSender;
    @Autowired
    private JavaMailSender javaMailSender;

    @Override
    public void sendEmailWithToken(String userEmail, String link) throws MessagingException {
        MimeMessage mimeMessage = javaMailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(mimeMessage,"utf-8");

        String subject = "Join Project Team Invitation";
        String text = "Click the link  to join this project team";

        // 🔑 Use HTML content
        String htmlContent = "<p>You have been invited to join a project team.</p>"
                + "<p>Click below to join:</p>"
                + "<a href=\"" + link + "\" style=\"display:inline-block;padding:10px 15px;"
                + "background-color:#4CAF50;color:white;text-decoration:none;"
                + "border-radius:5px;\">Join Project</a>";

        helper.setSubject(subject);
        helper.setText(text, true);
        helper.setText(htmlContent, true);
        helper.setTo(userEmail);

        try{
            mailSender.send(mimeMessage);
        }
        catch (MailSendException e){
            throw new MailSendException("Failed to send Email");
        }
    }
}
