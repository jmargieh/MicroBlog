package microblog.model;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;

import microblog.constants.AppConstants;

import org.apache.tomcat.dbcp.dbcp.BasicDataSource;

public class Topic {
	private String topic;
	private int count;
	
	
	public Topic(String topic, int count)
	{
		this.topic = topic;
		this.count = count;
	}
	
	public void increaseCount(Topic topic)
	{
		topic.count++;
	}
	

	public static Comparator<Topic> COMPARE_BY_TOPIC_COUNT = new Comparator<Topic>() {
        public int compare(Topic one, Topic other) {
        	int result = other.count - one.count;
            return result;
        }
    };
	
	static private void HandleTopic(String topic, ArrayList<Topic> topicsArrayList)
	{
		for (Topic t : topicsArrayList)
		{
			if(t.topic.compareTo(topic) == 0)
			{
				t.increaseCount(t);
				return;
			}
		}
		topicsArrayList.add(new Topic(topic,1));
	}
	
	// a function that extracts topic and call HandleTopic to check if exists to increase counter or add it to the list
	static private void ExtractTopics(String msgContent, ArrayList<Topic> topicsArrayList)
	{

			String topicSave[];
			String[] txtSplit = msgContent.split("#");
			
			for(int j=1; j < txtSplit.length; j++){
				topicSave = txtSplit[j].split("\\s");
				HandleTopic(topicSave[0], topicsArrayList);
			}	
	}
	
	/**
	 * @return ArrayList of size 10 at most of Topics mentioned all time.
	 * @throws SQLException if SQL command syntax is not ok, or statement not valid, nullPointer...
	 * @throws NamingException if constants name are not valid.
	 **/
	static public ArrayList<Topic> GetMostPopularTopics()
	{
		try{
			//obtain MicroBlogDB data source from Tomcat's context
			Context context = new InitialContext();
			
			BasicDataSource ds = (BasicDataSource)context.lookup(AppConstants.DB_DATASOURCE);
			Connection conn = ds.getConnection();
			
			ArrayList<Topic> TopicsArrayList = new ArrayList<Topic>();
			PreparedStatement stmt;
			
			
			try {
				stmt = conn.prepareStatement(AppConstants.SELECT_ALL_MESSAGES_STMT_NO_USERS);
				
				ResultSet rs = stmt.executeQuery();
				//loop over ResultSet -- start getString from index 7 since we've used an INNER JOIN query
				while (rs.next()){
					ExtractTopics(rs.getString(5), TopicsArrayList);
					}
				
				//Close Connections
				rs.close();
				stmt.close();
				conn.close();
				
				Collections.sort(TopicsArrayList,COMPARE_BY_TOPIC_COUNT);
				
				if(TopicsArrayList.size() > 10)
					TopicsArrayList = new ArrayList<Topic>(TopicsArrayList.subList(0, 10));
				
				return TopicsArrayList;
			}
			catch(SQLException e)
			{
				System.err.println(e.getMessage()); // print error to error stream
			}
			
			}
			catch (SQLException e) {
				System.err.println(e.getMessage()); // print error to error stream
	    	}
	    	catch(NamingException e)
	    	{
	    		System.err.println(e.getMessage()); // print error to error stream
	    	}
			
			return null; // if Debugger reached here means Database is still empty.
		
		
		
	}
	
	
	public String getTopic() {
		return topic;
	}
	public void setTopic(String topic) {
		this.topic = topic;
	}
	public int getCount() {
		return count;
	}
	public void setCount(int count) {
		this.count = count;
	}
	
	
	

}
