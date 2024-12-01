package fr.tp.inf112.projects.robotsim.model;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.annotation.JsonIgnore;

import fr.tp.inf112.projects.canvas.controller.Observable;
import fr.tp.inf112.projects.canvas.controller.Observer;
import fr.tp.inf112.projects.canvas.model.Canvas;
import fr.tp.inf112.projects.canvas.model.Figure;
import fr.tp.inf112.projects.canvas.model.Style;
import fr.tp.inf112.projects.robotsim.model.motion.Motion;
import fr.tp.inf112.projects.robotsim.model.shapes.PositionedShape;
import fr.tp.inf112.projects.robotsim.model.shapes.RectangularShape;

public class Factory extends Component implements Canvas, Observable {

    @JsonIgnore
    private static final long serialVersionUID = 5156526483612458192L;

    @JsonIgnore
    private static final ComponentStyle DEFAULT = new ComponentStyle(5.0f);

    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    @JsonManagedReference
    private final List<Component> components;

    @JsonIgnore
    private transient List<Observer> observers;

    @JsonIgnore
    private transient boolean simulationStarted;

    // Default constructor for Jackson
    public Factory() {
        this(0, 0, null);
    }

    public Factory(final int width, final int height, final String name) {
        super(null, new RectangularShape(0, 0, width, height), name);
        components = new ArrayList<>();
        observers = null;
        simulationStarted = false;
    }

    @JsonIgnore
	public List<Observer> getObservers() {
        if (observers == null) {
            observers = new ArrayList<>();
        }
        return observers;
    }

    @Override
    public boolean addObserver(Observer observer) {
        return getObservers().add(observer);
    }

    @Override
    public boolean removeObserver(Observer observer) {
        return getObservers().remove(observer);
    }

    @Override
	public void notifyObservers() {
        for (final Observer observer : getObservers()) {
            observer.modelChanged();
        }
    }

    public boolean addComponent(final Component component) {
        if (components.add(component)) {
            notifyObservers();
            return true;
        }
        return false;
    }

    public boolean removeComponent(final Component component) {
        if (components.remove(component)) {
            notifyObservers();
            return true;
        }
        return false;
    }

    @JsonIgnore
    @JsonManagedReference
    protected List<Component> getComponents() {
        return components;
    }

    @SuppressWarnings({ "unchecked", "rawtypes" })
    @Override
    public Collection<Figure> getFigures() {
        return (Collection) components;
    }

    @Override
    public String toString() {
        return super.toString() + " components=" + components + "]";
    }

    @JsonIgnore
    public boolean isSimulationStarted() {
        return simulationStarted;
    }

    public void startSimulation() {
        if (!isSimulationStarted()) {
            this.simulationStarted = true;
            notifyObservers();
            behave();
        }
    }

    public void stopSimulation() {
        if (isSimulationStarted()) {
            this.simulationStarted = false;
            notifyObservers();
        }
    }

    @Override
    public boolean behave() {
        boolean behaved = true;
        for (final Component component : getComponents()) {
            Thread componentThread = new Thread(component);
            componentThread.start();
            behaved = true; // Set to true as threads are started
        }
        return behaved;
    }

    @Override
    public Style getStyle() {
        return DEFAULT;
    }

    public boolean hasObstacleAt(final PositionedShape shape) {
        for (final Component component : getComponents()) {
            if (component.overlays(shape) && !component.canBeOverlayed(shape)) {
                return true;
            }
        }
        return false;
    }

    public boolean hasMobileComponentAt(final PositionedShape shape, final Component movingComponent) {
        for (final Component component : getComponents()) {
            if (component != movingComponent && component.isMobile() && component.overlays(shape)) {
                return true;
            }
        }
        return false;
    }

    public Component getMobileComponentAt(Position nextPosition, Robot robot) {
        for (final Component component : getComponents()) {
            if (component != robot && component.isMobile() && component.getPosition().equals(nextPosition)) {
                return component;
            }
        }
        return null;
    }

    public synchronized int moveComponent(final Motion motion, final Robot componentToMove) {
        if (motion == null) {
            return 0;
        }
        Position targetPosition = motion.getTargetPosition();
        if (getMobileComponentAt(targetPosition, componentToMove) == null) {
            return motion.moveToTarget();
        }
        return 0;
    }
}
 