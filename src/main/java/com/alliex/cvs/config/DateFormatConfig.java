package com.alliex.cvs.config;

import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import org.springframework.boot.autoconfigure.jackson.Jackson2ObjectMapperBuilderCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.format.DateTimeFormatter;

@Configuration
public class DateFormatConfig {

    private final String dateFormat = "dd-MM-yyyy";

    private final String datetimeFormat = "dd-MM-yyyy HH:mm:ss";

    @Bean
    public Jackson2ObjectMapperBuilderCustomizer jackson2ObjectMapperBuilderCustomizer() {
        return jacksonObjectMapperBuilder -> {
            // jacksonObjectMapperBuilder.timeZone(TimeZone.getTimeZone("UTC"));
            jacksonObjectMapperBuilder.simpleDateFormat(datetimeFormat);

            // Set LocalDate format.
            jacksonObjectMapperBuilder.serializers(new LocalDateSerializer(DateTimeFormatter.ofPattern(dateFormat)));
            jacksonObjectMapperBuilder.deserializers(new LocalDateDeserializer(DateTimeFormatter.ofPattern(dateFormat)));

            // Set LocalDateTime format.
            jacksonObjectMapperBuilder.serializers(new LocalDateTimeSerializer(DateTimeFormatter.ofPattern(datetimeFormat)));
            jacksonObjectMapperBuilder.deserializers(new LocalDateTimeDeserializer(DateTimeFormatter.ofPattern(datetimeFormat)));
        };
    }

}
