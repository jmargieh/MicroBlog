package microblog.model;

import java.util.ArrayList;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;

import org.apache.tomcat.dbcp.dbcp.BasicDataSource;

import microblog.constants.AppConstants; // import AppConstants
import microblog.model.Message;


public class User {
	private String userID;
	private String Username;
	private String Password;
	private String Nickname;
	private String Description;
	private String PhotoURL; // check if null return default error in getPhotoURL func
	private int Following_count;
	private int Follower_count;
	
	
	public User(String userID, String Username, String Password, String Nickname, String Description, String PhotoURL, int Following_count, int Follower_count)
	{
		this.setUserID(userID);
		this.setUsername(Username);
		this.setPassword(Password);
		this.setNickname(Nickname);
		this.setDescription(Description);
		this.setPhotoURL(PhotoURL);
		this.setFollowing_count(Following_count);
		this.setFollower_count(Follower_count);
		
	}
	
	public User(String Username, String Password, String Nickname, String Description, String PhotoURL, int Following_count, int Follower_count)
	{
		this.setUsername(Username);
		this.setPassword(Password);
		this.setNickname(Nickname);
		this.setDescription(Description);
		this.setPhotoURL(PhotoURL);
		this.setFollowing_count(Following_count);
		this.setFollower_count(Follower_count);
		
	}
	
	
	public User() {
		// TODO Auto-generated constructor stub
	}

