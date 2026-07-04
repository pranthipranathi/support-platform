package com.saas.support.customer;

import com.saas.support.common.exception.BusinessException;
import com.saas.support.common.exception.ResourceNotFoundException;
import com.saas.support.customer.dto.CreateCustomerRequest;
import com.saas.support.customer.dto.CustomerResponse;
import com.saas.support.customer.entity.Customer;
import com.saas.support.customer.repository.CustomerRepository;
import com.saas.support.customer.service.CustomerService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CustomerServiceTest {

    @Mock
    private CustomerRepository customerRepository;

    @InjectMocks
    private CustomerService customerService;

    private Customer customer;
    private UUID customerId;

    @BeforeEach
    void setUp() {
        customerId = UUID.randomUUID();
        customer = Customer.builder()
                .id(customerId)
                .email("john@acme.com")
                .firstName("John")
                .lastName("Doe")
                .company("Acme Corp")
                .isActive(true)
                .build();
    }

    @Test
    void getAllCustomers_returnsAllCustomers() {
        when(customerRepository.findAll()).thenReturn(List.of(customer));

        List<CustomerResponse> result = customerService.getAllCustomers();

        assertThat(result).hasSize(1);
        assertThat(result.get(0).getEmail()).isEqualTo("john@acme.com");
    }

    @Test
    void getCustomerById_existingId_returnsCustomer() {
        when(customerRepository.findById(customerId)).thenReturn(Optional.of(customer));

        CustomerResponse result = customerService.getCustomerById(customerId);

        assertThat(result.getId()).isEqualTo(customerId);
        assertThat(result.getFirstName()).isEqualTo("John");
    }

    @Test
    void getCustomerById_nonExistingId_throwsException() {
        UUID nonExistingId = UUID.randomUUID();
        when(customerRepository.findById(nonExistingId)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> customerService.getCustomerById(nonExistingId))
                .isInstanceOf(ResourceNotFoundException.class);
    }

    @Test
    void createCustomer_newEmail_returnsCreatedCustomer() {
        CreateCustomerRequest request = new CreateCustomerRequest();
        request.setEmail("jane@test.com");
        request.setFirstName("Jane");
        request.setLastName("Smith");

        when(customerRepository.existsByEmail("jane@test.com")).thenReturn(false);
        when(customerRepository.save(any(Customer.class))).thenReturn(customer);

        CustomerResponse result = customerService.createCustomer(request);

        assertThat(result).isNotNull();
        verify(customerRepository).save(any(Customer.class));
    }

    @Test
    void createCustomer_duplicateEmail_throwsException() {
        CreateCustomerRequest request = new CreateCustomerRequest();
        request.setEmail("john@acme.com");
        request.setFirstName("John");
        request.setLastName("Doe");

        when(customerRepository.existsByEmail("john@acme.com")).thenReturn(true);

        assertThatThrownBy(() -> customerService.createCustomer(request))
                .isInstanceOf(BusinessException.class);
    }
}