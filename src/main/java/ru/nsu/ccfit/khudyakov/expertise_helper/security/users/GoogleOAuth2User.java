package ru.nsu.ccfit.khudyakov.expertise_helper.security.users;

import org.springframework.security.oauth2.core.user.OAuth2User;

public class GoogleOAuth2User extends CustomOAuth2User {

    public GoogleOAuth2User(OAuth2User oAuth2User) {
        super(oAuth2User);
    }

    @Override
    public String getName() {
        return oAuth2User.getAttribute("name");
    }

    @Override
    public String getEmail() {
        return oAuth2User.getAttribute("email");
    }

}
