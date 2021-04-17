package ru.nsu.ccfit.khudyakov.expertise_helper.features.invitation.statemachine.actions;

import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientService;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.statemachine.StateContext;
import org.springframework.statemachine.action.Action;
import org.springframework.stereotype.Component;
import ru.nsu.ccfit.khudyakov.expertise_helper.features.invitation.InvitationService;
import ru.nsu.ccfit.khudyakov.expertise_helper.features.invitation.entities.Invitation;
import ru.nsu.ccfit.khudyakov.expertise_helper.features.invitation.statemachine.InvitationEvent;
import ru.nsu.ccfit.khudyakov.expertise_helper.features.invitation.statemachine.InvitationState;
import ru.nsu.ccfit.khudyakov.expertise_helper.files.FileManager;
import ru.nsu.ccfit.khudyakov.expertise_helper.mail.MailService;

import java.io.File;
import java.time.format.DateTimeFormatter;
import java.util.Locale;
import java.util.Map;

import static ru.nsu.ccfit.khudyakov.expertise_helper.features.invitation.statemachine.InvitationEvent.SEND_INVITATION_EMAIL_SUCCESS;


@Component
@RequiredArgsConstructor
public class SendInvitationEmailAction implements Action<InvitationState, InvitationEvent> {

    private static final DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    private final MessageSource messageSource;

    private final MailService mailService;

    private final FileManager fileManager;

    private final InvitationService invitationService;

    private final OAuth2AuthorizedClientService clientService;

    @Override
    public void execute(StateContext<InvitationState, InvitationEvent> context) {
        Invitation invitation = invitationService.getInvitation(context.getStateMachine().getId());
        OAuth2AuthorizedClient client = getClient();

        Locale locale = Locale.forLanguageTag("ru");

        String subject = messageSource.getMessage("invitation.email.subject", null, locale);
        String text = messageSource.getMessage("invitation.email.text",
                new Object[]{dateFormatter.format(invitation.getDeadlineDate()), client.getPrincipalName()},
                locale);

        String application = messageSource.getMessage("invitation.email.application", null, locale);
        File applicationFile = fileManager.load(invitation.getApplication().getLocation());

        String conclusion = messageSource.getMessage("invitation.email.conclusion", null, locale);
        File conclusionFile = fileManager.load(invitation.getConclusionPath());

        mailService.sendMessage(invitation.getApplication().getProject().getUser(),
                client,
                invitation.getExpert().getEmail(),
                subject,
                text,
                Map.of(application, applicationFile, conclusion, conclusionFile));

        context.getStateMachine().sendEvent(SEND_INVITATION_EMAIL_SUCCESS);
    }

    private OAuth2AuthorizedClient getClient() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        OAuth2AuthenticationToken oauth2Token = (OAuth2AuthenticationToken) authentication;
        return clientService.loadAuthorizedClient(
                oauth2Token.getAuthorizedClientRegistrationId(),
                authentication.getName()
        );
    }

}
