package com.saas.support.attachment.service;

import com.saas.support.attachment.dto.AttachmentResponse;
import com.saas.support.attachment.entity.Attachment;
import com.saas.support.attachment.repository.AttachmentRepository;
import com.saas.support.common.exception.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class AttachmentService {

    private final AttachmentRepository attachmentRepository;

    @Value("${application.storage.upload-dir:./uploads}")
    private String uploadDir;

    public List<AttachmentResponse> getAttachmentsByTicket(UUID ticketId) {
        return attachmentRepository.findByTicketId(ticketId)
                .stream().map(this::mapToResponse).collect(Collectors.toList());
    }

    @Transactional
    public AttachmentResponse uploadAttachment(UUID ticketId, UUID uploadedBy,
                                               MultipartFile file) throws IOException {
        Path uploadPath = Paths.get(uploadDir);
        if (!Files.exists(uploadPath)) {
            Files.createDirectories(uploadPath);
        }

        String fileName = UUID.randomUUID() + "_" + file.getOriginalFilename();
        Path filePath = uploadPath.resolve(fileName);
        Files.copy(file.getInputStream(), filePath);

        Attachment attachment = Attachment.builder()
                .ticketId(ticketId)
                .uploadedBy(uploadedBy)
                .fileName(file.getOriginalFilename())
                .fileSize(file.getSize())
                .mimeType(file.getContentType())
                .storagePath(filePath.toString())
                .build();

        Attachment saved = attachmentRepository.save(attachment);
        log.info("Attachment uploaded: {} for ticket: {}", saved.getFileName(), ticketId);
        return mapToResponse(saved);
    }

    @Transactional
    public void deleteAttachment(UUID id) {
        Attachment attachment = attachmentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Attachment", id.toString()));
        try {
            Files.deleteIfExists(Paths.get(attachment.getStoragePath()));
        } catch (IOException e) {
            log.warn("Could not delete file: {}", attachment.getStoragePath());
        }
        attachmentRepository.delete(attachment);
        log.info("Attachment deleted: {}", id);
    }

    private AttachmentResponse mapToResponse(Attachment attachment) {
        AttachmentResponse response = new AttachmentResponse();
        response.setId(attachment.getId());
        response.setTicketId(attachment.getTicketId());
        response.setUploadedBy(attachment.getUploadedBy());
        response.setFileName(attachment.getFileName());
        response.setFileSize(attachment.getFileSize());
        response.setMimeType(attachment.getMimeType());
        response.setCreatedAt(attachment.getCreatedAt() != null ?
                attachment.getCreatedAt() : java.time.LocalDateTime.now());
        return response;
    }
}