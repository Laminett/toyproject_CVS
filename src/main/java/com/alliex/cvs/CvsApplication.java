package com.alliex.cvs;

import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jackson.Jackson2ObjectMapperBuilderCustomizer;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@SpringBootApplication
public class CvsApplication implements Jackson2ObjectMapperBuilderCustomizer {

    final private String defaultDateFormat = "dd-MM-yyyy HH:mm:ss";

    public static void main(String[] args) {
        SpringApplication.run(CvsApplication.class, args);
    }

    @Override
    public void customize(Jackson2ObjectMapperBuilder builder) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(defaultDateFormat);
        LocalDateTimeSerializer localDateTimeSerializer = new LocalDateTimeSerializer(formatter);

        builder.failOnEmptyBeans(false).serializerByType(LocalDateTime.class, localDateTimeSerializer);
    }

}