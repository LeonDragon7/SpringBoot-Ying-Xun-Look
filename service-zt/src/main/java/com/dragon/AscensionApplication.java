package com.dragon;

import lombok.extern.slf4j.Slf4j;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;

@SpringBootApplication(exclude = {SecurityAutoConfiguration.class}) //禁用security
@MapperScan("com.dragon.mapper")
@Slf4j
public class AscensionApplication {
    public static void main(String[] args) {
        log.info("server start");
        SpringApplication.run(AscensionApplication.class,args);
    }
}
