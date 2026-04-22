package com.ciandt.mensagem_api.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;

import java.util.List;


public record WabaChangeDTO(
        String field,
        @Valid ValueDTO value
) {
    public record ValueDTO(
            @JsonProperty("messaging_product") @NotBlank String messagingProduct,
            MetadataDTO metadata,
            @NotEmpty @Valid List<MessageDTO> messages
    ) {}

    public record MetadataDTO(
            @JsonProperty("phone_number_id") String phoneNumberId,
            @JsonProperty("display_phone_number") String displayPhoneNumber
    ) {}

    public record MessageDTO(
            @NotBlank(message = "O campo 'from' é obrigatório")
            String from,
            @NotBlank(message = "O 'id' da mensagem é obrigatório")
            String id,
            String timestamp,
            @NotBlank(message = "O 'type' da mensagem é obrigatório")
            String type,
            @Valid TextDTO text
    ) {}

    public record TextDTO(@NotBlank(message = "O corpo da mensagem não pode estar vazio") String body) {}
}
