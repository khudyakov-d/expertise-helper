package ru.nsu.ccfit.khudyakov.expertise_helper.features.docs;

import lombok.RequiredArgsConstructor;
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
import ru.nsu.ccfit.khudyakov.expertise_helper.exceptions.ServiceException;
import ru.nsu.ccfit.khudyakov.expertise_helper.features.applications.entities.Application;
import ru.nsu.ccfit.khudyakov.expertise_helper.features.docs.entities.ContractId;
import ru.nsu.ccfit.khudyakov.expertise_helper.features.docs.entities.ExpertContract;
import ru.nsu.ccfit.khudyakov.expertise_helper.features.experts.ExpertRepository;
import ru.nsu.ccfit.khudyakov.expertise_helper.features.experts.entities.Expert;
import ru.nsu.ccfit.khudyakov.expertise_helper.features.invitation.InvitationRepository;
import ru.nsu.ccfit.khudyakov.expertise_helper.features.invitation.entities.Invitation;
import ru.nsu.ccfit.khudyakov.expertise_helper.features.invitation.entities.InvitationStatus;
import ru.nsu.ccfit.khudyakov.expertise_helper.features.invitation.statemachine.InvitationStateMachineRepository;
import ru.nsu.ccfit.khudyakov.expertise_helper.features.projects.ProjectRepository;
import ru.nsu.ccfit.khudyakov.expertise_helper.features.projects.entities.Project;
import ru.nsu.ccfit.khudyakov.expertise_helper.files.FileManager;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import static java.util.stream.Collectors.toList;
import static java.util.stream.Collectors.toMap;
import static ru.nsu.ccfit.khudyakov.expertise_helper.features.invitation.statemachine.InvitationState.EXPERT_CONCLUSION_UPLOADING;

@Service
@RequiredArgsConstructor
public class DocsService {

    private static final double BASE_COST = 2524;

    private static final String TOTAL_PAYMENT_TEMPLATE_PATH =
            Paths.get("payments/total_payment.xlsx").toString();

    private static final String DETAILED_PAYMENT_TEMPLATE_PATH =
            Paths.get("payments/detailed_payment.xlsx").toString();

    private final ExpertRepository expertRepository;

    private final ProjectRepository projectRepository;

    private final ContractRepository contractRepository;

    private final InvitationStateMachineRepository stateMachineRepository;

    private final ExpertProjectPaymentMapper expertProjectPaymentMapper;

    private final FileManager fileManager;

    private final ApplicationPaymentMapper applicationPaymentMapper;

    private final ActMapper actMapper;

    private final ContractMapper contractMapper;

    private final InvitationRepository invitationRepository;

    public List<Expert> getExperts(UUID projectId) {
        return expertRepository.findInvolvedInProject(projectId);
    }

    public byte[] createTotalPaymentSheet(UUID projectId) {
        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new ServiceException("Проект не найден"));

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
        ContractId contractId = new ContractId();
        contractId.setExpertId(expert.getId());
        contractId.setProjectId(project.getId());
        Optional<ExpertContract> contract = contractRepository.findById(contractId);
        return contract.orElseGet(() -> createExpertContract(expert, project));
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
        List<Expert> experts = expertRepository.findInvolvedInProject(project.getId());

        return experts.stream().collect(toMap(e -> e, this::getExpertApplications));
    }

    public List<Application> getExpertApplications(Expert e) {
        return e.getInvitations().stream()
                .filter(this::isInvitationApproved)
                .map(Invitation::getApplication)
                .collect(toList());
    }

    private boolean isInvitationApproved(Invitation invitation) {
        if (invitation.getStatus().equals(InvitationStatus.COMPLETED)) {
            return true;
        }

        if (invitation.getStatus().equals(InvitationStatus.IN_PROCESS)) {
            return stateMachineRepository.existsByMachineIdAndState(
                    invitation.getId().toString(),
                    EXPERT_CONCLUSION_UPLOADING.toString()
            );
        }

        return false;
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
        detailedPayment.setNumber(expertContract.getNumber());
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
                expertContract.getNumber(),
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

    private byte[] createContract(DetailedPaymentOutputData outputData, ExpertContract expertContract) {
        Expert expert = expertContract.getExpert();
        Project project = expertContract.getProject();

        List<Application> applications = getExpertApplications(expert);

        Contract contract = contractMapper.map(
                expertContract.getNumber(),
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

    public byte[] getDocumentsAsZipArchive(UUID projectId, UUID expertId) {
        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new ServiceException("Проект не найден"));

        Expert expert = expertRepository.findById(expertId)
                .orElseThrow(() -> new ServiceException("Эксперт не найден"));

        ExpertContract expertContract = getProjectContract(project, expert);

        DetailedPaymentOutputData detailedPayment = createDetailedPayment(expertContract);

        byte[] actBytes = createAct(detailedPayment, expertContract);

        byte[] contractBytes = createContract(detailedPayment, expertContract);

        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();

        try (ByteArrayOutputStream tempBos = byteArrayOutputStream;
             ZipOutputStream outputStream = new ZipOutputStream(tempBos)) {

            zip(detailedPayment.getSheetBytes(), outputStream, "рассчет.xlsx");
            zip(actBytes, outputStream, "акт.docx");
            zip(contractBytes, outputStream, "договор.docx");

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

    public Invitation getInitiationByExpertAndApplication(Expert expert, Application application) {
        return invitationRepository.findByExpertAndApplication(expert, application)
                .orElseThrow(IllegalStateException::new);
    }

}
