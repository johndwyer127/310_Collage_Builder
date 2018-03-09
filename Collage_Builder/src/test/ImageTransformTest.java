import static org.hamcrest.CoreMatchers.instanceOf;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import org.junit.Test;
import org.mockito.Mockito;

import server.CollageHandler;
import server.ImageTransform;

public class ImageTransformTest {
	private static final String FILE_NAME = "insufficientNumberImage.txt";
	private static final String GOOGLE_SEARCH_API_KEY = "AIzaSyB7_yytK04B7speZc4lXsHLr9ARmwPiUzw";
	private static final String GOOGLE_CX = "015527610641952349258:lx1x9pjo0ec";	// custom search engine identifier

	// tests that the constructor of the ImageTransform class creates a list field
	@Test
	public void testConstructor() {
		ImageTransform imageTransform = new ImageTransform("test");
		assertThat(imageTransform.getRetrievedImages(), instanceOf(ArrayList.class));
		assertThat(imageTransform, instanceOf(ImageTransform.class));
	}

	// tests that createCollageImages() works when there is an insufficient number of images found
	@Test
	public void testCreateCollageImageInsufficientNumber() {
		ImageTransform imageTransform = new ImageTransform("test");
		ImageTransform imageTransformSpy = Mockito.spy(imageTransform);

		Mockito.doReturn(false).when(imageTransformSpy).fetchImages();

		BufferedImage insufficientNumberImage = mock(BufferedImage.class);
		Mockito.doReturn(insufficientNumberImage).when(imageTransformSpy).generateInsufficientNumberImage();

		BufferedImage returnedBufferedImage = imageTransformSpy.createCollageImage();
		assertEquals(returnedBufferedImage, insufficientNumberImage);
	}

	//tests fetchImages by running fetchimages and mocks external google search functionality
	@Test
	public void testFetchImages() throws IOException {
		ImageTransform imageTransform = new ImageTransform("test");
		ImageTransform imageTransformSpy = Mockito.spy(imageTransform);
		URL testURL = new URL("http://www.google.com");
		HttpURLConnection testConnection = (HttpURLConnection) testURL.openConnection();
		
		PrintWriter writer = new PrintWriter("fakeLinks.txt", "UTF-8");
		writer.println("test.com");
		writer.println("test2.com");
		writer.println("test3.com");
		writer.close();
		FileReader textReader = new FileReader("fakeLinks.txt");
		BufferedReader testReader = new BufferedReader(textReader);
		BufferedImage testImage = new BufferedImage(1, 1, 1);

		Mockito.when(imageTransformSpy.generateRequestURL(0, "")).thenReturn(testURL);
		Mockito.when(imageTransformSpy.getConnectionFromRequestURL(testURL)).thenReturn(testConnection);

		Mockito.when(imageTransformSpy.generateBufferedReaderFromInputStream(testConnection)).thenReturn(testReader);
		Mockito.when(imageTransformSpy.generateBufferedImageFromURL(testURL)).thenReturn(testImage);
	}

	// tests that createCollageImages() works when there is a sufficient number of images found
	@Test
	public void testCreateCollageImageSufficientNumber() {
		ImageTransform imageTransform = new ImageTransform("test");
		ImageTransform imageTransformSpy = Mockito.spy(imageTransform);

		Mockito.doReturn(true).when(imageTransformSpy).fetchImages();

		BufferedImage testImage = mock(BufferedImage.class);
		Mockito.doReturn(testImage).when(imageTransformSpy).combineImages();

		BufferedImage returnedBufferedImage = imageTransformSpy.createCollageImage();
		assertEquals(returnedBufferedImage, testImage);
	}

	// tests that validateRetrievedImages() returns false when there are less than 30 images stored in retrieved images
	@Test
	public void testValidateRetrievedImagesInsufficientNumber() {
		ImageTransform imageTransform = new ImageTransform("test");
		List<BufferedImage> testImages = new ArrayList<BufferedImage>();
		imageTransform.setRetrievedImages(testImages);

		Boolean imagesValid = imageTransform.validateRetrievedImages();

		assertEquals(false, imagesValid);
	}


