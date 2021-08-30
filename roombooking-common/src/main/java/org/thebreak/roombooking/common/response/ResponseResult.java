package org.thebreak.roombooking.common.response;

import lombok.Data;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Data
public class ResponseResult<T> {

    private Boolean success;
    private int code;
    private String message;
    private LocalDateTime timestamp;
    private T data;

    public ResponseResult() {
        this.timestamp = LocalDateTime.now();
    }

    public ResponseResult(CommonCode commonCode) {
        this.success = commonCode.getSuccess();
        this.code = commonCode.getCode();
        this.message = commonCode.getMessage();
        this.timestamp = LocalDateTime.now();
    }

    public static <T> ResponseResult<T> success(T data) {
        ResponseResult<T> responseResult = new ResponseResult<>(CommonCode.SUCCESS);
        responseResult.setData(data);
        return responseResult;
    }

    public static <T> ResponseResult<T> success() {
        return new ResponseResult<>(CommonCode.SUCCESS);
    }

    public static <T> ResponseResult<T> fail() {
        return new ResponseResult<>(CommonCode.SYSTEM_ERROR);
    }

    public static <T> ResponseResult<T> fail(String message) {
        ResponseResult<T> responseResult = new ResponseResult<>();
        responseResult.setSuccess(false);
        responseResult.setCode(CommonCode.SYSTEM_ERROR.getCode());
        responseResult.setMessage(message);
        return responseResult;
    }
}
