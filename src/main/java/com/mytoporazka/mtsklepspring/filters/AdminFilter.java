package com.mytoporazka.mtsklepspring.filters;

import com.mytoporazka.mtsklepspring.components.DatabaseRepository;
import com.mytoporazka.mtsklepspring.components.LoginSessionManager;
import com.mytoporazka.mtsklepspring.components.UserInfo;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
@Order(1)
public class AdminFilter extends OncePerRequestFilter {

    private final LoginSessionManager loginSession;
    private final DatabaseRepository repository;

    public AdminFilter(LoginSessionManager loginSession, DatabaseRepository repository) {
        this.loginSession = loginSession;
        this.repository = repository;
    }

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain) throws ServletException, IOException {

        if(request.getRequestURI().startsWith("/app/admin")) {
            var userInfo = (UserInfo) request.getAttribute("userInfo");

            if (userInfo == null || !userInfo.isAdmin) {
                request = new HttpServletRequestWrapper(request) {
                    @Override
                    public String getRequestURI() {
                        return "/app/menu";
                    }
                };
            }
        }

        filterChain.doFilter(request, response);
    }

    @Bean
    public FilterRegistrationBean<AdminFilter> adminFilterRegistrationBean() {
        var bean = new FilterRegistrationBean<AdminFilter>();
        var filter = new AdminFilter(loginSession, repository);

        bean.setFilter(filter);
        bean.addUrlPatterns("/app/admin/*");
        bean.setOrder(3);
        return bean;
    }
}