	// tests that validateRetrievedImages() returns true and removes excess images when more than 30 images are stored in retrieved images
	@Test
	public void testValidateRetrievedImagesSufficientNumber() {
		ImageTransform imageTransform = new ImageTransform("test");
		List<BufferedImage> testImages = new ArrayList<BufferedImage>();

		for(int i = 0; i < 31; i++) {
			BufferedImage testImage = new BufferedImage(1, 1, 1);
			testImages.add(testImage);
		}

		imageTransform.setRetrievedImages(testImages);
		Boolean imagesValid = imageTransform.validateRetrievedImages();

		assertEquals(true, imagesValid);
		assertEquals(30, imageTransform.getRetrievedImages().size());
	}

	//tests the generateRequestURLResultNumberZero to make sure the url that is generated is a valid url
	@Test
	public void testGenerateRequestURLResultNumberZero() throws MalformedURLException{
		ImageTransform imageTransform = new ImageTransform("test");
		URL requestURL;
		requestURL = imageTransform.generateRequestURL(0, "");
		String urlStringGenerated = requestURL.toString();
		String validURL = "https://www.googleapis.com/customsearch/v1?key=" + GOOGLE_SEARCH_API_KEY + "&cx=" + GOOGLE_CX + "&q=test&searchType=image&imgType=photo&imgSize=medium&num=10";
		assertEquals(validURL, urlStringGenerated);
	}

	//tests generateRequestURLResultNumberNotZero function to make sure the url generated is valid
	@Test
	public void testGenerateRequestURLResultNumberNotZero() throws MalformedURLException{
		ImageTransform imageTransform = new ImageTransform("test");
		URL requestURL;
		requestURL = imageTransform.generateRequestURL(10, "");
		String urlStringGenerated = requestURL.toString();
		String validURL = "https://www.googleapis.com/customsearch/v1?key=" + GOOGLE_SEARCH_API_KEY + "&cx=" + GOOGLE_CX + "&q=test&searchType=image&imgType=photo&imgSize=medium&start=10&num=10";
		assertEquals(validURL, urlStringGenerated);
	}

	//tests the exception throw by generateRequestURLMalformedURLException by making the url not formed correctly
	@Test(expected = MalformedURLException.class)
	public void testGenerateRequestURLMalformedURLException() throws MalformedURLException {
		ImageTransform imageTransform = new ImageTransform("test");
		URL requestURL = imageTransform.generateRequestURL(10, "ppp");
	}

	//test the generateRotationAmount function by running it 100 times and making sure it is within the -45 to 45 range
	@Test
    public void generateRotationAmountTester() {
        ImageTransform it = new ImageTransform("test");
        for(int i = 0; i< 100; i++) {
            int randRot = it.generateRotationAmount();
            assertTrue(randRot<=45 && randRot>=-45);
        }

    }

