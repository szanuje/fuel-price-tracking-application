package com.licencjat.max.paliwa.security.user;


import org.springframework.data.util.Pair;

import java.util.Map;

public class Permissions {

    private static final Map<String, Pair<String, String>> userPermissions = Map.of(
            "USER", Pair.of(
                    "ACCESS_TEST,ACCESS_STATIONS,ACCESS_PRICES",
                    "/test,/stations/**,/prices/all"
            ),
            "ADMIN", Pair.of(
                    "ACCESS_USERS,ACCESS_CONSOLE",
                    "/console/**,/users/all"
            )
    );

    public static String getUserRoleAuthorities() {
        Pair<String, String> permissions = userPermissions.get("USER");
        return permissions.getFirst();
    }

    public static String getAdminRoleAuthorities() {
        Pair<String, String> permissions = userPermissions.get("ADMIN");
        return String.join(",", Permissions.getUserRoleAuthorities(), permissions.getFirst());
    }

    public static String getUserRoleMatchers() {
        Pair<String, String> permissions = userPermissions.get("USER");
        return permissions.getSecond();
    }

    public static String getAdminRoleMatchers() {
        Pair<String, String> permissions = userPermissions.get("ADMIN");
        return String.join(",", Permissions.getUserRoleMatchers(), permissions.getSecond());
    }

}
