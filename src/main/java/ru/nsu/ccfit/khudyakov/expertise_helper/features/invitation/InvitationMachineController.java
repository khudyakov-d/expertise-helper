package ru.nsu.ccfit.khudyakov.expertise_helper.features.invitation;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.multipart.MultipartFile;
import ru.nsu.ccfit.khudyakov.expertise_helper.features.experts.ExpertService;
import ru.nsu.ccfit.khudyakov.expertise_helper.features.invitation.entities.Invitation;
import ru.nsu.ccfit.khudyakov.expertise_helper.features.invitation.entities.InvitationStatus;
import ru.nsu.ccfit.khudyakov.expertise_helper.features.users.User;
import ru.nsu.ccfit.khudyakov.expertise_helper.features.users.UserService;
import ru.nsu.ccfit.khudyakov.expertise_helper.files.validation.FileExtension;
import ru.nsu.ccfit.khudyakov.expertise_helper.files.validation.FileNotEmpty;
import ru.nsu.ccfit.khudyakov.expertise_helper.security.users.CustomOAuth2User;

import javax.validation.Valid;
import java.util.List;
import java.util.UUID;

@Controller
@RequiredArgsConstructor
public class InvitationMachineController {

    private final UserService userService;

    private final InvitationMachineService invitationMachineService;

    @GetMapping("projects/applications/{applicationId}/invitations")
    public String getInvitations(User user, @PathVariable UUID applicationId, Model model) {
        model.addAttribute("invitationsInProcess",
                invitationMachineService.getInProcessInvitations(user, applicationId));
        List<Invitation> invitations = invitationMachineService.getInvitations(user, applicationId);

        model.addAttribute("statuses", InvitationStatus.values());
        model.addAttribute("invitations", invitations);
        return "/invitations/list";
    }

    @PostMapping("projects/applications/invitations/{invitationId}/events/generate-conclusion")
    public String conclusionGenerateEvent(User user, @PathVariable UUID invitationId) {
        UUID id = invitationMachineService.getInvitation(user, invitationId).getApplication().getId();
        invitationMachineService.conclusionGenerateEvent(invitationId);
        return "redirect:/projects/applications/" + id + "/invitations";
    }

    @PostMapping("projects/applications/invitations/{invitationId}/events/generate-retry")
    public String conclusionGeneratingRetryEvent(User user, @PathVariable UUID invitationId) {
        UUID id = invitationMachineService.getInvitation(user, invitationId).getApplication().getId();
        invitationMachineService.conclusionGeneratingRetryEvent(invitationId);
        return "redirect:/projects/applications/" + id + "/invitations";
    }

    @PostMapping("projects/applications/invitations/{invitationId}/events/email-skip")
    public String sendInvitationEmailSkipEvent(User user, @PathVariable UUID invitationId) {
        UUID id = invitationMachineService.getInvitation(user, invitationId).getApplication().getId();
        invitationMachineService.sendInvitationEmailSkipEvent(invitationId);
        return "redirect:/projects/applications/" + id + "/invitations";
    }

    @PostMapping("projects/applications/invitations/{invitationId}/events/email-send")
    public String sendInvitationEmailEvent(User user, @PathVariable UUID invitationId) {
        UUID id = invitationMachineService.getInvitation(user, invitationId).getApplication().getId();
        invitationMachineService.sendInvitationEmailEvent(invitationId);
        return "redirect:/projects/applications/" + id + "/invitations";
    }

    @PostMapping("projects/applications/invitations/{invitationId}/events/email-retry")
    public String sendInvitationEmailRetryEvent(User user, @PathVariable UUID invitationId) {
        UUID id = invitationMachineService.getInvitation(user, invitationId).getApplication().getId();
        invitationMachineService.sendInvitationEmailRetryEvent(invitationId);
        return "redirect:/projects/applications/" + id + "/invitations";
    }

    @PostMapping("projects/applications/invitations/{invitationId}/events/email-repeat")
    public String sendInvitationEmailRepeatEvent(User user, @PathVariable UUID invitationId) {
        UUID id = invitationMachineService.getInvitation(user, invitationId).getApplication().getId();
        invitationMachineService.sendInvitationEmailRepeatEvent(invitationId);
        return "redirect:/projects/applications/" + id + "/invitations";
    }

    @PostMapping("projects/applications/invitations/{invitationId}/events/email-continue")
    public String sendInvitationEmailContinueEvent(User user, @PathVariable UUID invitationId) {
        UUID id = invitationMachineService.getInvitation(user, invitationId).getApplication().getId();
        invitationMachineService.sendInvitationEmailContinueEvent(invitationId);
        return "redirect:/projects/applications/" + id + "/invitations";
    }

    @PostMapping("projects/applications/invitations/{invitationId}/events/expert-reject")
    public String expertRejectEvent(User user, @PathVariable UUID invitationId) {
        UUID id = invitationMachineService.getInvitation(user, invitationId).getApplication().getId();
        invitationMachineService.expertRejectEvent(invitationId);
        return "redirect:/projects/applications/" + id + "/invitations";
    }

    @PostMapping("projects/applications/invitations/{invitationId}/events/expert-ignore")
    public String expertIgnoreEvent(User user, @PathVariable UUID invitationId) {
        UUID id = invitationMachineService.getInvitation(user, invitationId).getApplication().getId();
        invitationMachineService.expertIgnoreEvent(invitationId);
        return "redirect:/projects/applications/" + id + "/invitations";
    }

    @PostMapping("projects/applications/invitations/{invitationId}/events/expert-uploading-result")
    public String expertUploadingResultEvent(User user, @PathVariable UUID invitationId) {
        UUID id = invitationMachineService.getInvitation(user, invitationId).getApplication().getId();
        invitationMachineService.expertUploadingResultEvent(invitationId);
        return "redirect:/projects/applications/" + id + "/invitations";
    }

    @PostMapping("projects/applications/invitations/{invitationId}/events/expert-upload-result")
    public String expertUploadResultEvent(User user,
                                          @PathVariable UUID invitationId,
                                          @Valid @FileNotEmpty @FileExtension("docx") MultipartFile file) {
        UUID id = invitationMachineService.getInvitation(user, invitationId).getApplication().getId();
        invitationMachineService.expertUploadResultEvent(invitationId, file);
        return "redirect:/projects/applications/" + id + "/invitations";
    }

    @ModelAttribute(value = "user", binding = false)
    public User getUser(@AuthenticationPrincipal CustomOAuth2User oAuth2User) {
        if (oAuth2User != null) {
            return userService.getByEmail(oAuth2User.getEmail());
        }
        return null;
    }
}
