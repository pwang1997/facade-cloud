package com.facade.facadecore;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class FacadeCoreApplication {

  public static void main(String[] args) {
    SpringApplication.run(FacadeCoreApplication.class, args);
  }

}
