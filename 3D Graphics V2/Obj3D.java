import java.util.*;
import java.lang.*;
import java.awt.*;
import java.io.*;
public interface Obj3D {
	public double mass();
	public double volume();
	public Vector collide(Line l); //returns the position of the collision of the line l and this object
	public Color getColor(Vector position); // returns the color of the position in/on this object represented by position vector v
	public Vector getNormal(Vector position);
	public Vector getPosition();
	public double getReflectivity();
}