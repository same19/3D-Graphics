import java.awt.*;
public class Light extends Sphere {
	private double intensity;
	public Light() {
		super();
		this.intensity = 1000;
	}
	public Light(Vector position, double intensity) {
		super(position, 0.3, Color.WHITE);
		this.intensity = intensity;
	}
	public Vector collide(Line l) {
		return null;
	}
	public double getIntensity() {
		return intensity;
	}
}