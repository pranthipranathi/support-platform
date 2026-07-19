package com.saas.support.notification.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import jakarta.mail.internet.MimeMessage;

@Slf4j
@Service
@ConditionalOnProperty(name = "spring.mail.host", havingValue = "smtp.gmail.com")
public class EmailService {

    private final JavaMailSender mailSender;

    @Value("${app.mail.from:noreply@support.com}")
    private String fromEmail;

    @Value("${app.mail.from-name:Support Platform}")
    private String fromName;

    public EmailService(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    @Async
    public void sendEmail(String to, String subject, String body) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");
            helper.setFrom(fromEmail, fromName);
            helper.setTo(to);
            helper.setSubject(subject);
            helper.setText(body, true);
            mailSender.send(message);
            log.info("Email sent to: {}", to);
        } catch (Exception e) {
            log.error("Failed to send email: {}", e.getMessage());
        }
    }

    @Async
    public void sendTicketCreatedEmail(String to, String customerName,
                                       String ticketSubject, String ticketId) {
        String subject = "Support Ticket Created - " + ticketSubject;
        String body = "<html><body><h2>Hello " + customerName + ",</h2>" +
                "<p>Your support ticket has been created.</p>" +
                "<p><b>Subject:</b> " + ticketSubject + "</p>" +
                "<p><b>Ticket ID:</b> " + ticketId + "</p>" +
                "<p>Our team will respond within 24 hours.</p></body></html>";
        sendEmail(to, subject, body);
    }

    @Async
    public void sendTicketResolvedEmail(String to, String customerName,
                                        String ticketSubject, String ticketId) {
        String subject = "Ticket Resolved - " + ticketSubject;
        String body = "<html><body><h2>Hello " + customerName + ",</h2>" +
                "<p>Your support ticket has been resolved.</p>" +
                "<p><b>Subject:</b> " + ticketSubject + "</p>" +
                "<p><b>Ticket ID:</b> " + ticketId + "</p></body></html>";
        sendEmail(to, subject, body);
    }

    @Async
    public void sendTicketAssignedEmail(String to, String agentName,
                                        String ticketSubject, String ticketId) {
        String subject = "Ticket Assigned - " + ticketSubject;
        String body = "<html><body><h2>Hello " + agentName + ",</h2>" +
                "<p>A ticket has been assigned to you.</p>" +
                "<p><b>Subject:</b> " + ticketSubject + "</p>" +
                "<p><b>Ticket ID:</b> " + ticketId + "</p></body></html>";
        sendEmail(to, subject, body);
    }
}