package hanu.gdsc.infrastructure.coderAuth.mail;

import javax.mail.MessagingException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import hanu.gdsc.domain.coderAuth.mail.SendMail;
import hanu.gdsc.domain.share.exceptions.InvalidInputException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import hanu.gdsc.domain.coderAuth.models.Email;
import hanu.gdsc.domain.coderAuth.exceptions.EmailSendingException;

@Service
public class SendMailImpl implements SendMail {
    @Autowired
    private JavaMailSender javaMailSender;

    public void sendMail(Email toAddress, String name, String code) throws InvalidInputException, EmailSendingException {
        Email fromAddress = new Email("dtlinh010202@gmail.com");
        String subject = "Verify your email";
        String content = "Dear "+name+",\n"
        +"Use this code below to verify \n"
        + code +"\n"
        + "Thanks,\n"
        +"Hanu gcsd";

        MimeMessage message = javaMailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message);
        
        try {
            helper.setFrom(new InternetAddress(fromAddress.toString()));
            helper.setTo(new InternetAddress(toAddress.toString()));
            helper.setSubject(subject);
            helper.setText(content);

        } catch (MessagingException exception) {
            throw new EmailSendingException();
        }
        javaMailSender.send(message);
    }
}
