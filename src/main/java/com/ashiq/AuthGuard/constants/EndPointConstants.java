package com.ashiq.AuthGuard.constants;

public interface EndPointConstants {
    // Auth
    String AUTH_BASE = "/api/v1/auth";
    String AUTH_REGENERATE_TOKEN = "/regenerate-token";
    String AUTH_REFRESH_TOKEN = "/refresh-token";

    // Permission Test

    String TEST_BASE = "/api/test";

    // USER endpoints
    String USER_VIEW = "/user/view";
    String USER_CREATE = "/user/create";
    String USER_UPDATE = "/user/update";
    String USER_DELETE = "/user/delete";

    // ROLE endpoints
    String ROLE_VIEW = "/role/view";
    String ROLE_CREATE = "/role/create";
    String ROLE_UPDATE = "/role/update";
    String ROLE_DELETE = "/role/delete";

    // Role-based access tests
    String ADMIN_ONLY = "/admin-only";
    String USER_ONLY = "/user-only";
}
