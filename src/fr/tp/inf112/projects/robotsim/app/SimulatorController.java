package fr.tp.inf112.projects.robotsim.app;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;

import fr.tp.inf112.projects.canvas.controller.CanvasViewerController;
import fr.tp.inf112.projects.canvas.controller.Observer;
import fr.tp.inf112.projects.canvas.model.Canvas;
import fr.tp.inf112.projects.canvas.model.CanvasPersistenceManager;
import fr.tp.inf112.projects.robotsim.model.Factory;
import fr.tp.inf112.projects.robotsim.model.KafkaFactoryModelChangeNotifier;
import fr.tp.inf112.projects.robotsim.model.FactoryModelChangedNotifier;

public class SimulatorController implements CanvasViewerController {
	@Autowired
	private KafkaTemplate<String, Factory> simulatorTemplate;

	private Factory factoryModel;

	private final CanvasPersistenceManager persistenceManager;

	public SimulatorController(final CanvasPersistenceManager persistenceManager) {
		this(null, persistenceManager);
	}

	public SimulatorController(final Factory factoryModel, final CanvasPersistenceManager persistenceManager) {
		this.factoryModel = factoryModel;
		this.persistenceManager = persistenceManager;
	}
	public void startSimulation(Factory factory) {
        FactoryModelChangedNotifier notifier = new KafkaFactoryModelChangeNotifier(factory, simulatorTemplate);
        factory.setNotifier(notifier);
        factory.startSimulation();
    }

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean addObserver(final Observer observer) {
		if (factoryModel != null) {
			return factoryModel.addObserver(observer);
		}

		return false;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean removeObserver(final Observer observer) {
		if (factoryModel != null) {
			return factoryModel.removeObserver(observer);
		}

		return false;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setCanvas(final Canvas canvasModel) {
		factoryModel = (Factory) canvasModel;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Canvas getCanvas() {
		return factoryModel;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void startAnimation() {
		factoryModel.startSimulation();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void stopAnimation() {
		factoryModel.stopSimulation();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean isAnimationRunning() {
		return factoryModel != null && factoryModel.isSimulationStarted();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public CanvasPersistenceManager getPersistenceManager() {
		return (CanvasPersistenceManager) persistenceManager;
	}
}
