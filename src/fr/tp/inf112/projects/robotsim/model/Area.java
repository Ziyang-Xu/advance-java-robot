package fr.tp.inf112.projects.robotsim.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonIgnore;

import fr.tp.inf112.projects.canvas.model.Style;
import fr.tp.inf112.projects.robotsim.model.shapes.PositionedShape;
import fr.tp.inf112.projects.robotsim.model.shapes.RectangularShape;

public class Area extends Component {

    @JsonIgnore
    private static final long serialVersionUID = 5022214804847296168L;

    @JsonIgnore
    private static final Style STYLE = new ComponentStyle(ComponentStyle.DEFAULT_DASH_PATTERN);

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Machine machine;

    public Area(final Room room, final RectangularShape shape, final String name) {
        super(room.getFactory(), shape, name);
        room.addArea(this);
        machine = null;
    }

    public Area() {
        this(null, null, null);
    }

    @JsonIgnore
    protected void setMachine(final Machine machine) {
        this.machine = machine;
    }

    @Override
    public boolean canBeOverlayed(final PositionedShape shape) {
        return true;
    }

    @Override
    public String toString() {
        return super.toString() + " machine=" + machine + "]";
    }

    @Override
    public Style getStyle() {
        return STYLE;
    }
}
