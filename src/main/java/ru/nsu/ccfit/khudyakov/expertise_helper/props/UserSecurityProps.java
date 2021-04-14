package ru.nsu.ccfit.khudyakov.expertise_helper.props;


import lombok.Data;
import lombok.NonNull;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotBlank;

@Data
@Configuration
@ConfigurationProperties(prefix = "user.security")
@Validated
public class UserSecurityProps {

    @NonNull
    private char[] keyStorePassword;

    @NotBlank
    private String keyStoreName;

}
