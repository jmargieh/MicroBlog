package microblog.servlets;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.SingleThreadModel;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import microblog.model.User;
/**
 * Servlet implementation class SessionAuth
 */
@SuppressWarnings("deprecation")
public class SessionAuth extends HttpServlet implements SingleThreadModel {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public SessionAuth() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		//Obtain the session object, create a new session if doesn't exist
		HttpSession session = request.getSession(true);
		
		//get session variable
		User user = (User) session.getAttribute("user");
		
		try{
			response.setContentType("application/json; charset=UTF-8");
				PrintWriter out = response.getWriter();
				//means user is logged in
				if(user != null)
				{
					out.println("{ \"result\": \"success\",\"nickname\":\""+user.getNickname()+"\"}");
					out.close();
				}
				else
				{
					out.println("{ \"result\": \"fail\"}");	
					out.close();
				}
		}catch (IOException e) {  
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
