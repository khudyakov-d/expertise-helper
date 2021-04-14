package ru.nsu.ccfit.khudyakov.expertise_helper.mail.senders;

import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import ru.nsu.ccfit.khudyakov.expertise_helper.features.users.User;
import ru.nsu.ccfit.khudyakov.expertise_helper.mail.MailUtils;

import javax.mail.internet.MimeMessage;
import java.io.File;
import java.util.Map;
import java.util.Properties;

public class YandexMailSender extends CustomMailSender {

    private static final String HOST = "smtp.yandex.ru";

    private static final int PORT = 465;

    private JavaMailSender javaMailSender;

    public YandexMailSender(User user, OAuth2AuthorizedClient client) {
        super(user, client);
        configureMailSender(user, client);
    }

    private void configureMailSender(User user, OAuth2AuthorizedClient client) {
        JavaMailSenderImpl mailSender = new JavaMailSenderImpl();

        mailSender.setPort(PORT);
        mailSender.setHost(HOST);
        mailSender.setProtocol("smtps");
        mailSender.setUsername(user.getEmail());
        mailSender.setPassword(client.getAccessToken().getTokenValue());

        Properties properties = mailSender.getJavaMailProperties();
        properties.setProperty("mail.transport.protocol", "smtps");
        properties.setProperty("mail.smtps.auth", "true");
        properties.setProperty("mail.smtps.auth.mechanisms", "XOAUTH2");

        javaMailSender = mailSender;
    }

    @Override
    public void sendEmail(String from, String to, String subject, String text, Map<String, File> files) {
        MimeMessage message = javaMailSender.createMimeMessage();
        MailUtils.fillMessage(message, from, to, subject, text, files);
        javaMailSender.send(message);
    }


}
