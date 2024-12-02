
package fr.tp.inf112.projects.robotsim.app;

import com.fasterxml.jackson.databind.ObjectMapper;
import fr.tp.inf112.projects.canvas.controller.Observer;
import fr.tp.inf112.projects.canvas.model.Canvas;
import fr.tp.inf112.projects.canvas.model.CanvasPersistenceManager;
import fr.tp.inf112.projects.robotsim.model.Factory;

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

    public RemoteSimulatorController(final CanvasPersistenceManager persistenceManager) {
        super(persistenceManager);
        this.httpClient = HttpClient.newHttpClient();
        this.objectMapper = new ObjectMapper();
    }

    @Override
    public void startAnimation() {
        try {
            URI uri = new URI("http", null, "localhost", 8080, "/simulation/start", null, null);
            HttpRequest request = HttpRequest.newBuilder().uri(uri).POST(HttpRequest.BodyPublishers.noBody()).build();
            httpClient.send(request, HttpResponse.BodyHandlers.ofString());
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
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void updateViewer() throws InterruptedException, URISyntaxException, IOException {
        while (((Factory) getCanvas()).isSimulationStarted()) {
            final Factory remoteFactoryModel = getFactory();
            setCanvas(remoteFactoryModel);
            Thread.sleep(100);
        }
    }

    private Factory getFactory() throws URISyntaxException, IOException, InterruptedException {
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
}
