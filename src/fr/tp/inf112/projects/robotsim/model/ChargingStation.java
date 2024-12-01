package fr.tp.inf112.projects.robotsim.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonIgnore;

import fr.tp.inf112.projects.robotsim.model.shapes.PositionedShape;
import fr.tp.inf112.projects.robotsim.model.shapes.RectangularShape;

public class ChargingStation extends Component {

    @JsonIgnore
    private static final long serialVersionUID = -154228412357092561L;

    @JsonInclude(JsonInclude.Include.NON_DEFAULT)
    private boolean charging;

    public ChargingStation(final Room room,
                           final RectangularShape shape,
                           final String name) {
        this(room.getFactory(), shape, name);
    }

    public ChargingStation(final Factory factory,
                           final RectangularShape shape,
                           final String name) {
        super(factory, shape, name);
        charging = false;
    }

    public ChargingStation() {
        // Default constructor for Jackson
        this((Factory) null, null, null);
    }

    @Override
    public String toString() {
        return super.toString() + "]";
    }

    @JsonIgnore
    protected boolean isCharging() {
        return charging;
    }

    @JsonIgnore
    protected void setCharging(boolean charging) {
        this.charging = charging;
    }

    @Override
    public boolean canBeOverlayed(final PositionedShape shape) {
        return true;
    }
}
