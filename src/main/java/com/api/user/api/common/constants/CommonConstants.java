package com.api.user.api.common.constants;

public class CommonConstants {

    public final static String EMAIL_REGEXP = "^[A-Za-z0-9+_.-]+@(.+)$";
    public final static String PASSWORD_REGEXP = "^(?=.*[A-Z])(?=.*[a-z])(?=.*\\d.*\\d)[A-Za-z\\d]{6,}$";
    public final static String EMAIL_ERROR_MSG = "Email is not valid.";
    public final static String NAME_ERROR_MSG = "Name is required";
    public final static String PHONE_ERROR_MSG = "Phones required. At least 1 phone is required.";
    public final static String PASS_ERROR_MSG =
            "Password must contain at least one uppercase letter, one lowercase letter, two digits, " +
                    "and be at least 6 characters long.";
    public final static String PAYLOAD_ERROR_MSG = "Validation error in Payload.";
    public final static String BODY_ERROR_MSG = "Payload is not valid.";
}
