package microblog.constants;

public interface AppConstants {

	//derby constants
	public final String DB_NAME = "MicroBlogDB";
	public final String DB_DATASOURCE = "java:comp/env/jdbc/MicroBlogDatasource";
	public final String PROTOCOL = "jdbc:derby:"; 
	
	
	//SQL statements
	public final String SELECT_ALL_USERS_STMT = "SELECT * FROM USERS";
	
	public final String SELECT_USER_BY_userID_STMT = "SELECT * FROM USERS "
			+ "WHERE userID=?";
	
	public final String SELECT_USER_BY_USERNAME_STMT = "SELECT * FROM USERS "
			+ "WHERE UserName=? OR Nickname=?";
	
	public final String SELECT_USER_BY_nickname_STMT = "SELECT * FROM USERS "
			+ "WHERE Nickname=?";
	
	
	public final String SELECT_USER_BY_USERNAME_AND_PASS_STMT = "SELECT * FROM USERS "
			+ "WHERE UserName=? AND Password=?";
	
	public final String INSERT_USER_TO_USER_STMT = "INSERT INTO USERS(Username, Password, Nickname, Description, PhotoURL, Following_count, Follower_count)"
			+ "VALUES(?,?,?,?,?,?,?)";
	
	public final String SELECT_ALL_FOLLOWING_OF_USERID = "SELECT * FROM FOLLOWING WHERE userID=?";
	
	public final String SELECT_USER_FOLLOWERS_BY_userID_STMT = "SELECT * FROM FOLLOWER INNER JOIN USERS ON "
			+ "FOLLOWER.Follower_userID = USERS.userID WHERE FOLLOWER.userID=?";
	
	// Select All followers of certain users ordered by follower_count descending.
	public final String SELECT_USER_TOP10FOLLOWERS_BY_userID_STMT = "SELECT * FROM FOLLOWER INNER JOIN USERS ON "
			+ "FOLLOWER.Follower_userID = USERS.userID WHERE FOLLOWER.userID=? ORDER BY USERS.follower_count DESC";
	
	public final String SELECT_USER_FOLLOWING_BY_userID_STMT = "SELECT * FROM FOLLOWING INNER JOIN USERS ON "
			+ "FOLLOWING.Following_userID = USERS.userID WHERE FOLLOWING.userID=?";
	
	// Select All following of certain users ordered by follower_count descending.
	public final String SELECT_USER_TOP10FOLLOWING_BY_userID_STMT = "SELECT * FROM FOLLOWING INNER JOIN USERS ON "
			+ "FOLLOWING.Following_userID = USERS.userID WHERE FOLLOWING.userID=? ORDER BY USERS.follower_count DESC";
	
	public final String SELECT_ALL_MESSAGES_STMT = "SELECT * FROM MESSAGES INNER JOIN USERS ON "
			+ "MESSAGES.Posted_by_userID = USERS.userID ORDER BY MESSAGES.msgID DESC";
	
	public final String SELECT_ALL_MESSAGES_BY_USERID_STMT = "SELECT * FROM MESSAGES INNER JOIN USERS ON "
			+ "MESSAGES.Posted_by_userID = USERS.userID WHERE MESSAGES.Posted_by_userID=? ORDER BY MESSAGES.msgID DESC";
	
	public final String SELECT_MESSAGES_EXCLUDE_USERID_STMT = "SELECT * FROM MESSAGES INNER JOIN USERS ON "
			+ "MESSAGES.Posted_by_userID = USERS.userID WHERE MESSAGES.Posted_by_userID <> ? ORDER BY MESSAGES.msgID DESC";

	
	public final String INSERT_FOLLOWER_TO_USER_STMT = "INSERT INTO FOLLOWING(userID, Following_userID) VALUES(?,?)";
	
	public final String INSERT_FOLLOWING_TO_USER_STMT = "INSERT INTO FOLLOWER(userID, Follower_userID) VALUES(?,?)";
	
	public final String UPDATE_FOLLOWING_COUNT_STMT = "UPDATE USERS SET following_count=following_count+1 WHERE userID=?";
	
	public final String UPDATE_FOLLOWER_COUNT_STMT = "UPDATE USERS SET follower_count=follower_count+1 WHERE userID=?";
	
	public final String SELECT_USER_AND_FOLLOWER_FROM_FOLLOWING_STMT = "SELECT * FROM FOLLOWING WHERE userID=? AND Following_userID=?";
	
	
	
	
	public final String SELECT_MESSAGES_BY_userID_STMT = "SELECT * FROM MESSAGES WHERE Posted_by_userID=?";
	
	public final String SELECT_ALL_MESSAGES_STMT_NO_USERS = "SELECT * FROM MESSAGES";
	
	public final String INSERT_MSG_TO_MESSAGES_STMT = "INSERT INTO MESSAGES(Posted_by_userID, Posted_by_Nickname, Date, Content, Republished_count)"
			+ "VALUES(?,?,?,?,?)";
	
	public final String UPDATE_MSG_REPUBLISHED_COUNT_STMT = "UPDATE MESSAGES SET Republished_count=Republished_count+1 WHERE msgID=?";
	
	public final String SELECT_MESSAGES_BY_USERID_STMT = "SELECT * FROM MESSAGES "
			+ "WHERE Posted_by_userID=? ORDER BY MESSAGES.msgID DESC";
	
	
	
}
