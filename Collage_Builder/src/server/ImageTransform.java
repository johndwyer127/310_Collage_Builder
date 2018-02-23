package server;
import java.awt.image.BufferedImage;
import java.util.List;
import java.util.ArrayList;

public class ImageTransform {

	private BufferedImage completeImage;
	private String topic;
	private List<BufferedImage> retreivedImages;


	//initializes ImageTransform with String paramater t for its topic
	public ImageTransform(String t){
		topic = t;
	}

	public BufferedImage createCollageImage(){

		BufferedImage i = new BufferedImage();
		return i;
	}

	public List fetchImages(){

		List<BufferedImage> images = new ArrayList<BufferedImage>();
		//google search here

		return images;
	}

	public void resizeImages(){

	}

	public void rotateImages(){

	}

	public void borderImages(){

	}

	public BufferedImage combineImages(){

		BufferedImage i = new BufferedImage();
		return i;
	}






}
