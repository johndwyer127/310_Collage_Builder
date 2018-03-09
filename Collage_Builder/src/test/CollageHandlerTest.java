import static org.junit.Assert.assertEquals;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Base64;

import javax.imageio.ImageIO;

import org.junit.Test;
import org.mockito.Mockito;

import static org.hamcrest.CoreMatchers.instanceOf;
import static org.junit.Assert.assertThat;

import server.CollageHandler;

public class CollageHandlerTest {

	private static final int COLLAGE_WIDTH = 1120;
	private static final int COLLAGE_HEIGHT = 600;
	private static final String FILE_NAME = "insufficientNumberImage.txt";
	
	
	// tests the CollageHandler constructor
	@Test
	public void testConstructor() {
		CollageHandler collageHandler = new CollageHandler("test");
		assertThat(collageHandler, instanceOf(CollageHandler.class));
	}
	
	// tests the CollageHandler class's method to encode BufferedImage objects as base64 encoded Strings
	@Test
	public void testConvertBufferedImageToBase64() {
		CollageHandler collageHandler = new CollageHandler("test");
		BufferedImage testImage = generateInsufficientNumberImage();
		
		String convertedImage = collageHandler.convertBufferedImageToBase64(testImage);
		
		assertEquals(getImageEncodedAsStringFromFile(), convertedImage);
	}
	
	// encoded the same buffered image using an online base64 encoder that can be found here:
	// https://www.base64encode.org/
	private String getImageEncodedAsStringFromFile() {
		String line = null;
		String fullImage = "";
		try {
			FileReader fileReader = new FileReader(FILE_NAME);
			
			BufferedReader bufferedReader = new BufferedReader(fileReader);
			
			while((line = bufferedReader.readLine()) != null) {
				fullImage += line;
			}
			
			bufferedReader.close();
		} catch(FileNotFoundException fnfe) {
			System.out.println("unable to open file");
			
		} catch(IOException ioe) {
			System.out.println("io exception");
		}
		return fullImage;
	}
	
	// helper function to generate an arbitrary bufferedImage to test the encoding functionality
	private BufferedImage generateInsufficientNumberImage() {
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
        
        try {
        	File fileImage = new File("insufficientNumberImage.png");
        	ImageIO.write(insufficentNumberImage, "png", fileImage);
        } catch (IOException e) {
        		System.out.println("e IOException");
        }

        return insufficentNumberImage;

    }
	
	@Test
	public void testThrowConvertBufferedImageToBase64() {
		CollageHandler ch = Mockito.mock(CollageHandler.class);
	}
	

}
