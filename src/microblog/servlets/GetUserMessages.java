package microblog.servlets;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;

import javax.servlet.ServletException;
import javax.servlet.SingleThreadModel;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import microblog.model.Message;
import microblog.model.User;

/**
 * Servlet implementation class GetUserMessages
 */
@SuppressWarnings("deprecation")
public class GetUserMessages extends HttpServlet implements SingleThreadModel {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public GetUserMessages() {
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
			
			ArrayList<Message> MsgsArray = new ArrayList<Message>();
			
			response.setContentType("application/json; charset=UTF-8");
			PrintWriter out = response.getWriter();
			
			// if name exist in url
			if (uri.indexOf("name") != -1)
			{
			String nickname = uri.substring(uri.indexOf("name") + 5);
			user = user.getUserByNickname(nickname);
			MsgsArray = Message.getTop10MsgsOfUserID(Integer.parseInt(user.getUserID()));
			}
			// if name does not exist in url
			else
			{
				HttpSession session = request.getSession(true);
				//get session variable
				user = (User) session.getAttribute("user");
				MsgsArray = Message.getTop10MsgsOfUserID(Integer.parseInt(user.getUserID()));
			}
			
			Gson gson = new Gson();
	    	//convert from messages ArrayList to json
	    	String userJsonResult = gson.toJson(MsgsArray, new TypeToken<ArrayList<Message>>() {}.getType());
	    	out.println(userJsonResult);
	    	out.close();
			
			
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
