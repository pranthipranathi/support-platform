package com.saas.support.customer.service;

import com.saas.support.common.exception.BusinessException;
import com.saas.support.common.exception.ResourceNotFoundException;
import com.saas.support.customer.dto.CreateCustomerRequest;
import com.saas.support.customer.dto.CustomerResponse;
import com.saas.support.customer.entity.Customer;
import com.saas.support.customer.repository.CustomerRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class CustomerService {

    private final CustomerRepository customerRepository;

    public List<CustomerResponse> getAllCustomers() {
        return customerRepository.findAll()
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    public CustomerResponse getCustomerById(UUID id) {
        Customer customer = customerRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Customer", id.toString()));
        return mapToResponse(customer);
    }

    @Transactional
    public CustomerResponse createCustomer(CreateCustomerRequest request) {
        if (customerRepository.existsByEmail(request.getEmail())) {
            throw new BusinessException("Email already in use: " + request.getEmail());
        }

        Customer customer = Customer.builder()
                .email(request.getEmail())
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .phone(request.getPhone())
                .company(request.getCompany())
                .isActive(true)
                .build();

        Customer saved = customerRepository.save(customer);
        log.info("Customer created: {}", saved.getId());
        return mapToResponse(saved);
    }

    @Transactional
    public void deleteCustomer(UUID id) {
        Customer customer = customerRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Customer", id.toString()));
        customerRepository.delete(customer);
        log.info("Customer deleted: {}", id);
    }

    private CustomerResponse mapToResponse(Customer customer) {
        CustomerResponse response = new CustomerResponse();
        response.setId(customer.getId());
        response.setEmail(customer.getEmail());
        response.setFirstName(customer.getFirstName());
        response.setLastName(customer.getLastName());
        response.setPhone(customer.getPhone());
        response.setCompany(customer.getCompany());
        response.setActive(customer.isActive());
        response.setCreatedAt(customer.getCreatedAt() != null ? customer.getCreatedAt() : java.time.LocalDateTime.now());
        response.setUpdatedAt(customer.getUpdatedAt() != null ? customer.getUpdatedAt() : java.time.LocalDateTime.now());
        return response;
    }
}