package com.likelion.market.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.likelion.market.entity.CommentEntity;
import lombok.Data;

@Data
public class CommentDto {
    @Data
    public static class CreateAndUpdateCommentRequest{
        String writer;
        String password;
        String content;
    }

    @Data
    public static class UpdateReplyRequest {
        String writer;
        String password;
        String reply;
    }

    @Data
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public static class ReadCommentsResponse {
        Long id;
        String content;
        String reply;

        public static ReadCommentsResponse fromEntity(CommentEntity entity) {
            ReadCommentsResponse readCommentsResponse = new ReadCommentsResponse();
            readCommentsResponse.setId(entity.getId());
            readCommentsResponse.setContent(entity.getContent());
            readCommentsResponse.setReply(entity.getReply());
            return readCommentsResponse;
        }
    }
}
