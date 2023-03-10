import java.util.*;
import java.lang.*;
import java.io.*;
public class Vector implements Cloneable { //3D Vector
	private double[] arr;
	private int size;
	public Vector() {
		this(0,0,0);
	}
	private Vector(int size) {
		this.arr = new double[size];
		this.size = size;
	}
	public Vector(double x, double y, double z) {
		this.arr = new double[3];
		arr[0] = x;
		arr[1] = y;
		arr[2] = z;
		size = 3;
	}
	public int size() {
		return size;
	}
	public void set(int i, double d) {
		this.arr[i] = d;
	}
	public double get(int i) {
		return arr[i];
	}
	public double normSq() {
		return this.dot(this);
	}
	public double norm() {
		return Math.sqrt(normSq());
	}
	public double magnitude() {
		return norm();
	}
	public double distance() {
		return norm();
	}
	public double distance(Vector o) {
		return subtract(o).norm();
	}
	public Vector add(Vector o) {
		if (!sameLength(o)) {
			throw new IllegalArgumentException("Dimensions of two vectors do not match while attempting to add them together");
		}
		Vector v = new Vector(size());
		for (int i = 0;i<size();i++) {
			v.set(i, this.get(i)+o.get(i));
		}
		return v;
	}
	public Vector subtract(Vector o) {
		return add(o.multiply(-1));
	}
	public Vector multiply(double n) {
		Vector v = new Vector(size());
		for (int i = 0;i<size();i++) {
			v.set(i, this.get(i)*n);
		}
		return v;
	}
	public Vector reflect(Vector v) { //reflects v over this vector
		return project(v).multiply(2).subtract(v);
	}
	public Vector normalize() {
		if (norm()==0) {
			return (Vector)clone();
		} else {
			return divide(norm());
		}
	}
	public Vector normalize(double newNorm) {
		return normalize().multiply(newNorm);
	}
	public Vector divide(double n) {
		return multiply(1/n);
	}
	public Vector project(Vector v) { //projects v onto this vector
		return multiply(dot(v)/(normSq()));
	}
	public Vector cross(Vector v) {
		if (!(v.size() == 3 && size()==3)) {
			throw new IllegalArgumentException("Dimensions of two vectors are not 3 while attempting to take their cross product");
		}
		Vector ret = new Vector(get(1)*v.get(2)-get(2)*v.get(1), get(2)*v.get(0)-get(0)*v.get(2), get(0)*v.get(1)-get(1)*v.get(0));
		return ret;
	}
	public double dot(Vector v) {
		if (!sameLength(v)) {
			throw new IllegalArgumentException("Dimensions of two vectors do not match while attempting to take their dot product");
		}
		double sum = 0;
		if (size == 0) {
			return 0;
		}
		for (int i = 0;i<size;i++) {
			sum += this.get(i)*v.get(i);
		}
		return sum;
	}
	public boolean sameLength(Vector v) {
		return this.size() == v.size();
	}
	public double[] getArray() {
		return arr;
	}
	public Object clone() {
		try {
			return super.clone();
		} catch (Throwable t) {
			return null;
		}
	}
	public double cos(Vector other) {
		return cosAngle(other);
	}
	public double cosAngle(Vector other) {
		return this.dot(other) / (norm()*other.norm());
	}
	public double angle(Vector other) {
		return Math.acos(cosAngle(other));
	}
	public static Vector average(Vector[] arr) {
		Vector sum = new Vector();
		for (int i = 0;i<arr.length;i++) {
			sum = sum.add(arr[i]);
		}
		sum = sum.multiply(1.0/arr.length);
		return sum;
	}
	public static double[] linearCombination(Vector dir1, Vector dir2, Vector v) { //make v from a linear combination of dir1 and dir2, return an array with the coefficients of dir1 and dir2 respectively in the linear combination
		Vector row1 = dir1.cross(dir2).cross(dir2);
		Vector row2 = dir1.cross(dir2).cross(dir1);
		row1 = row1.divide(row1.dot(dir1));
		row2 = row2.divide(row2.dot(dir2));
		double i = row1.dot(v);
		double j = row2.dot(v);
		double[] d =  {i,j};
		if (!dir1.multiply(i).add(dir2.multiply(j)).equals(v)) {
			System.out.println("NO LINEAR COMBO");
			return null;
		}
		return d;
		/*
		//<dir1.dot(v), dir2.dot(v)>
		double n1 = (v.get(0)*dir1.get(1)+v.get(1)*dir1.get(0))/(dir2.get(0)*dir1.get(1)-dir2.get(1)*dir1.get(0));
		double n2 = (v.get(1)*dir1.get(2)+v.get(2)*dir1.get(1))/(dir2.get(1)*dir1.get(2)-dir2.get(2)*dir1.get(1));
		double n3 = (v.get(0)*dir1.get(2)+v.get(2)*dir1.get(0))/(dir2.get(0)*dir1.get(2)-dir2.get(2)*dir1.get(0));
		double n;
		if (!finite(n1) && !finite(n2)) {
			n = n3;
		} else if (!finite(n1)) {
			n = n2;
		} else {
			n = n1;
		}
		double m1 = (v.get(1)-n*dir2.get(1))/dir1.get(1);
		double m2 = (v.get(2)-n*dir2.get(2))/dir1.get(2);
		double m3 = (v.get(0)-n*dir2.get(0))/dir1.get(0);
		double m;
		if (!finite(m1) && !finite(m2)) {
			m = m3;
		} else if (!finite(n1)) {
			m = m2;
		} else {
			m = m1;
		}
		double[] result = {m,n};
		System.out.println(Arrays.toString(result));
		if (v.get(2) == m*dir1.get(2)+n*dir2.get(2)) {
			return result;
		} else {
			System.out.println("linearCombo null");
			return null;
		}
		*/
	}
	public static boolean inOrder(Vector a, Vector b, Vector c) {
		//a-b is from b to a
		boolean BCsame = sameDirection(a.subtract(b), a.subtract(c));
		boolean ABsame = sameDirection(c.subtract(b), c.subtract(a));
		return BCsame && ABsame;
	}
	public static boolean sameDirection(Vector a, Vector b) {
		return a.dot(b) >= 0;
	}
	public static boolean finite(double d) {
		return !Double.isInfinite(d) && !Double.isInfinite(-d) && !Double.isNaN(d);
	}
	public boolean equals(Object o) {
		if (o instanceof Vector) {
			Vector v = (Vector) o;
			if (size() != v.size()) {
				return false;
			}
			for (int i = 0;i<size();i++) {
				if (Math.abs(get(i) - v.get(i)) > 0.0000000001) {
					return false;
				}
			}
			return true;
		}
		return false;
	}
	public String toString() {
		String s = "<";
		for (int i = 0;i<size;i++) {
			if (i == 0) {
				s += get(i);
			} else {
				s += ", "+get(i);
			}
		}
		return s+">";
	}
}