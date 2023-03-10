import java.io.File;
import java.io.FileWriter;
import java.awt.Color;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import java.io.*;
public class Texture {
	public static Color backgroundColor = Color.WHITE;
	private Color[][] arr;
	private String filename;
	public Texture() {
		this("wallpaper.jpg");
	}
	public Texture(String filename) {
		loadImage(filename);
	}
	public void loadImage(String filename) {
		this.filename = filename;
		BufferedImage img = null;
		try {
		    img = ImageIO.read(new File(filename));
		    Color[][] arr = new Color[img.getHeight()][img.getWidth()];
			for (int y = 0; y < img.getHeight(); y++) {
				for (int x = 0; x < img.getWidth(); x++) {
					//Retrieving contents of a pixel
					int pixel = img.getRGB(x,y);
					//Creating a Color object from pixel value
					Color color = new Color(pixel, true);
					arr[y][x] = color;
				}
			}
			this.arr = arr;
		} catch (IOException e) {
			System.out.println("ERROR");
		}
	}
	public Color get(double x, double y) {
		if (x >= 0 && x < 1 && y >= 0 && y < 1) {
			return arr[(int) (arr.length * y)][(int) (arr[0].length * x)];
		} else {
			return backgroundColor;
		}
	}

}