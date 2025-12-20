package cn.cccs7.controller;

import cn.cccs7.shared.model.NoOopsResponse;
import cn.cccs7.shared.model.util.NoOopsResponseBuilder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
public class TestController {

    @GetMapping("test")
    public NoOopsResponse<Map<String, String>> test() {
        Map<String, String> data = new HashMap<>();
        data.put("message", "test no-oops");
        data.put("service", "main-service");
        return NoOopsResponseBuilder.ok(data);
    }
    
    @GetMapping("test-with-trace")
    public NoOopsResponse<Map<String, String>> testWithTrace() {
        Map<String, String> data = new HashMap<>();
        data.put("message", "test traceId in response");
        data.put("service", "main-service");
        return NoOopsResponseBuilder.<Map<String, String>>builder()
                .success(true)
                .code("200")
                .data(data)
                .build();
    }
}