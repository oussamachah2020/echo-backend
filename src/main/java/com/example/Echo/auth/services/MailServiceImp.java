package com.example.Echo.auth.services;

import com.example.Echo.auth.models.dto.MailDto;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@Service
public class MailServiceImp  {
    @Autowired
    private JavaMailSender javaMailSender;

    @Value("${spring.mail.username}") private String sender;





    public String sendHtmlMail(MailDto details) {
        try {
            MimeMessage message = javaMailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

            helper.setFrom("oussamachahidi20@gmail.com");
            helper.setTo(details.getRecipient());
            helper.setSubject(details.getSubject());
            helper.setText(buildEmailTemplate(details.getRecipient()), true); // Enable HTML

            javaMailSender.send(message);
            return "Mail Sent Successfully...";
        } catch (MessagingException e) {
            e.printStackTrace();
            return "Error while Sending Mail";
        }
    }


        private String buildEmailTemplate(String username) {
            return """
                    <!DOCTYPE html>
                        <html>
                        <head>
                          <meta charset="UTF-8">
                          <meta name="viewport" content="width=device-width, initial-scale=1.0">
                          <title>Account Confirmation</title>
                          <style>
                            body {
                              font-family: Arial, sans-serif;
                              margin: 0;
                              padding: 0;
                              background-color: #f4f4f4;
                              text-align: center;
                            }
                            .container {
                              max-width: 600px;
                              background: #fff;
                              margin: 20px auto;
                              padding: 20px;
                              border-radius: 8px;
                              box-shadow: 0 0 10px rgba(0, 0, 0, 0.1);
                            }
                            .button {
                              display: inline-block;
                              padding: 10px 20px;
                              background: #007bff;
                              color: #fff;
                              text-decoration: none;
                              border-radius: 5px;
                              margin-top: 20px;
                            }
                          </style>
                        </head>
                        <body>
                          <div class="container">
                            <h2>Confirm Your Account</h2>
                            <p>Thank you for signing up! Click the button below to confirm your account.</p>
                            <a href="http://localhost:3000" class="button">Confirm Account</a>
                            <p>If you did not request this, please ignore this email.</p>
                          </div>
                        </body>
                        </html>
                """;
        }


}
