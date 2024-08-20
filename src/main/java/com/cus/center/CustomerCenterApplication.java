package com.cus.center;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class CustomerCenterApplication {

  public static void main(String[] args) {
    try {
      SpringApplication.run(CustomerCenterApplication.class, args);
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

}
