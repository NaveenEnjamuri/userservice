package com.usermanagement.configuration.authentication;

import com.usermanagement.configuration.Auth;
import com.usermanagement.constants.UserServiceConstants;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;
import org.springframework.security.web.savedrequest.DefaultSavedRequest;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component(value = "authenticationSuccessHandler")
public class AuthenticationSuccessHandler extends SavedRequestAwareAuthenticationSuccessHandler {

    @Value("${server.servlet.session.timeout.web}")
    private int timoutForWeb;

    @Value("${server.servlet.session.timeout.mobile}")
    private int timoutForMobile;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request,
             HttpServletResponse response, Authentication authentication)
              throws IOException, ServletException {
        String channel = request.getHeader(UserServiceConstants.REQUEST_HEADER_CHANNEL);
        if (Auth.isWeb(channel)) {
            request.getSession().setMaxInactiveInterval(timoutForWeb);
        } else if (Auth.isMobile(channel)) {
            request.getSession().setMaxInactiveInterval(timoutForWeb);
        } else if (Auth.isOthers(channel)) {
            request.getSession().setMaxInactiveInterval(timoutForWeb / 2);
        } else {
            request.getSession().setMaxInactiveInterval(timoutForWeb);
        }
        DefaultSavedRequest defaultSavedRequest = (DefaultSavedRequest) request.getSession()
                .getAttribute("SPRING_SECURITY_SAVED_REQUEST_KEY");
        if (defaultSavedRequest != null) {
            String requestUrl = defaultSavedRequest.getRequestURL() + "?" + defaultSavedRequest.getQueryString();
            getRedirectStrategy().sendRedirect(request, response, requestUrl);
        } else {
            request.getRequestDispatcher("/authenticationSuccess").forward(request, response);
        }
    }
}
