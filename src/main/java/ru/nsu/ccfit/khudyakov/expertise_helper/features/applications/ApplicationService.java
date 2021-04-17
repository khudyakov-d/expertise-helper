package ru.nsu.ccfit.khudyakov.expertise_helper.features.applications;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import ru.nsu.ccfit.khudyakov.expertise_helper.exceptions.NotFoundException;
import ru.nsu.ccfit.khudyakov.expertise_helper.exceptions.ServiceException;
import ru.nsu.ccfit.khudyakov.expertise_helper.features.applications.entities.Application;
import ru.nsu.ccfit.khudyakov.expertise_helper.features.invitation.InvitationService;
import ru.nsu.ccfit.khudyakov.expertise_helper.features.invitation.entities.InvitationStatus;
import ru.nsu.ccfit.khudyakov.expertise_helper.features.projects.ProjectService;
import ru.nsu.ccfit.khudyakov.expertise_helper.features.projects.entities.Project;
import ru.nsu.ccfit.khudyakov.expertise_helper.features.users.User;
import ru.nsu.ccfit.khudyakov.expertise_helper.files.FileManager;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ApplicationService {

    private final ApplicationRepository applicationRepository;

    private final ProjectService projectService;

    private final FileManager fileManager;

    public List<Application> findAllByProjectId(User user, UUID projectId) {
        if (!projectService.existByUserAndId(user, projectId)) {
            throw new NotFoundException();
        }
        return applicationRepository.findAllByProjectId(projectId);
    }

    public void add(User user, UUID projectId, Application application, MultipartFile applicationFile) {
        try {
            fileManager.save(application.getLocation(), applicationFile.getBytes());
        } catch (IOException e) {
            throw new ServiceException("application.add.error.unknown");
        }

        Project project = projectService.findByUserAndId(user, projectId);
        project.getApplications().add(application);
        application.setProject(project);

        applicationRepository.save(application);
    }

    public Application findByUserAndId(User user, UUID applicationId) {
        return applicationRepository.findByProjectUserAndId(user, applicationId)
                .orElseThrow(NotFoundException::new);
    }

    public int getCompletedInvitationCount(User user, UUID applicationId) {
        Application application = applicationRepository.findByProjectUserAndId(user, applicationId)
                .orElseThrow(NotFoundException::new);

        return (int) application.getInvitations().stream()
                .filter(i -> i.getStatus().equals(InvitationStatus.COMPLETED))
                .count();
    }

}
