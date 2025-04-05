package com.examportal.configuration.authentication;

import com.examportal.exception.custom.UnauthorisedException;
import org.springframework.stereotype.Component;

@Component("securityParams")
public interface SecurityParams {

    public String getMyUsername() throws UnauthorisedException;

    public String getMyIp() throws UnauthorisedException;

    public String[] getMyRoles() throws UnauthorisedException;

}
