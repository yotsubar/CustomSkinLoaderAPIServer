package org.yotsubar.mccustomskinloaderserver.error;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.servlet.http.HttpServletRequest;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class AppErrorHandler {

    private static final Logger LOGGER = LoggerFactory.getLogger(AppErrorHandler.class);

    @ExceptionHandler(ResourceNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public Map<String, String> notFound(ResourceNotFoundException e) {
        Map<String, String> result = new HashMap<>();
        result.put("code", "404");
        result.put("message", e.getResource() + " not found");
        return result;
    }

    @ExceptionHandler(BadRequestException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Map<String, String> badRequest(BadRequestException e) {
        Map<String, String> result = new HashMap<>();
        result.put("code", "400");
        if (StringUtils.hasText(e.getMessage())) {
            result.put("message", e.getMessage());
        }
        return result;
    }

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public Map<String, String> handleError(HttpServletRequest request, Exception e) {
        LOGGER.error("请求处理异常|uri={},msg={}", request.getRequestURI(), e.getMessage(), e);
        return Collections.singletonMap("code", "500");
    }
}
