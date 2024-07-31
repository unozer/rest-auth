package it.gp.rest_auth.utils;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;

import java.util.List;

public class JwtUtil {

    private static final int EXPIRE_HOUR_ACCESS_TOKEN = 24;
    private static final int EXPIRE_HOUR_REFRESH_TOKEN = 48;

    public static String createAccessToken(String username, String issuer, List<String> roles) {
        return "";
        //TODO
    }

    public static String createRefreshToken(String username) {
        return "";
        //TODO
    }

    public static UsernamePasswordAuthenticationToken parseToken(String token) {
        return null;
        //TODO
    }
}
