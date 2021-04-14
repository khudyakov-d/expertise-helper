package ru.nsu.ccfit.khudyakov.expertise_helper.docs.xlsx.total_payment;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TotalPayment {

    private Double baseCost;

    private List<ExpertProjectPayment> payments;

}
