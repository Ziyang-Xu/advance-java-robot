package fr.tp.inf112.projects.robotsim.model;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonIgnore;

public class Battery implements Serializable {

    @JsonIgnore
    private static final long serialVersionUID = 5744149485828674046L;

    @JsonInclude(JsonInclude.Include.NON_DEFAULT)
    private final float capacity;

    @JsonInclude(JsonInclude.Include.NON_DEFAULT)
    private float level;

    public Battery(final float capacity) {
        this.capacity = capacity;
        level = capacity;
    }

    public Battery() {
        this(0);
    }

    public float consume(float energy) {
        level -= energy;
        return level;
    }

    public float charge(float energy) {
        level += energy;
        return level;
    }

    @Override
    public String toString() {
        return "Battery [capacity=" + capacity + ", level=" + level + "]";
    }
}