	//tests borderImage function by creating a black image and testing its 4 corners berfore and after calling the function and making sure that before they are bblack and after they are white
	@Test
	public void testBorderImage() {
    ImageTransform it = new ImageTransform("Topic");

       BufferedImage startImage = new BufferedImage(100, 200, BufferedImage.TYPE_INT_ARGB);
       Graphics2D imageGraphicsManipulator = startImage.createGraphics();
       imageGraphicsManipulator = startImage.createGraphics();
       imageGraphicsManipulator.setRenderingHint(RenderingHints.KEY_ALPHA_INTERPOLATION, RenderingHints.VALUE_ALPHA_INTERPOLATION_QUALITY);
       imageGraphicsManipulator.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
       imageGraphicsManipulator.setRenderingHint(RenderingHints.KEY_COLOR_RENDERING, RenderingHints.VALUE_COLOR_RENDER_QUALITY);
       imageGraphicsManipulator.setRenderingHint(RenderingHints.KEY_DITHERING, RenderingHints.VALUE_DITHER_ENABLE);
       imageGraphicsManipulator.setRenderingHint(RenderingHints.KEY_FRACTIONALMETRICS, RenderingHints.VALUE_FRACTIONALMETRICS_ON);
       imageGraphicsManipulator.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
       imageGraphicsManipulator.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
       imageGraphicsManipulator.setRenderingHint(RenderingHints.KEY_STROKE_CONTROL, RenderingHints.VALUE_STROKE_PURE);
       imageGraphicsManipulator.setPaint(Color.BLACK);
       imageGraphicsManipulator.fillRect(0, 0, 100, 200);


       Color topLeftOrgNonBordered = new Color(startImage.getRGB(0, 0));
        Color topRightOrgNonBordered =  new Color(startImage.getRGB(startImage.getWidth()-1, 0));
        Color bottomRightOrgNonBordered =  new Color(startImage.getRGB(0, startImage.getHeight()-1));
        Color bottomLeftOrgNonBordered =  new Color(startImage.getRGB(startImage.getWidth()-1, startImage.getHeight()-1));

        it.borderImage(startImage);

        Color topLeftOrgBordered = new Color(startImage.getRGB(0, 0));
        Color topRightOrgBordered =  new Color(startImage.getRGB(startImage.getWidth()-1, 0));
        Color bottomRightOrgBordered =  new Color(startImage.getRGB(0, startImage.getHeight()-1));
        Color bottomLeftOrgBordered =  new Color(startImage.getRGB(startImage.getWidth()-1, startImage.getHeight()-1));

        Color white = new Color(255,255,255);
        assertEquals(true, topLeftOrgBordered.equals(white));
        assertEquals(true, topRightOrgBordered.equals(white));
        assertEquals(true, bottomRightOrgBordered.equals(white));
        assertEquals(true, bottomLeftOrgBordered.equals(white));

        assertEquals(false, topLeftOrgNonBordered.equals(white));
        assertEquals(false, topRightOrgNonBordered.equals(white));
        assertEquals(false, bottomRightOrgNonBordered.equals(white));
	    assertEquals(false, bottomLeftOrgNonBordered.equals(white));

  }
	
	//testing the resizeImages function by resizing the images and the compringthem to the correct hieght and width
	@Test
	public void testResizeImages() {
		ImageTransform imageTransform = new ImageTransform("test");

		List<BufferedImage> testImages = new ArrayList<BufferedImage>();

		for(int i = 0; i < 31; i++) {
			BufferedImage testImage = createFixedSizeBufferedImage();
			testImages.add(testImage);
		}

		imageTransform.setRetrievedImages(testImages);

		imageTransform.resizeImages();

		for(BufferedImage resizedImage : imageTransform.getRetrievedImages()) {
			// computed the correct resized height and width for a 500px by 500px image externally to be the following:
			int correctHeight = 109;
			int correctWidth = 109;

			assertEquals(correctHeight, resizedImage.getHeight());
			assertEquals(correctWidth, resizedImage.getWidth());
		}
	}

	//testing combineImages by combining the images and comfirming that it returns a BufferedImage
	@Test
	public void testCombineImages() {
		ImageTransform imageTransform = new ImageTransform("test");

		List<BufferedImage> testImages = new ArrayList<BufferedImage>();

		for(int i = 0; i < 31; i++) {
			BufferedImage testImage = createFixedSizeBufferedImage();
			testImages.add(testImage);
		}

		imageTransform.setRetrievedImages(testImages);

		BufferedImage collage = imageTransform.combineImages();

		assertThat(collage, instanceOf(BufferedImage.class));
	}

	//tests to make sure the image generated is the right image by comparingining it with the correct image's encoded string
	@Test
	public void testGenerateInsufficientNumberImage() {
		ImageTransform it = new ImageTransform("test");
		CollageHandler ch = new CollageHandler("test");
		BufferedImage testingBI = it.generateInsufficientNumberImage();
		String testBase64 = ch.convertBufferedImageToBase64(testingBI);
		String compareImage = getImageEncodedAsStringFromFile();
		assertThat(testingBI, instanceOf(BufferedImage.class));
		assertEquals(compareImage, testBase64);
	}
	
	//tests the getCompletedImage by making sure it returns a the same collage as was created and set in createCollageImage
	@Test
	public void testGetCompleteImage() {
		ImageTransform it = new ImageTransform("test");
		BufferedImage collage = it.createCollageImage();
		BufferedImage completeCollage = it.getCompleteImage();
		assertEquals(completeCollage,collage);
	}
	
	//helper image to cerate a buffered image we can test with 
	private BufferedImage createFixedSizeBufferedImage() {
		return new BufferedImage(500, 500, BufferedImage.TYPE_INT_ARGB);
	}
	
	//helper method to test an encoded string against
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

}
