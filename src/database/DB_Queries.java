package database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Calendar;
import java.util.GregorianCalendar;


public class DB_Queries {

	// create a private and static instance of DB_Queries class
	private static DB_Queries instance = new DB_Queries();

	// make the constructor private so that this class cannot be instantiated
	private DB_Queries() {
	}

	// get the only instance available
	public static DB_Queries getInstance() {
		return instance;
	}

	// get the instance of DB_Connection class
	DB_Connection db_connection = DB_Connection.getInstance();

	// establish connection
	private Connection con = db_connection.getConnection();

	private PreparedStatement ps;

	public ResultSet selectUserForLogin(String email, String password)
			throws SQLException {
		ResultSet rs = null;

		ps = con.prepareStatement("select count(*) as cnt, activated from user where email=? and password=?;");
		ps.setString(1, email);
		ps.setString(2, password);
		rs = ps.executeQuery();

		// System.out.println("Exception while Selecting user: ");

		return rs;
	}

	public boolean registerUser(String name, String email, String password,
			String temp_password) throws SQLException {

		ps = con.prepareStatement("insert into user(email, name, password, activated, created_on, temp_password) values (?,?,?,?,?,?)");
		ps.setString(1, email);
		ps.setString(2, name);
		// have to encrypt password here

		ps.setString(3, password);
		ps.setBoolean(4, false);

		// set current date
		// DateFormat dateFormat = new
		// SimpleDateFormat("yyyy-mm-dd HH:mm:ss");
		Calendar cal = new GregorianCalendar();
		Timestamp ts = new Timestamp(cal.getTimeInMillis());
		ps.setTimestamp(5, ts);
		ps.setString(6, temp_password);
		if (ps.executeUpdate() == 1) {
			return true;
		}

		// System.out.println("Exception while Inserting: ");

		return false;
	}

	public ResultSet selectTokenForConfirmation(String email)
			throws SQLException {
		ResultSet rs = null;

		ps = con.prepareStatement("select user_id, temp_password from user where email=?");
		ps.setString(1, email);
		rs = ps.executeQuery();

		// System.out.println("Exception while select user_id and temp_password");

		return rs;

	}

	public boolean updateActivated(int user_id, String token)
			throws SQLException {

		ps = con.prepareStatement("update user set activated=1 where user_id=? and temp_password=?");
		ps.setInt(1, user_id);
		ps.setString(2, token);
		if (ps.executeUpdate() == 1)
			return true;
		return false;

	}

	public ResultSet selectReceiverEmail(int user_id) throws SQLException {
		ResultSet rs = null;

		ps = con.prepareStatement("select email from user where user_id=?");
		ps.setInt(1, user_id);
		rs = ps.executeQuery();

		// System.out.println("Exception while fetching email id of recipient user");

		return rs;
	}

	public boolean addEmail(int user_id, String email, String password,
			String accountAccessType, String incoming_email_server,
			String outgoing_email_server, int incoming_email_port,
			int outgoing_email_port, boolean require_ssl) throws SQLException {

		ps = con.prepareStatement("insert into added_email(user_id, email, password, account_access_type, incoming_email_server, outgoing_email_server, incoming_email_port, outgoing_email_port, requires_ssl) values (?, ?, ?,?,?,?,?,?,?)");
		ps.setInt(1, user_id);
		ps.setString(2, email);
		ps.setString(3, password);
		ps.setString(4, accountAccessType);
		ps.setString(5, incoming_email_server);
		ps.setString(6, outgoing_email_server);
		ps.setInt(7, incoming_email_port);
		ps.setInt(8, outgoing_email_port);
		ps.setBoolean(9, require_ssl);
		if (ps.executeUpdate() == 1) {
			return true;
		}

		return false;
	}

	public ResultSet selectEmail(int user_id) throws SQLException {
		ResultSet rs = null;

		ps = con.prepareStatement("select email, password, account_access_type, incoming_email_server, incoming_email_port, requires_ssl from added_email where user_id = ?");
		ps.setInt(1, user_id);
		rs = ps.executeQuery();
		System.out.println("Executed query");

		// System.out.println("Exception while selecting added Emails");

		return rs;
	}

	/**
	 * Insert the token for the given user id. to be used when the user logs in.
	 * 
	 * @param user_id
	 * @param token
	 * @return
	 * @throws SQLException
	 */
	public boolean insertToken(int user_id, int token) throws SQLException {

		Calendar cal = new GregorianCalendar();
		Timestamp ts = new Timestamp(cal.getTimeInMillis());

		ps = con.prepareStatement("insert into token(user_id, token, timestamp) values (?,?,?)");
		ps.setInt(1, user_id);
		ps.setInt(2, token);
		ps.setTimestamp(3, ts);

		if (ps.executeUpdate() == 1) {
			return true;
		}

		// System.out.println("Exception while Inserting: ");

		return false;
	}

	/**
	 * For the given email and password passed, select the user id corresponding
	 * to it
	 * 
	 * @param email
	 * @param password
	 * @return
	 * @throws SQLException
	 */
	public ResultSet selectUserId(String email, String password)
			throws SQLException {
		ResultSet rs = null;

		ps = con.prepareStatement("select user_id from user where email=? and password=?");
		ps.setString(1, email);
		ps.setString(2, password);
		rs = ps.executeQuery();

		// System.out.println("Exception while fetching email id of recipient user");

		return rs;
	}

	public ResultSet selectUserFromToken(int accessToken) throws SQLException {
		ResultSet rs = null;
		ps = con.prepareStatement("select user_id, timestamp from token where token=?");
		ps.setInt(1, accessToken);
		rs = ps.executeQuery();
		return rs;
	}

	public ResultSet selectUserIdFromToken(int accessToken) throws SQLException {
		ResultSet rs = null;
		ps = con.prepareStatement("select user_id from token where token=?");
		ps.setInt(1, accessToken);
		rs = ps.executeQuery();
		return rs;
	}

	public boolean deleteToken(int accessToken) throws SQLException {
		ps = con.prepareStatement("delete from token where token=?");
		ps.setInt(1, accessToken);
		if (ps.executeUpdate() == 1)
			return true;
		else
			return false;
	}
}
