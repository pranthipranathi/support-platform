package com.saas.support.ticket.controller;

import com.saas.support.common.response.ApiResponse;
import com.saas.support.ticket.dto.CreateTicketRequest;
import com.saas.support.ticket.dto.TicketResponse;
import com.saas.support.ticket.dto.UpdateTicketRequest;
import com.saas.support.ticket.service.TicketService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/tickets")
@RequiredArgsConstructor
@Tag(name = "Tickets", description = "Ticket Management APIs")
public class TicketController {

    private final TicketService ticketService;

    @GetMapping
    @Operation(summary = "Get all tickets")
    public ResponseEntity<ApiResponse<List<TicketResponse>>> getAllTickets() {
        return ResponseEntity.ok(
                ApiResponse.success(ticketService.getAllTickets()));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get ticket by ID")
    public ResponseEntity<ApiResponse<TicketResponse>> getTicketById(
            @PathVariable UUID id) {
        return ResponseEntity.ok(
                ApiResponse.success(ticketService.getTicketById(id)));
    }

    @GetMapping("/customer/{customerId}")
    @Operation(summary = "Get tickets by customer")
    public ResponseEntity<ApiResponse<List<TicketResponse>>> getTicketsByCustomer(
            @PathVariable UUID customerId) {
        return ResponseEntity.ok(
                ApiResponse.success(ticketService.getTicketsByCustomer(customerId)));
    }

    @GetMapping("/agent/{agentId}")
    @Operation(summary = "Get tickets by agent")
    public ResponseEntity<ApiResponse<List<TicketResponse>>> getTicketsByAgent(
            @PathVariable UUID agentId) {
        return ResponseEntity.ok(
                ApiResponse.success(ticketService.getTicketsByAgent(agentId)));
    }

    @PostMapping
    @Operation(summary = "Create a new ticket")
    public ResponseEntity<ApiResponse<TicketResponse>> createTicket(
            @Valid @RequestBody CreateTicketRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success("Ticket created successfully",
                        ticketService.createTicket(request)));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update a ticket")
    public ResponseEntity<ApiResponse<TicketResponse>> updateTicket(
            @PathVariable UUID id,
            @Valid @RequestBody UpdateTicketRequest request) {
        return ResponseEntity.ok(
                ApiResponse.success("Ticket updated successfully",
                        ticketService.updateTicket(id, request)));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete a ticket")
    public ResponseEntity<ApiResponse<Void>> deleteTicket(@PathVariable UUID id) {
        ticketService.deleteTicket(id);
        return ResponseEntity.ok(
                ApiResponse.success("Ticket deleted successfully", null));
    }
}
