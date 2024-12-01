package fr.tp.inf112.projects.robotsim.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import fr.tp.inf112.projects.robotsim.model.shapes.PositionedShape;
import fr.tp.inf112.projects.robotsim.model.shapes.RectangularShape;

@JsonInclude(JsonInclude.Include.NON_NULL) // Include only non-null fields in JSON serialization
public class Machine extends Component {

	private static final long serialVersionUID = -1568908860712776436L;

	public Machine(final Area area,
				   final RectangularShape shape,
				   final String name) {
		super(area.getFactory(), shape, name);
		
		area.setMachine(this);
	}

	// Default constructor for Jackson
	public Machine() {
        this(null, null, null);
    }

	@JsonIgnore // Ignore this method during JSON serialization
	@Override
	public String toString() {
		return super.toString() + "]";
	}

	@JsonIgnore // Ignore this method during JSON serialization
	@Override
	public boolean canBeOverlayed(final PositionedShape shape) {
		return true;
	}
}
