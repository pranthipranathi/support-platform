package com.saas.support.comment;

import com.saas.support.comment.dto.CommentResponse;
import com.saas.support.comment.dto.CreateCommentRequest;
import com.saas.support.comment.entity.Comment;
import com.saas.support.comment.repository.CommentRepository;
import com.saas.support.comment.service.CommentService;
import com.saas.support.common.exception.ResourceNotFoundException;
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
class CommentServiceTest {

    @Mock
    private CommentRepository commentRepository;

    @InjectMocks
    private CommentService commentService;

    private Comment comment;
    private UUID commentId;
    private UUID ticketId;
    private UUID authorId;

    @BeforeEach
    void setUp() {
        commentId = UUID.randomUUID();
        ticketId = UUID.randomUUID();
        authorId = UUID.randomUUID();

        comment = Comment.builder()
                .id(commentId)
                .ticketId(ticketId)
                .authorId(authorId)
                .content("This is a test comment")
                .isInternal(false)
                .build();
    }

    @Test
    void getCommentsByTicket_returnsComments() {
        when(commentRepository.findByTicketIdOrderByCreatedAtAsc(ticketId))
                .thenReturn(List.of(comment));

        List<CommentResponse> result = commentService.getCommentsByTicket(ticketId);

        assertThat(result).hasSize(1);
        assertThat(result.get(0).getContent()).isEqualTo("This is a test comment");
    }

    @Test
    void createComment_validRequest_returnsComment() {
        CreateCommentRequest request = new CreateCommentRequest();
        request.setTicketId(ticketId);
        request.setContent("New comment");
        request.setInternal(false);

        when(commentRepository.save(any(Comment.class))).thenReturn(comment);

        CommentResponse result = commentService.createComment(request, authorId);

        assertThat(result).isNotNull();
        verify(commentRepository).save(any(Comment.class));
    }

    @Test
    void deleteComment_existingId_deletesComment() {
        when(commentRepository.findById(commentId)).thenReturn(Optional.of(comment));
        doNothing().when(commentRepository).delete(comment);

        commentService.deleteComment(commentId);

        verify(commentRepository).delete(comment);
    }

    @Test
    void deleteComment_nonExistingId_throwsException() {
        UUID nonExistingId = UUID.randomUUID();
        when(commentRepository.findById(nonExistingId)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> commentService.deleteComment(nonExistingId))
                .isInstanceOf(ResourceNotFoundException.class);
    }
}