package fr.tp.inf112.projects.robotsim.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import fr.tp.inf112.projects.robotsim.model.shapes.CircularShape;

@JsonInclude(JsonInclude.Include.NON_NULL) // Include only non-null fields in JSON serialization
public class Puck extends Component {

	private static final long serialVersionUID = -2194778403928041427L;

	public Puck(final Factory factory,
				final CircularShape shape,
				final String name) {
		super(factory, shape, name);
	}

	// Default constructor for Jackson
	protected Puck() {
		this(null, null, null);
	}

	@JsonIgnore // Ignore this method during JSON serialization
	@Override
	public String toString() {
		return super.toString() + "]";
	}
}
