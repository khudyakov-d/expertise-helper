package ru.nsu.ccfit.khudyakov.expertise_helper.features.docs;

import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;
import ru.nsu.ccfit.khudyakov.expertise_helper.docs.docx.act.Act;
import ru.nsu.ccfit.khudyakov.expertise_helper.docs.docx.act.ActMapper;
import ru.nsu.ccfit.khudyakov.expertise_helper.docs.docx.act.ActTemplateBuilder;
import ru.nsu.ccfit.khudyakov.expertise_helper.docs.docx.contract.Contract;
import ru.nsu.ccfit.khudyakov.expertise_helper.docs.docx.contract.ContractMapper;
import ru.nsu.ccfit.khudyakov.expertise_helper.docs.docx.contract.ContractTemplateBuilder;
import ru.nsu.ccfit.khudyakov.expertise_helper.docs.xlsx.payment.ApplicationPayment;
import ru.nsu.ccfit.khudyakov.expertise_helper.docs.xlsx.payment.ApplicationPaymentMapper;
import ru.nsu.ccfit.khudyakov.expertise_helper.docs.xlsx.payment.DetailedPayment;
import ru.nsu.ccfit.khudyakov.expertise_helper.docs.xlsx.payment.DetailedPaymentOutputData;
import ru.nsu.ccfit.khudyakov.expertise_helper.docs.xlsx.payment.DetailedPaymentTemplateBuilder;
import ru.nsu.ccfit.khudyakov.expertise_helper.docs.xlsx.total_payment.ExpertProjectPayment;
import ru.nsu.ccfit.khudyakov.expertise_helper.docs.xlsx.total_payment.ExpertProjectPaymentMapper;
import ru.nsu.ccfit.khudyakov.expertise_helper.docs.xlsx.total_payment.PageFactor;
import ru.nsu.ccfit.khudyakov.expertise_helper.docs.xlsx.total_payment.ProjectPaymentTemplateBuilder;
import ru.nsu.ccfit.khudyakov.expertise_helper.docs.xlsx.total_payment.TotalPayment;
import ru.nsu.ccfit.khudyakov.expertise_helper.exceptions.NotFoundException;
import ru.nsu.ccfit.khudyakov.expertise_helper.features.applications.entities.Application;
import ru.nsu.ccfit.khudyakov.expertise_helper.features.docs.entities.ContractId;
import ru.nsu.ccfit.khudyakov.expertise_helper.features.docs.entities.ExpertContract;
import ru.nsu.ccfit.khudyakov.expertise_helper.features.experts.ExpertService;
import ru.nsu.ccfit.khudyakov.expertise_helper.features.experts.entities.Expert;
import ru.nsu.ccfit.khudyakov.expertise_helper.features.invitation.InvitationService;
import ru.nsu.ccfit.khudyakov.expertise_helper.features.invitation.entities.Invitation;
import ru.nsu.ccfit.khudyakov.expertise_helper.features.projects.ProjectService;
import ru.nsu.ccfit.khudyakov.expertise_helper.features.projects.entities.Project;
import ru.nsu.ccfit.khudyakov.expertise_helper.features.users.User;
import ru.nsu.ccfit.khudyakov.expertise_helper.files.FileManager;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import static java.util.stream.Collectors.*;
import static java.util.stream.Collectors.toList;
import static java.util.stream.Collectors.toMap;

@Service
@RequiredArgsConstructor
public class DocsService {

    private static final double BASE_COST = 2524;

    private static final String TOTAL_PAYMENT_TEMPLATE_PATH =
            Paths.get("payments/total_payment.xlsx").toString();

    private static final String DETAILED_PAYMENT_TEMPLATE_PATH =
            Paths.get("payments/detailed_payment.xlsx").toString();

    private final ProjectService projectService;

    private final ExpertService expertService;

    private final ContractRepository contractRepository;

    private final ExpertProjectPaymentMapper expertProjectPaymentMapper;

    private final FileManager fileManager;

    private final ApplicationPaymentMapper applicationPaymentMapper;

    private final ActMapper actMapper;

    private final ContractMapper contractMapper;

    private final InvitationService invitationService;

    private final MessageSource messageSource;

    public byte[] createTotalPaymentSheet(Project project) {
        Map<Expert, List<Application>> expertsApplications = getExpertsApplications(project);

        List<ExpertProjectPayment> projectPayments = new ArrayList<>();

        for (Map.Entry<Expert, List<Application>> entry : expertsApplications.entrySet()) {
            Expert expert = entry.getKey();
            List<Application> applications = entry.getValue();
            ExpertContract expertContract = getProjectContract(project, expert);
            ExpertProjectPayment payment = expertProjectPaymentMapper.map(expert, applications, expertContract);
            projectPayments.add(payment);
        }

        TotalPayment totalPayment = new TotalPayment(BASE_COST, projectPayments);

        ProjectPaymentTemplateBuilder projectPaymentTemplateBuilder =
                new ProjectPaymentTemplateBuilder(TOTAL_PAYMENT_TEMPLATE_PATH, fileManager);

        return projectPaymentTemplateBuilder.toSheet(totalPayment).getSheetBytes();
    }

    private ExpertContract getProjectContract(Project project, Expert expert) {
        ContractId contractId = getContractId(project, expert);
        Optional<ExpertContract> contract = contractRepository.findById(contractId);
        return contract.orElseGet(() -> createExpertContract(expert, project));
    }

