package com.sist.web.security;

import java.security.Principal;

public class StompPrincipal implements Principal {

    private final String name;
    private final String role;

    public StompPrincipal(String name, String role) {
        this.name = name;
        this.role = role;
    }

    @Override
    public String getName() {
        return name;
    }

    public String getRole() {
        return role;
    }
}

