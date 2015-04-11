package microblog.model;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;

import microblog.constants.AppConstants;

import org.apache.tomcat.dbcp.dbcp.BasicDataSource;

import microblog.model.TimeHelper;
import microblog.model.User;

public class Message implements Comparable<Message> {
	private String msgID;
	private String Posted_by_userID;
	private String Posted_by_Nickname;
	private String pubDate;
	private String Content;
	private int Republished_count;
	private double popularity;
	private User user;
	
	public Message(String msgID,String Posted_by_userID, String Posted_by_Nickname, String pubDate, String Content, int Republished_count)
	{
		this.setMsgID(msgID);
		this.setPosted_by_userID(Posted_by_userID);
		this.setPosted_by_Nickname(Posted_by_Nickname);
		this.setDate(pubDate);
		this.setContent(Content);
		this.setRepublished_count(Republished_count);
	}
	
	public Message(String msgID,String Posted_by_userID, String Posted_by_Nickname, String pubDate, String Content, int Republished_count, User user)
	{
		this.setMsgID(msgID);
		this.setPosted_by_userID(Posted_by_userID);
		this.setPosted_by_Nickname(Posted_by_Nickname);
		this.setDate(pubDate);
		this.setContent(Content);
		this.setRepublished_count(Republished_count);
		this.setUserOfMessage(new User(user.getUsername(), user.getPassword(), user.getNickname(), user.getDescription(), user.getPhotoURL(), user.getFollowing_count(), user.getFollower_count()));
	}
	
	public Message(String msgID,String Posted_by_userID, String Posted_by_Nickname, String pubDate, String Content, int Republished_count,double popularity, User user)
	{
		this.setMsgID(msgID);
		this.setPosted_by_userID(Posted_by_userID);
		this.setPosted_by_Nickname(Posted_by_Nickname);
		this.setDate(pubDate);
		this.setContent(Content);
		this.setRepublished_count(Republished_count);
		this.setPopularity(popularity);
		this.setUserOfMessage(new User(user.getUsername(), user.getPassword(), user.getNickname(), user.getDescription(), user.getPhotoURL(), user.getFollowing_count(), user.getFollower_count()));
	}

	
	public static Comparator<Message> COMPARE_BY_MSG_ID = new Comparator<Message>() {
        public int compare(Message one, Message other) {
        	int result = Integer.parseInt(other.msgID) - Integer.parseInt(one.msgID);
            return result;
        }
    };
	
	@Override
    public int compareTo(Message comparemsg) {
        double comparePopularity=((Message)comparemsg).getPopularity();
        /* For Ascending order*/
        double result = comparePopularity-this.popularity; // Descending order
        
        if(result == 0)
        	return 0;
        else if(result > 0)
        	return 1;
        else return -1;
   
    }
	
	/**
	 * @param topic Topic
	 * @param msgContent message content
	 * @return true if topic exist in content, false otherwise
	 **/
	static private boolean isTopicExist(String msgContent, String topic)
	{

			String topicSave[];
			String[] txtSplit = msgContent.split("#");
			
			for(int j=1; j < txtSplit.length; j++){
				topicSave = txtSplit[j].split("\\s");
				if(topicSave[0].compareTo(topic) == 0)
				{
				return true;
				}
			}	
				return false;
	}
	
