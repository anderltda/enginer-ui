package br.com.enginer.infrastructure.configuration.deserializer;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.OffsetTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;


public class SafeLocalTimeDeserializer extends JsonDeserializer<LocalTime> {

    @Override
    public LocalTime deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
        String value = p.getText();

        if (value == null || value.trim().isEmpty()) {
            return null;
        }

        value = value.trim();

        try {
        	
            //1. Tenta o formato padrão (HH:mm:ss)
            return LocalTime.parse(value, DateTimeFormatter.ofPattern("HH:mm[:ss]"));
            
        } catch (DateTimeParseException ex1) {
        	
            try {
                // 2. Tenta ISO completo (com data e hora) → extrai apenas o horário
                if (value.contains("T")) {
                    return LocalDateTime.parse(value, DateTimeFormatter.ISO_DATE_TIME).toLocalTime();
                }

                // 3. Tenta offset UTC (ex: 15:30:00Z, 15:30:00+00:00)
                return OffsetTime.parse(value).toLocalTime();
                
            } catch (Exception ex2) {
                throw new IllegalArgumentException("Hora inválida: " + value, ex2);
            }
        }
    }
}
