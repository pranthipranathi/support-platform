package com.saas.support.attachment.controller;

import com.saas.support.attachment.dto.AttachmentResponse;
import com.saas.support.attachment.service.AttachmentService;
import com.saas.support.common.response.ApiResponse;
import com.saas.support.user.entity.User;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/attachments")
@RequiredArgsConstructor
@Tag(name = "Attachments", description = "File Attachment APIs")
public class AttachmentController {

    private final AttachmentService attachmentService;

    @GetMapping("/ticket/{ticketId}")
    @Operation(summary = "Get attachments for a ticket")
    public ResponseEntity<ApiResponse<List<AttachmentResponse>>> getByTicket(
            @PathVariable UUID ticketId) {
        return ResponseEntity.ok(
                ApiResponse.success(attachmentService.getAttachmentsByTicket(ticketId)));
    }

    @PostMapping(value = "/ticket/{ticketId}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @Operation(summary = "Upload a file attachment to a ticket")
    public ResponseEntity<ApiResponse<AttachmentResponse>> upload(
            @PathVariable UUID ticketId,
            @RequestParam("file") MultipartFile file,
            @AuthenticationPrincipal User currentUser) throws IOException {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success("File uploaded successfully",
                        attachmentService.uploadAttachment(ticketId, currentUser.getId(), file)));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete an attachment")
    public ResponseEntity<ApiResponse<Void>> delete(@PathVariable UUID id) {
        attachmentService.deleteAttachment(id);
        return ResponseEntity.ok(ApiResponse.success("Attachment deleted", null));
    }
}