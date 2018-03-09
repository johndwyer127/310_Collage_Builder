package server;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;
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
	private static final int TOTAL_COMBINED_AREA = SCALED_IMAGE_SIZE*30;
	private static final int FINAL_FOREGROUND_IMAGE_SIZE = (TOTAL_COMBINED_AREA-COLLAGE_SIZE)/29;
//	private static final String GOOGLE_SEARCH_API_KEY = "AIzaSyCQbxRMKMxuyaIVmosCa_k2sIv5BeavGFs";
//	private static final String GOOGLE_SEARCH_API_KEY = "AIzaSyADYi8Ob0jmPJbGEMCkJwrB31bOY80RtXs";
//	private static final String GOOGLE_SEARCH_API_KEY = "AIzaSyAh8tNso-_G-0h5DCft0JibbpPyLYhIPvE";
	private static final String GOOGLE_SEARCH_API_KEY = "AIzaSyB7_yytK04B7speZc4lXsHLr9ARmwPiUzw";
//	private static final String GOOGLE_CX = "007628912923159165220:9e6kozm2iea";	// custom search engine identifier
//	private static final String GOOGLE_CX = "008543189839369971484:b8selplq7z8";	// custom search engine identifier
//	private static final String GOOGLE_CX = "003847142776988134030:4xa5elxfuke";	// custom search engine identifier
	private static final String GOOGLE_CX = "015527610641952349258:lx1x9pjo0ec";


	public ImageTransform(String t) {
		this.topic = t;
		this.retrievedImages = new ArrayList<BufferedImage>();
	}

	public BufferedImage createCollageImage() {
		if(this.fetchImages()) {
			this.resizeImages();
			return this.combineImages();
		}

		return this.generateInsufficientNumberImage();
	}

	// retrieves top 30 image search results from Google Custom Search API
	// see: https://stackoverflow.com/questions/10257276/java-code-for-using-google-custom-search-api
	public boolean fetchImages() {
		
		try {
			// maintain count of number of results fetched from API
			int resultNum = 0;
			// initially fetch 40 images in case of bad/undownloadable links
			for(int i = 0; i < 4; i++) {
				URL requestURL = generateRequestURL(resultNum, "");

				HttpURLConnection connection = getConnectionFromRequestURL(requestURL);
				BufferedReader reader = generateBufferedReaderFromInputStream(connection);
				String output;

				// parse through JSON response for image links, line by line
				while((output = reader.readLine()) != null) {
					if(output.contains("\"link\": \"")) {
						String link = output.substring(output.indexOf("\"link\": \"") + ("\"link\": \"").length(), output.indexOf("\","));
						URL imageURL = new URL(link);
						try {
							BufferedImage resultImage = generateBufferedImageFromURL(imageURL);
							this.retrievedImages.add(resultImage);
						} catch(IIOException e) {
							System.out.println("Bad link/unable to generate buffered image from a url!");
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

		return validateRetrievedImages();
	}
	
	// for testing
	public HttpURLConnection getConnectionFromRequestURL(URL requestURL) throws IOException {
		HttpURLConnection connection = (HttpURLConnection) requestURL.openConnection();
		connection.setRequestMethod("GET");
		return connection;
	}
	
	// for testing
	public BufferedReader generateBufferedReaderFromInputStream(HttpURLConnection connection) throws IOException {
		BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
		return reader;
	}
	
	// for testing
	public BufferedImage generateBufferedImageFromURL(URL imageURL) throws IOException {
		BufferedImage resultImage = (BufferedImage) ImageIO.read(imageURL);
		return resultImage;
	}

	public boolean validateRetrievedImages() {
		// verify there are no null images
		int numImages = this.retrievedImages.size();
		// remove any null images (links that blocked us)
		for(int i = 0; i < numImages; i++) {
			if(this.retrievedImages.get(i) == null) {
				this.retrievedImages.remove(i);
				i--;
				numImages--;
			}
		}

		// not enough images
		if(this.retrievedImages.size() < 30) {
			System.out.println("Was unable to find enough pictures! current size: " + this.retrievedImages.size());
			this.retrievedImages.clear();
			return false;
		}

		else {
			// remove any extra images from the end of the list
			while(this.retrievedImages.size() > 30) {
				this.retrievedImages.remove(this.retrievedImages.size()-1);
			}
			System.out.println("Retrieved necessary images and trimmed list, current size: " + this.retrievedImages.size());
			return true;
		}
	}

	// generate url to make request to our Google custom search engine
	public URL generateRequestURL(int resultNumber, String garbageString) throws MalformedURLException{
		URL requestURL = null;
		if(resultNumber > 0) {
			requestURL = new URL("http" + garbageString + "s://www.googleapis.com/customsearch/v1?key=" + GOOGLE_SEARCH_API_KEY + "&cx=" + GOOGLE_CX + "&q=" + this.topic + "&searchType=image&imgType=photo&imgSize=medium&start=" + resultNumber + "&num=10");
		}
		else {
			requestURL = new URL("http" + garbageString + "s://www.googleapis.com/customsearch/v1?key=" + GOOGLE_SEARCH_API_KEY + "&cx=" + GOOGLE_CX + "&q=" + topic + "&searchType=image&imgType=photo&imgSize=medium&num=10");
		}
		return requestURL;
	}

	// scaling each image to roughly 1/20th of COLLAGE_SIZE, see: https://stackoverflow.com/questions/9417356/bufferedimage-resize
	public void resizeImages() {
		int numImages = this.retrievedImages.size();
		for(int i = 0; i < numImages; i++) {
			BufferedImage originalImage = this.retrievedImages.get(0);
			this.retrievedImages.remove(0);

			int originalWidth = originalImage.getWidth();
			int originalHeight = originalImage.getHeight();
			int originalImageSize = originalWidth * originalHeight;
			// determines scale factor such that the average sizes of the images is 1/20th of COLLAGE_SIZE
			double scaleFactor = originalImageSize/FINAL_FOREGROUND_IMAGE_SIZE;

			scaleFactor = Math.sqrt(scaleFactor);

			double scaledWidth = (originalWidth/scaleFactor);
			double scaledHeight = (originalHeight/scaleFactor);

			Image tmp = originalImage.getScaledInstance((int)scaledWidth, (int)scaledHeight, Image.SCALE_SMOOTH);
			BufferedImage resizedImage = new BufferedImage((int)scaledWidth, (int)scaledHeight, BufferedImage.TYPE_INT_ARGB);

			Graphics2D g2d = resizedImage.createGraphics();
			g2d.drawImage(tmp, 0, 0, null);

			this.retrievedImages.add(resizedImage);

			g2d.dispose();
		}
	}

	// generate random rotation amount for an image in radians within IMAGE_ROTATION_LIMIT
	// where -45 degrees <= IMAGE_ROTATION_LIMIT <= 45 degrees
	public int generateRotationAmount() {
		Random rand = new Random();
		int angle = rand.nextInt(46);

		if(rand.nextBoolean()) {
			return angle*-1;
		}

		return angle;
	}

	// adds 3px white frame around each image
	// see: https://stackoverflow.com/questions/4219511/draw-rectangle-border-thickness
	public void borderImage(BufferedImage image) {
			Graphics2D g2d = image.createGraphics();
			int height = image.getHeight();
			int width = image.getWidth();
			// sets border width to 3 pixels
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

	// generates collage from the retrieved bufferedImages
	public BufferedImage combineImages() {
		BufferedImage collageImage = new BufferedImage(COLLAGE_WIDTH, COLLAGE_HEIGHT, BufferedImage.TYPE_INT_ARGB);
		Graphics2D g = (Graphics2D) collageImage.getGraphics();
		int x = 0;
		int y = 0;
		int imageNum = 0;
		Random rand = new Random();
		for(BufferedImage image : this.retrievedImages) {
			if (imageNum == 0) {
				Image tmp = image.getScaledInstance((int)COLLAGE_WIDTH, (int)COLLAGE_HEIGHT, Image.SCALE_SMOOTH);
				BufferedImage resizedImage = new BufferedImage((int)COLLAGE_WIDTH, (int)COLLAGE_HEIGHT, BufferedImage.TYPE_INT_ARGB);

				this.borderImage(resizedImage);

				Graphics2D g2d = resizedImage.createGraphics();
				g2d.drawImage(tmp, 0, 0, null);
				g.drawImage(resizedImage, 0, 0, null);
			}
			else {
				this.borderImage(image);
				AffineTransform backup = g.getTransform();
				AffineTransform imageRotator = new AffineTransform();
				int rotationAmount = generateRotationAmount();

				imageRotator.rotate(Math.toRadians(rotationAmount), image.getWidth()/2, image.getHeight()/2);

				g.transform(imageRotator);

				// randomly generates the location of the next image on the collage within the bounds of the collage
				x = rand.nextInt(COLLAGE_WIDTH-600)+300;
				y = rand.nextInt(COLLAGE_HEIGHT-500)+200;
				g.drawImage(image, null,x, y);
				g.setTransform(backup);
			}

			imageNum++;
		}
		try {
			ImageIO.write(collageImage,"png",new File(this.topic + "-collage.png"));
		} catch(IOException e) {
			e.getMessage();
		}
		this.completeImage = collageImage;
		return collageImage;
	}

	// generates the buffered image that is exported when there is an insufficient number of images found
	public BufferedImage generateInsufficientNumberImage() {
		BufferedImage insufficentNumberImage = new BufferedImage(1, 1, BufferedImage.TYPE_INT_ARGB);
        Graphics2D imageGraphicsManipulator = insufficentNumberImage.createGraphics();
				// setting font size to 18
        Font imageFont = new Font("Arial", Font.PLAIN, 18);
        imageGraphicsManipulator.setFont(imageFont);
        imageGraphicsManipulator.dispose();

				// setting presets for the Graphics2D object to be able to write the image
        insufficentNumberImage = new BufferedImage(COLLAGE_WIDTH, COLLAGE_HEIGHT, BufferedImage.TYPE_INT_ARGB);
        imageGraphicsManipulator = insufficentNumberImage.createGraphics();
        imageGraphicsManipulator.setRenderingHint(RenderingHints.KEY_ALPHA_INTERPOLATION, RenderingHints.VALUE_ALPHA_INTERPOLATION_QUALITY);
        imageGraphicsManipulator.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        imageGraphicsManipulator.setRenderingHint(RenderingHints.KEY_COLOR_RENDERING, RenderingHints.VALUE_COLOR_RENDER_QUALITY);
        imageGraphicsManipulator.setRenderingHint(RenderingHints.KEY_DITHERING, RenderingHints.VALUE_DITHER_ENABLE);
        imageGraphicsManipulator.setRenderingHint(RenderingHints.KEY_FRACTIONALMETRICS, RenderingHints.VALUE_FRACTIONALMETRICS_ON);
        imageGraphicsManipulator.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
        imageGraphicsManipulator.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
        imageGraphicsManipulator.setRenderingHint(RenderingHints.KEY_STROKE_CONTROL, RenderingHints.VALUE_STROKE_PURE);
        imageGraphicsManipulator.setPaint(Color.WHITE);
        imageGraphicsManipulator.fillRect(0, 0, COLLAGE_WIDTH, COLLAGE_HEIGHT);
        imageGraphicsManipulator.setFont(imageFont);
        imageGraphicsManipulator.setColor(Color.BLACK);
        imageGraphicsManipulator.drawString("Insufficient number of images found", 445, COLLAGE_HEIGHT/2);
        imageGraphicsManipulator.dispose();

        return insufficentNumberImage;

    }

		// accessor method to return the retrieved images from Google/modified images at any point
	public List<BufferedImage> getRetrievedImages() {
		return this.retrievedImages;
	}
	
	public void setRetrievedImages(List<BufferedImage> images){
		this.retrievedImages = images;
	}

	// accessor method to return the complete collage image as a buffered image
	public BufferedImage getCompleteImage() {
		return this.completeImage;
	}

}