	/**
	 * @return ArrayList of all users in the Database.
	 * @throws SQLException SQL syntax error,NullPointer,...
	 * @throws NamingException if constants name are not valid.
	 **/
	public ArrayList<User> getAllUsers()
	{
		try{
		//obtain DB data source from Tomcat's context
		Context context = new InitialContext();
		
		BasicDataSource ds = (BasicDataSource)context.lookup(AppConstants.DB_DATASOURCE);
		Connection conn = ds.getConnection();
		
		ArrayList<User> UsersArrayList = new ArrayList<User>();
		PreparedStatement stmt;
		
		try {
			stmt = conn.prepareStatement(AppConstants.SELECT_ALL_USERS_STMT);
			
			ResultSet rs = stmt.executeQuery();
			//loop over ResultSet and add to ArrayList
			while (rs.next()){
				UsersArrayList.add(new User(rs.getString(1),rs.getString(2),rs.getString(3),rs.getString(4),rs.getString(5), rs.getString(6),rs.getInt(7),rs.getInt(8)));
			}
			
			//Close Connections
			rs.close();
			stmt.close();
			conn.close();
			return UsersArrayList;
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

	/**
	 * @param nickname user nickname
	 * @return a User object with all details about user with specified nickname
	 * @throws SQLException SQL syntax error,NullPointer,...
	 * @throws NamingException if constants name are not valid.
	 **/
	public User getUserByNickname(String nickname)
	{
		
		User tempUser = null;
		
		try{
			//obtain CustomerDB data source from Tomcat's context
			Context context = new InitialContext();
			
			BasicDataSource ds = (BasicDataSource)context.lookup(AppConstants.DB_DATASOURCE);
			Connection conn = ds.getConnection();
			
			PreparedStatement stmt;
			
			try {
				stmt = conn.prepareStatement(AppConstants.SELECT_USER_BY_nickname_STMT);
				stmt.setString(1,nickname);
				
				ResultSet rs = stmt.executeQuery();
				//loop over ResultSet - ResultSet will contain only one result since Nickname is unique in the system
				while (rs.next()){
				tempUser = new User(rs.getString(1),rs.getString(2),rs.getString(3),rs.getString(4),rs.getString(5), rs.getString(6),rs.getInt(7),rs.getInt(8));
				}
				
				//Close Connections
				rs.close();
				stmt.close();
				conn.close();
				
				return tempUser;
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
		return tempUser;
	}

	/**
	 * @param userID user Id
	 * @return a User object with all details about user with specified userID
	 * @throws SQLException SQL syntax error,NullPointer,...
	 * @throws NamingException if constants name are not valid.
	 **/
	public User getUserByID(int userID)
	{
		
		User tempUser = null;
		
		try{
			//obtain CustomerDB data source from Tomcat's context
			Context context = new InitialContext();
			
			BasicDataSource ds = (BasicDataSource)context.lookup(AppConstants.DB_DATASOURCE);
			Connection conn = ds.getConnection();
			
			PreparedStatement stmt;
			
			try {
				stmt = conn.prepareStatement(AppConstants.SELECT_USER_BY_userID_STMT);
				stmt.setInt(1,userID);
				
				ResultSet rs = stmt.executeQuery();
				//loop over ResultSet
				while (rs.next()){
				tempUser = new User(rs.getString(1),rs.getString(2),rs.getString(3),rs.getString(4),rs.getString(5), rs.getString(6),rs.getInt(7),rs.getInt(8));
				}
				
				//Close Connections
				rs.close();
				stmt.close();
				conn.close();
				
				return tempUser;
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
		return tempUser;
	}

	/**
	 * @param userID user user Id
	 * @return ArrayList of Users with all user Followers
	 * @throws SQLException SQL syntax error,NullPointer,...
	 * @throws NamingException if constants name are not valid.
	 **/
	public ArrayList<User> getUserFollowers(int userID)
	{
		try{
			//obtain CustomerDB data source from Tomcat's context
			Context context = new InitialContext();
			
			BasicDataSource ds = (BasicDataSource)context.lookup(AppConstants.DB_DATASOURCE);
			Connection conn = ds.getConnection();
			
			ArrayList<User> FollowersArrayList = new ArrayList<User>();
			PreparedStatement stmt;
			
			try {
				stmt = conn.prepareStatement(AppConstants.SELECT_USER_FOLLOWERS_BY_userID_STMT);
				stmt.setInt(1,userID);
				
				ResultSet rs = stmt.executeQuery();
				//loop over ResultSet -- start getString from index 4 since we've used an INNER JOIN query
				while (rs.next()){
					FollowersArrayList.add(new User(rs.getString(4),rs.getString(5),rs.getString(6),rs.getString(7),rs.getString(8), rs.getString(9),rs.getInt(10),rs.getInt(11)));
				}
				
				//Close Connections
				rs.close();
				stmt.close();
				conn.close();
				return FollowersArrayList;
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

	/**
	 * @param userID user user Id
	 * @return ArrayList of Users with all user Following
	 * @throws SQLException SQL syntax error,NullPointer,...
	 * @throws NamingException if constants name are not valid.
	 **/
	public ArrayList<User> getUserFollowing(int userID)
	{
		try{
			//obtain CustomerDB data source from Tomcat's context
			Context context = new InitialContext();
			
			BasicDataSource ds = (BasicDataSource)context.lookup(AppConstants.DB_DATASOURCE);
			Connection conn = ds.getConnection();
			
			ArrayList<User> FollowingArrayList = new ArrayList<User>();
			PreparedStatement stmt;
			
			try {
				stmt = conn.prepareStatement(AppConstants.SELECT_USER_FOLLOWING_BY_userID_STMT);
				stmt.setInt(1,userID);
				
				ResultSet rs = stmt.executeQuery();
				//loop over ResultSet -- start getString from index 4 since we've used an INNER JOIN query
				while (rs.next()){
					FollowingArrayList.add(new User(rs.getString(4),rs.getString(5),rs.getString(6),rs.getString(7),rs.getString(8), rs.getString(9),rs.getInt(10),rs.getInt(11)));
				}
				
				//Close Connections
				rs.close();
				stmt.close();
				conn.close();
				return FollowingArrayList;
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

	/**
	 * @param userID user user Id
	 * @return ArrayList of Messages posted by the user.
	 * @throws SQLException SQL syntax error,NullPointer,...
	 * @throws NamingException if constants name are not valid.
	 **/
	public ArrayList<Message> getUserMessages(int userID)
	{
		try{
			//obtain CustomerDB data source from Tomcat's context
			Context context = new InitialContext();
			
			BasicDataSource ds = (BasicDataSource)context.lookup(AppConstants.DB_DATASOURCE);
			Connection conn = ds.getConnection();
			
			ArrayList<Message> MessageArrayList = new ArrayList<Message>();
			PreparedStatement stmt;
			
			try {
				stmt = conn.prepareStatement(AppConstants.SELECT_MESSAGES_BY_userID_STMT);
				stmt.setInt(1,userID);
				
				ResultSet rs = stmt.executeQuery();
				//loop over ResultSet -- start getString from index 4 since we've used an INNER JOIN query
				while (rs.next()){
					MessageArrayList.add(new Message(rs.getString(1),rs.getString(2),rs.getString(3),rs.getString(4),rs.getString(4),rs.getInt(6)));
				}
				
				//Close Connections
				rs.close();
				stmt.close();
				conn.close();
				return MessageArrayList;
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

	/**
	 * @param userID user user Id
	 * @return ArrayList of Users with Top 10 user Followers ordered by followers count
	 * @throws SQLException SQL syntax error,NullPointer,...
	 * @throws NamingException if constants name are not valid.
	 **/
	public ArrayList<User> getUserTop10Followers(int userID)
	{
		try{
			//obtain CustomerDB data source from Tomcat's context
			Context context = new InitialContext();
			
			BasicDataSource ds = (BasicDataSource)context.lookup(AppConstants.DB_DATASOURCE);
			Connection conn = ds.getConnection();
			
			ArrayList<User> TopFollowersArrayList = new ArrayList<User>();
			PreparedStatement stmt;
			
			try {
				stmt = conn.prepareStatement(AppConstants.SELECT_USER_TOP10FOLLOWERS_BY_userID_STMT);
				stmt.setInt(1,userID);
				stmt.setMaxRows(10); // we need the top 10, limiting the ResultSet
				
				ResultSet rs = stmt.executeQuery();
				//loop over ResultSet -- start getString from index 4 since we've used an INNER JOIN query
				while (rs.next()){
					TopFollowersArrayList.add(new User(rs.getString(4),rs.getString(5),rs.getString(6),rs.getString(7),rs.getString(8), rs.getString(9),rs.getInt(10),rs.getInt(11)));
				}
				
				//Close Connections
				rs.close();
				stmt.close();
				conn.close();
				return TopFollowersArrayList;
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

	/**
	 * @param userID user user Id
	 * @return ArrayList of Users with Top 10 user Following ordered by followers count
	 * @throws SQLException SQL syntax error,NullPointer,...
	 * @throws NamingException if constants name are not valid.
	 **/
	public ArrayList<User> getUserTop10Following(int userID)
	{
		try{
			//obtain CustomerDB data source from Tomcat's context
			Context context = new InitialContext();
			
			BasicDataSource ds = (BasicDataSource)context.lookup(AppConstants.DB_DATASOURCE);
			Connection conn = ds.getConnection();
			
			ArrayList<User> TopFollowingArrayList = new ArrayList<User>();
			PreparedStatement stmt;
			
			try {
				stmt = conn.prepareStatement(AppConstants.SELECT_USER_TOP10FOLLOWING_BY_userID_STMT);
				stmt.setInt(1,userID);
				stmt.setMaxRows(10); // we need the top 10
				
				ResultSet rs = stmt.executeQuery();
				//loop over ResultSet -- start getString from index 4 since we've used an INNER JOIN query
				while (rs.next()){
					TopFollowingArrayList.add(new User(rs.getString(4),rs.getString(5),rs.getString(6),rs.getString(7),rs.getString(8), rs.getString(9),rs.getInt(10),rs.getInt(11)));
				}
				
				//Close Connections
				rs.close();
				stmt.close();
				conn.close();
				return TopFollowingArrayList;
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
	
	/**
	 * @param userID user user Id
	 * @param followedID followed user Id
	 * @return true if the Follow succeeded, false otherwise (userID is followed by followedID)
	 * @throws SQLException SQL syntax error,NullPointer,...
	 * @throws NamingException if constants name are not valid.
	 **/
	public boolean AddFollowerToUser(int userID, int followedID)
	{
		boolean updated=false;
		
		try{
			//obtain DB data source from Tomcat's context
			Context context = new InitialContext();
			
			BasicDataSource ds = (BasicDataSource)context.lookup(AppConstants.DB_DATASOURCE);
			Connection conn = ds.getConnection();
			
			PreparedStatement stmt,fstmt;
			
			try {
				
					stmt = conn.prepareStatement(AppConstants.INSERT_FOLLOWING_TO_USER_STMT);
					stmt.setInt(1,userID);
					stmt.setInt(2,followedID);
					
					fstmt = conn.prepareStatement(AppConstants.UPDATE_FOLLOWER_COUNT_STMT);
					fstmt.setInt(1,userID);
					
					//execute query commit changes and close insert statement
					fstmt.executeUpdate();
					stmt.executeUpdate();
					conn.commit();
					fstmt.close();
					stmt.close();
					
					updated = true;
				
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
		
		return updated;
	}

	
	// call this func to Add following to userID
	/**
	 * @param userID user user Id
	 * @param followingID following user Id
	 * @return true if the Follow succeeded, false otherwise (userID is Following followingID)
	 * @throws SQLException SQL syntax error,NullPointer,...
	 * @throws NamingException if constants name are not valid.
	 **/
	public boolean AddFollowingToUser(int userID, int followingID)
	{
		boolean added=false,updated=false;
		
		try{
			//obtain CustomerDB data source from Tomcat's context
			Context context = new InitialContext();
			
			BasicDataSource ds = (BasicDataSource)context.lookup(AppConstants.DB_DATASOURCE);
			Connection conn = ds.getConnection();
			
			PreparedStatement stmt,pstmt,fstmt;
			
			try {
				
				pstmt = conn.prepareStatement(AppConstants.SELECT_USER_AND_FOLLOWER_FROM_FOLLOWING_STMT);
				pstmt.setInt(1,userID);
				pstmt.setInt(2, followingID);
				
				ResultSet rs = pstmt.executeQuery();
				
				// userID is not following followingID already then perform insert query
				if(rs.next() == false){
					
					stmt = conn.prepareStatement(AppConstants.INSERT_FOLLOWER_TO_USER_STMT);
					stmt.setInt(1,userID);
					stmt.setInt(2, followingID);
					
					fstmt = conn.prepareStatement(AppConstants.UPDATE_FOLLOWING_COUNT_STMT);
					fstmt.setInt(1,userID);
					
					//execute query commit changes and close insert statement
					fstmt.executeUpdate();
					stmt.executeUpdate();
					conn.commit();
					fstmt.close();
					stmt.close();
					
					updated = AddFollowerToUser(followingID, userID);
					added = true;
				}
				
				//release rs object close select statement and close connection with DB
				rs.close();
				pstmt.close();
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
		
		return (added && updated);
		
	}

	/**
	 * @param Username user name
	 * @param password Password
	 * @param Nickname nickname
	 * @param Description Description
	 * @param PhotoURL photo url
	 * @param Following_count following count initialization
	 * @param Follower_count Follower count initialization
	 * @return ArrayList of Users with Top 10 user Followers ordered by followers count
	 * @throws SQLException SQL syntax error,NullPointer,...
	 * @throws NamingException if constants name are not valid.
	 **/
	public boolean RegisterUser(String Username, String password, String Nickname, String Description, String PhotoURL, int Following_count ,int Follower_count)
	{
		boolean added=false;
		
		try{
			//obtain CustomerDB data source from Tomcat's context
			Context context = new InitialContext();
			
			BasicDataSource ds = (BasicDataSource)context.lookup(AppConstants.DB_DATASOURCE);
			Connection conn = ds.getConnection();
			
			PreparedStatement stmt,pstmt;
			
			try {
				
				pstmt = conn.prepareStatement(AppConstants.SELECT_USER_BY_USERNAME_STMT);
				pstmt.setString(1,Username);
				pstmt.setString(2,Nickname);
				
				
				ResultSet rs = pstmt.executeQuery();
				
				// userID is not following followingID already then perform insert query
				if(!rs.next()){
					
					stmt = conn.prepareStatement(AppConstants.INSERT_USER_TO_USER_STMT);
					stmt.setString(1,Username);
					stmt.setString(2, password);
					stmt.setString(3, Nickname);
					stmt.setString(4, Description);
					stmt.setString(5, PhotoURL);
					stmt.setInt(6,Following_count); // should be 0
					stmt.setInt(7,Follower_count); // should be 0
					
					//execute query commit changes and close insert statement
					stmt.executeUpdate();
					conn.commit();
					stmt.close();
					
					added = true;
				}
				
				//release rs object close select statement and close connection with DB
				rs.close();
				pstmt.close();
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
	 * @param Username user name
	 * @param password password
	 * @return User user if exist, null otherwise
	 * @throws SQLException SQL syntax error,NullPointer,...
	 * @throws NamingException if constants name are not valid.
	 **/
	public User isUserExist(String Username, String password)
	{
		User tempUser=null;
		
		try{
			//obtain CustomerDB data source from Tomcat's context
			Context context = new InitialContext();
			
			BasicDataSource ds = (BasicDataSource)context.lookup(AppConstants.DB_DATASOURCE);
			Connection conn = ds.getConnection();
			
			PreparedStatement pstmt;
			
			try {
				
				pstmt = conn.prepareStatement(AppConstants.SELECT_USER_BY_USERNAME_AND_PASS_STMT);
				pstmt.setString(1,Username);
				pstmt.setString(2,password);
				
				
				ResultSet rs = pstmt.executeQuery();
				
				if(rs.next()){
					tempUser = new User(rs.getString(1),rs.getString(2),rs.getString(3),rs.getString(4),rs.getString(5), rs.getString(6),rs.getInt(7),rs.getInt(8));
				}
				
				//release rs object close select statement and close connection with DB
				rs.close();
				pstmt.close();
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
		
		return tempUser;

	}
	
	/* Getters & Setters */
	
	public String getUserID() {
		return userID;
	}

	public void setUserID(String userID) {
		this.userID = userID;
	}
	

	public String getUsername() {
		return Username;
	}
	

	public void setUsername(String username) {
		Username = username;
	}

	public String getPassword() {
		return Password;
	}

	public void setPassword(String password) {
		Password = password;
	}

	public String getNickname() {
		return Nickname;
	}

	public void setNickname(String nickname) {
		Nickname = nickname;
	}

	public String getDescription() {
		return Description;
	}

	public void setDescription(String description) {
		Description = description;
	}

	public String getPhotoURL() {
		return PhotoURL;
	}

	public void setPhotoURL(String photoURL) {
		PhotoURL = photoURL;
	}

	public int getFollowing_count() {
		return Following_count;
	}

	public void setFollowing_count(int following_count) {
		Following_count = following_count;
	}

	public int getFollower_count() {
		return Follower_count;
	}

	public void setFollower_count(int follower_count) {
		Follower_count = follower_count;
	}
	
	/* Getters & Setters */
}
