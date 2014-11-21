/**
 * 
 */
package h4202.view;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * @author H4202
 * The Controller, a servlet responsable for the communication between the Model and the View.
 *
 */
public class ActionServlet extends HttpServlet {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	 protected void processRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
	        HttpSession session= request.getSession(true);
	        response.setContentType("text/html;charset=UTF-8");
	        PrintWriter out = response.getWriter();
	        
	        String view="/home.jsp";
	        
	        Action action = null;
	        
	        //On met ici ce que nous on va faire
	        
	        
	        request.getRequestDispatcher(view).forward(request, response);
            
            out.close();
	 }
	   

}
