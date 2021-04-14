package ru.nsu.ccfit.khudyakov.expertise_helper.mail;

import org.springframework.mail.javamail.MimeMessageHelper;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.io.File;
import java.util.Map;

public class MailUtils {

    public static void fillMessage(MimeMessage mimeMessage,
                                    String from,
                                    String to,
                                    String subject,
                                    String text,
                                    Map<String, File> files) {
        try {
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true);
            helper.setTo(to);
            helper.setFrom(from);
            helper.setSubject(subject);
            helper.setText(text);

            for (Map.Entry<String, File> entry : files.entrySet()) {
                helper.addAttachment(entry.getKey(), entry.getValue());

            }
        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }

}
