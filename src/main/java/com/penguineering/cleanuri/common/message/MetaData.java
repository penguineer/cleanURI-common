package com.penguineering.cleanuri.common.message;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.micronaut.context.annotation.Bean;

import java.time.Instant;

@Bean
public class MetaData {
    public enum Fields {
        @JsonProperty("id") ID,
        @JsonProperty("title") TITLE

    }

    public static class Builder {
        public static Builder withValue(String value) {
            return new Builder(value);
        }
        private final String value;
        private Instant timestamp;

        Builder(String value) {
            this.value = value;
            this.timestamp = Instant.now();
        }

        public Builder setTimestamp(Instant timestamp) {
            this.timestamp = timestamp;
            return this;
        }

        public MetaData instance() {
            return new MetaData(value, timestamp);
        }
    }


    private final String value;
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private final Instant timestamp;

    MetaData(@JsonProperty(value = "value", required = true) String value,
              @JsonProperty(value = "timestamp", required = true) Instant timestamp) {
        if (value == null || value.isBlank())
            throw new IllegalArgumentException("Metadata value must not be null or blank!");
        this.value = value;
        this.timestamp = timestamp;
    }

    public String getValue() {
        return value;
    }

    public Instant getTimestamp() {
        return timestamp;
    }
}
