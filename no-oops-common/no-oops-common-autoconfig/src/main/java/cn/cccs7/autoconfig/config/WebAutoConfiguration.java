package cn.cccs7.autoconfig.config;

import org.slf4j.MDC;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;
import org.springframework.boot.autoconfigure.jackson.Jackson2ObjectMapperBuilderCustomizer;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.core.Ordered;
import jakarta.servlet.DispatcherType;
import org.springframework.web.servlet.config.annotation.CorsRegistration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.TimeZone;
import java.util.UUID;

@AutoConfiguration
public class WebAutoConfiguration {
    private static final String TRACE_HEADER = "X-Trace-Id";

    @Bean
    public WebMvcConfigurer corsConfigurer(CommonAutoConfiguration.CommonProperties properties) {
        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                CommonAutoConfiguration.CommonProperties.Cors cors = properties.getCors();
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
    public RestTemplate restTemplate(RestTemplateBuilder builder, CommonAutoConfiguration.CommonProperties properties) {
        CommonAutoConfiguration.CommonProperties.RestTemplateProps rt = properties.getRestTemplate();
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
    public Jackson2ObjectMapperBuilderCustomizer jacksonCustomizer(CommonAutoConfiguration.CommonProperties properties) {
        return new Jackson2ObjectMapperBuilderCustomizer() {
            @Override
            public void customize(Jackson2ObjectMapperBuilder builder) {
                CommonAutoConfiguration.CommonProperties.JacksonProps jp = properties.getJackson();
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

    private static String[] toArray(List<String> list, String... defaults) {
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
}