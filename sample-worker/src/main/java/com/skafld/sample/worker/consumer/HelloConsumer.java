package com.skafld.sample.worker.consumer;

import com.skafld.sample.worker.event.HelloEvent;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.stereotype.Service;

@Service
public class HelloConsumer {
    private static final Logger logger = LoggerFactory.getLogger(HelloConsumer.class);

    @KafkaListener(
        containerFactory = "helloKafkaListenerContainerFactory",
        topics = "${sample.hello.topic-name}",
        groupId = "${sample.hello.group-id}")
    public void consumeEvent(
        final ConsumerRecord<String, HelloEvent> record,
        final Acknowledgment ack) {

        final String key = record.key();
        final HelloEvent event = record.value();

        logger.trace("Key : {}, Event: {}", key, event);

        ack.acknowledge();
    }
}
