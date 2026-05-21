package com.service;
 

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;

/**
 * Assignment 4 - Email Service
 *
 * Uses Spring's JavaMailSender to send a registration success email.
 * Sends HTML email to the address the user provided in the form.
 *
 * Flow:
 *  1. Controller calls sendRegistrationEmail() after form validates
 *  2. This service builds a MimeMessage (supports HTML)
 *  3. JavaMailSender sends it via Gmail SMTP
 *  4. User receives email at the provided address
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class EmailService {

    private final JavaMailSender mailSender;

    @Value("${spring.mail.username}")
    private String fromEmail;

    @Value("${app.mail.sender-name:Registration System}")
    private String senderName;

    /**
     * Sends a registration success email.
     *
     * @param toEmail  recipient email (from the form)
     * @param name     recipient name (for personalisation)
     * @param phone    phone number (included in the email)
     */
    public void sendRegistrationEmail(String toEmail, String name, String phone) {
        try {
            MimeMessage message = mailSender.createMimeMessage();

            // MimeMessageHelper — true = multipart (required for HTML)
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

            helper.setFrom(fromEmail, senderName);
            helper.setTo(toEmail);
            helper.setSubject("🎉 Registration Successful — Welcome, " + name + "!");

            // HTML email body
            String html = buildEmailHtml(name, toEmail, phone);
            helper.setText(html, true);  // true = isHtml

            mailSender.send(message);
            log.info("Registration email sent to: {}", toEmail);

        } catch (MessagingException e) {
            log.error("Failed to send email to {}: {}", toEmail, e.getMessage());
            throw new RuntimeException("Email could not be sent. Please try again.", e);
        } catch (Exception e) {
            log.error("Unexpected email error: {}", e.getMessage());
            throw new RuntimeException("Email service error: " + e.getMessage(), e);
        }
    }

    /** Builds a clean HTML email body */
    private String buildEmailHtml(String name, String email, String phone) {
        return """
            <!DOCTYPE html>
            <html>
            <head><meta charset="UTF-8"/></head>
            <body style="margin:0;padding:0;font-family:'Segoe UI',Arial,sans-serif;background:#f0f4f8;">
              <table width="100%%" cellpadding="0" cellspacing="0">
                <tr><td align="center" style="padding:40px 20px;">
                  <table width="560" cellpadding="0" cellspacing="0"
                         style="border-radius:16px;overflow:hidden;box-shadow:0 4px 20px rgba(0,0,0,.1);">
                    <!-- Header -->
                    <tr>
                      <td style="background:linear-gradient(135deg,#1e3a5f,#2563eb);
                                 padding:32px;text-align:center;color:#fff;">
                        <div style="font-size:3rem;margin-bottom:8px;">🎉</div>
                        <h2 style="margin:0;font-weight:800;">Registration Successful!</h2>
                        <p style="margin:8px 0 0;opacity:.85;">Welcome to the system, %s</p>
                      </td>
                    </tr>
                    <!-- Body -->
                    <tr>
                      <td style="background:#fff;padding:32px;">
                        <p style="color:#475569;line-height:1.7;">
                          Hi <strong>%s</strong>,<br><br>
                          Your registration has been completed successfully.
                          Here are your registered details:
                        </p>
                        <!-- Details table -->
                        <table width="100%%" style="border-collapse:collapse;margin:20px 0;">
                          <tr style="background:#f8fafc;">
                            <td style="padding:12px 16px;font-weight:600;color:#64748b;
                                       border-radius:8px 0 0 8px;">👤 Name</td>
                            <td style="padding:12px 16px;color:#1e293b;font-weight:500;">%s</td>
                          </tr>
                          <tr>
                            <td style="padding:12px 16px;font-weight:600;color:#64748b;">📧 Email</td>
                            <td style="padding:12px 16px;color:#1e293b;font-weight:500;">%s</td>
                          </tr>
                          <tr style="background:#f8fafc;">
                            <td style="padding:12px 16px;font-weight:600;color:#64748b;
                                       border-radius:8px 0 0 8px;">📱 Phone</td>
                            <td style="padding:12px 16px;color:#1e293b;font-weight:500;">%s</td>
                          </tr>
                        </table>
                        <p style="color:#64748b;font-size:.88rem;margin-top:24px;">
                          If you did not register, please ignore this email.
                        </p>
                      </td>
                    </tr>
                    <!-- Footer -->
                    <tr>
                      <td style="background:#f8fafc;padding:20px;text-align:center;
                                 color:#94a3b8;font-size:.8rem;border-top:1px solid #e2e8f0;">
                        Spring MVC Form Application — Assignment 4
                      </td>
                    </tr>
                  </table>
                </td></tr>
              </table>
            </body>
            </html>
            """.formatted(name, name, name, email, phone);
    }
}