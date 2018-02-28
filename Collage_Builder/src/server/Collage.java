package server;

public class Collage {

	private String topic;
	private String image;

	//reuturns collage topic
	public String getTopic(){
		return topic;
	}
	// returns collage image
	public String getImage(){
		return image;
	}

	//sets collage topic to String paramater t
	public void setTopic(String t){
		topic = t;
	}

	//sets collage image to String paramater i
	public void setImage(String i){
		image = i;
	}
	
}
