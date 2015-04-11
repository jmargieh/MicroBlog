package microblog.servlets;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.SingleThreadModel;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.google.gson.Gson;

import microblog.model.User;

/**
 * Servlet implementation class GetLoggedUserInfo
 */
@SuppressWarnings("deprecation")
public class GetUserInfo extends HttpServlet implements SingleThreadModel {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public GetUserInfo() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
	
			try{
						String uri = request.getRequestURI();
						User user = new User();
						
						if (uri.indexOf("name") != -1)
						{
						String nickname = uri.substring(uri.indexOf("name") + 5);
						user = user.getUserByNickname(nickname);
						}
						else
						{
							HttpSession session = request.getSession(true);
							//get session variable
							user = (User) session.getAttribute("user");
						}
						response.setContentType("application/json; charset=UTF-8");
						PrintWriter out = response.getWriter();
						if(user != null)
						{
							user = user.getUserByNickname(user.getNickname());
							Gson gson = new Gson();
							String customerJsonResult = gson.toJson(user);
							out.println(customerJsonResult);
						}
						else
						{
							out.println("{ \"id\": \"null\"}");	
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
