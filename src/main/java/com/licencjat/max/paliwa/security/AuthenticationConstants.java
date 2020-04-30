package com.licencjat.max.paliwa.security;

import org.springframework.data.util.Pair;
import org.springframework.stereotype.Controller;

import java.util.ArrayList;
import java.util.List;

@Controller
public class AuthenticationConstants {

    static final String MAIN_PAGE_URL = "http://localhost:8080/";

    static final String MAIN_PAGE_ENDPOINT = "/";
    static final String ERROR_PAGE_ENDPOINT = "/error";
    static final String LOGIN_POST_URL_ENDPOINT = "/login";
    static final String REGISTER_POST_URL_ENDPOINT = "/register";
    static final String DEFAULT_SUCCESS_URL_ENDPOINT = "/";
    static final String LOGOUT_URL_ENDPOINT = "/logout";
    static final String LOGOUT_SUCCESS_URL_ENDPOINT = "/";
    static final String TEST_URL_ENDPOINT = "/test";

    static final String GET_MARKERS_ENDPOINT = "/stations/get";
    static final String GET_STATION_ENDPOINT = "/stations/{id}";
    static final String ADD_PRICE_ENDPOINT = "/prices/add/**";

    static final String ADD_STATION_ENDPOINT = "/stations/add";

    static final String CONSOLE_URL_ENDPOINTS = "/console/**";

    private static final Pair<String, String> ADD_STATION_AUTHORITY = Pair.of("ACCESS_ADDSTATION", ADD_STATION_ENDPOINT);

    private static final Pair<String, String> CONSOLE_ACCESS_AUTHORITY = Pair.of("ACCESS_CONSOLE", CONSOLE_URL_ENDPOINTS);

    private static List<Pair<String, String>> userPairs() {
        return List.of(
                ADD_STATION_AUTHORITY
        );
    }

    private static List<Pair<String, String>> adminPairs() {
        return List.of(
                CONSOLE_ACCESS_AUTHORITY
        );
    }

    public static String[] guestMatchers() {
        return List.of(
                MAIN_PAGE_ENDPOINT,
                LOGIN_POST_URL_ENDPOINT,
                REGISTER_POST_URL_ENDPOINT,
                ERROR_PAGE_ENDPOINT,
                TEST_URL_ENDPOINT,
                CONSOLE_URL_ENDPOINTS,
                GET_MARKERS_ENDPOINT,
                GET_STATION_ENDPOINT,
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
