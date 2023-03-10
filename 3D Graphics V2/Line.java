import java.awt.*;
public class Line implements Obj3D {
	private static final Color DEFAULT_COLOR = Color.BLACK;
	private Vector dir; //the direction vector of the line
	private Vector point; //one point on the line
	private Color color;
	public Line(Vector point1, Vector point2) { //two points on the line
		if (!point1.sameLength(point2)) {
			throw new RuntimeException("Vector arguments to the line constructor do not match in length");
		}
		this.point = point1;
		this.dir = point2.subtract(point1).normalize();
		this.color = DEFAULT_COLOR;
	}
	public Line() {
		this(new Vector(), new Vector());
	}
	public double volume() {
		return 0;
	}
	public Vector getPosition() {
		return getPoint();
	}
	public Vector getNormal(Vector position) {
		return null;
	}
	public double mass() {
		return 0;
	}
	public Color getColor(Vector v) {
		return color;
	}
	public Vector getDirection() {
		return dir;
	}
	public Vector getPoint() {
		return point;
	}
	public double getReflectivity() {
		return 0;
	}
	public void setPoint(Vector point) {
		if (!point.sameLength(point)) {
			throw new RuntimeException("Vector argument does not match in length to the point on the line");
		}
		this.point = point;
	}
	public void setDirection(Vector dir) {
		if (!dir.sameLength(dir)) {
			throw new RuntimeException("Vector argument does not match in length to the direction vector of the line");
		}
		this.dir = dir;
	}
	public int numDimensions() {
		return dir.size();
	}
	public int size() {
		return dir.size();
	}
	public Vector collide(Line l) {
		return intersect(l);
	}
	public Vector intersect(Line l) {
		System.out.println("Intersect: "+this.getDirection()+" "+l.getDirection());
		Vector a = l.getPoint().subtract(point);
		double[] d = Vector.linearCombination(dir, l.getDirection(), a);
		if (d == null) {
			System.out.println("NULL");
			return null;
		}
		return point.add(dir.multiply(d[0]));
	}

}