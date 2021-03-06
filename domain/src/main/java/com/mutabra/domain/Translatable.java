/*
 * Copyright (c) 2008-2013 Ivan Khalopik.
 * All rights reserved.
 */

package com.mutabra.domain;

/**
 * @author Ivan Khalopik
 * @since 1.0
 */
public interface Translatable {
    String NAME = "name";
    String DESCRIPTION = "description";

    String SUCCESS = "success";
    String FAILURE = "failure";

    String getBasename();

    String getCode();
}
