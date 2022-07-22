package com.penguineering.cleanuri.common.message;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.exc.MismatchedInputException;
import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import org.junit.jupiter.api.Test;

import java.net.URI;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@MicronautTest
public class TestExtractionTask {
    static final URI EXAMPLE_COM = URI.create("https://www.example.com");
    static final URI EXAMPLE_COM_2 = URI.create("https://www.example.com/2");
    static final ExtractionRequest REQUEST = ExtractionRequest.Builder.withURI(EXAMPLE_COM).instance();

    @Test
    public void testWithRequestOnly() {
        final ExtractionTask task = ExtractionTask.Builder.withRequest(REQUEST).instance();

        assertNotNull(task);
        assertSame(REQUEST, task.getRequest());
        assertNull(task.getCanonizedURI());
        assertTrue(task.getMeta().isEmpty());
        assertTrue(task.getErrors().isEmpty());

        ObjectMapper mapper = new ObjectMapper();
        String json = assertDoesNotThrow(
                () -> mapper.writeValueAsString(task)
        );
        assertEquals("{\"request\":{\"uri\":\"https://www.example.com\"}}", json);
    }

    @Test
    public void testCanonizedURI() {
        final ExtractionTask task = ExtractionTask.Builder
                .withRequest(REQUEST)
                .setCanonizedURI(EXAMPLE_COM_2)
                .instance();

        assertNotNull(task);
        assertSame(REQUEST, task.getRequest());
        assertEquals(EXAMPLE_COM_2, task.getCanonizedURI());
        assertTrue(task.getMeta().isEmpty());
        assertTrue(task.getErrors().isEmpty());

        ObjectMapper mapper = new ObjectMapper();
        String json = assertDoesNotThrow(
                () -> mapper.writeValueAsString(task)
        );
        assertEquals(
                "{\"request\":{\"uri\":\"https://www.example.com\"}" +
                        ",\"canonized-uri\":\"https://www.example.com/2\"}",
                json);
    }

    @Test
    public void testMeta() {
        final ExtractionTask task = ExtractionTask.Builder
                .withRequest(REQUEST)
                .putMeta("test", MetaData.Builder.withValue("foo").setTimestamp(1).instance())
                .instance();

        assertNotNull(task);
        assertSame(REQUEST, task.getRequest());
        assertTrue(task.getMeta().containsKey("test"));
        assertEquals("foo", task.getMeta().get("test").getValue());

        ObjectMapper mapper = new ObjectMapper();
        String json = assertDoesNotThrow(
                () -> mapper.writeValueAsString(task)
        );
        assertEquals(
                "{\"request\":{\"uri\":\"https://www.example.com\"}," +
                        "\"meta\":{\"test\":{\"value\":\"foo\",\"timestamp\":1}}}",
                json);
    }

    @Test
    public void testErrors() {
        final ExtractionTask task = ExtractionTask.Builder
                .withRequest(REQUEST)
                .addError("error1")
                .addError("error2")
                .instance();

        assertNotNull(task);
        assertSame(REQUEST, task.getRequest());
        assertTrue(task.getMeta().isEmpty());
        assertEquals(List.of("error1", "error2"), task.getErrors());

        ObjectMapper mapper = new ObjectMapper();
        String json = assertDoesNotThrow(
                () -> mapper.writeValueAsString(task)
        );
        assertEquals(
                "{\"request\":{\"uri\":\"https://www.example.com\"},\"errors\":[\"error1\",\"error2\"]}",
                json);
    }

    @Test
    public void testJson() {
        final ExtractionTask task = ExtractionTask.Builder
                .withRequest(REQUEST)
                .setCanonizedURI(EXAMPLE_COM_2)
                .putMeta("test", MetaData.Builder.withValue("foo").setTimestamp(1).instance())
                .addError("error1")
                .addError("error2")
                .addError("error2")
                .instance();
        // "error2" duplicated on purpose: this must be an (ordered) list!

        ObjectMapper mapper = new ObjectMapper();
        String json = assertDoesNotThrow(
                () -> mapper.writeValueAsString(task)
        );

        assertEquals(
                "{\"request\":" +
                        "{\"uri\":\"https://www.example.com\"}," +
                        "\"canonized-uri\":\"https://www.example.com/2\"," +
                        "\"meta\":{\"test\":{\"value\":\"foo\",\"timestamp\":1}}," +
                        "\"errors\":[\"error1\",\"error2\",\"error2\"]}",
                json);

        // full object
        final ExtractionTask task2 = assertDoesNotThrow(
                () -> mapper.readValue(json, ExtractionTask.class)
        );
        assertNotNull(task2);
        assertEquals(REQUEST.getURI(), task2.getRequest().getURI());
        assertEquals(EXAMPLE_COM_2, task2.getCanonizedURI());
        assertEquals("foo", task2.getMeta().get("test").getValue());
        assertEquals(List.of("error1", "error2", "error2"), task2.getErrors());

        // request only
        final ExtractionTask task3 = assertDoesNotThrow(
                () -> mapper.readValue(
                        "{\"request\":" +
                        "{\"uri\":\"https://www.example.com\"}}",
                        ExtractionTask.class)
        );
        assertNotNull(task3);
        assertEquals(REQUEST.getURI(), task3.getRequest().getURI());
        assertNull(task3.getCanonizedURI());
        assertTrue(task3.getMeta().isEmpty());
        assertTrue(task3.getErrors().isEmpty());

        // missing request
        assertThrows(
                MismatchedInputException.class,
                () -> mapper.readValue("{}", ExtractionRequest.class)
        );
    }

    @Test
    public void testCopy() {
        // test if copied task yields the same JSON
        final ExtractionTask task1 = ExtractionTask.Builder
                .withRequest(REQUEST)
                .setCanonizedURI(EXAMPLE_COM_2)
                .putMeta("test", MetaData.Builder.withValue("foo").setTimestamp(1).instance())
                .addError("error2")
                .addError("error1")
                .instance();
        final ExtractionTask task2 = ExtractionTask.Builder.copy(task1).instance();
        assertNotNull(task2);

        ObjectMapper mapper = new ObjectMapper();
        String json1 = assertDoesNotThrow(
                () -> mapper.writeValueAsString(task1)
        );
        String json2 = assertDoesNotThrow(
                () -> mapper.writeValueAsString(task2)
        );

        assertEquals(json1, json2);
    }
}
