package com.ciandt.mensagem_service.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class JsonSanitizer {

    private final ObjectMapper objectMapper;

    /**
     * Se o JSON começar com aspas, ele foi serializado como String pelo Producer.
     * O readValue(payload, String.class) remove a camada extra de aspas e os escapes (\").
     * @param payload String
     * @return String
     */
    public String sanitize(String payload) {
        if (payload == null || payload.isBlank()) {
            return payload;
        }
        try {
            if (payload.startsWith("\"")) {
                return objectMapper.readValue(payload, String.class);
            }
        } catch (JsonProcessingException e) {
            return payload;
        }
        return payload;
    }
}
