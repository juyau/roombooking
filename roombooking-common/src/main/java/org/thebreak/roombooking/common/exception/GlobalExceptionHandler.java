package org.thebreak.roombooking.common.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.thebreak.roombooking.common.response.CommonCode;
import org.thebreak.roombooking.common.response.ResponseResult;

import java.time.DateTimeException;
import java.util.Arrays;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

//    @ExceptionHandler(value = HttpRequestMethodNotSupportedException.class)
//    @ResponseStatus(HttpStatus.METHOD_NOT_ALLOWED)
//    public ResponseResult httpRequestMethodNotSupportedException(HttpRequestMethodNotSupportedException e) {
//        return new ResponseResult(405, e.getMessage());
//    }


    @ExceptionHandler(value = CustomException.class)
    public ResponseResult customExceptions(CustomException e) {
        log.info(e.getCommonCode().getMessage());
        return new ResponseResult(e.getCommonCode());
    }

    @ExceptionHandler(value = Exception.class)
    public ResponseResult<CommonCode> allOtherExceptions(Exception e) {

        // missing request body;
        if (e instanceof HttpMessageNotReadableException) {
            return ResponseResult.fail("Required request body is missing.");
        }

        if (e instanceof IllegalArgumentException) {
            System.out.println(e.getMessage() + "IllegalArgumentException");
            return ResponseResult.fail(e.getMessage());
        }

        // param type not match, such as ObjectId length or type not match;
        if (e instanceof MissingServletRequestParameterException) {
            return ResponseResult.fail(e.getMessage());
        }

        // param type not match, such as ObjectId length or type not match;
        if (e instanceof MethodArgumentTypeMismatchException) {
            System.out.println(e.getMessage() + "MethodArgumentTypeMismatchException");
            return ResponseResult.fail(CommonCode.INVALID_PARAM.getMessage());
        }

        // http method not supported; - - need to check
        if (e instanceof MethodArgumentNotValidException) {
            System.out.println(e.getMessage() + "MethodArgumentNotValidException");
            return ResponseResult.fail(e.getMessage());
        }

        // http method not supported;
        if (e instanceof HttpRequestMethodNotSupportedException) {
            return ResponseResult.fail(e.getMessage());
        }

        // post Date time format invalid;
        if (e instanceof DateTimeException) {
            return ResponseResult.fail(e.getMessage());
        }

        log.error(Arrays.toString(e.getStackTrace()));
        e.printStackTrace();

        return ResponseResult.fail();
    }

}
