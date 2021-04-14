package ru.nsu.ccfit.khudyakov.expertise_helper.security.users;

import org.springframework.security.oauth2.core.user.OAuth2User;

public class YandexOAuth2User extends CustomOAuth2User {


    public YandexOAuth2User(OAuth2User oAuth2User) {
        super(oAuth2User);
    }

    @Override
    public String getName() {
        return oAuth2User.getAttribute("real_name");
    }

    @Override
    public String getEmail() {
        return oAuth2User.getAttribute("default_email");
    }

}
