package com.penguineering.cleanuri.common.message;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.micronaut.context.annotation.Bean;
import io.micronaut.serde.annotation.Serdeable;

import java.net.URI;
import java.time.Duration;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

@Bean
@Serdeable
public class ExtractionRequest {
    public static class Builder {
        public static Builder withURI(URI uri) {
            return new Builder(uri);
        }

        private final URI uri;
        private Set<MetaData.Fields> fields = null;
        private Duration ageLimit = null;

        Builder(URI uri) {
            this.uri = uri;
        }

        public Builder addField(MetaData.Fields field) {
            if (this.fields == null)
                this.fields = new HashSet<>();
            this.fields.add(field);
            return this;
        }

        public Builder setAgeLimit(Duration ageLimit) {
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

    private final URI uri;
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private final Set<MetaData.Fields> fields;

    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private final Duration ageLimit;

    ExtractionRequest(@JsonProperty(value = "uri", required = true) URI uri,
                      @JsonProperty("fields") Set<MetaData.Fields> fields,
                      @JsonProperty("age-limit") Duration ageLimit) {
        if (uri == null || uri.toString().isBlank())
            throw new IllegalArgumentException("URI must not be null or blank!");
        this.uri = uri;
        this.fields = fields;
        this.ageLimit = ageLimit;
    }

    @JsonProperty("uri")
    public URI getURI() {
        return uri;
    }

    @JsonProperty("fields")
    public Set<MetaData.Fields> getFields() {
        return fields == null ? Collections.emptySet() : Collections.unmodifiableSet(fields);
    }

    @JsonProperty("age-limit")
    public Duration getAgeLimit() {
        return ageLimit;
    }
}
