package ru.nsu.ccfit.khudyakov.expertise_helper.security;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;


@Configuration
@RequiredArgsConstructor
public class WebSecurityConfiguration extends WebSecurityConfigurerAdapter {

    private final OAuth2UserService userService;

    private final OAuth2SuccessHandler successHandler;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests()
                .antMatchers("/oauth2/**", "/login")
                    .permitAll()
                .anyRequest()
                    .authenticated()
                .and()
                .oauth2Login()
                    .loginPage("/login")
                    .userInfoEndpoint()
                        .userService(userService)
                    .and()
                    .successHandler(successHandler)
                    .failureHandler((request, response, exception) -> exception.printStackTrace())
                .and()
                .logout()
                    .logoutUrl("/logout")
                    .clearAuthentication(true)
                    .deleteCookies()
                    .permitAll();
    }

}
