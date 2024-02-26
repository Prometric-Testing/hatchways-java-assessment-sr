package com.skafld.sample.worker.consumer;

import com.skafld.sample.worker.event.HelloEvent;

import org.springframework.kafka.support.Acknowledgment;
import org.apache.kafka.clients.consumer.ConsumerRecord;

import org.junit.jupiter.api.Test;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

public class HelloConsumerTest {

    @Test
    public void testConsumeEvent() {
        final HelloEvent event = new HelloEvent("Friend");
        final Acknowledgment ack = mock(Acknowledgment.class);
        final ConsumerRecord<String, HelloEvent> record = new ConsumerRecord<>("topic", 0, 123L, "key", event);
        final HelloConsumer consumer = new HelloConsumer();

        consumer.consumeEvent(record, ack);

        verify(ack, times(1)).acknowledge();
    }
}
