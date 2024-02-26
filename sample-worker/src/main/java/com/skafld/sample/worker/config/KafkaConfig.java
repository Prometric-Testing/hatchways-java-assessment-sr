package com.skafld.sample.worker.config;

import java.util.Map;

import com.skafld.sample.worker.event.HelloEvent;

import com.fasterxml.jackson.core.type.TypeReference;
import org.apache.kafka.common.serialization.Deserializer;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.springframework.boot.autoconfigure.kafka.KafkaProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.listener.ContainerProperties;
import org.springframework.kafka.support.serializer.JsonDeserializer;

@EnableKafka
@Configuration
public class KafkaConfig {
    @Bean
    public ConsumerFactory<String, HelloEvent> helloConsumerFactory(final KafkaProperties properties) {
        return createConsumerFactory(properties, new TypeReference<HelloEvent>() {

        });
    }

    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, HelloEvent> helloKafkaListenerContainerFactory(
        final ConsumerFactory<String, HelloEvent> consumerFactory) {
        return createKafkaListenerContainerFactory(consumerFactory);
    }

    private <T> ConsumerFactory<String, T> createConsumerFactory(
        final KafkaProperties properties,
        final TypeReference<T> type) {
        final Deserializer<String> keyDeserializer = new StringDeserializer();
        final Deserializer<T> valueDeserializer = new JsonDeserializer<>(type);
        final Map<String, Object> config = properties.buildConsumerProperties();

        return new DefaultKafkaConsumerFactory<>(config, keyDeserializer, valueDeserializer);
    }

    public <T> ConcurrentKafkaListenerContainerFactory<String, T> createKafkaListenerContainerFactory(
        final ConsumerFactory<String, T> consumerFactory) {
        final ConcurrentKafkaListenerContainerFactory<String, T> factory = new ConcurrentKafkaListenerContainerFactory<>();
        final ContainerProperties properties = factory.getContainerProperties();

        properties.setAckMode(ContainerProperties.AckMode.MANUAL_IMMEDIATE);

        factory.setConsumerFactory(consumerFactory);

        return factory;
    }
}
