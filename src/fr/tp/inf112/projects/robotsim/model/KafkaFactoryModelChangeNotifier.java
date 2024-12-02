
package fr.tp.inf112.projects.robotsim.model;

import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.config.TopicBuilder;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;

import fr.tp.inf112.projects.canvas.controller.Observer;

import org.springframework.kafka.support.KafkaHeaders;

import java.util.concurrent.CompletableFuture;

public class KafkaFactoryModelChangeNotifier implements FactoryModelChangedNotifier {
    private final Factory factoryModel;
    private final KafkaTemplate<String, Factory> simulationEventTemplate;

    public KafkaFactoryModelChangeNotifier(Factory factoryModel, KafkaTemplate<String, Factory> simulationEventTemplate) {
        this.factoryModel = factoryModel;
        this.simulationEventTemplate = simulationEventTemplate;

        // Create Kafka topic
        TopicBuilder.name("simulation-" + factoryModel.getId()).build();
    }

    @Override
    public void notifyObservers() {
        try {
            // Construct the message
            Message<Factory> factoryMessage = MessageBuilder.withPayload(factoryModel)
                    .setHeader(KafkaHeaders.TOPIC, "simulation-" + factoryModel.getId())
                    .build();

            // Send the message
            CompletableFuture<?> sendResult = simulationEventTemplate.send(factoryMessage);
            sendResult.whenComplete((result, ex) -> {
                if (ex != null) {
                    throw new RuntimeException(ex);
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void addObserver(Observer observer) {
        // No-op for Kafka notifier
    }

    @Override
    public void removeObserver(Observer observer) {
        // No-op for Kafka notifier
    }

	
}
