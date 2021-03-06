package com.buzzardsview.angularspringgapi.config.filter;

import java.io.IOException;
import java.util.Optional;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import static com.buzzardsview.angularspringgapi.security.AppTokenProvider.addAuthentication;
import static com.buzzardsview.angularspringgapi.security.AppTokenProvider.getUserFromToken;


@Component
public class RestFilter implements Filter {

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse,
                         FilterChain filterChain) throws IOException, ServletException {

        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;

        Optional<String> userFromToken = getUserFromToken(request);

        if (((HttpServletRequest) servletRequest).getMethod().equals("GET")) {
            userFromToken.ifPresent(s -> request.setAttribute("userId", s));
            filterChain.doFilter(request, servletResponse);
            return;
        }

        if (userFromToken.isEmpty()) {
            response.sendError(HttpStatus.UNAUTHORIZED.value());
            return;
        }

        request.setAttribute("userId", userFromToken.get());
        addAuthentication(response, userFromToken.get());
        filterChain.doFilter(request, servletResponse);
    }

    @Override
    public void destroy() {
    }
}