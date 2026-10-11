package com.sdatithe.web;

import org.springframework.security.oauth2.jwt.Jwt;

/** Reads the authenticated identity from the JWT. Scope (member / church id) always comes from the token. */
public final class Auth {
    private Auth() {}

    public static Long id(Jwt jwt) { return Long.valueOf(jwt.getSubject()); }

    public static String role(Jwt jwt) { return jwt.getClaimAsString("role"); }

    public static Long churchId(Jwt jwt) {
        Object c = jwt.getClaim("churchId");
        return c == null ? null : ((Number) c).longValue();
    }
}
