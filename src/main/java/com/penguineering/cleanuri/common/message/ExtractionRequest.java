package com.penguineering.cleanuri.common.message;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.micronaut.context.annotation.Bean;
import io.micronaut.core.annotation.Internal;

import java.net.URI;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

@Bean
public class ExtractionRequest {
    public enum Field {
        @JsonProperty("id") ID,
        @JsonProperty("title") TITLE
    }

    public static class Builder {
        public static Builder withURI(URI uri) {
            return new Builder(uri);
        }

        private final URI uri;
        private Set<Field> fields = null;
        private Long ageLimit = null;

        Builder(URI uri) {
            this.uri = uri;
        }

        public Builder addField(Field field) {
            if (this.fields == null)
                this.fields = new HashSet<>();
            this.fields.add(field);
            return this;
        }

        public Builder setAgeLimit(long ageLimit) {
            this.ageLimit = ageLimit;
            return this;
        }

        public ExtractionRequest instance() {
            final ExtractionRequest instance = new ExtractionRequest(uri, fields, ageLimit);
            this.fields = null;
            this.ageLimit = null;
            return instance;
        }
    }

    @JsonProperty("uri")
    private final URI uri;
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private final Set<Field> fields;

    @JsonProperty("age-limit")
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private final Long ageLimit;

    @Internal
    ExtractionRequest(@JsonProperty(value = "uri", required = true) URI uri,
                      @JsonProperty("fields") Set<Field> fields,
                      @JsonProperty("age-limit") Long ageLimit) {
        if (uri == null || uri.toString().isBlank())
            throw new IllegalArgumentException("URI must not be null or blank!");
        this.uri = uri;
        this.fields = fields;
        this.ageLimit = ageLimit;
    }

    public URI getURI() {
        return uri;
    }

    public Set<Field> getFields() {
        return fields == null ? Collections.emptySet() : Collections.unmodifiableSet(fields);
    }

    public Long getAgeLimit() {
        return ageLimit;
    }
}
