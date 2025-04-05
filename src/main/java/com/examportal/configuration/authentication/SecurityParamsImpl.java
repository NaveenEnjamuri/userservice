package com.examportal.configuration.authentication;

import com.examportal.exception.custom.UnauthorisedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.web.authentication.WebAuthenticationDetails;

public class SecurityParamsImpl implements SecurityParams{

    private org.springframework.security.core.userdetails.User getSecurityUser() {
        return (org.springframework.security.core.userdetails.User) SecurityContextHolder.
                getContext().getAuthentication().getPrincipal();
    }

    @Override
    public String getMyUsername() throws UnauthorisedException {
        try {
            return getSecurityUser().getUsername();
        } catch (Exception e) {
            throw new UnauthorisedException("You haven't logged-in. Please login and Try again. ");
        }
    }

    @Override
    public String getMyIp() throws UnauthorisedException {
        try {
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            WebAuthenticationDetails details = (WebAuthenticationDetails) auth.getDetails();
            return details.getRemoteAddress();
        } catch (Exception e) {
            throw new UnauthorisedException(Messages.UNAUTHORISED);
        }
    }

    @Override
    public String[] getMyRoles() throws UnauthorisedException {
        String[] roles = null;
        try {
            roles = new String[SecurityContextHolder.getContext().getAuthentication().getAuthorities().toArray().length];

            for (int i = 0; i < roles.length; i++) {
                roles[i] = SecurityContextHolder.getContext().getAuthentication().getAuthorities().toArray()[i].toString();
            }
            return roles;
        } catch (Exception e) {
            throw new UnauthorisedException(Messages.UNAUTHORISED);
        }

    }
}
