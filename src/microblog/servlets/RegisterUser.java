package microblog.servlets;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.SingleThreadModel;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import microblog.model.User;

/**
 * Servlet implementation class RegisterUser
 */
@SuppressWarnings("deprecation")
public class RegisterUser extends HttpServlet implements SingleThreadModel {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public RegisterUser() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
				HttpSession session = null;
				boolean registered;
				//wrap input stream with a buffered reader to allow reading the file line by line
				BufferedReader br = new BufferedReader(new InputStreamReader(request.getInputStream(),"UTF-8"));
				StringBuilder jsonFileContent = new StringBuilder();
				//read line by line from file
				String nextLine = null;
				while ((nextLine = br.readLine()) != null){
					jsonFileContent.append(nextLine);
				}

				
				Gson gson = new GsonBuilder().create();
	            User u = gson.fromJson(jsonFileContent.toString(),User.class);
	            
	            // Register the user
	            registered = u.RegisterUser(u.getUsername(), u.getPassword(), u.getNickname(), u.getDescription(), u.getPhotoURL(), 0 , 0);
	            u = u.getUserByNickname(u.getNickname());
	            
	            try {
	            	response.setContentType("application/json; charset=UTF-8");
	            	PrintWriter out = response.getWriter();
	                if(registered){
	                  	session = request.getSession();
	        			session.setAttribute("user", u);
	                	out.println("{ \"result\": \"registered\"}");
	                	out.close();
	                }
	                else{
	                	out.println("{ \"result\": \"exist\"}");
	                	out.close();
	                }
	                	
	            } catch (IOException e) {  
	                e.printStackTrace();  
	            }
		
	}

}
