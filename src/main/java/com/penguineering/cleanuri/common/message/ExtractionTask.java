package com.penguineering.cleanuri.common.message;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.micronaut.context.annotation.Bean;

import java.net.URI;
import java.util.*;

@Bean
public class ExtractionTask {
    public static class Builder {
        public static Builder withRequest(ExtractionRequest request) {
            return new Builder(request);
        }

        public static Builder copy(ExtractionTask task) {
            final Builder builder = new Builder(task.getRequest());

            builder.canonizedURI = task.canonizedURI;

            if (task.meta != null && !task.meta.isEmpty())
                builder.meta = new HashMap<>(task.meta);

            if (task.errors != null && !task.errors.isEmpty())
                builder.errors = new ArrayList<>(task.errors);

            return  builder;
        }

        private final ExtractionRequest request;
        private URI canonizedURI = null;
        private Map<MetaData.Fields, MetaData> meta = null;
        private List<String> errors = null;

        public Builder(ExtractionRequest request) {
            this.request = request;
        }

        public Builder setCanonizedURI(URI canonizedURI) {
            this.canonizedURI = canonizedURI;
            return this;
        }

        public Builder putMeta(MetaData.Fields key, MetaData data) {
            if (this.meta == null)
                this.meta = new HashMap<>();
            this.meta.put(key, data);
            return this;
        }

        public Builder addError(String error) {
            if (this.errors == null)
                this.errors = new ArrayList<>();
            this.errors.add(error);
            return this;
        }

        public ExtractionTask instance() {
            final ExtractionTask instance = new ExtractionTask(request, canonizedURI, meta, errors);
            this.meta = null;
            this.errors = null;
            return instance;
        }
    }

    private final ExtractionRequest request;

    @JsonProperty("canonized-uri")
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private final URI canonizedURI;

    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private final Map<MetaData.Fields, MetaData> meta;

    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private final List<String> errors;

    ExtractionTask(@JsonProperty(value = "request", required = true) ExtractionRequest request,
                   @JsonProperty("canonized-uri") URI canonizedURI,
                   @JsonProperty("meta") Map<MetaData.Fields, MetaData> meta,
                   @JsonProperty("errors") List<String> errors) {
        if (request == null)
            throw new IllegalArgumentException("Request must not be null!");

        this.request = request;
        this.canonizedURI = canonizedURI;
        this.meta = meta;
        this.errors = errors;
    }

    public ExtractionRequest getRequest() {
        return request;
    }

    public URI getCanonizedURI() {
        return canonizedURI;
    }

    public Map<MetaData.Fields, MetaData> getMeta() {
        return meta == null ? Collections.emptyMap() : Collections.unmodifiableMap(meta);
    }

    public List<String> getErrors() {
        return errors == null ? Collections.emptyList() : Collections.unmodifiableList(errors);
    }
}
