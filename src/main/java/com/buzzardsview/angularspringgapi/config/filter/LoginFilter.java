package com.buzzardsview.angularspringgapi.config.filter;

import com.buzzardsview.angularspringgapi.security.AppTokenProvider;
import com.buzzardsview.angularspringgapi.security.GoogleTokenVerifier;
import com.buzzardsview.angularspringgapi.security.exception.InvalidTokenException;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken.Payload;
import java.io.IOException;
import java.security.GeneralSecurityException;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class LoginFilter implements Filter {

    private GoogleTokenVerifier googleTokenVerifier;

    @Autowired
    public LoginFilter(GoogleTokenVerifier googleTokenVerifier) {
        this.googleTokenVerifier = googleTokenVerifier;
    }

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse,
                         FilterChain filterChain) throws IOException, ServletException {

        String idToken = ((HttpServletRequest) servletRequest).getHeader("X-ID-TOKEN");
        HttpServletResponse response = (HttpServletResponse) servletResponse;
        HttpServletRequest request = (HttpServletRequest) servletRequest;

        if (idToken != null) {
            final Payload payload;
            try {
                payload = googleTokenVerifier.verify(idToken);
                if (payload != null) {
                    String username = payload.getSubject();
                    String name = (String) payload.get("name");
                    String picture = (String) payload.get("picture");
                    request.setAttribute("name", name);
                    request.setAttribute("userId", username);
                    request.setAttribute("picture", picture);
                    AppTokenProvider.addAuthentication(response, username);
                    filterChain.doFilter(servletRequest, response);
                    return;
                }
            } catch (GeneralSecurityException | InvalidTokenException e) {
                // This is not a valid token, we will send HTTP 401 back
            }
        }
        ((HttpServletResponse) servletResponse).sendError(HttpServletResponse.SC_UNAUTHORIZED);
    }

    @Override
    public void destroy() {
    }
}