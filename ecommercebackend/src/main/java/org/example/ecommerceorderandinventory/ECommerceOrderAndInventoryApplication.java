package org.example.ecommerceorderandinventory;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;

@SpringBootApplication
@EnableMethodSecurity
public class ECommerceOrderAndInventoryApplication {
    public static void main(String[] args) {
        SpringApplication.run(ECommerceOrderAndInventoryApplication.class, args);
    }

}
