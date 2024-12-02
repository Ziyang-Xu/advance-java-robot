
package consumer;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.serialization.StringDeserializer;

import java.io.IOException;
import java.net.URISyntaxException;
import java.time.Duration;
import java.util.Collections;
import java.util.Properties;
import java.util.logging.Logger;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import fr.tp.inf112.projects.robotsim.app.RemoteSimulatorController;
import fr.tp.inf112.projects.robotsim.model.Factory;
import util.SimulationServiceUtils;

public class FactorySimulationEventConsumer {
    private static final Logger LOGGER = Logger.getLogger(FactorySimulationEventConsumer.class.getName());
    private final KafkaConsumer<String, String> consumer;
    private final RemoteSimulatorController controller;
    private final ObjectMapper objectMapper;

    public FactorySimulationEventConsumer(final RemoteSimulatorController controller, final ObjectMapper objectMapper) {
        this.controller = controller;
        this.objectMapper = objectMapper;
        final Properties props = SimulationServiceUtils.getDefaultConsumerProperties();
        props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
        props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
        this.consumer = new KafkaConsumer<>(props);
        String topicName = null;
        try {
            topicName = SimulationServiceUtils.getTopicName(controller.getFactory());
        } catch (URISyntaxException | IOException | InterruptedException e) {
            e.printStackTrace();
        }
        this.consumer.subscribe(Collections.singletonList(topicName));
    }

    public void consumeMessages() throws JsonMappingException {
        try {
            while (controller.isAnimationRunning()) {
                final ConsumerRecords<String, String> records = consumer.poll(Duration.ofMillis(100));
                for (final ConsumerRecord<String, String> record : records) {
                    LOGGER.fine("Received JSON Factory text '" + record.value() + "'.");
                    Factory factory = null;
                    try {
                        factory = objectMapper.readValue(record.value(), Factory.class);
                    } catch (JsonProcessingException e) {
                        e.printStackTrace();
                    }
                    controller.setCanvas(factory);
                }
            }
        } finally {
            consumer.close();
        }
    }
}
