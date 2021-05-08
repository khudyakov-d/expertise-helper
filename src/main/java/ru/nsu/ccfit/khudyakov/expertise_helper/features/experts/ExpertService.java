package ru.nsu.ccfit.khudyakov.expertise_helper.features.experts;

import com.poiji.exception.PoijiExcelType;
import com.poiji.option.PoijiOptions;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import ru.nsu.ccfit.khudyakov.expertise_helper.exceptions.NotFoundException;
import ru.nsu.ccfit.khudyakov.expertise_helper.exceptions.ServiceException;
import ru.nsu.ccfit.khudyakov.expertise_helper.features.experts.dtos.ExpertSheetDto;
import ru.nsu.ccfit.khudyakov.expertise_helper.features.experts.entities.Expert;
import ru.nsu.ccfit.khudyakov.expertise_helper.features.users.User;

import java.io.InputStream;
import java.util.List;
import java.util.UUID;

import static com.poiji.bind.Poiji.fromExcel;


@Service
@RequiredArgsConstructor
public class ExpertService {

    private final ExpertRepository expertRepository;

    public void add(User user, Expert expert) {
        if (expertRepository.findByEmail(expert.getEmail()) != null) {
            throw new ServiceException("expert.add.error");
        }

        user.getExperts().add(expert);
        expert.setUser(user);
        expertRepository.save(expert);
    }

    public void add(User user, List<Expert> experts) {
        try {
            for (Expert expert : experts) {
                add(user, expert);
            }
        } catch (ServiceException ex) {
            System.out.println(ex.getMessage());
        }
    }

    public List<ExpertSheetDto> getExpertFromSheet(InputStream inputStream) {
        PoijiOptions options = PoijiOptions.PoijiOptionsBuilder.settings()
                .namedHeaderMandatory(true)
                .build();

        return fromExcel(inputStream, PoijiExcelType.XLSX, ExpertSheetDto.class, options);
    }

    public void update(User user, Expert expert) {
        expert.setUser(user);
        expertRepository.save(expert);
    }

    public void delete(User user, Expert expert) {
        user.getExperts().remove(expert);
        expertRepository.delete(expert);
    }

    public Expert findByUserAndId(User user, UUID expertId) {
        return expertRepository.findByUserAndId(user, expertId).orElseThrow(NotFoundException::new);
    }

    public Page<Expert> getExperts(User user, Pageable pageable) {
        return expertRepository.findByUser(user, pageable);
    }

    public List<Expert> getExperts(User user) {
        return expertRepository.findByUser(user);
    }

    public List<Expert> findInvolvedInProject(UUID projectId) {
        return expertRepository.findInvolvedInProject(projectId);
    }

}
