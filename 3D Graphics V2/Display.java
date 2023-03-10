import java.util.*;
import java.lang.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.io.*;
import java.awt.image.*;
import javax.imageio.*;
import java.util.concurrent.*;
public class Display extends JPanel {
	private String name;
	public JFrame frame;
	private Color[][] pixels;
	private int scale;
	private int frame_w;
	private int frame_h;
	private int screen_w;
	private int screen_h;
	private String saveFolder;
	private final Color default_color = Color.WHITE;
	/*
	public static void main(String[] args) {
		Display d = new Display();
		Color[][] arr = {{Color.BLUE, Color.ORANGE},
						 {Color.RED, Color.GREEN}};
		d.paint(arr);
	}
	*/
	public Display() {
		this(500,500,"output");
	}
	public Display(int screen_w, int screen_h, String saveFolder) {
		super();
		this.name = "Display";
		try {
	        System.setProperty("apple.laf.useScreenMenuBar", "true");
	        System.setProperty("com.apple.mrj.application.apple.menu.about.name", name);
	        UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
	    } catch(Throwable t) {
	        System.out.println("ERROR: "+t);
	    }
	    this.scale = 10;
	    this.saveFolder = saveFolder;
	    this.screen_w = screen_w;
	    this.screen_h = screen_h;
	    setup();
	}
	private void setup() {
		frame = new JFrame(name);
        frame.setFocusable(true);
        frame.requestFocusInWindow();
        frame.setPreferredSize(new Dimension(screen_w,screen_h));
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        //menu bar
        {
        	JMenuBar menuBar = new JMenuBar();
            JMenu menu = new JMenu("Options");
            menuBar.add(menu);
        	frame.setJMenuBar(menuBar);
        }
        frame.getContentPane().add(this);
        frame.pack();
        frame_w = frame.getContentPane().getWidth();
        frame_h = frame.getContentPane().getHeight();
        frame.setLocationByPlatform(true);
        frame.setVisible(true);
        File[] files = new File(saveFolder).listFiles();
	    if(files!=null) { //delete all files in saveFolder
	        for(File f : files) {
                f.delete();
	        }
	    }
	}
	public void paintComponent(Graphics g) {
		if (pixels == null) {
			return;
		}
		setScale();
		for (int r = 0;r<pixels.length;r++) {
			for (int c = 0;c<pixels[0].length;c++) {
				if (pixels[r][c] == null) {
					g.setColor(default_color);
				} else {
					g.setColor(pixels[r][c]);
				}
				g.fillRect((int) c*scale,(int)(pixels.length-r-1)*scale, scale, scale);
			}
		}
		g.setColor(Color.RED);
		// g.drawString("Sam Engel", pixels[0].length * scale - 100, 30);
	}
	private void setScale() {
		frame_w = frame.getWidth();
        frame_h = frame.getHeight();
        int scale_1 = (int)((frame_w-1)/((double)pixels[0].length)-1)+1;
        int scale_2 = (int)((frame_h-23)/((double)pixels.length)-1)+1;
        scale = Math.min(scale_1,scale_2);
	}
	public void paint(Color[][] pixels) {
		paint(pixels, null);
	}
	public void paint(Color[][] pixels, String saveFileName) {
		this.pixels = pixels;
		if (saveFolder != null && saveFileName != null) {
			int height = pixels.length;
			int width = pixels[0].length;
			BufferedImage img = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
			for (int y = 0; y < height; y++) {
				for (int x = 0; x < width; x++) {
					img.setRGB(x, height-y-1, pixels[y][x].getRGB());
				}
			}
			try {
	            File f = new File(saveFolder+"/"+saveFileName);
	            ImageIO.write(img, "png", f);
	        }
	        catch (IOException e) {
	            System.out.println(e);
	        }
		}
		repaint();
	}
	public static boolean sleep(long milliseconds) {
		try {
			TimeUnit.MILLISECONDS.sleep(milliseconds);
			return true;
		} catch(Throwable t) {
			System.out.println("ERROR: " +t);
			return false;
		}
	}
}