package ru.nsu.ccfit.khudyakov.expertise_helper.recommendations;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.nsu.ccfit.khudyakov.expertise_helper.features.applications.entities.Application;
import ru.nsu.ccfit.khudyakov.expertise_helper.features.docs.DocsService;
import ru.nsu.ccfit.khudyakov.expertise_helper.features.experts.ExpertService;
import ru.nsu.ccfit.khudyakov.expertise_helper.features.experts.entities.Expert;
import ru.nsu.ccfit.khudyakov.expertise_helper.features.users.User;
import ru.nsu.ccfit.khudyakov.expertise_helper.recommendations.model.Doc;
import ru.nsu.ccfit.khudyakov.expertise_helper.recommendations.model.ExpertApplicationWordBag;
import ru.nsu.ccfit.khudyakov.expertise_helper.recommendations.model.TargetApplicationWordBag;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class RecommendationService {

    private final LsaRecommendationSystem lsaRecommendationSystem;

    private final DataNormalizer dataNormalizer;

    private final ExpertService expertService;

    private final ExpertApplicationWordBagMapper expertApplicationWordBagMapper;

    public List<Expert> getExpertsSortedBySimilarity(User user, Application application) {
        List<String> words = getApplicationWords(application);

        List<Expert> experts = expertService.getExperts(user);
        List<Doc> docs = expertApplicationWordBagMapper.map(experts);

        TargetApplicationWordBag targetWordBag = new TargetApplicationWordBag(UUID.randomUUID(), words);
        docs.add(targetWordBag);

        List<UUID> docsOrder = lsaRecommendationSystem.calcDocsOrder(docs, targetWordBag.getID());
        experts.sort(Comparator.comparing(e -> docsOrder.indexOf(e.getId())));

        return experts;
    }

    private List<String> getApplicationWords(Application application) {
        String topic = application.getTopic();
        List<String> words = dataNormalizer.normalizeText(topic);
        words.add(application.getTopicNumber());
        return words;
    }

}
