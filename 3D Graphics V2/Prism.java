import java.util.*;
import java.lang.*;
import java.awt.*;
import java.io.*;
public class Prism extends Polyhedron {
	public Prism() {
		this(new Vector(0,0,0), new Vector(1,0,0), new Vector(0,1,0), new Vector(0,0,1));
	}
	public Prism(Color c, Vector point, Plane p1, Plane p2, Plane p3, Plane p4, Plane p5, Plane p6) {
		super(c,point,p1,p2,p3,p4,p5,p6);
	}
	public Prism(Vector corner, Vector s1, Vector s2, Vector s3) {
		/*
		Plane p1 = new Plane(corner, corner.add(s1), corner.add(s2));
		Plane p2 = new Plane(corner, corner.add(s1), corner.add(s3));
		Plane p3 = new Plane(corner, corner.add(s2), corner.add(s3));
		Plane p4 = new Plane(corner.add(s1.add(s2).add(s3)), corner.add(s1.add(s2).add(s3)).subtract(s1), corner.add(s1.add(s2).add(s3)).subtract(s2));
		Plane p5 = new Plane(corner.add(s1.add(s2).add(s3)), corner.add(s1.add(s2).add(s3)).subtract(s1), corner.add(s1.add(s2).add(s3)).subtract(s3));
		Plane p6 = new Plane(corner.add(s1.add(s2).add(s3)), corner.add(s1.add(s2).add(s3)).subtract(s2), corner.add(s1.add(s2).add(s3)).subtract(s3));
		*/
		this(default_color, corner.add(s1.multiply(0.5)).add(s2.multiply(0.5)).add(s3.multiply(0.5)), new Plane(corner, corner.add(s1), corner.add(s2)), new Plane(corner, corner.add(s1), corner.add(s3)),
										 new Plane(corner, corner.add(s2), corner.add(s3)), new Plane(corner.add(s1.add(s2).add(s3)),
										 corner.add(s1.add(s2).add(s3)).subtract(s1), corner.add(s1.add(s2).add(s3)).subtract(s2)),
										 new Plane(corner.add(s1.add(s2).add(s3)), corner.add(s1.add(s2).add(s3)).subtract(s1), corner.add(s1.add(s2).add(s3)).subtract(s3)),
										 new Plane(corner.add(s1.add(s2).add(s3)), corner.add(s1.add(s2).add(s3)).subtract(s2), corner.add(s1.add(s2).add(s3)).subtract(s3)));
	}
}