package com.api.user.infrastructure.repository.constants;

public class UserConstants {

    public static final String SELECT_USER_WITH_PHONES_BY_EMAIL =
            "SELECT * FROM user u INNER JOIN phones p ON u.email = p.user_email WHERE u.email = '%s'";
}
