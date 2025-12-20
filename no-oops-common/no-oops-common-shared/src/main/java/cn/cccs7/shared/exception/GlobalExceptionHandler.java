package cn.cccs7.shared.exception;

import cn.cccs7.shared.model.NoOopsResponse;
import cn.cccs7.shared.model.util.NoOopsResponseBuilder;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * Global Exception Handler to intercept and unify all exceptions thrown by Controllers
 */
@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    /**
     * Handle custom business exceptions (BizException)
     * Logged as INFO as these are expected business logic flows
     */
    @ExceptionHandler(BizException.class)
    public NoOopsResponse<Void> handleBizException(BizException e, HttpServletRequest request) {
        log.info("Business Exception: uri={}, code={}, message={}",
                request.getRequestURI(), e.getCode(), e.getMessage());

        return NoOopsResponseBuilder.<Void>builder()
                .success(false)
                .code(e.getCode())
                .message(e.getMessage())
                .build();
    }

    /**
     * Handle Bean Validation exceptions
     * Triggered by @Valid, @NotNull, @Size, etc.
     */
    @ExceptionHandler({MethodArgumentNotValidException.class, BindException.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public NoOopsResponse<Void> handleValidationException(Exception e, HttpServletRequest request) {
        String message = e instanceof MethodArgumentNotValidException ex
                ? ex.getBindingResult().getAllErrors().get(0).getDefaultMessage()
                : ((BindException) e).getBindingResult().getAllErrors().get(0).getDefaultMessage();

        log.warn("Validation Failed: uri={}, message={}", request.getRequestURI(), message);

        return NoOopsResponseBuilder.<Void>builder()
                .success(false)
                .code("400")
                .message(message)
                .build();
    }

    /**
     * Handle unexpected system exceptions
     * Logged as ERROR for alerting and troubleshooting
     */
    @ExceptionHandler(Throwable.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public NoOopsResponse<Void> handleThrowable(Throwable e, HttpServletRequest request) {
        log.error("Internal System Error: uri={}", request.getRequestURI(), e);

        return NoOopsResponseBuilder.<Void>builder()
                .success(false)
                .code("500")
                .message("Internal Server Error, please contact administrator")
                .build();
    }
}