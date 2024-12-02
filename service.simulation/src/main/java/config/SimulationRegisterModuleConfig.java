
package config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.jsontype.BasicPolymorphicTypeValidator;
import com.fasterxml.jackson.databind.jsontype.PolymorphicTypeValidator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import java.util.ArrayList;
import java.util.LinkedHashSet;

@Configuration
public class SimulationRegisterModuleConfig {

    @Bean
    @Primary
    public ObjectMapper objectMapper() {
        PolymorphicTypeValidator typeValidator = BasicPolymorphicTypeValidator.builder()
                .allowIfSubType("fr.tp.inf112.projects.canvas.model.PositionedShape")
                .allowIfSubType("fr.tp.inf112.projects.canvas.model.Component")
                .allowIfSubType("fr.tp.inf112.projects.canvas.model.BasicVertex")
                .allowIfSubType(ArrayList.class.getName())
                .allowIfSubType(LinkedHashSet.class.getName())
                .build();
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.activateDefaultTyping(typeValidator, ObjectMapper.DefaultTyping.NON_FINAL);
        return objectMapper;
    }
}
