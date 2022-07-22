package com.penguineering.cleanuri.common.message;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.exc.MismatchedInputException;
import com.fasterxml.jackson.databind.exc.ValueInstantiationException;
import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import org.junit.jupiter.api.Test;

import java.net.URI;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;


@MicronautTest
public class TestExtractionRequest {
    static final URI EXAMPLE_COM = URI.create("https://www.example.com");

    @Test
    public void testEmpty() {
        // workaround for missing runtime classes in JUnit parametrized tests
        final URI[] inputs = new URI[] {null, URI.create("")};
        for (URI value : inputs) {
            IllegalArgumentException e = assertThrows(
                    IllegalArgumentException.class,
                    () -> ExtractionRequest.Builder.withURI(value).instance()
            );
            assertEquals("URI must not be null or blank!", e.getMessage());
        }
    }

    @Test
    public void testURI() {
        final ExtractionRequest request = ExtractionRequest.Builder.withURI(EXAMPLE_COM).instance();

        assertNotNull(request);
        assertEquals(EXAMPLE_COM, request.getURI());
        assertTrue(request.getFields().isEmpty());
        assertNull(request.getAgeLimit());

        ObjectMapper mapper = new ObjectMapper();
        String json = assertDoesNotThrow(
                () -> mapper.writeValueAsString(request)
        );
        assertEquals("{\"uri\":\"https://www.example.com\"}", json);
    }

    @Test
    public void testFields() {
        // empty fields have been tested in testURI

        final ExtractionRequest.Builder builder = ExtractionRequest.Builder.withURI(EXAMPLE_COM);

        builder.addField(MetaData.Fields.ID);
        assertEquals(Set.of(MetaData.Fields.ID), builder.instance().getFields());

        // fields should be reset after instance()
        builder.addField(MetaData.Fields.TITLE);
        assertEquals(Set.of(MetaData.Fields.TITLE), builder.instance().getFields());

        // but both fields can also be set
        builder.addField(MetaData.Fields.TITLE);
        builder.addField(MetaData.Fields.ID);
        final ExtractionRequest request = builder.instance();
        assertEquals(Set.of(MetaData.Fields.ID, MetaData.Fields.TITLE),
                request.getFields());
    }

    @Test
    public void testAgeLimit() {
        // empty age limit has been tested in testURI

        final ExtractionRequest.Builder builder = ExtractionRequest.Builder.withURI(EXAMPLE_COM);

        builder.setAgeLimit(1);
        ExtractionRequest request = builder.instance();
        assertNotNull(request);
        assertEquals(1, request.getAgeLimit());

        // builder should have reset
        assertNull(builder.instance().getAgeLimit());

        ObjectMapper mapper = new ObjectMapper();
        String json = assertDoesNotThrow(
                () -> mapper.writeValueAsString(request)
        );
        assertEquals("{\"uri\":\"https://www.example.com\",\"age-limit\":1}", json);
    }

    @Test
    public void testJson() {
        final ExtractionRequest req1 = ExtractionRequest.Builder
                .withURI(EXAMPLE_COM)
                .addField(MetaData.Fields.ID)
                .setAgeLimit(2).instance();

        ObjectMapper mapper = new ObjectMapper();
        final String json = assertDoesNotThrow(
                () -> mapper.writeValueAsString(req1)
        );
        assertEquals("{\"uri\":\"https://www.example.com\",\"fields\":[\"id\"],\"age-limit\":2}", json);

        final ExtractionRequest req2 = assertDoesNotThrow(
                () -> mapper.readValue(json, ExtractionRequest.class)
        );
        assertNotNull(req2);
        assertEquals(EXAMPLE_COM, req2.getURI());
        assertEquals(Set.of(MetaData.Fields.ID), req2.getFields());
        assertEquals(2, req2.getAgeLimit());

        final ExtractionRequest req3 = assertDoesNotThrow(
                () -> mapper.readValue(
                        "{\"uri\":\"https://www.example.com\",\"age-limit\":2}",
                        ExtractionRequest.class)
        );
        assertNotNull(req3);
        assertEquals(EXAMPLE_COM, req3.getURI());
        assertTrue(req3.getFields().isEmpty());
        assertEquals(2, req3.getAgeLimit());

        final ExtractionRequest req4 = assertDoesNotThrow(
                () -> mapper.readValue(
                        "{\"uri\":\"https://www.example.com\",\"fields\":[\"id\", \"title\"]}",
                        ExtractionRequest.class)
        );
        assertNotNull(req4);
        assertEquals(EXAMPLE_COM, req4.getURI());
        assertEquals(Set.of(MetaData.Fields.ID, MetaData.Fields.TITLE), req4.getFields());
        assertNull(req4.getAgeLimit());
    }

    @Test
    public void testMissingJson() {
        final String json_no_uri = "{\"fields\":[\"id\"],\"age-limit\":2}";
        final String json_empty_uri = "{\"uri\":\"\",\"fields\":[\"id\"],\"age-limit\":2}";

        ObjectMapper mapper = new ObjectMapper();

        assertThrows(
                MismatchedInputException.class,
                () -> mapper.readValue(json_no_uri, ExtractionRequest.class)
        );

        assertThrows(
                ValueInstantiationException.class,
                () -> mapper.readValue(json_empty_uri, ExtractionRequest.class)
        );
    }
}
