/*
 * Copyright (c) 2022 HyTrust Inc. All rights reserved.
 */

package com.learnjava.util;

import com.learnjava.exceptions.MoviesInfoServerException;
import com.learnjava.exceptions.ReviewsServerException;
import reactor.core.Exceptions;
import reactor.util.retry.Retry;
import reactor.util.retry.RetrySpec;

import java.time.Duration;

/**
 * @author Xiaojun.Yang
 */
public class RetryUtil {
    public static Retry retrySpec() {
        return RetrySpec.fixedDelay(3, Duration.ofSeconds(1))
                .filter((ex) -> ex instanceof MoviesInfoServerException || ex instanceof ReviewsServerException)
                .onRetryExhaustedThrow(((retryBackoffSpec, retrySignal) -> Exceptions.propagate(retrySignal.failure())));
    }
}
