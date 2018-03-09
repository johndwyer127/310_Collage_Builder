import static org.junit.Assert.*;

import org.junit.Test;
import server.Collage;

public class CollageTester {

	//tests the getter and setter methods of collage class by setting the them and then getting them
	@Test
	public void setterAndGetter() {
		Collage c = new Collage();
		c.setImage("testImage");
		c.setTopic("testTopic");
		
		assertEquals("testImage", c.getImage());
		assertEquals("testTopic", c.getTopic());
	}

}