	/**
	 * @param topic message topic
	 * @return ArrayList of size 10 at most of Messages containing the Topic
	 * @throws SQLException if SQL command syntax is not ok, or statement not valid, nullPointer...
	 * @throws ParseException if Parse was failed
	 * @throws NamingException if constants name are not valid.
	 **/
	static public ArrayList<Message> GetMessagesByTopic(String topic)
	{
		try{
			//obtain MicroBlogDB data source from Tomcat's context
			Context context = new InitialContext();
			
			BasicDataSource ds = (BasicDataSource)context.lookup(AppConstants.DB_DATASOURCE);
			Connection conn = ds.getConnection();
			
			ArrayList<Message> MessagesArrayList = new ArrayList<Message>();
			PreparedStatement stmt;
			
			
			try {
				stmt = conn.prepareStatement(AppConstants.SELECT_ALL_MESSAGES_STMT);
				
				ResultSet rs = stmt.executeQuery();
				//loop over ResultSet -- start getString from index 7 since we've used an INNER JOIN query
				while (rs.next()){
					if(isTopicExist(rs.getString(5),topic))
					{
					User user= new User(rs.getString(7),rs.getString(8),rs.getString(9),rs.getString(10),rs.getString(11), rs.getString(12),rs.getInt(13),rs.getInt(14));
					MessagesArrayList.add(new Message(rs.getString(1), rs.getString(2), rs.getString(3), rs.getString(4), rs.getString(5), rs.getInt(6), user));
					}
					}
				
				//Close Connections
				rs.close();
				stmt.close();
				conn.close();
				
				Date dNow = new Date(); // DateTime Now
				long diffInMillis;
				
				//loop over ArrayList
				for (Message m : MessagesArrayList)
				{
					DateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm");
					Date postDate = formatter.parse(m.getDate());
					diffInMillis = dNow.getTime() - postDate.getTime(); // calculate difference in MilliSeconds relative to 1970..
					m.setDate( TimeHelper.millisToLongDHMS(diffInMillis,m.getDate()) ); // Call Time Helper and set the new date in the array
				}
				
				if(MessagesArrayList.size() > 10)
					MessagesArrayList = new ArrayList<Message>(MessagesArrayList.subList(0, 10));
				
				return MessagesArrayList;
			}
			catch(SQLException e)
			{
				System.err.println(e.getMessage()); // print error to error stream
			} catch (ParseException e) {
				System.err.println(e.getMessage());
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
	
	/**
	 * @param postedByUserID the user id
	 * @param  Nickname user nickname
	 * @param Content message content
	 * @return : True if the message was posted successfully.
	 * @throws ParseException if Parse was failed
	 * @throws NamingException if constants name are not valid.
	 **/
	static public boolean PostNewMsg(int postedByUserID, String Nickname ,String Content)
	{
		boolean added=false;
		
		try{
			//obtain DB data source from Tomcat's context
			Context context = new InitialContext();
			
			BasicDataSource ds = (BasicDataSource)context.lookup(AppConstants.DB_DATASOURCE);
			Connection conn = ds.getConnection();
			
			PreparedStatement stmt;
			
			try {
				
					
					stmt = conn.prepareStatement(AppConstants.INSERT_MSG_TO_MESSAGES_STMT);
					
					Date dNow = new Date();
				    SimpleDateFormat ft = new SimpleDateFormat ("dd/MM/yyyy HH:mm");
					
				    stmt.setInt(1, postedByUserID);
				    stmt.setString(2, Nickname);
					stmt.setString(3, ft.format(dNow)); // format date to String
					stmt.setString(4, Content);
					stmt.setInt(5, 0); // republished set to 0
					
					//execute query commit changes and close insert statement
					stmt.executeUpdate();
					conn.commit();
					stmt.close();
					
					added = true;
				
				//close connection with DB
				conn.close();
				
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
		
		return added;
	}
	

	/**
	 * @param republishedMsgID Message ID to be republished
	 * @param postedByUserID the republisher user id
	 * @param Nickname republisher Nickname
	 * @param content Message content
	 * @return : True if the message was republished successfully
	 * @throws : SQLException, ParseException if Parse was failed, NamingException if constants name are not valid.
	 **/
	static public boolean RepublishMsg(int republishedMsgID, int postedByUserID, String Nickname ,String content)
	{
		boolean updated=false, added=false;
		
		try{
			//obtain CustomerDB data source from Tomcat's context
			Context context = new InitialContext();
			
			BasicDataSource ds = (BasicDataSource)context.lookup(AppConstants.DB_DATASOURCE);
			Connection conn = ds.getConnection();
			
			PreparedStatement fstmt;
			
			try {
					
					added = PostNewMsg(postedByUserID,Nickname,content);
					
					fstmt = conn.prepareStatement(AppConstants.UPDATE_MSG_REPUBLISHED_COUNT_STMT);
					fstmt.setInt(1,republishedMsgID);
					
					//execute query commit changes and close insert statement
					fstmt.executeUpdate();
					conn.commit();
					fstmt.close();
					
					//close connection with DB
					conn.close();
					
					updated = true;
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
		
		return (added && updated);
	}

	
	/**
	 * @param userID user ID
	 * @return ArrayList of last 10 messages posted by user id.
	 * @throws SQLException SQL syntax error,NullPointer,...
	 * @throws NamingException if constants name are not valid.
	 **/
	static public ArrayList<Message> getTop10MsgsOfUserID(int userID)
	{
		try{
			//obtain DB data source from Tomcat's context
			Context context = new InitialContext();
			
			BasicDataSource ds = (BasicDataSource)context.lookup(AppConstants.DB_DATASOURCE);
			Connection conn = ds.getConnection();
			
			ArrayList<Message> MessagesArrayList = new ArrayList<Message>();
			PreparedStatement stmt;
			
			try {
				stmt = conn.prepareStatement(AppConstants.SELECT_MESSAGES_BY_USERID_STMT);
				stmt.setInt(1,userID);
				stmt.setMaxRows(10); // we need the top 10 by date
				
				ResultSet rs = stmt.executeQuery();
				//loop over ResultSet
				while (rs.next()){
					MessagesArrayList.add(new Message(rs.getString(1),rs.getString(2),rs.getString(3),rs.getString(4),rs.getString(5),rs.getInt(6)));
				}
				
				//Close Connections
				rs.close();
				stmt.close();
				conn.close();
				
				Date dNow = new Date(); // DateTime Now
				long diffInMillis;
				
				/*
				 * loop over ArrayList & parse Date according to how long ago the message
				 * was posted using a class TimeHelper.
				*/
				for (Message m : MessagesArrayList)
				{
					DateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm");
					Date postDate = formatter.parse(m.getDate());
					diffInMillis = dNow.getTime() - postDate.getTime(); // calculate difference in MilliSeconds relative to 1970..
					m.setDate( TimeHelper.millisToLongDHMS(diffInMillis,m.getDate()) ); // Call Time Helper and set the new date in the array
				}
				
				
				return MessagesArrayList;
			}
			catch(SQLException e)
			{
				System.err.println(e.getMessage()); // print error to error stream
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			}
			catch (SQLException e) {
				System.err.println(e.getMessage()); // print error to error stream
	    	}
	    	catch(NamingException e)
	    	{
	    		System.err.println(e.getMessage()); // print error to error stream
	    	}
			
			return null; // if reached here means Database is still empty.
	}

	/**
	 * @param userID user ID
	 * @return ArrayList of last 10 popular messages posted by the user's following
	 * @throws SQLException SQL syntax error,NullPointer,...
	 * @throws NamingException if constants name are not valid.
	 **/
	static public ArrayList<Message> getTop10MsgsOfUserFollowing(int userID)
	{
		try{
			//obtain DB data source from Tomcat's context
			Context context = new InitialContext();
			
			BasicDataSource ds = (BasicDataSource)context.lookup(AppConstants.DB_DATASOURCE);
			Connection conn = ds.getConnection();
			
			ArrayList<Message> MessagesArrayList = new ArrayList<Message>();
			PreparedStatement stmt,nstmt;
			
			try {
				stmt = conn.prepareStatement(AppConstants.SELECT_ALL_FOLLOWING_OF_USERID);
				stmt.setInt(1,userID);
				
				
				
				//stmt.setMaxRows(10); // we need the top 10 by date
				
				double msgPopularity = 0;
				ResultSet rs = stmt.executeQuery();
				
				while(rs.next())
				{
					nstmt = conn.prepareStatement(AppConstants.SELECT_ALL_MESSAGES_BY_USERID_STMT);
					nstmt.setInt(1,rs.getInt(3));
					ResultSet nrs = nstmt.executeQuery();
					//loop over ResultSet calculate Message Popularity and add to Messages ArrayList.
					while (nrs.next()){
						User user= new User(nrs.getString(7),nrs.getString(8),nrs.getString(9),nrs.getString(10),nrs.getString(11), nrs.getString(12),nrs.getInt(13),nrs.getInt(14));
						msgPopularity = (Math.log10(2 + user.getFollower_count()) / Math.log10(2) ) * (Math.log10(2 + nrs.getInt(6)) / Math.log10(2) );
						MessagesArrayList.add(new Message(nrs.getString(1), nrs.getString(2), nrs.getString(3), nrs.getString(4), nrs.getString(5), nrs.getInt(6), msgPopularity ,user));
					}
				}
				
				
				//Close Connections
				rs.close();
				stmt.close();
				conn.close();
				
				Date dNow = new Date(); // DateTime Now
				long diffInMillis;
				
				/*
				 * loop over ArrayList & parse Date according to how long ago the message
				 * was posted using a class TimeHelper.
				*/
				for (Message m : MessagesArrayList)
				{
					DateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm");
					Date postDate = formatter.parse(m.getDate());
					diffInMillis = dNow.getTime() - postDate.getTime(); // calculate difference in MilliSeconds relative to 1970..
					m.setDate( TimeHelper.millisToLongDHMS(diffInMillis,m.getDate()) ); // Call Time Helper and set the new date in the array
				}
				
				/*
				 *Sort ArrayList By Popularity (from higher to lower) CompareTo was
				 *overrided in advance to support sorting objects
				 *check if ArrayList size bigger than 10, then subList it
				 *and Finally use COMPARE_BY_MSG_ID to Sort messages by creation
				 *time since bigger id means newer msg
				 */
				Collections.sort(MessagesArrayList);
				if(MessagesArrayList.size() > 10)
				MessagesArrayList = new ArrayList<Message>(MessagesArrayList.subList(0, 10));
				
				Collections.sort(MessagesArrayList, Message.COMPARE_BY_MSG_ID);
				
				return MessagesArrayList;
			}
			catch(SQLException e)
			{
				System.err.println(e.getMessage()); // print error to error stream
			} catch (ParseException e) {
				System.err.println(e.getMessage());
			}
			
			}
			catch (SQLException e) {
				System.err.println(e.getMessage()); // print error to error stream
	    	}
	    	catch(NamingException e)
	    	{
	    		System.err.println(e.getMessage()); // print error to error stream
	    	}
			
			return null; // if reached here means Database is still empty.
	}

	/**
	 * @param userID user ID
	 * @return ArrayList of last 10 popular messages posted by all user excluding the user ID
	 * @throws SQLException SQL syntax error,NullPointer,...
	 * @throws NamingException if constants name are not valid.
	 **/
	static public ArrayList<Message> getTopMsgsExcludeUserID(int userID)
	{
		try{
			//obtain DB data source from Tomcat's context
			Context context = new InitialContext();
			
			BasicDataSource ds = (BasicDataSource)context.lookup(AppConstants.DB_DATASOURCE);
			Connection conn = ds.getConnection();
			
			ArrayList<Message> MessagesArrayList = new ArrayList<Message>();
			PreparedStatement stmt;
			
			try {
				stmt = conn.prepareStatement(AppConstants.SELECT_MESSAGES_EXCLUDE_USERID_STMT);
				stmt.setInt(1,userID);
				//stmt.setMaxRows(10); // we need the top 10 by date
				
				double msgPopularity = 0;
				ResultSet rs = stmt.executeQuery();
				//loop over ResultSet calculate Message Popularity and add to Messages ArrayList.
				while (rs.next()){
					User user= new User(rs.getString(7),rs.getString(8),rs.getString(9),rs.getString(10),rs.getString(11), rs.getString(12),rs.getInt(13),rs.getInt(14));
					msgPopularity = (Math.log10(2 + user.getFollower_count()) / Math.log10(2) ) * (Math.log10(2 + rs.getInt(6)) / Math.log10(2) );
					MessagesArrayList.add(new Message(rs.getString(1), rs.getString(2), rs.getString(3), rs.getString(4), rs.getString(5), rs.getInt(6), msgPopularity ,user));
				}
				
				//Close Connections
				rs.close();
				stmt.close();
				conn.close();
				
				Date dNow = new Date(); // DateTime Now
				long diffInMillis;
				
				/*
				 * loop over ArrayList & parse Date according to how long ago the message
				 * was posted using a class TimeHelper.
				*/
				for (Message m : MessagesArrayList)
				{
					DateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm");
					Date postDate = formatter.parse(m.getDate());
					diffInMillis = dNow.getTime() - postDate.getTime(); // calculate difference in MilliSeconds relative to 1970..
					m.setDate( TimeHelper.millisToLongDHMS(diffInMillis,m.getDate()) ); // Call Time Helper and set the new date in the array
				}
				
				/*
				 *Sort ArrayList By Popularity (from higher to lower) CompareTo was
				 *overrided in advance to support sorting objects
				 *check if ArrayList size bigger than 10, then subList it
				 *and Finally use COMPARE_BY_MSG_ID to Sort messages by creation
				 *time since bigger id means newer msg
				 */
				Collections.sort(MessagesArrayList);
				if(MessagesArrayList.size() > 10)
					MessagesArrayList = new ArrayList<Message>(MessagesArrayList.subList(0, 10));
				
				Collections.sort(MessagesArrayList, Message.COMPARE_BY_MSG_ID);
				
				return MessagesArrayList;
			}
			catch(SQLException e)
			{
				System.err.println(e.getMessage()); // print error to error stream
			} catch (ParseException e) {
				System.err.println(e.getMessage());
			}
			
			}
			catch (SQLException e) {
				System.err.println(e.getMessage()); // print error to error stream
	    	}
	    	catch(NamingException e)
	    	{
	    		System.err.println(e.getMessage()); // print error to error stream
	    	}
			
			return null; // if reached here means Database is still empty.
	}

	

	public String getMsgID() {
		return msgID;
	}


	public void setMsgID(String msgID) {
		this.msgID = msgID;
	}


	
	public String getPosted_by_userID() {
		return Posted_by_userID;
	}


	
	public void setPosted_by_userID(String posted_by_userID) {
		Posted_by_userID = posted_by_userID;
	}


	
	public String getContent() {
		return Content;
	}


	
	public void setContent(String content) {
		Content = content;
	}


	
	public int getRepublished_count() {
		return Republished_count;
	}


	
	public void setRepublished_count(int republished_count) {
		Republished_count = republished_count;
	}

	public String getDate() {
		return pubDate;
	}

	public void setDate(String pubDate) {
		this.pubDate = pubDate;
	}

	public String getPosted_by_Nickname() {
		return Posted_by_Nickname;
	}

	public void setPosted_by_Nickname(String posted_by_Nickname) {
		Posted_by_Nickname = posted_by_Nickname;
	}

	public User getUserOfMessage() {
		return user;
	}

	public void setUserOfMessage(User user) {
		this.user = user;
	}

	public double getPopularity() {
		return popularity;
	}

	public void setPopularity(double popularity) {
		this.popularity = popularity;
	}
	
	
	
}
