package cn.cccs7;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EnableJpaRepositories(basePackages = "cn.cccs7.repository") // Only scan infrastructure layer repositories
public class NoOopsApplication {
    public static void main(String[] args) {
        SpringApplication.run(NoOopsApplication.class, args);
    }
}