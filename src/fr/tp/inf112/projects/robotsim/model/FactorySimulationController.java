
package fr.tp.inf112.projects.robotsim.model;

import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/factory")
public class FactorySimulationController {

	
    private static final Logger logger = Logger.getLogger(FactorySimulationController.class.getName());

    @Autowired
    private FactoryPersistenceManager factoryPersistenceManager;

    private final Map<String, Factory> simulatedFactories = new HashMap<>();

    @PostMapping("/start/{id}")
    public boolean startSimulation(@PathVariable String id) {
        try {
            Factory factory = factoryPersistenceManager.loadFactory(id);
            if (factory != null) {
                simulatedFactories.put(id, factory);
                factory.startSimulation();
                logger.info("Started simulation for factory ID: " + id);
                return true;
            } else {
                logger.warning("Factory model not found for ID: " + id);
                return false;
            }
        } catch (Exception e) {
            logger.severe("Error starting simulation for factory ID: " + id + " - " + e.getMessage());
            return false;
        }
    }

    @GetMapping("/retrieve/{id}")
    public Factory retrieveSimulation(@PathVariable String id) {
        Factory factory = simulatedFactories.get(id);
        if (factory != null) {
            logger.info("Retrieved simulation for factory ID: " + id);
        } else {
            logger.warning("No simulation found for factory ID: " + id);
        }
        return factory;
    }


@PostMapping("/stop/{id}")
public boolean stopSimulation(@PathVariable String id) {
    if (simulatedFactories.containsKey(id)) {
        Factory factory = simulatedFactories.remove(id);
        // Stop the simulation
        factory.stopSimulation();
        logger.info("Stopped simulation for factory ID: " + id);
        return true;
    } else {
        logger.warning("No simulation found to stop for factory ID: " + id);
        return false;
    }
}

}
