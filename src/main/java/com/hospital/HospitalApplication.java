package com.hospital;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * MediCare with Billing Management System
 * ----------------------------------------
 * Run this class → server starts at http://localhost:8080
 * Login: admin / admin123
 */
@SpringBootApplication
public class HospitalApplication {
    public static void main(String[] args) {
        SpringApplication.run(HospitalApplication.class, args);
    }
}
