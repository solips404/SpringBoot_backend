package Service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailSender {
    @Autowired
    private JavaMailSender mailSender;
    public void setMailSender(String Email,String Subject,String body){
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom("syaro920115tw@gmail.com");
        message.setTo(Email);
        message.setText(body);
        message.setSubject(Subject);

        mailSender.send(message);
        System.out.println("Mail Sent successfully...");
    }
}
