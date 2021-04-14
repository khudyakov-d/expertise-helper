package ru.nsu.ccfit.khudyakov.expertise_helper.docs.xlsx.payment;

import lombok.Data;

import java.util.List;

@Data
public class DetailedPayment {

    private String number;

    private List<ApplicationPayment> payments;

}
