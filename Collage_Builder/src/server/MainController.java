package server;

import java.io.IOException;
import java.util.ArrayList;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * Servlet implementation class MainController
 */
@WebServlet("/MainController")
public class MainController extends HttpServlet {
	private static final long serialVersionUID = 1L;

    /**
     * @see HttpServlet#HttpServlet()
     */
    public MainController() {
        super();
        // TODO Auto-generated constructor stub
    }

    //Handles setting the initial MainCollage & PreviousCollageList
    //Also creates
    public void doGet(HttpServletRequest request, HttpServletResponse response)
    		throws ServletException, IOException  {
				String topic = request.getParameter("topic");
				Collage topicCollage = buildCollage(topic);
				HttpSession session = request.getSession();
				
					ArrayList<Collage> previousList = (ArrayList<Collage>) session.getAttribute("PreviousCollageList");
					previousList.add((Collage) session.getAttribute("MainCollage"));
					session.setAttribute("MainCollage", topicCollage);
					request.getRequestDispatcher("CollageViewerPage.jsp").forward(request, response);
			

				ServletContext sc = this.getServletContext();
				RequestDispatcher rd = sc.getRequestDispatcher("/CollageViewerPage.jsp");
				rd.include(request, response);

				return;
    }

    public void doPost(HttpServletRequest request, HttpServletResponse response)
    		throws ServletException, IOException  {
				String topic = request.getParameter("topic");
				Collage topicCollage = buildCollage(topic);
				HttpSession session = request.getSession();

					session.setAttribute("MainCollage", topicCollage);
					ArrayList<Collage> previousList = new ArrayList<Collage>();
					session.setAttribute("PreviousCollageList", previousList);

				RequestDispatcher view = getServletContext().getRequestDispatcher("/CollageViewerPage.jsp");
				view.forward(request, response);

				return;
    }

	//builds collage with String paramater topic
	public Collage buildCollage(String topic){
		CollageHandler ch = new CollageHandler(topic);
		Collage c = ch.build();
		return c;

	}
}
