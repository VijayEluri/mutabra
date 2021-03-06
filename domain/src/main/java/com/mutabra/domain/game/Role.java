/*
 * Copyright (c) 2008-2013 Ivan Khalopik.
 * All rights reserved.
 */

package com.mutabra.domain.game;

import com.mutabra.domain.CodeUtils;
import com.mutabra.domain.Translatable;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

/**
 * @author Ivan Khalopik
 * @since 1.0
 */
public enum Role implements Translatable {
    ADMIN("*"),
    USER("game:play");

    public static final String BASENAME = "role";

    private final String code;
    private final Set<String> permissions;

    private Role(final String... permissions) {
        this.permissions = new HashSet<String>(Arrays.asList(permissions));

        code = CodeUtils.generateCode(this);
    }

    public String getBasename() {
        return BASENAME;
    }

    public String getCode() {
        return code;
    }

    public Set<String> getPermissions() {
        return permissions;
    }
}
