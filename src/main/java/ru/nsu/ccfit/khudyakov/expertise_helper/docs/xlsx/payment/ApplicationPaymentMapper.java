package ru.nsu.ccfit.khudyakov.expertise_helper.docs.xlsx.payment;

import org.mapstruct.Mapper;
import ru.nsu.ccfit.khudyakov.expertise_helper.features.applications.entities.Application;

@Mapper(componentModel = "spring")
public interface ApplicationPaymentMapper {


    ApplicationPayment map(Application application,
                           Double baseCost,
                           Double degreeFactor,
                           Double pageFactor);

}
