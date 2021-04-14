package ru.nsu.ccfit.khudyakov.expertise_helper.security;

import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Component;
import ru.nsu.ccfit.khudyakov.expertise_helper.security.users.GoogleOAuth2User;
import ru.nsu.ccfit.khudyakov.expertise_helper.security.users.MailOAuth2User;
import ru.nsu.ccfit.khudyakov.expertise_helper.security.users.YandexOAuth2User;

@Component
public class OAuth2UserService extends DefaultOAuth2UserService {

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2User oAuth2User = super.loadUser(userRequest);

        switch (userRequest.getClientRegistration().getRegistrationId()) {
            case "google":
                return new GoogleOAuth2User(oAuth2User);
            case "yandex":
                return new YandexOAuth2User(oAuth2User);
            case "mailru":
                return new MailOAuth2User(oAuth2User);
            default:
                throw new IllegalStateException();
        }
    }

}
