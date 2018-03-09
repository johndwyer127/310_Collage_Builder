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

import server.ImageTransform;

public class ImageTransformTest {

	private static final String GOOGLE_SEARCH_API_KEY = "AIzaSyADYi8Ob0jmPJbGEMCkJwrB31bOY80RtXs";
	private static final String GOOGLE_CX = "008543189839369971484:b8selplq7z8";	// custom search engine identifier

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

	@Test
	public void testFetchImages() throws IOException {
		ImageTransform imageTransform = new ImageTransform("test");
		ImageTransform imageTransformSpy = Mockito.spy(imageTransform);
		URL testURL = new URL("http://www.google.com");
		HttpURLConnection testConnection = (HttpURLConnection) testURL.openConnection();
		InputStream mockInputStream = Mockito.mock(InputStream.class);
		InputStreamReader mockInputStreamReader = Mockito.mock(InputStreamReader.class);
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

	@Test
	public void testGenerateRequestURLResultNumberZero() throws MalformedURLException{
		ImageTransform imageTransform = new ImageTransform("test");
		URL requestURL;
		requestURL = imageTransform.generateRequestURL(0, "");
		String urlStringGenerated = requestURL.toString();
		String validURL = "https://www.googleapis.com/customsearch/v1?key=" + GOOGLE_SEARCH_API_KEY + "&cx=" + GOOGLE_CX + "&q=test&searchType=image&imgType=photo&imgSize=medium&num=10";
		assertEquals(validURL, urlStringGenerated);
	}

	@Test
	public void testGenerateRequestURLResultNumberNotZero() throws MalformedURLException{
		ImageTransform imageTransform = new ImageTransform("test");
		URL requestURL;
		requestURL = imageTransform.generateRequestURL(10, "");
		String urlStringGenerated = requestURL.toString();
		String validURL = "https://www.googleapis.com/customsearch/v1?key=" + GOOGLE_SEARCH_API_KEY + "&cx=" + GOOGLE_CX + "&q=test&searchType=image&imgType=photo&imgSize=medium&start=10&num=10";
		assertEquals(validURL, urlStringGenerated);
	}

	@Test(expected = MalformedURLException.class)
	public void testGenerateRequestURLMalformedURLException() throws MalformedURLException {
		ImageTransform imageTransform = new ImageTransform("test");
		URL requestURL = imageTransform.generateRequestURL(10, "ppp");
	}

	@Test
    public void generateRotationAmountTester() {
        ImageTransform it = new ImageTransform("test");
        for(int i = 0; i< 100; i++) {
            int randRot = it.generateRotationAmount();
            assertTrue(randRot<=45 && randRot>=-45);
        }

    }

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

	private BufferedImage createFixedSizeBufferedImage() {
		return new BufferedImage(500, 500, BufferedImage.TYPE_INT_ARGB);
	}

}
