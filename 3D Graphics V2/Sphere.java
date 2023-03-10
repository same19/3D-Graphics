import java.util.*;
import java.lang.*;
import java.awt.*;
import java.io.*;
public class Sphere implements Obj3D {
	private Color color;
	private Vector center;
	private double radius;
	public Sphere() {
		this(new Vector(),1,Color.BLUE);
	}
	public Sphere(Vector center) {
		this(center,1,Color.BLUE);
	}
	public Sphere(Vector center, double radius) {
		this(center, radius, Color.BLUE);
	}
	public Sphere(Vector center, double radius, Color c) {
		this.center = center;
		this.radius = radius;
		color = c;
	}
	public Vector getNormal(Vector position) {
		return position.subtract(center).normalize();
	}
	public double volume() {
		return Math.PI * 4.0/3.0 * radius*radius*radius;
	}
	public double mass() {
		return volume();
	}
	public double getReflectivity() {
		return 0.8;
	}
	public void setPosition(Vector v) {
		this.center = v;
	}
	// public boolean equals(Sphere o) {

	// }
	public Vector collide(Line l) {//returns null if no collision
		Vector nearest = l.getDirection().project(center.subtract(l.getPoint())).add(l.getPoint());
		if (l == null){
			return null;
		}
		double minDistance = nearest.distance(center);
		if (minDistance > radius) {
			return null; //no collision, as the minimum distance from the line to the center is greater than the radius
		}

		double distanceToEdge = Math.sqrt(radius * radius - minDistance * minDistance);
		// Vector collision = x.subtract(w.normalize(Math.sqrt(radius*radius - x.normSq())));
		Vector collision = nearest.subtract(l.getDirection().normalize(distanceToEdge));
		return collision;
	}
	public Color getColor(Vector v) {
		/*double[] scale = {0,0,0};
		for (int index = 0;index<3;index++) {
			scale[index] = (v.get(index)-(center.get(index)-radius))/(2*radius);
			if (scale[index] < 0 || scale[index] > 1) {
				System.out.println("DIMENSION: "+new Integer(index).toString() + " " + new Double(v.distance(center)).toString());
				scale[index] = 0;
			}
		}
		return new Color((int)(255*scale[0]), (int)(255*scale[1]), (int)(255*scale[2]));*/
		return color;
	}
	public void move(Vector v) {
		center = center.add(v);
	}
	public Color getColor() {
		return color;
	}
	public Vector getPosition() {
		return getCenter();
	}
	public Vector getCenter() {
		return center;
	}
	public double getRadius() {
		return radius;
	}
	public void setCenter(Vector v) {
		center = v;
	}
	public void setRadius(double d) {
		radius = d;
	}
	public void setColor(Color c) {
		color = c;
	}
}