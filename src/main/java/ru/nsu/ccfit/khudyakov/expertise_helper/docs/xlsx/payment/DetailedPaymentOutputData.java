package ru.nsu.ccfit.khudyakov.expertise_helper.docs.xlsx.payment;

import lombok.Getter;
import lombok.Setter;
import ru.nsu.ccfit.khudyakov.expertise_helper.docs.xlsx.SheetTemplateBuilder;


@Getter
@Setter
public class DetailedPaymentOutputData extends SheetTemplateBuilder.BuilderOutputData {

    private double cost;

    private double costWithTaxes;

}
