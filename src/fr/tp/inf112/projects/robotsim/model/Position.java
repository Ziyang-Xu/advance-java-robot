package fr.tp.inf112.projects.robotsim.model;

import java.io.Serializable;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonIgnore;

@JsonInclude(JsonInclude.Include.NON_NULL) // Include only non-null fields in JSON serialization
public class Position implements Serializable {

	private static final long serialVersionUID = 7274819087013715987L;

	private int xCoordinate;
	private int yCoordinate;

	public Position(final int xCoordinate, 
					final int yCoordinate) {
		super();
		this.xCoordinate = xCoordinate;
		this.yCoordinate = yCoordinate;
	}
	
	// Default constructor for Jackson
	public Position() {
		super();
	}

	public int getxCoordinate() {
		return xCoordinate;
	}

	public int getyCoordinate() {
		return yCoordinate;
	}

	public boolean setxCoordinate(final int xCoordinate) {
		if (this.xCoordinate == xCoordinate) {
			return false;
		}
		this.xCoordinate = xCoordinate;
		return true;
	}

	public boolean setyCoordinate(final int yCoordinate) {
		if (this.yCoordinate == yCoordinate) {
			return false;
		}
		this.yCoordinate = yCoordinate;
		return true;
	}

	@JsonIgnore // Ignore this method during JSON serialization
	@Override
	public boolean equals(final Object objectToCompare) {
		if (objectToCompare == null) {
			return false;
		}
		
		final Position position = (Position) objectToCompare;
		return getxCoordinate() == position.getxCoordinate() && getyCoordinate() == position.getyCoordinate();
	}

	@JsonIgnore // Ignore this method during JSON serialization
	@Override
	public String toString() {
		final StringBuilder strBuild = new StringBuilder("Position = (");
		strBuild.append(getxCoordinate());
		strBuild.append(", ");
		strBuild.append(getyCoordinate());
		strBuild.append(")");
		
		return strBuild.toString();
	}
}
