package ru.nsu.ccfit.khudyakov.expertise_helper.features.invitation;

import lombok.RequiredArgsConstructor;
import org.apache.commons.io.FileUtils;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ContentDisposition;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import ru.nsu.ccfit.khudyakov.expertise_helper.docs.docx.subject_conclusion.SubjectConclusionDocument;
import ru.nsu.ccfit.khudyakov.expertise_helper.exceptions.ServiceException;
import ru.nsu.ccfit.khudyakov.expertise_helper.features.experts.ExpertService;
import ru.nsu.ccfit.khudyakov.expertise_helper.features.experts.dtos.ExpertDto;
import ru.nsu.ccfit.khudyakov.expertise_helper.features.experts.entities.Expert;
import ru.nsu.ccfit.khudyakov.expertise_helper.features.invitation.entities.Invitation;
import ru.nsu.ccfit.khudyakov.expertise_helper.features.users.User;
import ru.nsu.ccfit.khudyakov.expertise_helper.features.users.UserService;
import ru.nsu.ccfit.khudyakov.expertise_helper.files.FileManager;
import ru.nsu.ccfit.khudyakov.expertise_helper.security.users.CustomOAuth2User;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Controller
@RequiredArgsConstructor
public class InvitationController {

    private final FileManager fileManager;

    private final UserService userService;

    private final InvitationService invitationService;

    private final ExpertService expertService;

    @GetMapping("/projects/applications/{applicationId}/invitations/add")
    public String add(User user, @PathVariable UUID applicationId, Model model) {
        List<Expert> experts = expertService.getExperts(user);
        model.addAttribute("experts", experts);
        model.addAttribute("expert_header", ExpertDto.getExpertFieldsNames());
        model.addAttribute("applicationId", applicationId);

        return "/invitations/add";
    }

    @PostMapping("/projects/applications/{applicationId}/invitations/{expertId}")
    public String add(User user,
                      @PathVariable UUID applicationId,
                      @PathVariable UUID expertId,
                      @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate deadlineDate,
                      RedirectAttributes redirectAttributes) {
        try {
            invitationService.createInvitation(user, applicationId, expertId, deadlineDate);
        } catch (ServiceException e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
            return "redirect:/projects/applications/" + applicationId + "/invitations/add";
        }

        return "redirect:/projects/applications/" + applicationId + "/invitations";
    }

    @GetMapping("/projects/applications/invitations/{invitationId}/subject")
    public String addSubjectConclusion(@PathVariable UUID invitationId, Model model) {
        model.addAttribute("invitationId", invitationId);
        return "/invitations/conclusions/subject";
    }

    @PostMapping("projects/applications/invitations/{invitationId}/subject")
    public String addSubjectConclusion(User user,
                                       @PathVariable UUID invitationId,
                                       SubjectConclusionDocument subjectConclusionDocument) {
        UUID id = invitationService.getInvitation(user, invitationId).getApplication().getId();
        invitationService.addSubjectConclusion(user, invitationId, subjectConclusionDocument);
        return "redirect:/projects/applications/" + id + "/invitations";
    }

    @GetMapping("/projects/applications/invitations/{invitationId}/conclusion/download")
    public ResponseEntity<Resource> downloadConclusion(User user, @PathVariable UUID invitationId) {
        Invitation invitation = invitationService.getInvitation(user, invitationId);
        File file = fileManager.load(invitation.getConclusionPath());

        try {
            byte[] bytes = FileUtils.readFileToByteArray(file);
            ByteArrayResource resource = new ByteArrayResource(bytes);
            HttpHeaders httpHeaders = getHttpHeaders(invitation);

            return ResponseEntity.ok()
                    .headers(httpHeaders)
                    .body(resource);
        } catch (IOException e) {
            throw new IllegalStateException(e);
        }
    }

    private HttpHeaders getHttpHeaders(Invitation invitation) {
        ContentDisposition contentDisposition = ContentDisposition.builder("attachment")
                .filename("Заключение " + invitation.getExpert().getName() + ".docx", StandardCharsets.UTF_8)
                .build();
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentDisposition(contentDisposition);
        return httpHeaders;
    }

    @ModelAttribute(value = "user", binding = false)
    public User getUser(@AuthenticationPrincipal CustomOAuth2User oAuth2User) {
        if (oAuth2User != null) {
            return userService.getByEmail(oAuth2User.getEmail());
        }
        return null;
    }

}
