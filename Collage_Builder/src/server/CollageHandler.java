package server;
import sun.misc.BASE64Encoder;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import javax.imageio.ImageIO;
import java.io.IOException;

public class CollageHandler {

	private String topic;
	private Collage newCollage;
	private ImageTransform imageTransformer;

	//initializes the CollageHandler with String paramater t for collage topic
	public CollageHandler(String t){
		topic = t;

	}

	//builds collage with ImageTransform object and returns Collage object
	public Collage build(){
		imageTransformer = new ImageTransform(topic);




		Collage c = new Collage();
		return c;
	}

	//converts BufferedImage paramater image into returned base64 string
	public String convertBufferedImageToBase64(BufferedImage image){

		String type = "png";
		String imageString = null;
		ByteArrayOutputStream bos = new ByteArrayOutputStream();

		try {
			ImageIO.write(image, type, bos);
			byte[] imageBytes = bos.toByteArray();

			BASE64Encoder encoder = new BASE64Encoder();
			imageString = encoder.encode(imageBytes);

			bos.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return imageString;
	}

}
