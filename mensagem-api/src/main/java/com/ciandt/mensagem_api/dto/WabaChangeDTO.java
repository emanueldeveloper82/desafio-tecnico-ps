package com.ciandt.mensagem_api.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;

import java.util.List;


public record WabaChangeDTO(
        String field,
        ValueDTO value
) {
    public record ValueDTO(
            @JsonProperty("messaging_product") @NotBlank String messagingProduct,
            MetadataDTO metadata,
            List<MessageDTO> messages
    ) {}

    public record MetadataDTO(
            @JsonProperty("phone_number_id") String phoneNumberId,
            @JsonProperty("display_phone_number") String displayPhoneNumber
    ) {}

    public record MessageDTO(
            String from,
            String id,
            String timestamp,
            String type,
            TextDTO text
    ) {}

    public record TextDTO(String body) {}
}
