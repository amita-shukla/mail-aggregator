package database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DB_Connection {
	
	private static DB_Connection instance;
	private DB_Connection(){}
	
	public String DRIVER="com.mysql.jdbc.Driver";	
	private String DBURL="jdbc:mysql://localhost:3306/mail_application";
	private String DBUSER="root";
	private String DBPASSWORD="";
	
	public static DB_Connection getInstance(){
		if(instance==null)
		{
			instance = new DB_Connection();
		}
		return instance;
	}
	
	public Connection getConnection(){
		try {
			Class.forName(DRIVER);
			Connection con = DriverManager.getConnection(DBURL, DBUSER, DBPASSWORD);
			return con;
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
		catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
		
	}
}
