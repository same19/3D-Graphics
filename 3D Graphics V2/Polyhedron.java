import java.util.*;
import java.lang.*;
import java.awt.*;
import java.io.*;
public class Polyhedron implements Obj3D {
	private Plane[] faces;
	private Vector pointInside;
	private Color color;
	protected static Color default_color = Color.CYAN;
	public Polyhedron() {
		faces = new Plane[0];
		color = default_color;
	}
	public Polyhedron(Color color, Vector pointInside, Plane ... faces) {
		this.color = color;
		this.pointInside = pointInside;
		this.faces = faces;
		for (int i = 0;i<this.faces.length;i++) {
			this.faces[i].setNormalSide(pointInside);
		}
	}
	public Vector[] getVertices() {
		Vector[] vertices = new Vector[0];
		return vertices;
	}
	public boolean contains(Vector v) {
		return contains(v, new Plane[0]);
	}
	public boolean contains(Vector v, Plane ... doNotInclude) {
		java.util.List l = Arrays.asList(doNotInclude);
		for (Plane p : faces) {
			if (!p.onNormalSide(v) && !l.contains(p)) {
				return false;
			}
		}
		return true;
	}
	public double getReflectivity() {
		return 0;
	}
	public Vector getNormal(Vector p) {
		ArrayList<Vector> normals = new ArrayList<Vector>();
		for (int i = 0;i<faces.length;i++) {
			if (faces[i].contains(p) && contains(p, faces[i])) {
				normals.add(faces[i].getNormal(p).multiply(-1));
			}
		}
		if (normals.size()==0) {
			return new Vector();
		} else {
			Vector average = new Vector();
			for (int i = 0;i<normals.size();i++) {
				average = average.add(normals.get(i));
			}
			return average.divide(normals.size());
		}
	}
	public double mass() {
		return volume();
	}
	public double volume() {
		Vector[] vertices =getVertices();
		Vector[] pyramidSides = new Vector[vertices.length];
		for (int i = 0;i<vertices.length;i++) {
			//pyramidSides.add(vertices[i].subtract(pointInside));
		}
		return 0;

	}
	public Vector getPosition() {
		return Vector.average(getVertices());
	}
	public Vector collide(Line l) {
		ArrayList<Vector> collisions = new ArrayList<Vector>();
		Vector v;
		for (int i = 0;i<faces.length;i++) {
			v = faces[i].collide(l);
			if (v != null && contains(v,faces[i])) {
				collisions.add(v);
			}
		}
		if (collisions.size()==0) {
			return null;
		} else if (collisions.size()==1) {
			return collisions.get(0);
		}
		int minIndex = 0;
		for (int i = 1;i<collisions.size();i++) {
			if (collisions.get(i).distance(l.getPoint())<collisions.get(minIndex).distance(l.getPoint())) {
				minIndex = i;
			}
		}
		Vector collision = collisions.get(minIndex);
		if (collision.subtract(l.getPoint()).dot(l.getDirection())<0) {
			return null;
		}
		return collision;
	}
	public Color getColor(Vector v) {
		return color;
	}
	public void move(Vector direction) {
		for (int i = 0;i<faces.length;i++) {
			faces[i].setPoint(faces[i].getPoint().add(direction));
		}
	}
	public Vector collide(Polyhedron p) {
		Vector[] vertices = getVertices();
		Vector[] pVertices = p.getVertices();
		return null;
	}
}