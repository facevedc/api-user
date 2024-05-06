package com.api.user.infrastructure.repository.constants;

public class UserConstants {

    public static final String SELECT_USER_WITH_PHONES_BY_EMAIL =
            "SELECT u, p FROM UserEntity u INNER JOIN PhoneEntity p ON u.email = p.userEmail.email WHERE u.email = '%s'";
}
