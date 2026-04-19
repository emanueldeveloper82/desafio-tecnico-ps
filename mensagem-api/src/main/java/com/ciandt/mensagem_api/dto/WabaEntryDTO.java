package com.ciandt.mensagem_api.dto;

import java.util.List;

public record WabaEntryDTO(
        String id,
        List<WabaChangeDTO> changes
) {}
