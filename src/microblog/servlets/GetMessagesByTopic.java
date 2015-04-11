package microblog.servlets;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;

import javax.servlet.ServletException;
import javax.servlet.SingleThreadModel;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import microblog.model.Message;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

/**
 * Servlet implementation class GetMessagesByTopic
 */
@SuppressWarnings("deprecation")
public class GetMessagesByTopic extends HttpServlet implements SingleThreadModel {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public GetMessagesByTopic() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
	
		try{
			
			String uri = request.getRequestURI();
			
			ArrayList<Message> MsgsArray = new ArrayList<Message>();
			
			response.setContentType("application/json; charset=UTF-8");
			PrintWriter out = response.getWriter();
			// if /topic exist
			if (uri.indexOf("topic") != -1)
			{
				String topic = uri.substring(uri.indexOf("topic") + 6);
					if(topic != ""){
					MsgsArray = Message.GetMessagesByTopic(topic); // get all messages with topic
					}
			}
			
			Gson gson = new Gson();
	    	//convert from followers ArrayList to json
	    	String msgsJsonResult = gson.toJson(MsgsArray, new TypeToken<ArrayList<Message>>() {}.getType());
	    	out.println(msgsJsonResult);
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
