//M. M. Kuttel 2024 mkuttel@gmail.com
// Class for storing  locations of people (swimmers only for now, but could add other types) in the simulation

package medleySimulation;

import java.awt.Color;

public class PeopleLocation  { // this is made a separate class so don't have to access thread
	
	private final int ID; //each person has an ID
	private Color myColor; //colour of the person
	
	private boolean inStadium; //are they here?
	private boolean arrived; //have they arrived at the event?
	private GridBlock location; //which GridBlock are they on?
	
	//constructor
	PeopleLocation(int ID , Color c) {
		myColor = c;
		inStadium = false; //not in pool
		arrived = false; //have not arrived
		this.ID=ID;
	}
	
	//setter
	public  void setInStadium(boolean in) {
		inStadium = in;
	}
	
	//getter and setter
	public boolean getArrived() {
		return arrived;
	}
	public void setArrived() {
		arrived=true;
	}

//getter and setter
	public GridBlock getLocation() {
		return location;
	}
	public  void setLocation(GridBlock location) {
		this.location = location;
	}

	//getter
	public  int getX() { return location.getX();}	
	
	//getter
	public  int getY() {	return location.getY();	}
	
	//getter
	public  int getID() {	return ID;	}

	//getter
	public  boolean inPool() {
		return inStadium;
	}
	//getter and setter
	public  Color getColor() { return myColor; }
	public  void setColor(Color myColor) { this.myColor= myColor; }
}
