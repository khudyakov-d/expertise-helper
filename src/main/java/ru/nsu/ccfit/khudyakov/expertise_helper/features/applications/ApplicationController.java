package ru.nsu.ccfit.khudyakov.expertise_helper.features.applications;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import ru.nsu.ccfit.khudyakov.expertise_helper.exceptions.ServiceException;
import ru.nsu.ccfit.khudyakov.expertise_helper.features.applications.dtos.NewApplicationDto;
import ru.nsu.ccfit.khudyakov.expertise_helper.features.applications.entities.Application;
import ru.nsu.ccfit.khudyakov.expertise_helper.features.applications.mappers.ApplicationMapper;
import ru.nsu.ccfit.khudyakov.expertise_helper.features.invitation.InvitationService;
import ru.nsu.ccfit.khudyakov.expertise_helper.features.projects.ProjectService;
import ru.nsu.ccfit.khudyakov.expertise_helper.features.projects.entities.Project;
import ru.nsu.ccfit.khudyakov.expertise_helper.features.users.User;
import ru.nsu.ccfit.khudyakov.expertise_helper.features.users.UserService;
import ru.nsu.ccfit.khudyakov.expertise_helper.security.users.CustomOAuth2User;

import javax.validation.Valid;
import java.util.List;
import java.util.UUID;

import static ru.nsu.ccfit.khudyakov.expertise_helper.ControllerUtils.getErrors;

@Controller
@RequiredArgsConstructor
public class ApplicationController {

    private final UserService userService;

    private final ProjectService projectService;

    private final ApplicationMapper applicationMapper;

    private final ApplicationService applicationService;


    @GetMapping("/projects/{projectId}/applications")
    public String getList(User user, @PathVariable UUID projectId, Model model) {
        List<Application> applicationList = applicationService.findAllByProjectId(user, projectId);

        Project project = projectService.findByUserAndId(user, projectId);

        model.addAttribute("requiredNumberExperts", project.getRequiredNumberExperts());
        model.addAttribute("user", user);
        model.addAttribute("applicationService", applicationService);
        model.addAttribute("applications", applicationMapper.toApplicationDto(applicationList));
        model.addAttribute("projectId", projectId);

        return "/applications/list";
    }

    @GetMapping("/projects/{projectId}/applications/add")
    public String add(@PathVariable String projectId, Model model) {
        model.addAttribute("projectId", projectId);
        return "/applications/add";
    }

    @PostMapping("/projects/{projectId}/applications")
    public String add(User user,
                      @PathVariable UUID projectId,
                      @Valid NewApplicationDto newApplicationDto,
                      BindingResult bindingResult,
                      RedirectAttributes redirectAttributes) {

        if (bindingResult.hasErrors()) {
            getErrors(bindingResult).forEach(redirectAttributes::addFlashAttribute);
            redirectAttributes.addFlashAttribute("application", newApplicationDto);
            return "redirect:/projects/" + projectId +  "/applications/add";
        }

        Application application = applicationMapper.toApplication(newApplicationDto);
        try {
            applicationService.add(user, projectId, application, newApplicationDto.getApplicationDocument());
        } catch (ServiceException e) {
            redirectAttributes.addAttribute("error", e.getMessage());
            return "redirect:/projects/add";
        }

        return "redirect:/projects/" + projectId + "/applications";
    }


    @ModelAttribute(value = "user", binding = false)
    public User getUser(@AuthenticationPrincipal CustomOAuth2User oAuth2User) {
        if (oAuth2User != null) {
            return userService.getByEmail(oAuth2User.getEmail());
        }
        return null;
    }

}
