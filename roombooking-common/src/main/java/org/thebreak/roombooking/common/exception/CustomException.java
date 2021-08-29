package org.thebreak.roombooking.common.exception;

import org.thebreak.roombooking.common.response.CommonCode;

public class CustomException extends RuntimeException {

    CommonCode commonCode;

    public CustomException(CommonCode commonCode) {
        this.commonCode = commonCode;
    }

    public static void cast(CommonCode commonCode) {
        throw new CustomException(commonCode);
    }

    public CommonCode getCommonCode() {
        return commonCode;
    }
}
