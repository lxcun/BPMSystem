package com.atw.bpmsystem;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication/*(exclude = DataSourceAutoConfiguration.class)*/

public class BpmsystemApplication {


    public static void main(String[] args) {
        SpringApplication.run(BpmsystemApplication.class, args);

    }
}
