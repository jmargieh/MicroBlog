package microblog.servlets;


import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.SingleThreadModel;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.xml.ws.http.HTTPException;


/**
 * Servlet implementation class LogoutUser
 */
@SuppressWarnings("deprecation")
public class LogoutUser extends HttpServlet implements SingleThreadModel {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public LogoutUser() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
   
        try {
        	
        	response.setContentType("application/json; charset=UTF-8");
			PrintWriter out = response.getWriter();
        	
        	HttpSession session = request.getSession(true);
            if(session.getAttribute("user") != null){
            	session.invalidate(); // release session
            	out.println("{ \"result\": \"success\"}");	
            }
            else{
            	out.println("{ \"result\": \"fail\"}");            
            }
            	
        } catch (HTTPException e) {  
            e.printStackTrace();  
        }
        
		
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
	}

}
