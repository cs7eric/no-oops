package cn.cccs7.autoconfig.config;

import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.validation.BindException;
import org.springframework.lang.Nullable;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.time.Duration;
import java.time.ZoneId;
import java.util.TimeZone;
import java.util.UUID;
import jakarta.servlet.DispatcherType;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.core.Ordered;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.client.RestTemplate;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;
import org.springframework.boot.autoconfigure.jackson.Jackson2ObjectMapperBuilderCustomizer;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.CorsRegistration;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.slf4j.MDC;

@AutoConfiguration
@EnableConfigurationProperties(CommonAutoConfiguration.CommonProperties.class)
@RestControllerAdvice
public class CommonAutoConfiguration {

    private static final String TRACE_HEADER = "X-Trace-Id";

    @Bean
    public WebMvcConfigurer corsConfigurer(CommonProperties properties) {
        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                CommonProperties.Cors cors = properties.getCors();
                if (cors == null || !cors.isEnabled()) {
                    return;
                }
                CorsRegistration reg = registry.addMapping("/**");
                List<String> patterns = cors.getAllowedOriginPatterns();
                List<String> origins = cors.getAllowedOrigins();
                boolean allowCredentials = cors.isAllowCredentials();
                if (patterns != null && !patterns.isEmpty()) {
                    reg.allowedOriginPatterns(toArray(patterns, "*"));
                } else if (allowCredentials && origins != null && origins.stream().anyMatch(o -> "*".equals(o))) {
                    reg.allowedOriginPatterns("*");
                } else {
                    reg.allowedOrigins(toArray(origins, "*"));
                }
                reg.allowedMethods(toArray(cors.getAllowedMethods(), "GET", "POST", "PUT", "DELETE", "OPTIONS"));
                reg.allowedHeaders(toArray(cors.getAllowedHeaders(), "*"));
                reg.allowCredentials(cors.isAllowCredentials());
                reg.maxAge(cors.getMaxAge());
            }
        };
    }

    @Bean
    @Primary
    public RestTemplate restTemplate(RestTemplateBuilder builder, CommonProperties properties) {
        CommonProperties.RestTemplateProps rt = properties.getRestTemplate();
        Duration connect = rt != null && rt.getConnectTimeout() != null ? rt.getConnectTimeout() : Duration.ofSeconds(2);
        Duration read = rt != null && rt.getReadTimeout() != null ? rt.getReadTimeout() : Duration.ofSeconds(5);
        
        // 使用新的方法替代已过时的setter方法
        RestTemplate tpl = builder
                .connectTimeout(connect)
                .readTimeout(read)
                .build();
                
        tpl.getInterceptors().add((request, body, execution) -> {
            String traceId = MDC.get("traceId");
            if (traceId != null && !traceId.isEmpty()) {
                request.getHeaders().add(TRACE_HEADER, traceId);
            }
            return execution.execute(request, body);
        });
        return tpl;
    }

    @Bean
    public Jackson2ObjectMapperBuilderCustomizer jacksonCustomizer(CommonProperties properties) {
        return new Jackson2ObjectMapperBuilderCustomizer() {
            @Override
            public void customize(Jackson2ObjectMapperBuilder builder) {
                CommonProperties.JacksonProps jp = properties.getJackson();
                String tz = jp != null && jp.getTimezone() != null ? jp.getTimezone() : "UTC";
                String df = jp != null && jp.getDateFormat() != null ? jp.getDateFormat() : "yyyy-MM-dd HH:mm:ss";
                builder.timeZone(TimeZone.getTimeZone(ZoneId.of(tz)));
                builder.simpleDateFormat(df);
            }
        };
    }

    @Bean
    public TraceIdFilter traceIdFilter() {
        return new TraceIdFilter();
    }

    @Bean
    public FilterRegistrationBean<TraceIdFilter> traceIdFilterRegistration(TraceIdFilter filter) {
        FilterRegistrationBean<TraceIdFilter> registration = new FilterRegistrationBean<>(filter);
        registration.setName("traceIdFilter");
        registration.setOrder(Ordered.HIGHEST_PRECEDENCE);
        registration.addUrlPatterns("/*");
        registration.setDispatcherTypes(DispatcherType.REQUEST, DispatcherType.ERROR);
        return registration;
    }


    @Bean
    @ConditionalOnClass(name = "feign.RequestInterceptor")
    public Object traceIdFeignInterceptor() {
        try {
            Class<?> iface = Class.forName("feign.RequestInterceptor");
            ClassLoader cl = iface.getClassLoader();
            java.lang.reflect.InvocationHandler handler = (proxy, method, args) -> {
                if ("apply".equals(method.getName()) && args != null && args.length == 1 && args[0] != null) {
                    String traceId = MDC.get("traceId");
                    if (traceId != null && !traceId.isEmpty()) {
                        java.lang.reflect.Method headerMethod = args[0].getClass().getMethod("header", String.class, String[].class);
                        headerMethod.invoke(args[0], TRACE_HEADER, new String[]{traceId});
                    }
                }
                return null;
            };
            return java.lang.reflect.Proxy.newProxyInstance(cl, new Class[]{iface}, handler);
        } catch (Exception e) {
            throw new IllegalStateException("Failed to create Feign RequestInterceptor proxy", e);
        }
    }

    private static String[] toArray(@Nullable List<String> list, String... defaults) {
        if (list == null || list.isEmpty()) {
            return defaults;
        }
        return list.toArray(new String[0]);
    }

    public static class TraceIdFilter extends OncePerRequestFilter {
        @Override
        protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
            String traceId = request.getHeader(TRACE_HEADER);
            if (traceId == null || traceId.isEmpty()) {
                traceId = UUID.randomUUID().toString().replace("-", "");
            }
            // propagate trace id to response header and request attributes
            response.setHeader(TRACE_HEADER, traceId);
            request.setAttribute(TRACE_HEADER, traceId);
            request.setAttribute(cn.cccs7.shared.constant.NoOopsConstants.REQUEST_DATETIME, java.time.LocalDateTime.now());
            MDC.put("traceId", traceId);
            try {
                filterChain.doFilter(request, response);
            } finally {
                MDC.remove("traceId");
            }
        }
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<String> handleValidationExceptions(MethodArgumentNotValidException ex) {
        StringBuilder errors = new StringBuilder();
        ex.getBindingResult().getFieldErrors().forEach(error -> 
            errors.append(error.getField()).append(": ").append(error.getDefaultMessage()).append("; ")
        );
        return ResponseEntity.badRequest().body(errors.toString());
    }

    @ExceptionHandler(BindException.class)
    public ResponseEntity<String> handleBindException(BindException ex) {
        StringBuilder errors = new StringBuilder();
        ex.getBindingResult().getFieldErrors().forEach(error ->
            errors.append(error.getField()).append(": ").append(error.getDefaultMessage()).append("; ")
        );
        return ResponseEntity.badRequest().body(errors.toString());
    }

    @ConfigurationProperties(prefix = "noops.common")
    public static class CommonProperties {
        private Cors cors = new Cors();
        private RestTemplateProps restTemplate = new RestTemplateProps();
        private JacksonProps jackson = new JacksonProps();

        public Cors getCors() { return cors; }
        public void setCors(Cors cors) { this.cors = cors; }
        public RestTemplateProps getRestTemplate() { return restTemplate; }
        public void setRestTemplate(RestTemplateProps restTemplate) { this.restTemplate = restTemplate; }
        public JacksonProps getJackson() { return jackson; }
        public void setJackson(JacksonProps jackson) { this.jackson = jackson; }

        public static class Cors {
            private boolean enabled = true;
            private List<String> allowedOrigins = new ArrayList<>(Collections.singletonList("*"));
            private List<String> allowedMethods = new ArrayList<>();
            private List<String> allowedHeaders = new ArrayList<>(Collections.singletonList("*"));
            private List<String> allowedOriginPatterns = new ArrayList<>();
            private boolean allowCredentials = true;
            private long maxAge = 1800L;

            public Cors() {
                if (allowedMethods.isEmpty()) {
                    allowedMethods.add("GET");
                    allowedMethods.add("POST");
                    allowedMethods.add("PUT");
                    allowedMethods.add("DELETE");
                    allowedMethods.add("OPTIONS");
                }
            }

            public boolean isEnabled() { return enabled; }
            public void setEnabled(boolean enabled) { this.enabled = enabled; }
            public List<String> getAllowedOrigins() { return allowedOrigins; }
            public void setAllowedOrigins(List<String> allowedOrigins) { this.allowedOrigins = allowedOrigins; }
            public List<String> getAllowedMethods() { return allowedMethods; }
            public void setAllowedMethods(List<String> allowedMethods) { this.allowedMethods = allowedMethods; }
            public List<String> getAllowedHeaders() { return allowedHeaders; }
            public void setAllowedHeaders(List<String> allowedHeaders) { this.allowedHeaders = allowedHeaders; }
            public List<String> getAllowedOriginPatterns() { return allowedOriginPatterns; }
            public void setAllowedOriginPatterns(List<String> allowedOriginPatterns) { this.allowedOriginPatterns = allowedOriginPatterns; }
            public boolean isAllowCredentials() { return allowCredentials; }
            public void setAllowCredentials(boolean allowCredentials) { this.allowCredentials = allowCredentials; }
            public long getMaxAge() { return maxAge; }
            public void setMaxAge(long maxAge) { this.maxAge = maxAge; }
        }

        public static class RestTemplateProps {
            private Duration connectTimeout = Duration.ofSeconds(2);
            private Duration readTimeout = Duration.ofSeconds(5);

            public Duration getConnectTimeout() { return connectTimeout; }
            public void setConnectTimeout(Duration connectTimeout) { this.connectTimeout = connectTimeout; }
            public Duration getReadTimeout() { return readTimeout; }
            public void setReadTimeout(Duration readTimeout) { this.readTimeout = readTimeout; }
        }

        public static class JacksonProps {
            private String timezone = "UTC";
            private String dateFormat = "yyyy-MM-dd HH:mm:ss";

            public String getTimezone() { return timezone; }
            public void setTimezone(String timezone) { this.timezone = timezone; }
            public String getDateFormat() { return dateFormat; }
            public void setDateFormat(String dateFormat) { this.dateFormat = dateFormat; }
        }
    }
}