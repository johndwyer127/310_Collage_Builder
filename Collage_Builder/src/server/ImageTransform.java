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
import java.net.HttpURLConnection;
import java.io.BufferedReader;
import java.net.URL;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.io.IOException;


public class ImageTransform {
	private String topic;
	private List<BufferedImage> retrievedImages;
	private BufferedImage completeImage;

	private static final int IMAGE_WIDTH = 56;	// px
	private static final int IMAGE_HEIGHT = 30;	// px
	private static final String GOOGLE_SEARCH_API_KEY = "AIzaSyCQbxRMKMxuyaIVmosCa_k2sIv5BeavGFs";
	private static final String GOOGLE_CX = "007628912923159165220:9e6kozm2iea";

	public ImageTransform(String t) {
		this.topic = t;
		this.retrievedImages = new ArrayList<BufferedImage>();
	}

	public BufferedImage createCollageImage() {
		return null;
	}
	
	// retrieves top 30 image search results from Google Custom Search API
	// used: https://stackoverflow.com/questions/10257276/java-code-for-using-google-custom-search-api
	private void fetchImages() {
		try {

			URL requestUrl = new URL("https://www.googleapis.com/customsearch/v1?key=" + GOOGLE_SEARCH_API_KEY + "&cx=" + GOOGLE_CX + "&q=" + topic + "&alt=json");
			HttpURLConnection connection = (HttpURLConnection) requestUrl.openConnection();
			connection.setRequestMethod("GET");
					BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
	
			String output;
			System.out.println("Output from search: .... \n");
			int imageNumber = 0;	
			while((output = reader.readLine()) != null && imageNumber < 30) {
				if(output.contains("\"link\": \"")) {
					String link = output.substring(output.indexOf("\"link\": \"") + ("\"link\": \"").length(), output.indexOf("\","));
					System.out.println(link);	// will print Google search links
					URL imageURL = new URL(link);
					BufferedImage resultImage = (BufferedImage) ImageIO.read(imageURL);
					this.retrievedImages.add(resultImage);
				}
			}	
			connection.disconnect();
		} catch(IOException e) {
			System.out.println(e.getMessage());
			e.printStackTrace();
		} 
			
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
	// taken from: https://stackoverflow.com/questions/4918482/rotating-bufferedimage-instances
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
	// taken from: https://stackoverflow.com/questions/4219511/draw-rectangle-border-thickness
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

	// TODO: write method to generate collage from the retrieved bufferedImages
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
