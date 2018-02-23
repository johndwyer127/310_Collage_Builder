package server;
import java.awt.image.*;

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

	//converts BufferedImage paramater image into returned string
	public String convertBufferedImageToBase64(BufferedImage image){

		String s = "";
		return s;

	}





}