    private ContractId getContractId(Project project, Expert expert) {
        ContractId contractId = new ContractId();
        contractId.setExpertId(expert.getId());
        contractId.setProjectId(project.getId());
        return contractId;
    }

    private ExpertContract createExpertContract(Expert expert, Project project) {
        ExpertContract expertContract = new ExpertContract();
        expertContract.setContractId(new ContractId());
        expertContract.setExpert(expert);
        expertContract.setProject(project);
        contractRepository.save(expertContract);
        return expertContract;
    }

    private Map<Expert, List<Application>> getExpertsApplications(Project project) {
        List<Expert> experts = expertService.findInvolvedInProject(project.getId());

        return experts.stream()
                .collect(toMap(e -> e, this::getExpertApplications))
                .entrySet()
                .stream()
                .filter(entry -> !entry.getValue().isEmpty())
                .collect(toMap(Map.Entry::getKey, Map.Entry::getValue));
    }

    public List<Application> getExpertApplications(Expert e) {
        return e.getInvitations().stream()
                .filter(invitationService::isInvitationApproved)
                .map(Invitation::getApplication)
                .collect(toList());
    }

    private DetailedPaymentOutputData createDetailedPayment(ExpertContract expertContract) {
        Expert expert = expertContract.getExpert();

        DetailedPaymentTemplateBuilder templateBuilder =
                new DetailedPaymentTemplateBuilder(DETAILED_PAYMENT_TEMPLATE_PATH, fileManager);

        List<Application> applications = getExpertApplications(expert);

        List<ApplicationPayment> applicationPayments = applications.stream()
                .map(a -> getApplicationPayment(expert, a))
                .collect(toList());

        DetailedPayment detailedPayment = new DetailedPayment();
        detailedPayment.setNumber(expertContract.getContractNumber());
        detailedPayment.setPayments(applicationPayments);

        return templateBuilder.toSheet(detailedPayment);
    }

    private ApplicationPayment getApplicationPayment(Expert expert, Application application) {
        return applicationPaymentMapper.map(
                application,
                BASE_COST,
                expert.getDegree().getFactor(),
                PageFactor.getFromCount(application.getPagesCount()).getValue()
        );
    }

    private byte[] createAct(DetailedPaymentOutputData outputData, ExpertContract expertContract) {
        Expert expert = expertContract.getExpert();
        Project project = expertContract.getProject();

        List<Application> applications = getExpertApplications(expert);

        Act act = actMapper.map(
                expertContract.getContractNumber(),
                expert,
                applications.size(),
                outputData.getCost(),
                outputData.getCostWithTaxes()
        );

        ActTemplateBuilder actTemplateBuilder = new ActTemplateBuilder(
                fileManager.load(project.getActPath()),
                fileManager
        );

        return actTemplateBuilder.saveToDocx(act);
    }

    private byte[] setContractData(DetailedPaymentOutputData outputData, ExpertContract expertContract) {
        Expert expert = expertContract.getExpert();
        Project project = expertContract.getProject();

        List<Application> applications = getExpertApplications(expert);

        Contract contract = contractMapper.map(
                expertContract.getContractNumber(),
                expert,
                applications.size(),
                outputData.getCost(),
                outputData.getCostWithTaxes()
        );

        ContractTemplateBuilder contractTemplateBuilder = new ContractTemplateBuilder(
                fileManager.load(project.getContractPath()),
                fileManager
        );

        return contractTemplateBuilder.saveToDocx(contract);
    }

    public byte[] getDocumentsAsZipArchive(User user, UUID projectId, Expert expert) {
        Project project = projectService.findByUserAndId(user, projectId);

        ExpertContract expertContract = getProjectContract(project, expert);

        DetailedPaymentOutputData detailedPayment = createDetailedPayment(expertContract);
        byte[] actBytes = createAct(detailedPayment, expertContract);
        byte[] contractBytes = setContractData(detailedPayment, expertContract);

        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        try (ByteArrayOutputStream tempBos = byteArrayOutputStream;
             ZipOutputStream outputStream = new ZipOutputStream(tempBos)) {

            Locale locale = Locale.forLanguageTag("ru");

            zip(detailedPayment.getSheetBytes(),
                    outputStream,
                    messageSource.getMessage("docs.file.name.payment", null, locale));

            zip(actBytes, outputStream, messageSource.getMessage("docs.file.name.act", null, locale));
            zip(contractBytes, outputStream, messageSource.getMessage("docs.file.name.contract",
                    null,
                    locale));

        } catch (IOException e) {
            throw new IllegalStateException(e);
        }

        return byteArrayOutputStream.toByteArray();
    }

    private void zip(byte[] bytes, ZipOutputStream outputStream, String s) throws IOException {
        ZipEntry zipEntry = new ZipEntry(s);
        zipEntry.setSize(bytes.length);
        outputStream.putNextEntry(zipEntry);
        outputStream.write(bytes);
        outputStream.closeEntry();
    }

    public void setContractData(User user, UUID projectId, UUID expertId, LocalDate date, String number) {
        Expert expert = expertService.findByUserAndId(user, expertId);
        Project project = projectService.findByUserAndId(user, projectId);

        ContractId contractId = getContractId(project, expert);
        ExpertContract expertContract = contractRepository.findById(contractId)
                .orElseThrow(NotFoundException::new);

        expertContract.setContractNumber(number);
        expertContract.setContractDate(date);

        contractRepository.save(expertContract);
    }

}
