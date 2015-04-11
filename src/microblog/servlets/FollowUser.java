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
 * Servlet implementation class FollowUser
 */

@SuppressWarnings("deprecation")
public class FollowUser extends HttpServlet implements SingleThreadModel {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public FollowUser() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
	
		try{
			String uri = request.getRequestURI();
			HttpSession session = request.getSession(true);
			//get session variable
			User user = (User) session.getAttribute("user");
			
			response.setContentType("application/json; charset=UTF-8");
			PrintWriter out = response.getWriter();
			
			
			if (uri.indexOf("id") != -1 && user !=null)
			{
			String id = uri.substring(uri.indexOf("id") + 3);
			
			if(user.getUserID().compareTo(id) == 0){
				out.println("{ \"result\": \"fail\",\"message\":\"You can't follow \"}"); // can't follow yourself
				out.close();
			}
			else{
				if(user.AddFollowingToUser(Integer.parseInt(user.getUserID()), Integer.parseInt(id))) //getUserID Started To Follow id
				{
					out.println("{ \"result\": \"success\"}");	
					out.close();
				}
				else
				{
					out.println("{ \"result\": \"fail\",\"message\":\"You are already following \"}"); // return message as jason
					out.close();	
				}
				}
			}
			else
			{
				out.println("{ \"result\": \"fail\"}"); // will reach the else if id doesn't exist in URI
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
		
	}

}
