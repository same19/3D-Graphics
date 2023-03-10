import java.util.*;
import java.lang.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.io.*;
import java.awt.image.*;
import javax.imageio.*;
import java.util.concurrent.*;
public class Engine3D implements KeyListener {
	public class RayTraceHit {
		public Obj3D hit;
		public Vector position;
		public Color color;
		public RayTraceHit(){
			hit = null;
			position = null;
			color = background_color;
		}
		public RayTraceHit(Vector position, Obj3D hit, Color color) {
			this.position = position;
			this.hit = hit;
			this.color = color;
		}
	}
	private double ambientLight = 4;
	private int reflectRecurseNumber = 2;
	private double planckLength = 0.01;
	private boolean shading;
	private ArrayList<Obj3D> objects;
	private Camera camera;
	private Thread[] threads;
	public static boolean run = true;
	//private Vector[] cameraViewCorners; //{topleft, topright, bottomleft, bottomright}
	private Color[][] screen;
	private int screen_w; //in pixels
	private int screen_h;
	private Color background_color;
	public static void main(String[] args) {
		// Line l1 = new Line(new Vector(), new Vector(1,0,0));
		// Line l2 = new Line(new Vector(0,-1,0), new Vector(1,1,0));
		// System.out.println(l1.intersect(l2));
		Plane plane1 = new Plane();//new Plane(new Vector(0,0,0), new Vector(1,0,0), new Vector(0,2,1)); //xy plane
		Engine3D g = new Engine3D();
		Display d = new Display(g.getScreenWidth(), g.getScreenHeight(), "output");
		d.frame.addKeyListener(g);
		// Sphere sphere1 = new Sphere(new Vector(-2,-0.5,1),1,Color.GREEN);
		Sphere sphere = new Sphere(new Vector(0,0,4), 1, Color.GRAY);
		// Prism p = new Prism(new Vector(-2,-2,0), new Vector(1,0,0), new Vector(1,-1,0), new Vector(0,0.5,1));
		Light l = new Light(new Vector(0,3,10), 4000);
		// for (int i = -2; i < 2; i++) {
		// 	for (int j = -2; j < 2; j++) {
		// 		g.add(new Sphere(new Vector(2.2*i,2.2*2,1), 1, Color.RED));
		// 		g.add(new Sphere(new Vector(2.2*i,2.2*1,1), 1, Color.GREEN));
		// 		g.add(new Sphere(new Vector(2.2*i,-2.2*0,1), 1, Color.GRAY));
		// 		g.add(new Sphere(new Vector(2.2*i,-2.2*1,1), 1, Color.BLUE));
		// 	}
		// }
		g.add(plane1);
		g.add(sphere);
		g.add(l);
		double sumFrame = 0;
		double sumPaint = 0;
		double deltaTime = 0;
			double FPS = 24.0;
			int times = (int) (FPS * 10.0);
		double acceleration = -3;
		double velocity = 0;
		g.setup();
		g.generate();
		g.stopThreads();
		d.paint(g.getScreen());
		long startTime = System.nanoTime();
		long lastTime = startTime;
		for (int i =0;i<times;i++) {
			while (!run) {
				try {
					TimeUnit.MILLISECONDS.sleep(1);
				} catch(Throwable t) {
					;
				}
			}
			g.setup();
			g.generate();
			g.stopThreads();
			d.paint(g.getScreen(), "image"+i+".png");
			// deltaTime = 0.000000001 * (System.nanoTime()-lastTime);
			deltaTime = 1/FPS;
			if (sphere.getPosition().get(2) <= sphere.getRadius() && velocity < 0) {
				// Vector resetPosition = new Vector(sphere2.getPosition().get(0), sphere2.getPosition().get(1), sphere2.getRadius());
				// sphere2.setPosition(resetPosition);
				velocity *= -.96;
			} else {
				velocity += deltaTime * acceleration;
			}
			sphere.move(new Vector(0,0,1).multiply(deltaTime * velocity));
			// camera.setPosition(camera.getPosition().add(new Vector(0,4,0).multiply(0.05)));
			lastTime = System.nanoTime();
		}
		long endTime = System.nanoTime();
		double duration = (endTime - startTime)/1000000.0;
		System.out.println(1000*times/duration+" FPS");
	}
	public Engine3D() {
		objects = new ArrayList<Obj3D>();
		// System.out.println(camera.getDirection());
		shading = true;
		screen_w = 1600;
		screen_h = 900;
		setupCamera();
		screen = new Color[screen_h][screen_w];
		background_color = Color.BLACK;//new Color(135, 206, 235);
	}
	public void add(Obj3D obj) {
		objects.add(obj);
	}
	public void remove(Obj3D obj) {
		objects.remove(obj);
	}
	public Camera getCamera() {
		return camera;
	}
	public void setCamera(Camera camera) {
		this.camera = camera;
	}
	private void setupCamera() {
		double width = 500;
		double height = (screen_h / ((double)screen_w)) * width;
		camera = new Camera(new Vector(0,-10,5), new Vector(-5 * (width / 500.0),1.7,5 * (height / 500.0)), new Vector(5 * (width / 500.0),1.7,5 * (height / 500.0)), new Vector(-5 * (width / 500.0),0,-5 * (height / 500.0)), new Vector(5 * (width / 500.0),0,-5 * (height / 500.0)));
	}
	public void setup() {
		final int chunkSize = screen_h;
		threads = new Thread[screen_h/chunkSize];
		//new Thread() { public void run() {
		for (int r0 = 0;r0<screen_h;r0 += chunkSize) {
			final int r = r0;
			threads[r0/chunkSize] = new Thread() { public void run() {
					for (int r1 = r;r1<r+chunkSize && r1<screen_h;r1++) {
						for (int c1 = 0;c1<screen_w;c1++) {
							final int c = c1;
							// threads[r1][c] = new Thread() { public void run() {
								Line l = createLine(c/((double)screen_w-1),r1/((double)screen_h-1));
								RayTraceHit collision = rayTrace(l);
								shade(collision); //shade separate from rayTrace
								if (collision.color != null) {
									screen[r1][c] = collision.color;
								} else {
									screen[r1][c] = background_color;
								}
							// }};
							// threads[r1][c].start();
						}
					}
			}};
		}
		
		//}}.start();
	}
	public void generate() {
		for (int r1 = 0;r1<threads.length;r1++) {
			threads[r1].start();
		}
	}
	public void stopThreads() {
		try {
			for (int r1 = 0;r1<threads.length;r1++) {
				threads[r1].join();
			}
		}
		catch (Exception e) {
			System.out.println("ERROR");
		}
	}
	private Line createLine(double x, double y) { //x is percentage (range 0 - 1 inclusive) from left of cameraView, y is percentage from bottom of cameraView
		Vector start = camera.getPosition();
		Vector top = camera.viewCorners()[0].multiply(1-x).add(camera.viewCorners()[1].multiply(x));
		Vector bottom = camera.viewCorners()[2].multiply(1-x).add(camera.viewCorners()[3].multiply(x));
		Vector end = bottom.multiply(1-y).add(top.multiply(y));
		//System.out.println("END: "+end);
		return new Line(start, end);
	}
	public static int minIndex(double[] d) {
		if (d.length==0) {
			return -1;
		}
		int index = 0;
		for (int i = 1;i<d.length;i++) {
			if (d[i]<d[index]) {
				index = i;
			}
		}
		return index;
	}
	public static int maxIndex(double[] d) {
		if (d.length==0) {
			return -1;
		}
		int index = 0;
		for (int i = 1;i<d.length;i++) {
			if (d[i]>d[index]) {
				index = i;
			}
		}
		return index;
	}
	public Color[][] getScreen() {
		return screen;
	}
	public int getScreenWidth() {
		return screen_w;
	}
	public int getScreenHeight() {
		return screen_h;
	}
	public static double timeMethod(LambdaMethod lm) { //in milliseconds
		long startTime = System.nanoTime();
		lm.run();
		long endTime = System.nanoTime();
		return (endTime - startTime)/1000000.0;
	}
	private void setRayTraceHitColor(RayTraceHit r) {
		r.color = r.hit.getColor(r.position);
	}
	public RayTraceHit rayTrace(Line l) {
		Vector from = l.getPoint();
		RayTraceHit r = null;
		Vector collisionPos;
		for (int i = 0;i<objects.size();i++) {
			collisionPos = objects.get(i).collide(l);
			if (collisionPos == null || collisionPos.distance(l.getPoint()) < planckLength) {
				continue;
			}
			if (l.getDirection().cos(collisionPos.subtract(from)) >= 0 && (r == null || collisionPos.distance(from) < r.position.distance(from))) {
				r = new RayTraceHit(collisionPos, objects.get(i), null);
			}
		}

		if (r == null) {
			// System.out.println("NULL, not setting color");
			// System.out.println("NOT SETTING COLOR");
			return new RayTraceHit();
		} else {
			// System.out.println("SETTING COLOR");
			setRayTraceHitColor(r);
		}
		return r;
	}
	public RayTraceHit shade(RayTraceHit r) {
		return shade(r, new RayTraceHit(camera.getPosition(), null, null), 0, reflectRecurseNumber);
	}
	public RayTraceHit shade(RayTraceHit r, RayTraceHit origin, int recurseNum, int maxRecurseNum) {
		if (r.position == null) {
			return r;
		}
		if (recurseNum >= maxRecurseNum) {
			return r;
		}
		// System.out.println(r.position.distance(r.hit.getPosition()) - 1);
		double intensity = ambientLight;
		double lightSourceIntensity = 0;
		Light lightSource = getLightSource();
		if (lightSource == null) {
			return r;
		}
		// System.out.println(recurseNum);
		// System.out.println(lightSource.getPosition());
		// System.out.println(r.position);
		// System.out.println(r);
		Line fromLight = new Line(lightSource.getPosition(), r.position);
		Line toLight = new Line(r.position, lightSource.getPosition());
		RayTraceHit collision = rayTrace(fromLight);
		Vector normal = r.hit.getNormal(r.position);
		if (collision.hit == r.hit && normal.dot(toLight.getDirection()) >= 0) {
			lightSourceIntensity = lightSource.getIntensity() * 1/(Math.pow(lightSource.getPosition().distance(r.position),2)) * normal.cos(toLight.getDirection());
		}

		intensity += lightSourceIntensity;
		intensity = sigma(intensity);
		// r.color = new Color((int) (intensity * r.color.getRed()), (int) (intensity * r.color.getGreen()), (int) (intensity * r.color.getBlue()));
		// return r;
		double gamma = r.hit.getReflectivity();
		// double gamma = 0;
		Line incidence = new Line(r.position, origin.position);
		Line reflect = new Line(r.position, r.position.add(normal.reflect(incidence.getDirection())));
		RayTraceHit reflectHit = rayTrace(reflect);
		if (reflectHit != null && reflectHit.position != null) {
			shade(reflectHit, r, recurseNum+1, maxRecurseNum);
		} else {
			reflectHit.color = background_color;//new Color(135, 206, 235);//Color.LIGHT_BLUE;
		}

		// intensity = 1;
		int red = (int) (intensity * (r.color.getRed() + gamma * reflectHit.color.getRed()));
		if (red > 255) {
			red = 255;
		}
		int green = (int) (intensity * (r.color.getGreen() + gamma * reflectHit.color.getGreen()));
		if (green > 255) {
			green = 255;
		}
		int blue = (int) (intensity * (r.color.getBlue() + gamma * reflectHit.color.getBlue()));
		if (blue > 255) {
			blue = 255;
		}
		r.color = new Color(red, green, blue);

		// r.color = Color.YELLOW;
		// return;
		return r;
		
		
	}
	private double sigma(double n) {
		double k = 4;
		return Math.pow(n/(n+1),k);
	}
	// public static RayTraceHit rayTrace(Line l, Engine3D engine) {
	// 	return engine.rayTrace(l);
	// }
	public Light getLightSource() {
		Light light = null;
		for (int i = 0;i<objects.size();i++) {
			if (objects.get(i) instanceof Light) {
				//toLight.add(new Line(camera.position, ((Light)objects.get(i)).getPosition()));
				light = (Light)objects.get(i); //from hit to light
				return light;
			}
		}
		return light;
	}
	public void keyPressed(KeyEvent e) {
		if (e.getKeyCode()==KeyEvent.VK_SPACE) {
			run = !run;
		}
	}
	public void keyTyped(KeyEvent e) {

	}
	public void keyReleased(KeyEvent e) {

	}

}
interface LambdaMethod {
	public void run();
}