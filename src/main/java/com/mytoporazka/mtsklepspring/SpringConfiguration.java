package com.mytoporazka.mtsklepspring;

import com.mytoporazka.mtsklepspring.components.HardCodedConfig;
import com.mytoporazka.mtsklepspring.components.LoginSessionManager;
import com.mytoporazka.mtsklepspring.components.UserInfo;
import com.mytoporazka.mtsklepspring.components.interfaces.Config;
import com.mytoporazka.mtsklepspring.filters.UserFilter;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SpringConfiguration {

    @Bean
    public Config getConfig() {
        return new HardCodedConfig();
    }
}
