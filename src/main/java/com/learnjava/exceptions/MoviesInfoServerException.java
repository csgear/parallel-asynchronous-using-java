/*
 * Copyright (c) 2022 HyTrust Inc. All rights reserved.
 */

package com.learnjava.exceptions;

/**
 * @author Xiaojun.Yang
 */
public class MoviesInfoServerException extends RuntimeException{
    public MoviesInfoServerException(String message) {
        super(message);
    }
}
