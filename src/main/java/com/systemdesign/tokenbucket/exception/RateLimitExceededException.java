package com.systemdesign.tokenbucket.exception;

import com.systemdesign.tokenbucket.common.exception.BusinessException;
import com.systemdesign.tokenbucket.common.exception.ExceptionCode;

public class RateLimitExceededException extends BusinessException {

    public RateLimitExceededException(ExceptionCode exceptionCode, Object... rejectedValues) {
        super(exceptionCode, rejectedValues);
    }
}
