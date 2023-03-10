public class Camera {
	private Vector position;
	private Vector[] viewCorners; //{topleft, topright, bottomleft, bottomright}
	public Camera() {
		this(new Vector(0,0,10), new Vector(-5,5,0), new Vector(5,5,0), new Vector(-5,-5,0), new Vector(5,-5,0));
	}
	public Camera(Vector position, Vector topLeft, Vector topRight, Vector bottomLeft, Vector bottomRight) {
		this.position = position;
		this.viewCorners = new Vector[4];
		this.viewCorners[0] = topLeft;
		this.viewCorners[1] = topRight;
		this.viewCorners[2] = bottomLeft;
		this.viewCorners[3] = bottomRight;
		System.out.println("Up and down angle: "+topLeft.subtract(position).angle(bottomLeft.subtract(position))*180/3.141593);
		System.out.println("Right and left angle: "+topLeft.subtract(position).angle(topRight.subtract(position))*180/3.141593);
	}
	public Vector[] viewCorners() {
		return viewCorners;
	}
	public Vector getPosition() {
		return position;
	}
	public Vector getDirection() {
		return Vector.average(viewCorners).subtract(position);
	}
	public void setPosition(Vector position) {
		this.position = position;
	}
	public void setDirection(Vector direction) {
		;
	}
	// public double mass() {
	// 	return 0;
	// }
	// public double volume() {
	// 	return 0;
	// }
	// public Vector collide(Line l) {
	// 	return null;
	// }
	// public Color getColor(Vector position) {
	// 	return null;
	// }
	// public Vector getNormal(Vector position);
	// public Vector getPosition();

}