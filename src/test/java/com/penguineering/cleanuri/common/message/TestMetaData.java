package com.penguineering.cleanuri.common.message;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.exc.MismatchedInputException;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.time.format.DateTimeFormatter;

import static org.junit.jupiter.api.Assertions.*;

@MicronautTest
public class TestMetaData {
    @Test
    public void testEmpty() {
        // workaround for missing runtime classes in JUnit parametrized tests
        final String[] inputs = new String[] {null, ""};
        for (String value : inputs) {
            IllegalArgumentException e = assertThrows(
                    IllegalArgumentException.class,
                    () -> MetaData.Builder.withValue(value).instance()
            );
            assertEquals("Metadata value must not be null or blank!", e.getMessage());
        }
    }

    @Test
    public void testWithValue() {
        final String value = "foo";
        final MetaData data = MetaData.Builder.withValue(value).instance();

        assertNotNull(data);
        assertEquals(value, data.getValue());
        //assertTrue(data.getTimestamp() > 0);

        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());

        String json = assertDoesNotThrow(
                () -> mapper.writeValueAsString(data)
        );
        assertTrue(json.startsWith("{\"value\":\"foo\",\"timestamp\":"));
        assertTrue(json.endsWith("}"));
    }

    @Test
    public void testComplete() {
        final String value = "foo";
        final String ts = "2022-09-15T13:17:09.840Z";
        final Instant timestamp = Instant.from(DateTimeFormatter.ISO_INSTANT.parse(ts));
        final MetaData data = MetaData.Builder.withValue(value).setTimestamp(timestamp).instance();

        assertNotNull(data);
        assertEquals(value, data.getValue());
        assertEquals(timestamp, data.getTimestamp());

        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());

        String json = assertDoesNotThrow(
                () -> mapper.writeValueAsString(data)
        );
        assertEquals(String.format("{\"value\":\"foo\",\"timestamp\":\"%s\"}", ts), json);
    }

    @Test
    public void testFromCompleteJson() {
        final String ts = "2022-05-13T23:04:00.100Z";
        final String json = String.format("{\"value\":\"foo\",\"timestamp\":\"%s\"}", ts);

        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());

        final MetaData data = assertDoesNotThrow(
                () -> mapper.readValue(json, MetaData.class)
        );
        assertNotNull(data);
        assertEquals("foo", data.getValue());
        assertEquals(ts, data.getTimestamp().toString());
    }

    @Test
    public void testIncompleteJson() {
        final String json_no_value = "{\"timestamp\":1658509802251}";
        final String json_no_timestamp = "{\"value\":\"foo\"}";

        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());

        assertThrows(
                MismatchedInputException.class,
                () -> mapper.readValue(json_no_value, MetaData.class)
        );

        assertThrows(
                MismatchedInputException.class,
                () -> mapper.readValue(json_no_timestamp, MetaData.class)
        );
    }
}
