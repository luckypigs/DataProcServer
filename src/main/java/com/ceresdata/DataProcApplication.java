package com.ceresdata;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class DataProcApplication {

    public static void main(String[] args) {
        SpringApplication.run(DataProcApplication.class, args);
    }

}
