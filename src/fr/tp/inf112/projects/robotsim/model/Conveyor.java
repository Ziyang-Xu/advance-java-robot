package fr.tp.inf112.projects.robotsim.model;

import com.fasterxml.jackson.annotation.JsonIgnore;

import fr.tp.inf112.projects.robotsim.model.shapes.PositionedShape;

public class Conveyor extends Component {

    @JsonIgnore
    private static final long serialVersionUID = 3686048824190456547L;

    public Conveyor(final Factory factory,
                    final PositionedShape shape,
                    final String name) {
        super(factory, shape, name);
    }

    // Default constructor for Jackson
    public Conveyor() {
        super(null, null, null);
    }

    @Override
    public String toString() {
        return super.toString() + "]";
    }

    @Override
    public boolean canBeOverlayed(final PositionedShape shape) {
        return true;
    }
}
