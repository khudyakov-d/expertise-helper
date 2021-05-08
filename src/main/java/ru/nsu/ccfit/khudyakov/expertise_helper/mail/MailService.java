package ru.nsu.ccfit.khudyakov.expertise_helper.mail;

import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.stereotype.Service;
import ru.nsu.ccfit.khudyakov.expertise_helper.features.users.User;
import ru.nsu.ccfit.khudyakov.expertise_helper.mail.senders.CustomMailSender;
import ru.nsu.ccfit.khudyakov.expertise_helper.mail.senders.GoogleMailSender;
import ru.nsu.ccfit.khudyakov.expertise_helper.mail.senders.MailruMailSender;
import ru.nsu.ccfit.khudyakov.expertise_helper.mail.senders.YandexMailSender;

import java.io.File;
import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.HashMap;
import java.util.Map;

@Service
public class MailService {

    private CustomMailSender getMailSender(User user, OAuth2AuthorizedClient client) {
        switch (client.getClientRegistration().getRegistrationId()) {
            case "google":
                try {
                    return new GoogleMailSender(user, client);
                } catch (GeneralSecurityException | IOException e) {
                    throw new IllegalStateException();
                }
            case "yandex":
                return new YandexMailSender(user, client);
            case "mailru":
                return new MailruMailSender(user, client);
            default:
                throw new IllegalStateException("Unknown provider");
        }
    }

    public void sendMessage(User user,
                            OAuth2AuthorizedClient client,
                            String to,
                            String subject,
                            String text,
                            Map<String, File> files) {
        CustomMailSender mailSender = getMailSender(user, client);
        mailSender.sendEmail(user.getEmail(), to, subject, text, files);
    }


}
