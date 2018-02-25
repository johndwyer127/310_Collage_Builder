
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import javax.imageio.IIOException;
import javax.imageio.ImageIO;


public class ImageTransform {
	private String topic;
	private List<BufferedImage> retrievedImages;
	private BufferedImage completeImage;

	private static final int COLLAGE_WIDTH = 1120;
	private static final int COLLAGE_HEIGHT = 600;
	private static final int COLLAGE_SIZE = COLLAGE_WIDTH * COLLAGE_HEIGHT; // total number of pixels
	private static final int SCALED_IMAGE_SIZE = COLLAGE_SIZE/20;
//	private static final String GOOGLE_SEARCH_API_KEY = "AIzaSyCQbxRMKMxuyaIVmosCa_k2sIv5BeavGFs";
	private static final String GOOGLE_SEARCH_API_KEY = "AIzaSyADYi8Ob0jmPJbGEMCkJwrB31bOY80RtXs";
//	private static final String GOOGLE_CX = "007628912923159165220:9e6kozm2iea";	// custom search engine identifier
	private static final String GOOGLE_CX = "008543189839369971484:b8selplq7z8";	// custom search engine identifier

	public ImageTransform(String t) {
		this.topic = t;
		this.retrievedImages = new ArrayList<BufferedImage>();
	}

	public BufferedImage createCollageImage() {
		this.fetchImages();
//		for(BufferedImage image : this.retrievedImages) {
//			System.out.println("initial height: " + image.getHeight() + ", initial width: " + image.getWidth());
//		}
//		for(BufferedImage image : this.retrievedImages) {
//			System.out.println("bordered height: " + image.getHeight() + ", bordered width: " + image.getWidth());
//		}
//		for(BufferedImage image : this.retrievedImages) {
//			System.out.println("rotated height: " + image.getHeight() + ", rotated width: " + image.getWidth());
//		}
		this.resizeImages();
//		for(BufferedImage image : this.retrievedImages) {
//			System.out.println("resized height: " + image.getHeight() + ", resized width: " + image.getWidth());
//		}
		this.borderImages();
//		this.rotateImages();
		int imageNum = 0;
		for(BufferedImage image : this.retrievedImages) {
			System.out.println("final height: " + image.getHeight() + ", final width: " + image.getWidth());
//			File outputFile = new File("output" + imageNum + ".png");
//			try {
//				ImageIO.write(image, "png", outputFile);
//			} catch (IOException e) {
//				e.printStackTrace();
//			}
//			imageNum++;
		}
		this.combineImages();
		return null;
	}

