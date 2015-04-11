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

import microblog.model.Message;
import microblog.model.User;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

/**
 * Servlet implementation class GetTop10MessagesOfUserFollowing
 */
@SuppressWarnings("deprecation")
public class GetTop10MessagesOfUserFollowing extends HttpServlet implements SingleThreadModel {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public GetTop10MessagesOfUserFollowing() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
	
		try{
			
			//Obtain the session object, create a new session if doesn't exist
			HttpSession session = request.getSession(true);
			
			//get session variable
			User user = (User) session.getAttribute("user");
			
			ArrayList<Message> MsgsArray = new ArrayList<Message>();
			
			response.setContentType("application/json; charset=UTF-8");
			PrintWriter out = response.getWriter();
			
			if (user != null)
			{
			MsgsArray = Message.getTop10MsgsOfUserFollowing(Integer.parseInt(user.getUserID())); // get arrayList of To 10 messages user fol.
			}
			
			Gson gson = new Gson();
	    	//convert from Messages ArrayList to json
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
