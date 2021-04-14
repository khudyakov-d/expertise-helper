package ru.nsu.ccfit.khudyakov.expertise_helper.features.projects.mappers;

import org.assertj.core.util.Files;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;
import ru.nsu.ccfit.khudyakov.expertise_helper.features.projects.dtos.NewProjectDto;
import ru.nsu.ccfit.khudyakov.expertise_helper.features.projects.dtos.ProjectTypeDto;
import ru.nsu.ccfit.khudyakov.expertise_helper.features.projects.entities.Project;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import static org.apache.commons.compress.utils.IOUtils.toByteArray;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class ProjectMapperTest {

    private final ProjectMapper mapper = Mappers.getMapper(ProjectMapper.class);

    @Test
    void toProject() throws IOException {
        File file = Files.newTemporaryFile();
        try (FileInputStream input = new FileInputStream(file)) {
            MultipartFile multipartFile1 = new MockMultipartFile("file1", toByteArray(input));
            MultipartFile multipartFile2 = new MockMultipartFile("file2", toByteArray(input));
            MultipartFile multipartFile3 = new MockMultipartFile("file3", toByteArray(input));

            NewProjectDto newProjectDto = new NewProjectDto(
                    "project",
                    2300d,
                    2,
                    ProjectTypeDto.SUBJECT,
                    multipartFile1,
                    multipartFile2,
                    multipartFile3
            );

            Project project = mapper.toProject(newProjectDto);

            assertNotNull(project.getActPath());
            assertNotNull(project.getContractPath());
            assertNotNull(project.getConclusionPath());
        }
    }

}