	// retrieves top 30 image search results from Google Custom Search API
	// see: https://stackoverflow.com/questions/10257276/java-code-for-using-google-custom-search-api
	private boolean fetchImages() {
		try {
			// maintain count of number of results fetched from API
			int resultNum = 0;
			// initially fetch 40 images in case of bad/undownloadable links
			for(int i = 0; i < 4; i++) {
				URL requestURL = generateRequestURL(resultNum);

				HttpURLConnection connection = (HttpURLConnection) requestURL.openConnection();
				connection.setRequestMethod("GET");
				BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));

				String output;
				System.out.println("Output from search: .... \n");

				// parse through JSON response for image links, line by line
				while((output = reader.readLine()) != null) {
					if(output.contains("\"link\": \"")) {
						String link = output.substring(output.indexOf("\"link\": \"") + ("\"link\": \"").length(), output.indexOf("\","));
						System.out.println(link);
						URL imageURL = new URL(link);
						try {
							BufferedImage resultImage = (BufferedImage) ImageIO.read(imageURL);
							this.retrievedImages.add(resultImage);
							System.out.println("addedImage: current size: " + this.retrievedImages.size());
						} catch(IIOException e) {
							System.out.println("bad link");
						}
					}
				}

				connection.disconnect();
				resultNum += 10;
			}

		} catch(IOException e) {
			System.out.println(e.getMessage());
			e.printStackTrace();
		}

		System.out.println("returning fetched images");
		return validateRetrievedImages();
	}

	private boolean validateRetrievedImages() {
		// verify there are no null images
		int numImages = this.retrievedImages.size();
		for(int i = 0; i < numImages; i++) {
			if(this.retrievedImages.get(i) == null) {
				this.retrievedImages.remove(i);
				i--;
				numImages--;
			}
		}

		// not enough images
		if(this.retrievedImages.size() < 30) {
			System.out.println("was unable to find enough pictures! current size: " + this.retrievedImages.size());
			this.retrievedImages.clear();
			return false;
		}

		else {
			// remove any extra images from the end of the list
			while(this.retrievedImages.size() > 30) {
				this.retrievedImages.remove(this.retrievedImages.size()-1);
			}
			System.out.println("got necessary images and trimmed list, current size: " + this.retrievedImages.size());
			return true;
		}
	}

	private URL generateRequestURL(int resultNumber) throws MalformedURLException {
		URL requestURL;
		if(resultNumber > 0) {
			requestURL = new URL("https://www.googleapis.com/customsearch/v1?key=" + GOOGLE_SEARCH_API_KEY + "&cx=" + GOOGLE_CX + "&q=" + this.topic + "&searchType=image&imgType=photo&imgSize=medium&start=" + resultNumber + "&num=10");
//			requestURL = new URL("https://www.googleapis.com/customsearch/v1?key=" + GOOGLE_SEARCH_API_KEY + "&cx=" + GOOGLE_CX + "&q=" + this.topic + "&searchType=image&start=" + resultNumber + "&num=10");
		}
		else {
			requestURL = new URL("https://www.googleapis.com/customsearch/v1?key=" + GOOGLE_SEARCH_API_KEY + "&cx=" + GOOGLE_CX + "&q=" + topic + "&searchType=image&imgType=photo&imgSize=medium&num=10");
//			requestURL = new URL("https://www.googleapis.com/customsearch/v1?key=" + GOOGLE_SEARCH_API_KEY + "&cx=" + GOOGLE_CX + "&q=" + topic + "&searchType=image&num=10");
		}

		return requestURL;
	}

	// scaling each image to roughly 1/20th of COLLAGE_SIZE, see: https://stackoverflow.com/questions/9417356/bufferedimage-resize
	private void resizeImages() {
		int numImages = this.retrievedImages.size();
		for(int i = 0; i < numImages; i++) {
			BufferedImage originalImage = this.retrievedImages.get(0);
			this.retrievedImages.remove(0);

			int originalWidth = originalImage.getWidth();
			int originalHeight = originalImage.getHeight();
			int originalImageSize = originalWidth * originalHeight;
			double scaleFactor = originalImageSize/SCALED_IMAGE_SIZE;

			if(scaleFactor == 0) {
				scaleFactor = 1;
			}

			scaleFactor = Math.sqrt(scaleFactor);

			double scaledWidth = (originalWidth/scaleFactor);
			double scaledHeight = (originalHeight/scaleFactor);

			Image tmp = originalImage.getScaledInstance((int)scaledWidth, (int)scaledHeight, Image.SCALE_SMOOTH);
			System.out.println("originalWidth: " + originalWidth + ", originalHeight: " + originalHeight + " , scaledWidth: " + scaledWidth + ", scaledHeight: " + scaledHeight);
			BufferedImage resizedImage = new BufferedImage((int)scaledWidth, (int)scaledHeight, BufferedImage.TYPE_INT_ARGB);

			Graphics2D g2d = resizedImage.createGraphics();
			g2d.drawImage(tmp, 0, 0, null);

			this.retrievedImages.add(resizedImage);

			g2d.dispose();
		}
	}

	// rotating images within IMAGE_ROTATION_LIMIT
	// TODO: -- unsure if this will work at the moment
	// taken from: https://stackoverflow.com/questions/4918482/rotating-bufferedimage-instances
	private void rotateImages() {
		AffineTransform imageRotator = new AffineTransform();
		int numImages = this.retrievedImages.size();
		for(int i = 0; i < numImages; i++) {
			BufferedImage originalImage = this.retrievedImages.get(0);
			this.retrievedImages.remove(0);
			imageRotator.rotate(generateRotationAmount());
			imageRotator.translate(-originalImage.getWidth()/2, -originalImage.getHeight()/2);

			Graphics2D g2d = originalImage.createGraphics();
			g2d.drawImage(originalImage, 0, 0, null);

			this.retrievedImages.add(originalImage);
			g2d.dispose();
		}
//		for(BufferedImage image : retrievedImages) {
//			imageRotator.rotate(generateRotationAmount());
//			imageRotator.translate(-image.getWidth()/2, -image.getHeight()/2);
//			Graphics2D g2d = image.createGraphics();
//			g2d.drawImage(image, 0, 0, null);
//			g2d.dispose();
//		}
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
		BufferedImage collageImage = new BufferedImage(COLLAGE_WIDTH, COLLAGE_HEIGHT, BufferedImage.TYPE_INT_ARGB);
		Graphics g = collageImage.getGraphics();
		int x = 0;
		int y = 0;
		int imageNum = 0;
		int rowHeight = 0;
		int colWidth = 0;
		Random rand = new Random();
		for(BufferedImage image : this.retrievedImages) {
			if(x == 0) {
				rowHeight = image.getHeight();
			}
			if(image.getHeight() < rowHeight) {
				rowHeight = image.getHeight();
			}
			if(rowHeight+image.getHeight() > COLLAGE_HEIGHT) {
				rowHeight = rand.nextInt((COLLAGE_HEIGHT-image.getHeight()) + 1);
			}
			if(colWidth + image.getWidth() > COLLAGE_WIDTH) {
				colWidth = rand.nextInt((COLLAGE_WIDTH-image.getWidth()) + 1);
			}
			g.drawImage(image, x, y, null);
			x += image.getWidth();
			if(x >= COLLAGE_WIDTH) {
				x = 0;
				y += rowHeight;
			}
			System.out.println("imageNum: " + imageNum + ", currY: " + y + ", currX: " + x);
			imageNum++;
		}
		try {
			ImageIO.write(collageImage,"png",new File(this.topic + "-collage.png"));
		} catch(IOException e) {
			e.getMessage();
		}

	}

	public List<BufferedImage> getRetrievedImages() {
		return this.retrievedImages;
	}

	public BufferedImage getCompleteImage() {
		return this.completeImage;
	}

	// for testing purposes
	public static void main(String[] args) {
		ImageTransform imageTransform = new ImageTransform("cow");
		imageTransform.createCollageImage();
	}
}
