package ru.nsu.ccfit.khudyakov.expertise_helper.mail;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.annotation.RegisteredOAuth2AuthorizedClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.nsu.ccfit.khudyakov.expertise_helper.features.users.User;
import ru.nsu.ccfit.khudyakov.expertise_helper.features.users.UserService;
import ru.nsu.ccfit.khudyakov.expertise_helper.security.users.CustomOAuth2User;

@RestController
@RequiredArgsConstructor
public class MailTestController {

    private final MailService mailService;

    private final UserService userService;

    @GetMapping("/mail/test")
    public void sendMessage(@RegisteredOAuth2AuthorizedClient OAuth2AuthorizedClient client,
                            @AuthenticationPrincipal CustomOAuth2User oAuth2User) {
        User user = userService.getByEmail(oAuth2User.getEmail());

        mailService.sendTestMessage(user, "d.khudyakov@g.nsu.ru", client);
    }


}
