package cn.cccs7.shared.model.util;

import cn.cccs7.shared.constant.NoOopsConstants;
import cn.cccs7.shared.model.NoOopsResponse;
import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.MDC;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.time.LocalDateTime;

public class NoOopsResponseBuilder<T> {

    private final NoOopsResponse<T> response;

    private NoOopsResponseBuilder() {
        this.response = new NoOopsResponse<>();
        this.response.setTimestamp(LocalDateTime.now());

        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        String traceIdStr = null;
        LocalDateTime requestedAt = null;
        if (attributes != null) {
            HttpServletRequest request = attributes.getRequest();

            Object reqTime = request.getAttribute(NoOopsConstants.REQUEST_DATETIME);
            if (reqTime instanceof LocalDateTime dateTime) {
                requestedAt = dateTime;
            }

            Object traceIdAttr = request.getAttribute("X-Trace-Id");
            if (traceIdAttr != null) {
                traceIdStr = traceIdAttr.toString();
            }
            if (traceIdStr == null || traceIdStr.isEmpty()) {
                String headerTraceId = request.getHeader("X-Trace-Id");
                if (headerTraceId != null && !headerTraceId.isEmpty()) {
                    traceIdStr = headerTraceId;
                }
            }
        }
        if (requestedAt == null) {
            requestedAt = LocalDateTime.now();
        }
        this.response.setRequestedTime(requestedAt);

        if (traceIdStr == null || traceIdStr.isEmpty()) {
            String mdcTrace = MDC.get("traceId");
            if (mdcTrace != null && !mdcTrace.isEmpty()) {
                traceIdStr = mdcTrace;
            }
        }
        if (traceIdStr != null && !traceIdStr.isEmpty()) {
            this.response.setTraceId(traceIdStr);
        }
    }

    public static <T> NoOopsResponseBuilder<T> builder() {
        return new NoOopsResponseBuilder<>();
    }

    public NoOopsResponseBuilder<T> success(boolean success) {
        response.setSuccess(success);
        return this;
    }

    public NoOopsResponseBuilder<T> code(String code) {
        response.setCode(code);
        return this;
    }

    public NoOopsResponseBuilder<T> message(String message) {
        response.setMessage(message);
        return this;
    }

    public NoOopsResponseBuilder<T> data(T data) {
        response.setData(data);
        return this;
    }

    public static <T> NoOopsResponse<T> ok(T data) {
        return NoOopsResponseBuilder.<T>builder()
                .success(true)
                .code(NoOopsConstants.RESPONSE_CODE_SUCCESS)
                .data(data)
                .build();
    }

    public static <T> NoOopsResponse<T> fail(String code, String message) {
        return NoOopsResponseBuilder.<T>builder()
                .success(false)
                .code(code)
                .message(message)
                .build();
    }

    public NoOopsResponse<T> build() {
        return this.response;
    }
}