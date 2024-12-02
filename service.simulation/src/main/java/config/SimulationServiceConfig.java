
package config;

import com.fasterxml.jackson.databind.ObjectMapper;

import fr.tp.inf112.projects.robotsim.model.Factory;

import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;
import org.springframework.kafka.support.serializer.JsonSerializer;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class SimulationServiceConfig {

    @Bean
    @Primary
    public ObjectMapper objectMapper() {
        ObjectMapper objectMapper = new ObjectMapper();
        // Customize the ObjectMapper as needed
        return objectMapper;
    }

    @Bean
    public ProducerFactory<String, Factory> producerFactory() {
        Map<String, Object> config = new HashMap<>();
        config.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092"); // Replace with your Kafka server details
        JsonSerializer<Factory> factorySerializer = new JsonSerializer<>(objectMapper());
        return new DefaultKafkaProducerFactory<>(config, new StringSerializer(), factorySerializer);
    }

    @Bean
    @Primary
    public KafkaTemplate<String, Factory> kafkaTemplate() {
        return new KafkaTemplate<>(producerFactory());
    }
}
