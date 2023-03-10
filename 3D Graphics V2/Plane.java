import java.util.*;
import java.lang.*;
import java.awt.*;
import java.io.*;
public class Plane implements Obj3D {
	private Vector dir1;
	private Vector dir2;
	private Vector point;
	private Color color0;
	private Color color1;
	private Vector[] mapping;
	private double normalConstant;
	private Texture texture;
	private double[] mins = {-5,-5};
	private double[] maxes = {5,5};
	public Plane() {
		this(new Vector(), new Vector(1,0,0), new Vector(0,1,0));
	}
	public Plane(Vector point1, Vector point2, Vector point3) {
		this.point = point1;
		this.dir1 = point2.subtract(point1);
		this.dir2 = point3.subtract(point1);
		this.color0 = new Color(191, 49, 17);
		this.color1 = new Color(255, 174, 0);
		Vector row1 = dir1.cross(dir2).cross(dir2);
		Vector row2 = dir1.cross(dir2).cross(dir1);
		row1 = row1.divide(row1.dot(dir1));
		row2 = row2.divide(row2.dot(dir2));
		mapping = new Vector[2];
		mapping[0] = row1;
		mapping[1] = row2;
		normalConstant = 1;
		this.texture = new Texture("wallpaper.jpg");
	}
	public double mass() {
		return 0;
	}
	public double volume() {
		return 0;
	}
	public double getReflectivity() {
		return 0; //turned off for bugs
	}
	public Vector getDir1() {
		return dir1;
	}
	public Vector getDir2() {
		return dir2;
	}
	public void setPoint(Vector point) {
		this.point = point;
	}
	public Vector getPoint() {
		return point;
	}
	public Vector getNormal(Vector position) {
		return normal();
	}
	public Vector normal() {
		return dir1.cross(dir2).multiply(normalConstant);
	}
	public Vector normal(double n) {
		return normal().normalize(n);
	}
	public boolean contains(Vector p) {
		return Math.abs(p.dot(normal()))<0.0001;
	}
	public void setNormalSide(Vector v) {
		if (!onNormalSide(v)) {
			normalConstant *= -1;
		}
	}
	public Vector getPosition() {
		return getPoint();
	}
	public boolean onNormalSide(Vector p) {
		Vector v = p.subtract(point);
		return v.dot(normal())>=0;
	}
	public double distance(Vector p) {
		return normal().project(p.subtract(point)).norm();
	}
	public Vector collide(Line l) {
		if (l == null) {
			return null;
		}
		Vector normal = normal(distance(l.getPoint()));
		if (l.getDirection().dot(normal)==0 && !contains(l.getPoint())) { //parallel to the plane and not in it
			return null;
		} else if (l.getDirection().dot(normal())==0) { //parallel to the plane and contained in it
			if (map(l.getPoint())[0] > mins[0] && map(l.getPoint())[0] < maxes[0] && map(l.getPoint())[1] > mins[1] && map(l.getPoint())[1] < maxes[1]) {
				return l.getPoint();
			} else {
				return null;
			}
		}
		if (onNormalSide(l.getPoint())) {
			normal = normal.multiply(-1);
		}
		Vector dir = l.getDirection();
		if (dir.dot(normal)<0) {
			dir = dir.multiply(-1);
		}
		double dirLen = (dir.norm()*normal.normSq())/(dir.dot(normal));
		Vector newDir = dir.normalize(dirLen);
		Vector collision = newDir.add(l.getPoint());
		// if (collision.subtract(l.getPoint()).dot(l.getDirection()) <= 0) {
		// 	return null;
		// }
		if (map(collision)[0] > mins[0] && map(collision)[0] < maxes[0] && map(collision)[1] > mins[1] && map(collision)[1] < maxes[1]) {
			return collision;
		} else {
			return null;
		}
	}
	public Color getColor(Vector v) {
		// System.out.println(map(v)[0] + " " + map(v)[1] + " " + (map(v)[0] - mins[0])/(maxes[0]-mins[0]) + " " + (map(v)[1] - mins[1])/(maxes[1]-mins[1]));
		return texture.get((map(v)[0] - mins[0])/(maxes[0]-mins[0]), (map(v)[1] - mins[1])/(maxes[1]-mins[1]));
		// return color0;
		/*
		// v = <a,b,c>
		//dir1 = <x1,y1,z1>
		//dir2 = <x2,y2,z2>
		//a = m*x1 + n*x2
		//b = m*y1 + n*y2
		//a*y1-b*x1 = n*x2*y1 - n*y2*x1
				//c = m*z1 + n*z2
		*/
		// if ((Math.floor(mapping[0].dot(v)) + Math.floor(mapping[1].dot(v))) % 2 == 0) {
		// 	return color0;
		// } else {
		// 	return color1;
		// }
	}
	private double[] map(Vector v) {
		double way0 = mapping[0].dot(v)/(mapping[0].normSq());
		double way1 = -mapping[1].dot(v)/(mapping[1].normSq());
		double way2 = normal().dot(v)/(normal().normSq());
		double[] arr = {way0, way1, way2};
		return arr;
	}
	public boolean equals(Object o) {
		if (!(o instanceof Plane)) {
			return false;
		}
		Plane p = (Plane) o;
		return p.getDir1().equals(dir1) && p.getDir2().equals(dir2) && point.equals(p.getPoint());
	}
	public Vector project(Vector v) {
		Vector normalToV = normal().project(v.subtract(point));
		Vector result = v.subtract(normalToV);
		return result;
	}
	
}