package com.mytoporazka.mtsklepspring.filters;

import com.mytoporazka.lib.domain.User;
import com.mytoporazka.lib.domain.UserType;
import com.mytoporazka.mtsklepspring.components.DatabaseRepository;
import com.mytoporazka.mtsklepspring.components.LoginSessionManager;
import com.mytoporazka.mtsklepspring.components.UserInfo;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Arrays;

@Component
@Order(0)
public class UserFilter extends OncePerRequestFilter {

    private final LoginSessionManager loginSession;
    private final DatabaseRepository repository;

    public UserFilter(LoginSessionManager loginSession, DatabaseRepository repository) {
        this.loginSession = loginSession;
        this.repository = repository;
    }

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain) throws ServletException, IOException {

        if(request.getRequestURI().startsWith("/app")) {
            var cookies = request.getCookies();
            if (cookies == null) {
                request = new HttpServletRequestWrapper((HttpServletRequest) request) {
                    @Override
                    public String getRequestURI() {
                        return "/login";
                    }
                };
            } else {
                var cookie = Arrays
                        .stream(cookies).filter(c ->
                                c.getName().equals("id") &&
                                        c.getValue() != null &&
                                        !c.getValue().equals(""))
                        .findFirst();

                if (cookie.isEmpty()) {
                    request = new HttpServletRequestWrapper((HttpServletRequest) request) {
                        @Override
                        public String getRequestURI() {
                            return "/login";
                        }
                    };
                } else {
                    var cookieValue = cookie.get().getValue();
                    var existsSession = loginSession.existsSession(cookieValue);

                    if (!existsSession) {
                        request = new HttpServletRequestWrapper((HttpServletRequest) request) {
                            @Override
                            public String getRequestURI() {
                                return "/login";
                            }
                        };
                    } else {
                        var idOption = loginSession.getUserIdBySessionId(cookieValue);
                        if (idOption.isEmpty()) {
                            return;
                        }
                        User user = repository.user.getFirstByPredicate(u -> u.userId == idOption.get());
                        var userInfo = new UserInfo();
                        userInfo.email = user.email;
                        userInfo.isAdmin = user.userType == UserType.ADMIN;
                        userInfo.id = user.userId;
                        userInfo.sessionId = cookieValue;
                        request.setAttribute("userInfo", userInfo);
                    }
                }
            }
        }

        filterChain.doFilter(request, response);
    }

    @Bean
    public FilterRegistrationBean<UserFilter> userFilterRegistrationBean() {
        var bean = new FilterRegistrationBean<UserFilter>();
        var filter = new UserFilter(loginSession, repository);

        bean.setFilter(filter);
        bean.addUrlPatterns("/app/*");
        bean.setOrder(2);
        return bean;
    }
}
