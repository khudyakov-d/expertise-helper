package ru.nsu.ccfit.khudyakov.expertise_helper.mail.senders;

import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import ru.nsu.ccfit.khudyakov.expertise_helper.features.users.User;

import java.io.File;
import java.util.Map;

public abstract class CustomMailSender {

    protected final OAuth2AuthorizedClient client;
    protected final User user;

    protected CustomMailSender(User user, OAuth2AuthorizedClient client) {
        this.user = user;
        this.client = client;
    }

    public abstract void sendEmail(String from, String to, String subject, String text, Map<String, File> files);


}
