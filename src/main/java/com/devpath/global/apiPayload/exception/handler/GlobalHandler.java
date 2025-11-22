package com.devpath.global.apiPayload.exception.handler;

import com.devpath.global.apiPayload.code.BaseErrorCode;
import com.devpath.global.apiPayload.exception.GeneralException;

public class GlobalHandler extends GeneralException {
    public GlobalHandler(BaseErrorCode code) {
        super(code);
    }
}
