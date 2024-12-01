
package com.example.service.simulation;

import fr.tp.inf112.projects.canvas.model.CanvasPersistenceManager;
import fr.tp.inf112.projects.robotsim.app.RemoteSimulatorController;
import fr.tp.inf112.projects.robotsim.model.RemoteFactoryPersistenceManager;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class Application {

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

    public RemoteSimulatorController createRemoteSimulatorController() {
        RemoteFactoryPersistenceManager persistenceManager = new RemoteFactoryPersistenceManager(null);
        return new RemoteSimulatorController((CanvasPersistenceManager) persistenceManager);
    }
}
