package com.ciandt.mensagem_api.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;

import java.util.List;

public record WabaEntryDTO(
        String id,
        @NotEmpty @Valid List<WabaChangeDTO> changes
) {}
