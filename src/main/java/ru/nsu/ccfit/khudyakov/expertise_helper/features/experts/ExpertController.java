package ru.nsu.ccfit.khudyakov.expertise_helper.features.experts;

import com.poiji.exception.HeaderMissingException;
import com.poiji.exception.PoijiException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.multipart.MaxUploadSizeExceededException;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import ru.nsu.ccfit.khudyakov.expertise_helper.exceptions.ServiceException;
import ru.nsu.ccfit.khudyakov.expertise_helper.features.experts.dtos.ExpertDegreeDto;
import ru.nsu.ccfit.khudyakov.expertise_helper.features.experts.dtos.ExpertDto;
import ru.nsu.ccfit.khudyakov.expertise_helper.features.experts.dtos.ExpertScienceCategoryDto;
import ru.nsu.ccfit.khudyakov.expertise_helper.features.experts.dtos.ExpertSheetDto;
import ru.nsu.ccfit.khudyakov.expertise_helper.features.experts.dtos.SheetFileBucket;
import ru.nsu.ccfit.khudyakov.expertise_helper.features.experts.entities.Expert;
import ru.nsu.ccfit.khudyakov.expertise_helper.features.experts.mappers.ExpertMapper;
import ru.nsu.ccfit.khudyakov.expertise_helper.features.experts.mappers.ExpertSheetMapper;
import ru.nsu.ccfit.khudyakov.expertise_helper.features.users.User;
import ru.nsu.ccfit.khudyakov.expertise_helper.features.users.UserService;
import ru.nsu.ccfit.khudyakov.expertise_helper.security.users.CustomOAuth2User;

import javax.validation.Valid;
import javax.validation.Validator;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.springframework.data.domain.Sort.Direction.DESC;
import static ru.nsu.ccfit.khudyakov.expertise_helper.ControllerUtils.getErrors;

@Controller
@RequiredArgsConstructor
public class ExpertController {

    private final UserService userService;

    private final ExpertService expertService;

    private final ExpertMapper expertMapper;

    private final ExpertSheetMapper expertSheetMapper;

    private final Validator validator;

    @GetMapping("/experts/import/sheet")
    public String sheetImport(Model model) {
        model.addAttribute("expert_header", ExpertDto.getExpertFieldsNames());
        return "experts/import-sheet";
    }

    @PostMapping("/experts/import/sheet")
    public String sheetImport(@ModelAttribute("user") User user,
                              @Valid SheetFileBucket fileBucket,
                              BindingResult bindingResult,
                              RedirectAttributes attributes) {
        if (bindingResult.hasErrors()) {
            getErrors(bindingResult).forEach(attributes::addFlashAttribute);
            return "redirect:/experts/import/sheet";
        }

        MultipartFile file = fileBucket.getFile();

        try (InputStream inputStream = file.getInputStream()) {
            List<ExpertSheetDto> expertDtos = expertService.getExpertFromSheet(inputStream);
            List<Expert> experts = toExperts(expertDtos);
            expertService.add(user, experts);

            attributes.addFlashAttribute("success", "success");
        } catch (HeaderMissingException e) {
            attributes.addFlashAttribute("error", "experts.import.sheet.error.header");
        } catch (PoijiException | IOException e) {
            attributes.addFlashAttribute("error", "experts.import.sheet.error.unknown");
        }

        return "redirect:/experts/import/sheet";
    }


    @ExceptionHandler(MaxUploadSizeExceededException.class)
    public String handleUploadOverSize(MaxUploadSizeExceededException e, RedirectAttributes attributes) {
        attributes.addAttribute("error", "file.upload.error.size.max");
        return "redirect:/experts/import/sheet";
    }

    private List<Expert> toExperts(List<ExpertSheetDto> expertDtos) {
        List<Expert> experts = new ArrayList<>();

        for (ExpertSheetDto expertSheetDto : expertDtos) {
            boolean b = validator.validate(expertSheetDto).isEmpty();
            if (!b) {
                continue;
            }

            try {
                Expert expert = expertSheetMapper.toExpert(expertSheetDto);
                experts.add(expert);
            } catch (IllegalArgumentException e) {
                e.getMessage();
            }
        }

        return experts;
    }

    @GetMapping("/experts")
    public String getList(@ModelAttribute("user") User user,
                          Model model,
                          @PageableDefault(sort = "id", direction = DESC) Pageable pageable) {
        model.addAttribute("page", getExpertDtosPage(user, pageable));
        model.addAttribute("expert_header", ExpertDto.getExpertFieldsNames());

        return "experts/list";
    }

    private Page<ExpertDto> getExpertDtosPage(User user, Pageable pageable) {
        Page<Expert> expertsPage = expertService.getExperts(user, pageable);
        List<ExpertDto> expertDtos = expertMapper.toExpertDto(expertsPage.getContent());
        return new PageImpl<>(expertDtos, pageable, expertsPage.getTotalElements());
    }

    @GetMapping("/experts/{id}")
    public String edit(@ModelAttribute("user") User user, @PathVariable UUID id, Model model) {
        Expert expert = expertService.findByUserAndId(user, id);
        ExpertDto expertDto = expertMapper.toExpertDto(expert);

        model.addAttribute("expert", expertDto);
        model.addAttribute("degrees", ExpertDegreeDto.values());
        model.addAttribute("categories", ExpertScienceCategoryDto.values());

        return "experts/edit";
    }

    @PutMapping("/experts/edit")
    public String edit(@ModelAttribute("user") User user,
                       @Valid ExpertDto expertDto,
                       BindingResult bindingResult,
                       Model model) {
        if (bindingResult.hasErrors()) {
            handleExpertValidationErrors(bindingResult, model);
            model.addAttribute("expert", expertDto);
            return "experts/edit";
        }

        Expert expert = expertMapper.toExpert(expertDto);
        expertService.update(user, expert);

        return "redirect:/experts";
    }


    @DeleteMapping("/experts/{id}")
    public String delete(@ModelAttribute("user") User user,
                         @PathVariable UUID id) {
        Expert expert = expertService.findByUserAndId(user, id);
        expertService.delete(user, expert);

        return "redirect:/experts";
    }


    @GetMapping("/experts/add")
    public String add(Model model) {
        model.addAttribute("degrees", ExpertDegreeDto.values());
        model.addAttribute("categories", ExpertScienceCategoryDto.values());

        return "experts/add";
    }

    @PostMapping("/experts/add")
    public String add(@ModelAttribute("user") User user,
                      @Valid ExpertDto expertDto,
                      BindingResult bindingResult,
                      RedirectAttributes attributes) {
        if (bindingResult.hasErrors()) {
            getErrors(bindingResult).forEach(attributes::addFlashAttribute);
            return "redirect:/experts/add";
        }

        Expert expert = expertMapper.toExpert(expertDto);
        try {
            expertService.add(user, expert);
        } catch (ServiceException ex) {
            attributes.addFlashAttribute("error", ex.getMessage());
        }


        return "redirect:/experts";
    }

    @ModelAttribute(value = "user", binding = false)
    public User getUser(@AuthenticationPrincipal CustomOAuth2User oAuth2User) {
        if (oAuth2User != null) {
            return userService.getByEmail(oAuth2User.getEmail());
        }
        return null;
    }

    private void handleExpertValidationErrors(BindingResult bindingResult, Model model) {
        model.addAttribute("degrees", ExpertDegreeDto.values());
        model.addAttribute("categories", ExpertScienceCategoryDto.values());
        model.mergeAttributes(getErrors(bindingResult));
    }


}
