package com.api.user.api.common.constants;

public class CommonConstants {

    public final static String EMAIL_REGEXP = "^[A-Za-z0-9+_.-]+@(.+)$";
    public final static String PASSWORD_REGEXP = "^(?=.*[A-Z])(?=.*[a-z])(?=.*\\d.*\\d)[A-Za-z\\d]{6,}$";
    public final static String EMAIL_ERROR_MSG = "Email is not valid.";
    public final static String EMAIL_REQUIRED_MSG = "Email is required.";
    public final static String PAYLOAD_ERROR_MSG = "Validation error in Payload.";
    public final static String BODY_ERROR_MSG = "Payload is not valid.";
}
