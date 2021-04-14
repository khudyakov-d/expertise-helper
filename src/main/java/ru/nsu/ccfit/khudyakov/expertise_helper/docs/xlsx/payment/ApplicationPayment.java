package ru.nsu.ccfit.khudyakov.expertise_helper.docs.xlsx.payment;

import lombok.Data;

import java.util.Arrays;
import java.util.List;

@Data
public class ApplicationPayment {

    private String topicNumber;

    private String topic;

    private String organization;

    private Integer pagesCount;

    private Double baseCost;

    private Double degreeFactor;

    private Double pageFactor;

    public List<Object> getFieldsAsList() {
        return Arrays.asList(
                topicNumber,
                topic,
                organization,
                baseCost,
                degreeFactor,
                pagesCount,
                pageFactor
        );
    }


}
