package jp.co.goalist.gsc.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;
import lombok.extern.slf4j.Slf4j;

import java.util.Objects;

@Slf4j
@Converter
public class JsonColumnConverter<T extends Object> implements AttributeConverter<T, String> {

    private static final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public String convertToDatabaseColumn(T attribute) {
        try {
            return objectMapper.writeValueAsString(attribute);
        } catch (JsonProcessingException e) {
            log.error("Convert to database column error: {}", attribute);
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public T convertToEntityAttribute(String dbData) {
        try {
            if (Objects.isNull(dbData)) {
                return null;
            }
            return objectMapper.readValue(dbData, new TypeReference<T>() {
            });
        } catch (JsonProcessingException e) {
            log.error("Convert to entity attribute error: {}", dbData);
            e.printStackTrace();
            return null;
        }
    }

}
