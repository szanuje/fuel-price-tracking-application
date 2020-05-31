package com.licencjat.max.fuel.security;

import org.springframework.data.util.Pair;

import java.util.ArrayList;
import java.util.List;

public class AuthenticationConstants {

    public static final String MAIN_PAGE_ENDPOINT = "/";
    public static final String ERROR_PAGE_ENDPOINT = "/error";

    public static final String LOGIN_ENDPOINT = "/login";
    public static final String LOGOUT_ENDPOINT = "/logout";
    public static final String REGISTER_ENDPOINT = "/register";

    public static final String TEST_URL_ENDPOINT = "/test";
    public static final String ADD_STATION_ENDPOINT = "/stations/add";

    public static final String GET_MARKERS_ENDPOINT = "/api/markers/get";
    public static final String ADD_PRICE_ENDPOINT = "/api/prices/add";

    public static final String CONSOLE_ENDPOINT = "/console/**";

    private static final Pair<String, String> ADD_STATION_AUTHORITY =
            Pair.of("ACCESS_ADDSTATION", ADD_STATION_ENDPOINT);
    private static final Pair<String, String> CONSOLE_ACCESS_AUTHORITY =
            Pair.of("ACCESS_CONSOLE", CONSOLE_ENDPOINT);

    private static List<Pair<String, String>> userPairs() {
        return List.of(
                ADD_STATION_AUTHORITY
        );
    }

    private static List<Pair<String, String>> adminPairs() {
        return List.of(
                ADD_STATION_AUTHORITY,
                CONSOLE_ACCESS_AUTHORITY
        );
    }

    public static String[] guestMatchers() {
        return List.of(
                MAIN_PAGE_ENDPOINT,
                LOGIN_ENDPOINT,
                REGISTER_ENDPOINT,
                ERROR_PAGE_ENDPOINT,
                TEST_URL_ENDPOINT,
                GET_MARKERS_ENDPOINT,
                ADD_PRICE_ENDPOINT
        ).toArray(String[]::new);
    }

    public static String[] userRoleAuthorities() {
        List<String> authorities = new ArrayList<>();
        userPairs().forEach(f -> authorities.add(f.getFirst()));
        return authorities.toArray(String[]::new);
    }

    public static String[] adminRoleAuthorities() {
        List<String> authorities = new ArrayList<>();
        adminPairs().forEach(f -> authorities.add(f.getFirst()));
        return authorities.toArray(String[]::new);
    }

    public static String userRoleAuthoritiesAsString() {
        return String.join(",", userRoleAuthorities());
    }

    public static String adminRoleAuthoritiesAsString() {
        return String.join(",", adminRoleAuthorities());
    }

    public static String[] userRoleMatchers() {
        List<String> matchers = new ArrayList<>();
        userPairs().forEach(f -> matchers.add(f.getSecond()));
        return matchers.toArray(String[]::new);
    }

    public static String[] adminRoleMatchers() {
        List<String> matchers = new ArrayList<>();
        adminPairs().forEach(f -> matchers.add(f.getSecond()));
        return matchers.toArray(String[]::new);
    }
}
