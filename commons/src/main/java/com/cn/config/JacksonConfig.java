package com.cn.config;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.ser.std.DateSerializer;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import com.fasterxml.jackson.module.paramnames.ParameterNamesModule;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Date;

/**
 * 解决jackson 无法格式化Instant 时间类型
 * @author ngcly
 */
@Configuration
public class JacksonConfig {
    @Value("${spring.jackson.date-format}")
    private String formatValue;

    @Bean(name = "format")
    DateTimeFormatter format() {
        return DateTimeFormatter.ofPattern(formatValue);
    }

    @Bean
    public ObjectMapper serializingObjectMapper(@Qualifier("format") DateTimeFormatter format) {
        JavaTimeModule javaTimeModule = new JavaTimeModule();
        javaTimeModule.addSerializer(LocalDateTime.class, new LocalDateTimeSerializer(format));
        javaTimeModule.addSerializer(Instant.class, new InstantCustomSerializer(format));
        javaTimeModule.addSerializer(Date.class, new DateSerializer(false, new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")));
        ObjectMapper mapper = new ObjectMapper()
                .registerModule(new ParameterNamesModule())
                .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS)
                .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
                .registerModule(javaTimeModule);
        return mapper;
    }

    class InstantCustomSerializer extends JsonSerializer<Instant> {
        private DateTimeFormatter format;

        private InstantCustomSerializer(DateTimeFormatter formatter) {
            this.format = formatter;
        }

        @Override
        public void serialize(Instant instant, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException {
            if (instant == null) {
                return;
            }
            String jsonValue = format.format(instant.atZone(ZoneId.systemDefault()));
            jsonGenerator.writeString(jsonValue);
        }
    }
}
