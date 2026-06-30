package com.saas.support.customer.dto;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
public class CustomerResponse {

    private UUID id;
    private String email;
    private String firstName;
    private String lastName;
    private String phone;
    private String company;
    private boolean isActive;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}