package com.penguineering.cleanuri.common.message;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.exc.MismatchedInputException;
import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import org.junit.jupiter.api.Test;

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
        assertTrue(data.getTimestamp() > 0);

        ObjectMapper mapper = new ObjectMapper();
        String json = assertDoesNotThrow(
                () -> mapper.writeValueAsString(data)
        );
        assertTrue(json.startsWith("{\"value\":\"foo\",\"timestamp\":"));
        assertTrue(json.endsWith("}"));
    }

    @Test
    public void testComplete() {
        final String value = "foo";
        final long timestamp = 1658509802251L;
        final MetaData data = MetaData.Builder.withValue(value).setTimestamp(timestamp).instance();

        assertNotNull(data);
        assertEquals(value, data.getValue());
        assertEquals(timestamp, data.getTimestamp());

        ObjectMapper mapper = new ObjectMapper();
        String json = assertDoesNotThrow(
                () -> mapper.writeValueAsString(data)
        );
        assertEquals("{\"value\":\"foo\",\"timestamp\":1658509802251}", json);
    }

    @Test
    public void testFromCompleteJson() {
        final String json = "{\"value\":\"foo\",\"timestamp\":1658509802251}";
        ObjectMapper mapper = new ObjectMapper();

        final MetaData data = assertDoesNotThrow(
                () -> mapper.readValue(json, MetaData.class)
        );
        assertNotNull(data);
        assertEquals("foo", data.getValue());
        assertEquals(1658509802251L, data.getTimestamp());
    }

    @Test
    public void testIncompleteJson() {
        final String json_no_value = "{\"timestamp\":1658509802251}";
        final String json_no_timestamp = "{\"value\":\"foo\"}";
        ObjectMapper mapper = new ObjectMapper();

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
