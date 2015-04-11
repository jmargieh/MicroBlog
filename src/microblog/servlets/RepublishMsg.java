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

import microblog.model.Message;
import microblog.model.User;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

/**
 * Servlet implementation class RepublishMsg
 */
@SuppressWarnings("deprecation")
public class RepublishMsg extends HttpServlet implements SingleThreadModel {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public RepublishMsg() {
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
	
		try{
		//wrap input stream with a buffered reader to allow reading the file line by line
		BufferedReader br = new BufferedReader(new InputStreamReader(request.getInputStream(),"UTF-8"));
		StringBuilder jsonFileContent = new StringBuilder();
		//read line by line from file
		String nextLine = null;
		while ((nextLine = br.readLine()) != null){
			jsonFileContent.append(nextLine);
		}
		
		//Obtain the session object, create a new session if doesn't exist
		HttpSession session = request.getSession(true);
		//get session variable
		User user = (User) session.getAttribute("user");
		
		response.setContentType("application/json; charset=UTF-8");
		PrintWriter out = response.getWriter();
		
		Gson gson = new GsonBuilder().create();
        Message m = gson.fromJson(jsonFileContent.toString(),Message.class);
        
        Message.RepublishMsg(Integer.parseInt(m.getMsgID()), Integer.parseInt(user.getUserID()), user.getNickname(), "RE: "+m.getContent());
		
        ArrayList<Message> MsgsArray = new ArrayList<Message>();
        MsgsArray = Message.getTop10MsgsOfUserID(Integer.parseInt(user.getUserID()));
        
        Gson tgson = new Gson();
    	//convert from Messages ArrayList to json
    	String userJsonResult = tgson.toJson(MsgsArray, new TypeToken<ArrayList<Message>>() {}.getType());
    	out.println(userJsonResult);
    	out.close();
    	
		}catch (IOException e) {  
	        e.printStackTrace();  
	    }
		
	}

}
