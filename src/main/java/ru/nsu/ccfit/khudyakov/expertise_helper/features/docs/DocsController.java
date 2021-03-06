package ru.nsu.ccfit.khudyakov.expertise_helper.features.docs;

import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
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
import ru.nsu.ccfit.khudyakov.expertise_helper.features.experts.ExpertService;
import ru.nsu.ccfit.khudyakov.expertise_helper.features.experts.entities.Expert;
import ru.nsu.ccfit.khudyakov.expertise_helper.features.invitation.InvitationService;
import ru.nsu.ccfit.khudyakov.expertise_helper.features.projects.ProjectService;
import ru.nsu.ccfit.khudyakov.expertise_helper.features.projects.entities.Project;
import ru.nsu.ccfit.khudyakov.expertise_helper.features.users.User;
import ru.nsu.ccfit.khudyakov.expertise_helper.features.users.UserService;
import ru.nsu.ccfit.khudyakov.expertise_helper.security.users.CustomOAuth2User;

import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.util.Locale;
import java.util.UUID;

@Controller
@RequiredArgsConstructor
public class DocsController {

    private final Locale locale = Locale.forLanguageTag("ru");

    private final DocsService docsService;

    private final ExpertService expertService;

    private final UserService userService;

    private final ProjectService projectService;

    private final InvitationService invitationService;

    private final MessageSource messageSource;

    @GetMapping("projects/{projectId}/docs")
    public String docs(User user, @PathVariable UUID projectId, Model model) {
        model.addAttribute("projectId", projectId);
        model.addAttribute("experts", projectService.getProjectExperts(user, projectId));
        model.addAttribute("invitationService", invitationService);
        model.addAttribute("docsService", docsService);

        return "/docs/list";
    }

    @GetMapping("projects/{projectId}/docs/total-payment")
    public ResponseEntity<Resource> getTotalPayment(User user, @PathVariable UUID projectId) {
        Project project = projectService.findByUserAndId(user, projectId);
        byte[] paymentSheetBytes = docsService.createTotalPaymentSheet(project);

        ByteArrayResource resource = new ByteArrayResource(paymentSheetBytes);

        String name = messageSource.getMessage("docs.file.name.payment.total",
                new Object[]{project.getTitle()},
                locale);

        HttpHeaders httpHeaders = getHttpHeaders(name);

        return ResponseEntity.ok()
                .headers(httpHeaders)
                .body(resource);
    }

    @GetMapping("projects/{projectId}/docs/{expertId}")
    public ResponseEntity<Resource> getDocs(User user, @PathVariable UUID projectId, @PathVariable UUID expertId) {
        Expert expert = expertService.findByUserAndId(user, expertId);

        byte[] zipBytes = docsService.getDocumentsAsZipArchive(user, projectId, expert);
        ByteArrayResource resource = new ByteArrayResource(zipBytes);

        String name = messageSource.getMessage("docs.file.name.zip", new Object[]{expert.getName()}, locale);
        HttpHeaders httpHeaders = getHttpHeaders(name);

        return ResponseEntity.ok()
                .headers(httpHeaders)
                .body(resource);
    }

    @PostMapping("projects/{projectId}/docs/{expertId}/contract")
    public String setContractNumber(User user,
                                  @PathVariable UUID projectId,
                                  @PathVariable UUID expertId,
                                  @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate contractDate,
                                  String contractNumber) {
        docsService.setContractData(user, projectId, expertId, contractDate, contractNumber);

        return "redirect:/projects/" + projectId + "/docs";
    }


    private HttpHeaders getHttpHeaders(String filename) {
        ContentDisposition contentDisposition = ContentDisposition.builder("attachment")
                .filename(filename, StandardCharsets.UTF_8)
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
