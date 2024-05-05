package com.api.user.infrastructure.repository.constants;

public class SessionConstants {

    public static final String SELECT_LAST_SESSION_AVAILABLE =
            "SELECT * FROM session t WHERE t.email_user = '%s' AND t.expired > '%s' ORDER BY t.create DESC";

    public static final String SELECT_SESSION_BY_EMAIL_TOKEN =
            "SELECT * FROM session t WHERE t.token = '%s' AND t.email_user = '%s'";

}
