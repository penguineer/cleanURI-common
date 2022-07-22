package com.penguineering.cleanuri.common.message;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.micronaut.context.annotation.Bean;

import java.time.Clock;

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
        private long timestamp;

        Builder(String value) {
            this.value = value;
            this.timestamp = Clock.systemUTC().millis();
        }

        public Builder setTimestamp(long timestamp) {
            this.timestamp = timestamp;
            return this;
        }

        public MetaData instance() {
            return new MetaData(value, timestamp);
        }
    }


    private final String value;
    private final long timestamp;

    MetaData(@JsonProperty(value = "value", required = true) String value,
              @JsonProperty(value = "timestamp", required = true) long timestamp) {
        if (value == null || value.isBlank())
            throw new IllegalArgumentException("Metadata value must not be null or blank!");
        this.value = value;
        this.timestamp = timestamp;
    }

    public String getValue() {
        return value;
    }

    public long getTimestamp() {
        return timestamp;
    }
}
