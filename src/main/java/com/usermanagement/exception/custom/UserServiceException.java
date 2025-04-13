package com.usermanagement.exception.custom;

public class UserServiceException extends Exception{

    private static final long serialVersionUID = -1531621793117954640L;

    private final String message;

    public  UserServiceException(String message) {
        super(message);
        this.message = message;
    }

    @Override
    public String getMessage() {
        return message;
    }

}
