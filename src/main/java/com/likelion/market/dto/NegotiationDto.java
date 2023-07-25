package com.likelion.market.dto;

import com.likelion.market.annotations.Status;
import com.likelion.market.entity.NegotiationEntity;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

@Data
public class NegotiationDto {
    @Data
    public static class CreateAndUpdateRequest {
        private String writer;
        private String password;
        private Integer suggestedPrice;
        @Status(statusList = {"수락", "거절", "확정"})
        private String status;
    }

    @Data
    public static class ReadNegotiationResponse {
        private Long id;
        private Integer suggestedPrice;
        private String status;

        public static ReadNegotiationResponse fromEntity(NegotiationEntity entity) {
            ReadNegotiationResponse negotiation = new ReadNegotiationResponse();
            negotiation.setId(entity.getId());
            negotiation.setSuggestedPrice(entity.getSuggestedPrice());
            negotiation.setStatus(entity.getStatus());
            return negotiation;
        }
    }
}
