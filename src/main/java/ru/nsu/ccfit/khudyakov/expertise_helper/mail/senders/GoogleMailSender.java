package ru.nsu.ccfit.khudyakov.expertise_helper.mail.senders;

import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.googleapis.auth.oauth2.GoogleCredential;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.services.gmail.Gmail;
import com.google.api.services.gmail.model.Message;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.registration.ClientRegistration;
import ru.nsu.ccfit.khudyakov.expertise_helper.features.users.User;
import ru.nsu.ccfit.khudyakov.expertise_helper.mail.MailUtils;

import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.internet.MimeMessage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.Map;
import java.util.Properties;

import static com.google.api.client.repackaged.org.apache.commons.codec.binary.Base64.encodeBase64URLSafeString;

public class GoogleMailSender extends CustomMailSender {

    private final JacksonFactory JSON_FACTORY = JacksonFactory.getDefaultInstance();

    private final static String APPLICATION_NAME = "expertise-helper";

    private final Gmail gmail;

    public GoogleMailSender(User user, OAuth2AuthorizedClient client) throws GeneralSecurityException, IOException {
        super(user, client);
        NetHttpTransport httpTransport = GoogleNetHttpTransport.newTrustedTransport();
        gmail = createGmail(client, httpTransport);
    }

    @Override
    public void sendEmail(String from, String to, String subject, String text, Map<String, File> files) {
        try {
            MimeMessage message = new MimeMessage(Session.getDefaultInstance(new Properties(), null));
            MailUtils.fillMessage(message, from, to, subject, text, files);
            gmail
                    .users()
                    .messages()
                    .send(user.getEmail(), createMessageWithEmail(message))
                    .execute();

        } catch (MessagingException | IOException e) {
            e.printStackTrace();
        }
    }

    private Gmail createGmail(OAuth2AuthorizedClient client, NetHttpTransport httpTransport) {
        Credential credential = authorize(client);
        return new Gmail.Builder(httpTransport, JSON_FACTORY, credential)
                .setApplicationName(APPLICATION_NAME)
                .build();
    }

    private Credential authorize(OAuth2AuthorizedClient client) {
        try {
            ClientRegistration clientRegistration = client.getClientRegistration();

            return new GoogleCredential.Builder()
                    .setTransport(GoogleNetHttpTransport.newTrustedTransport())
                    .setJsonFactory(JSON_FACTORY)
                    .setClientSecrets(clientRegistration.getClientId(), clientRegistration.getClientSecret())
                    .build()
                    .setAccessToken(client.getAccessToken().getTokenValue());
        } catch (GeneralSecurityException | IOException e) {
            throw new IllegalStateException();
        }
    }

    private Message createMessageWithEmail(MimeMessage emailContent) throws MessagingException, IOException {
        ByteArrayOutputStream buffer = new ByteArrayOutputStream();
        emailContent.writeTo(buffer);

        return new Message().setRaw(encodeBase64URLSafeString(buffer.toByteArray()));
    }

}
