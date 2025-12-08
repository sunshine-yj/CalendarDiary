package db;

import java.sql.*;


public class DBConnect {
	
	String address ="jdbc:mysql://nsyun.synology.me:3306/db";
	String uid = "user";
	String pwd = "user1234";
	String jdbc_driver = "com.mysql.cj.jdbc.Driver";
	
	
	DBConnect() {
		try {
			Class.forName(jdbc_driver);
		}catch(ClassNotFoundException e) {
			System.out.println("->JDBC Driver 오류");
			e.printStackTrace();
		}
	}
	
	
	public Connection getConnection() {
		Connection conn = null;
		try {
			conn = DriverManager.getConnection(address, uid, pwd);
		}catch(Exception e) {
			e.printStackTrace();
		}
		
		return conn;
	}
	
	
}
