import java.io.IOException;
import java.util.ArrayList;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.junit.Test;
import org.mockito.Mockito;
import static org.hamcrest.CoreMatchers.instanceOf;
import static org.junit.Assert.assertThat;

import server.Collage;
import server.CollageHandler;
import server.MainController;

public class MainControllerTest {
    
	@Test
    public void testDoGet() {
        HttpServletRequest request = Mockito.mock(HttpServletRequest.class);
        HttpServletResponse response = Mockito.mock(HttpServletResponse.class);
        HttpSession session = Mockito.mock(HttpSession.class);
        RequestDispatcher rd = Mockito.mock(RequestDispatcher.class);
        MainController mainController = new MainController();
        MainController mainControllerSpy = Mockito.spy(mainController);
        
        Mockito.when(request.getParameter("topic")).thenReturn("Cat");
        Mockito.when(request.getSession()).thenReturn(session);
        Mockito.when(request.getRequestDispatcher("/CollageViewerPage.jsp")).thenReturn(rd);
        Collage mainCollage = new Collage();
        Mockito.when(mainControllerSpy.buildCollage("Cat")).thenReturn(mainCollage);
        ArrayList<Collage> previousCollageListTest = new ArrayList<Collage>();
        Mockito.when(mainControllerSpy.getPreviousCollageList(session)).thenReturn(previousCollageListTest);
        
        try {
            mainControllerSpy.doGet(request, response);
            Mockito.verify(session).setAttribute("MainCollage", mainCollage);
            Mockito.verify(rd).forward(request, response);
        } catch (ServletException e) {
            System.out.println("ServletException testing MainController doPost()");
        } catch (IOException e) {
            System.out.println("IOException testing MainController doPost()");
        }
        
    }
	
	
    @Test
    public void testDoPost() {
        HttpServletRequest request = Mockito.mock(HttpServletRequest.class);
        HttpServletResponse response = Mockito.mock(HttpServletResponse.class);
        HttpSession session = Mockito.mock(HttpSession.class);
        RequestDispatcher rd = Mockito.mock(RequestDispatcher.class);
        MainController mainController = new MainController();
        MainController mainControllerSpy = Mockito.spy(mainController);
        
        Mockito.when(request.getParameter("topic")).thenReturn("Cat");
        Mockito.when(request.getSession()).thenReturn(session);
        Mockito.when(request.getRequestDispatcher("/CollageViewerPage.jsp")).thenReturn(rd);
        Collage mainCollage = new Collage();
        Mockito.when(mainControllerSpy.buildCollage("Cat")).thenReturn(mainCollage);
        ArrayList<Collage> previousCollageListTest = new ArrayList<Collage>();
        Mockito.when(mainControllerSpy.createNewListForTesting()).thenReturn(previousCollageListTest);
        
        try {
            mainControllerSpy.doPost(request, response);
            Mockito.verify(session).setAttribute("MainCollage", mainCollage);
            Mockito.verify(session).setAttribute("PreviousCollageList", previousCollageListTest);
            Mockito.verify(rd).forward(request, response);
        } catch (ServletException e) {
            System.out.println("ServletException testing MainController doPost()");
        } catch (IOException e) {
            System.out.println("IOException testing MainController doPost()");
        }
        
    }
    
    @Test
    public void testBuildCollage() {
    		MainController mainController = new MainController();
    		CollageHandler ch = Mockito.mock(CollageHandler.class);
    		Collage testCollage = new Collage();
    		Mockito.when(ch.build()).thenReturn(testCollage);
    		
    		Collage producedCollage = mainController.buildCollage("Cat");
    		assertThat(producedCollage, instanceOf(Collage.class));
    }
    
    @Test
    public void testCreateNewListForTesting() {
    		MainController mainController = new MainController();
    		ArrayList<Collage> previousCollageList = mainController.createNewListForTesting();
    		assertThat(previousCollageList, instanceOf(ArrayList.class));
    }
    
    // don't know how the FUCK to do this one...
    @Test
    public void testPreviousCollageList() {
    		MainController mainController = new MainController();
    		HttpSession session = Mockito.mock(HttpSession.class);
    		ArrayList<Collage> previousCollageList = mainController.getPreviousCollageList(session);
//    		assertThat(previousCollageList, instanceOf(ArrayList.class));
    }

}