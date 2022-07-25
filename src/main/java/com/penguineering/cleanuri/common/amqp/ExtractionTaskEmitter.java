package com.penguineering.cleanuri.common.amqp;

import com.penguineering.cleanuri.common.message.ExtractionTask;
import io.micronaut.rabbitmq.annotation.Binding;
import io.micronaut.rabbitmq.annotation.RabbitClient;
import io.micronaut.rabbitmq.annotation.RabbitProperty;

@RabbitClient
public interface ExtractionTaskEmitter {
    @SuppressWarnings("unused")
    @RabbitProperty(name = "contentType", value = "application/json")
    void send(@Binding String destination,
              @RabbitProperty("correlationId") String correlationId,
              @RabbitProperty("replyTo") String replyTo,
              ExtractionTask task);
}
