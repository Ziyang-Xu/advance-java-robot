
package fr.tp.inf112.projects.robotsim.app;

import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import consumer.FactorySimulationEventConsumer;
import fr.tp.inf112.projects.canvas.controller.Observer;
import fr.tp.inf112.projects.canvas.model.Canvas;
import fr.tp.inf112.projects.canvas.model.CanvasPersistenceManager;
import fr.tp.inf112.projects.robotsim.model.Factory;
import fr.tp.inf112.projects.robotsim.model.LocalFactoryModelChangedNotifier;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.List;

public class RemoteSimulatorController extends SimulatorController {

    private final HttpClient httpClient;
    private final ObjectMapper objectMapper;
    private final LocalFactoryModelChangedNotifier notifier;
    private final FactorySimulationEventConsumer eventConsumer;
    private boolean animationRunning;

    public RemoteSimulatorController(final CanvasPersistenceManager persistenceManager) {
        super(persistenceManager);
        this.httpClient = HttpClient.newHttpClient();
        this.objectMapper = new ObjectMapper();
        this.notifier = new LocalFactoryModelChangedNotifier();
        this.eventConsumer = new FactorySimulationEventConsumer(this, objectMapper);
        this.animationRunning = false;
    }

    @Override
    public void startAnimation() {
        try {
            URI uri = new URI("http", null, "localhost", 8080, "/simulation/start", null, null);
            HttpRequest request = HttpRequest.newBuilder().uri(uri).POST(HttpRequest.BodyPublishers.noBody()).build();
            httpClient.send(request, HttpResponse.BodyHandlers.ofString());
            this.animationRunning = true;
            new Thread(() -> {
				try {
					eventConsumer.consumeMessages();
				} catch (JsonMappingException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}).start();
            updateViewer();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void stopAnimation() {
        try {
            URI uri = new URI("http", null, "localhost", 8080, "/simulation/stop", null, null);
            HttpRequest request = HttpRequest.newBuilder().uri(uri).POST(HttpRequest.BodyPublishers.noBody()).build();
            httpClient.send(request, HttpResponse.BodyHandlers.ofString());
            this.animationRunning = false;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public boolean isAnimationRunning() {
        return animationRunning;
    }

    private void updateViewer() throws InterruptedException, URISyntaxException, IOException {
        while (((Factory) getCanvas()).isSimulationStarted()) {
            final Factory remoteFactoryModel = getFactory();
            setCanvas(remoteFactoryModel);
            Thread.sleep(100);
        }
    }

    public Factory getFactory() throws URISyntaxException, IOException, InterruptedException {
        URI uri = new URI("http", null, "localhost", 8080, "/simulation/factory", null, null);
        HttpRequest request = HttpRequest.newBuilder().uri(uri).GET().build();
        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        return objectMapper.readValue(response.body(), Factory.class);
    }

    @Override
    public void setCanvas(final Canvas canvasModel) {
        final List<Observer> observers = ((Factory) getCanvas()).getObservers();
        super.setCanvas(canvasModel);
        for (final Observer observer : observers) {
            ((Factory) getCanvas()).addObserver(observer);
        }
        ((Factory) getCanvas()).notifyObservers();
    }

    public boolean addObserver(Observer observer) {
        notifier.addObserver(observer);
		return animationRunning;
    }

    public boolean removeObserver(Observer observer) {
        notifier.removeObserver(observer);
		return animationRunning;
    }
}
