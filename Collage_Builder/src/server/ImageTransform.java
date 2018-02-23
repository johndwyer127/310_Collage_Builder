package server;
import java.awt.image.BufferedImage;
import java.awt.Image;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Color;
import java.awt.BasicStroke;
import java.util.*;
import javax.imageio.ImageIO;
import java.awt.geom.AffineTransform;

public class ImageTransform {
	private String topic;
	private List<BufferedImage> retrievedImages;
	private BufferedImage completeImage;

	private static final int IMAGE_WIDTH = 56;	// px
	private static final int IMAGE_HEIGHT = 30;	// px

	public ImageTransform(String t) {
		this.topic = t;
	}

	public BufferedImage createCollageImage() {
		return null;
	}
	
	private List<BufferedImage> fetchImages() {
		return null;
	}

	// scaling each image to 1/20th of COLLAGE_SIZE, taken from: https://stackoverflow.com/questions/9417356/bufferedimage-resize
	private void resizeImages() {
		for(BufferedImage image : retrievedImages) {
			Image tmp = image.getScaledInstance(IMAGE_WIDTH, IMAGE_HEIGHT, Image.SCALE_SMOOTH);
			image = new BufferedImage(IMAGE_WIDTH, IMAGE_HEIGHT, BufferedImage.TYPE_INT_ARGB);
			
			Graphics2D g2d = image.createGraphics();
			g2d.drawImage(tmp, 0, 0, null);
			g2d.dispose();
		}		
	}

	// rotating images within IMAGE_ROTATION_LIMIT
	// TODO: -- unsure if this will work at the moment
	private void rotateImages() {
		AffineTransform imageRotator = new AffineTransform();
		for(BufferedImage image : retrievedImages) {
			imageRotator.rotate(generateRotationAmount());
			imageRotator.translate(-image.getWidth()/2, -image.getHeight()/2);
			Graphics2D g2d = image.createGraphics();
			g2d.drawImage(image, 0, 0, null);
			g2d.dispose();
		}
	}

	// generate random rotation amount for an image in radians within IMAGE_ROTATION_LIMIT
	// where -45 degrees <= IMAGE_ROTATION_LIMIT <= 45 degrees 
	private double generateRotationAmount() {
		Random rand = new Random();
		double angle = rand.nextFloat()*2*Math.PI;
		angle /= 8;
		if(rand.nextBoolean()) {
			return angle*-1;
		}
		return angle;
	}

	// add 3px white frame around each image
	private void borderImages() {
		for(BufferedImage image : retrievedImages) {
			Graphics2D g2d = image.createGraphics();
			int height = image.getHeight();
			int width = image.getWidth();
			int borderWidth = 3;
			int borderControl = 1;
			g2d.setColor(Color.WHITE);
			g2d.setStroke(new BasicStroke(borderWidth));
			g2d.drawLine(0, 0, 0, height);
			g2d.drawLine(0, 0, width, 0);
			g2d.drawLine(0, height - borderControl, width, height - borderControl);
			g2d.drawLine(width - borderControl, height - borderControl, width - borderControl, 0);
			g2d.drawImage(image, 0, 0, null);
			g2d.dispose();
		}	
	}

	private void combineImages() {

	}

	public List<BufferedImage> getRetrievedImages() {
		return this.retrievedImages;
	}

	public BufferedImage getCompleteImage() {
		return this.completeImage;	
	}

	// for testing purposes
	public static void main(String[] args) {
		System.out.println("it compiles");
	}	
}
