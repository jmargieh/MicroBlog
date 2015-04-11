package microblog.servlets;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
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

import microblog.model.User;

/**
 * Servlet implementation class GetUserFollowingOrFollower
 */
@SuppressWarnings("deprecation")
public class GetUserFollowingOrFollower extends HttpServlet implements SingleThreadModel {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public GetUserFollowingOrFollower() {
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
		String uri = request.getRequestURI();
		
		if (uri.indexOf("type") != -1 && user != null){//filter customer by specific name
			
			String type = uri.substring(uri.indexOf("type") + 5);
			ArrayList<User> usersResult = new ArrayList<User>(); 
			
			response.setContentType("application/json; charset=UTF-8");
			PrintWriter out = response.getWriter();
			
			// if param sent in url is Followers
			if(type.compareTo("Followers") ==0 )
			{
				usersResult = user.getUserTop10Followers(Integer.parseInt(user.getUserID()));
				
				Gson gson = new Gson();
	        	//convert from followers ArrayList to json
	        	String userJsonResult = gson.toJson(usersResult, new TypeToken<ArrayList<User>>() {}.getType());
	        	out.println(userJsonResult);
	        	out.close();
			}
			// if param sent in url is Following
			else if(type.compareTo("Following") ==0 )
			{
				usersResult = user.getUserTop10Following(Integer.parseInt(user.getUserID()));
				
				Gson gson = new Gson();
	        	//convert from followers ArrayList to json
	        	String userJsonResult = gson.toJson(usersResult, new TypeToken<ArrayList<User>>() {}.getType());
	        	out.println(userJsonResult);
	        	out.close();
			}
			else{
				// Do Nothing - types passed should be strings either Following or Followers
			}
			
			
		}
		else {
			response.sendRedirect("../index.html"); // go back to login page user not logged in
		}
		
		
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		BufferedReader br = new BufferedReader(new InputStreamReader(request.getInputStream()));
		StringBuilder jsonFileContent = new StringBuilder();
		//read line by line from file
		String nextLine = null;
		while ((nextLine = br.readLine()) != null){
			jsonFileContent.append(nextLine);
		}
		
		
		Gson gson = new Gson(); 
		
		ArrayList<String> list = gson.fromJson(jsonFileContent.toString(), new TypeToken<ArrayList<String>>(){}.getType());
		
		User user = new User();
		
		ArrayList<User> usersResult = new ArrayList<User>();
		
		response.setContentType("application/json");
		PrintWriter out = response.getWriter();
		
		if(list.get(1).compareTo("loggedUser") == 0 )
		{
			//Obtain the session object, create a new session if doesn't exist
			HttpSession session = request.getSession(true);
			//get session variable
			user = (User) session.getAttribute("user");
		}
		else{
			user = user.getUserByNickname(list.get(1));
		}
		
		
		if(list.get(0).compareTo("Followers") == 0 )
		{
			usersResult = user.getUserTop10Followers(Integer.parseInt(user.getUserID()));
			
        	//convert from followers ArrayList to json
        	String userJsonResult = gson.toJson(usersResult, new TypeToken<ArrayList<User>>() {}.getType());
        	out.println(userJsonResult);
        	out.close();
		}
		else if(list.get(0).compareTo("Following") == 0 )
		{
			usersResult = user.getUserTop10Following(Integer.parseInt(user.getUserID()));
			
        	//convert from followers ArrayList to json
        	String userJsonResult = gson.toJson(usersResult, new TypeToken<ArrayList<User>>() {}.getType());
        	out.println(userJsonResult);
        	out.close();
		}
		else{
			// Do Nothing - types passed should be strings either Following or Followers
		}
		
		
		
	}

}
