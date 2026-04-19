package com.ciandt.mensagem_api.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;

import java.util.List;

public record WabaWebhookDTO(
        @NotBlank String object,
        @NotEmpty List<WabaEntryDTO> entry
) {}