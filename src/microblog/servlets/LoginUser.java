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

import microblog.model.User;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

/**
 * Servlet implementation class LoginUser
 */
@SuppressWarnings("deprecation")
public class LoginUser extends HttpServlet implements SingleThreadModel {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public LoginUser() {
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
	
		User exist;
		HttpSession session = null;
		//loop over inputStream
		BufferedReader br = new BufferedReader(new InputStreamReader(request.getInputStream(),"UTF-8"));
		StringBuilder jsonFileContent = new StringBuilder();
		//read line by line from file
		String nextLine = null;
		while ((nextLine = br.readLine()) != null){
			jsonFileContent.append(nextLine);
		}

		
		Gson gson = new GsonBuilder().create();
        User u = gson.fromJson(jsonFileContent.toString(),User.class);
        
        exist = u.isUserExist(u.getUsername(), u.getPassword());
       
        
        try {
        	
        	response.setContentType("application/json; charset=UTF-8");
        	PrintWriter out = response.getWriter();
        	
            if(exist != null){
            	session = request.getSession();
    			session.setAttribute("user", exist);
    			out.println("{ \"result\": \"success\"}");
    			out.close();
            }
            else{
            	
            	out.println("{ \"result\": \"fail\"}");
            	out.close();
            
            }
            	
        } catch (IOException e) {  
            e.printStackTrace();  
        }
        
		
	}

}
