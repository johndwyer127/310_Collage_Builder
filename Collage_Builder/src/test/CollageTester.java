import static org.junit.Assert.*;

import org.junit.Test;
import server.Collage;

public class CollageTester {

	@Test
	public void setterAndGetter() {
		Collage c = new Collage();
		c.setImage("testImage");
		c.setTopic("testTopic");
		
		assertEquals("testImage", c.getImage());
		assertEquals("testTopic", c.getTopic());
	}

}
