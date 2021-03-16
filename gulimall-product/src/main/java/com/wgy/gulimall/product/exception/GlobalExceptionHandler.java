package com.wgy.gulimall.product.exception;

import com.wgy.common.exception.BusinessCode;
import com.wgy.common.utils.R;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

/**
 * @Author WGY
 * @Date 2021/3/7
 * @Time 19:44
 * @description 集中处理所有异常
 * To change this template use File | Settings | File Templates.
 **/
@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public R handleValidateException(MethodArgumentNotValidException e) {
        log.error("数据校验出现问题{}，异常类型{}",e.getMessage(),e.getClass());
        final BindingResult bindingResult = e.getBindingResult();
        final Map<String, String> resultMap = new HashMap<>();
        bindingResult.getFieldErrors().forEach(error -> {
            resultMap.put(error.getField(), error.getDefaultMessage());
        });
        return R.error(BusinessCode.PARAM_VALID_ERROR).put("data", resultMap);
    }

    @ExceptionHandler(Throwable.class)
    public R handleException(Throwable throwable) {
        log.error("错误：", throwable);
        return R.error(BusinessCode.UN_KNOW_ERROR);
    }
}
