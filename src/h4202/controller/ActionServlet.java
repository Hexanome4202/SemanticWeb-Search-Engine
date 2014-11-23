package h4202.controller;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * @author H4202 The Controller, a servlet responsable for the communication
 *         between the Model and the View.
 * 
 */

public class ActionServlet extends HttpServlet {
	protected String view;
	protected Action action;

	private static final long serialVersionUID = 1L;

	protected void processRequest(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		HttpSession session = request.getSession(true);
		response.setContentType("text/html;charset=UTF-8");
		PrintWriter out = response.getWriter();

		// Both can be null /!\
		String todo = request.getParameter("todo");
		String keyWords = request.getParameter("keyWords");
		
		this.setViewAndAction(todo, keyWords);

		if(this.action != null) this.action.execute(request, session);

		request.getRequestDispatcher(view).forward(request, response);

		this.action = null;
		out.close();
	}

	/**
	 * Handles the HTTP <code>GET</code> method.
	 * 
	 * @param request
	 *            servlet request
	 * @param response
	 *            servlet response
	 * @throws ServletException
	 *             if a servlet-specific error occurs
	 * @throws IOException
	 *             if an I/O error occurs
	 */
	@Override
	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		processRequest(request, response);
	}

	/**
	 * Handles the HTTP <code>POST</code> method.
	 * 
	 * @param request
	 *            servlet request
	 * @param response
	 *            servlet response
	 * @throws ServletException
	 *             if a servlet-specific error occurs
	 * @throws IOException
	 *             if an I/O error occurs
	 */
	@Override
	protected void doPost(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		processRequest(request, response);
	}

	/**
	 * Returns a short description of the servlet.
	 * 
	 * @return a String containing servlet description
	 */
	@Override
	public String getServletInfo() {
		return "Short description";
	}// </editor-fold>
	
	public void setViewAndAction(String todo, String keyWords) {
		System.out.println(keyWords);
		System.out.println(todo);
		if(todo == null) {
			this.view = "home.jsp";
		} else if("search".equals(todo) && keyWords != null && !"".equals(keyWords)) {
			this.view = "research.jsp";
			this.action = new ResearchAction();
			//this.action = new ThreadedSearch();
		} else {
			this.view = "home.jsp";
		}
	}
}
