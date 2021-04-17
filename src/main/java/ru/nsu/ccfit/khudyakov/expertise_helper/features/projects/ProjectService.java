package ru.nsu.ccfit.khudyakov.expertise_helper.features.projects;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import ru.nsu.ccfit.khudyakov.expertise_helper.exceptions.NotFoundException;
import ru.nsu.ccfit.khudyakov.expertise_helper.exceptions.ServiceException;
import ru.nsu.ccfit.khudyakov.expertise_helper.features.projects.dtos.EditProjectDto;
import ru.nsu.ccfit.khudyakov.expertise_helper.features.projects.entities.Project;
import ru.nsu.ccfit.khudyakov.expertise_helper.features.users.User;
import ru.nsu.ccfit.khudyakov.expertise_helper.files.FileManager;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ProjectService {

    private final FileManager fileManager;

    private final ProjectRepository projectRepository;

    public void add(User user, Project project, MultipartFile act, MultipartFile contract, MultipartFile conclusion) {
        boolean existsByTitle = projectRepository.existsByTitleAndUser(project.getTitle(), user);
        if (existsByTitle) {
            throw new ServiceException("project.add.error.exist");
        }

        try {
            fileManager.save(project.getActPath(), act.getBytes());
            fileManager.save(project.getContractPath(), contract.getBytes());
            fileManager.save(project.getConclusionPath(), conclusion.getBytes());
        } catch (IOException e) {
            fileManager.delete(project.getActPath());
            fileManager.delete(project.getContractPath());
            fileManager.delete(project.getConclusionPath());

            throw new ServiceException("project.files.error.unknown");
        }

        user.getProjects().add(project);
        project.setUser(user);

        projectRepository.save(project);
    }

    public void edit(User user, EditProjectDto projectDto) {
        Project project = projectRepository.findByUserAndId(user, projectDto.getId())
                .orElseThrow(() -> new ServiceException("project.edit.error.not.found"));

        project.setRequiredNumberExperts(projectDto.getRequiredNumberExperts());
        project.setBaseRate(projectDto.getBaseRate());

        try {
            if (projectDto.getAct() != null) {
                fileManager.save(project.getActPath(), projectDto.getAct().getBytes());
            }

            if (projectDto.getContract() != null) {
                fileManager.save(project.getContractPath(), projectDto.getContract().getBytes());
            }

            if (projectDto.getConclusion() != null) {
                fileManager.save(project.getConclusionPath(), projectDto.getConclusion().getBytes());
            }
        } catch (IOException e) {
            throw new ServiceException("project.files.error.unknown");
        }

        projectRepository.save(project);
    }

    public List<Project> findAllByUser(User user) {
        return projectRepository.findAllByUser(user);
    }

    public Project findByUserAndId(User user, UUID id) {
        return projectRepository.findByUserAndId(user, id).orElseThrow(NotFoundException::new);
    }

    public boolean existByUserAndId(User user, UUID id) {
        return projectRepository.existsByUserAndId(user, id);
    }

}
