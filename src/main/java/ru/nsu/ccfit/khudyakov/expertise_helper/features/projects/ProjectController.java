package ru.nsu.ccfit.khudyakov.expertise_helper.features.projects;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import ru.nsu.ccfit.khudyakov.expertise_helper.exceptions.ServiceException;
import ru.nsu.ccfit.khudyakov.expertise_helper.features.projects.dtos.EditProjectDto;
import ru.nsu.ccfit.khudyakov.expertise_helper.features.projects.dtos.NewProjectDto;
import ru.nsu.ccfit.khudyakov.expertise_helper.features.projects.dtos.ProjectInfoDto;
import ru.nsu.ccfit.khudyakov.expertise_helper.features.projects.entities.Project;
import ru.nsu.ccfit.khudyakov.expertise_helper.features.projects.mappers.ProjectMapper;
import ru.nsu.ccfit.khudyakov.expertise_helper.features.users.User;
import ru.nsu.ccfit.khudyakov.expertise_helper.features.users.UserService;
import ru.nsu.ccfit.khudyakov.expertise_helper.security.users.CustomOAuth2User;

import javax.validation.Valid;
import java.util.List;
import java.util.UUID;

import static ru.nsu.ccfit.khudyakov.expertise_helper.ControllerUtils.getErrors;
import static ru.nsu.ccfit.khudyakov.expertise_helper.features.projects.dtos.ProjectTypeDto.values;

@Controller
@RequiredArgsConstructor
public class ProjectController {

    private final ProjectService projectService;

    private final UserService userService;

    private final ProjectMapper projectMapper;

    @GetMapping(path = {"/", "/projects"})
    public String getList(@AuthenticationPrincipal CustomOAuth2User oAuth2User, Model model) {
        User user = userService.getByEmail(oAuth2User.getEmail());

        List<Project> projectList = projectService.findAllByUser(user);
        List<ProjectInfoDto> projectInfoDtos = projectMapper.toProjectDto(projectList);

        model.addAttribute("projects", projectInfoDtos);

        return "projects/list";
    }

    @GetMapping(path = "/projects/add")
    public String add(Model model) {
        model.addAttribute("projectTypes", values());
        return "projects/add";
    }

    @PostMapping("/projects/add")
    public String add(User user,
                      @Valid NewProjectDto newProjectDto,
                      BindingResult bindingResult,
                      RedirectAttributes attributes) {
        if (bindingResult.hasErrors()) {
            getErrors(bindingResult).forEach(attributes::addFlashAttribute);
            return "redirect:/projects/add";
        }

        try {
            Project project = projectMapper.toProject(newProjectDto);

            projectService.add(
                    user,
                    project,
                    newProjectDto.getAct(),
                    newProjectDto.getContract(),
                    newProjectDto.getConclusion()
            );
        } catch (ServiceException e) {
            attributes.addFlashAttribute("error", e.getMessage());
            return "redirect:/projects/add";
        }

        return "redirect:/projects";
    }

    @GetMapping(path = "/projects/{projectId}/edit")
    public String edit(User user, @PathVariable UUID projectId, Model model) {
        Project project = projectService.findByUserAndId(user, projectId);
        model.addAttribute("project", project);
        return "projects/edit";
    }

    @PutMapping(path = "/projects/edit")
    public String edit(User user,
                       @Valid EditProjectDto editProjectDto,
                       BindingResult bindingResult,
                       RedirectAttributes attributes) {
        if (bindingResult.hasErrors()) {
            getErrors(bindingResult).forEach(attributes::addFlashAttribute);
            return "redirect:/projects/" + editProjectDto.getId() + "/edit";
        }

        projectService.edit(user, editProjectDto);

        return "redirect:/projects";
    }

    @ModelAttribute(value = "user", binding = false)
    public User getUser(@AuthenticationPrincipal CustomOAuth2User oAuth2User) {
        if (oAuth2User != null) {
            return userService.getByEmail(oAuth2User.getEmail());
        }
        return null;
    }

}
