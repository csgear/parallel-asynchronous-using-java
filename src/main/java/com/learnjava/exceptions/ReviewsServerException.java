/*
 * Copyright (c) 2022 HyTrust Inc. All rights reserved.
 */

package com.learnjava.exceptions;

/**
 * @author Xiaojun.Yang
 */
public class ReviewsServerException extends RuntimeException{
    public ReviewsServerException(String message) {
        super(message);
    }
}
