package ru.nsu.ccfit.khudyakov.expertise_helper.features.experts.dtos;

import com.poiji.annotation.ExcelCellName;
import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;

@Data
public class ExpertSheetDto {

    @ExcelCellName("ФИО")
    @NotBlank
    private String name;

    @ExcelCellName("Организация")
    @NotBlank
    private String organization;

    @ExcelCellName("Должность")
    private String post;

    @ExcelCellName("Ученая степень")
    @NotNull
    private String degree;

    @ExcelCellName("Категория ОУС")
    private String scienceCategory;

    @ExcelCellName("Почта")
    @Email
    @NotBlank
    private String email;

    @ExcelCellName("Рабочий телефон")
    private String workPhone;

    @ExcelCellName("Личный телефон")
    private String personalPhone;

    @ExcelCellName("Дата рождения")
    private LocalDate birthDate;

}
