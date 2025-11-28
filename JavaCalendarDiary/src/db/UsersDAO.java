package db;

import java.sql.*;


public class UsersDAO {
	
	DBConnect dbc = new DBConnect();
	Connection conn = null;
	Statement stmt = null;
	PreparedStatement pstmt= null;
	ResultSet rs = null;
	
	
	// 회원가입 메소드 
	public boolean insertUser(String _uid, String _pwd) {
		
		boolean bool = false;
		
		conn = dbc.getConnection();
		String query = "insert into users_t16 values(?,?)";
		
		try {
			
			pstmt = conn.prepareStatement(query);
			
			pstmt.setString(1, _uid);
			pstmt.setString(2, _pwd);
			
			int updated = pstmt.executeUpdate();
			if(updated == 1) {
				System.out.println("-> " + _uid + " 회원가입에 성공함");
				bool = true;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		removeAll();
		return bool;
	}
	
	
	// 로그인 메소드 
	public String loginUser(String _uid, String _pwd) {
		
		conn = dbc.getConnection();
		String query = "select pwd from users_t16 where user_id = ? and pwd = ?";
		String user_id = null;
		
		try {
			
			pstmt = conn.prepareStatement(query);
			
			pstmt.setString(1, _uid);
			pstmt.setString(2, _pwd);
			rs = pstmt.executeQuery();
			
			
			while(rs.next()) {
				user_id = rs.getString(1);
				System.out.println();
			}
			

			if(user_id != null) {
				System.out.println("-> " + _uid + " 로그인에 성공함");
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		removeAll();
		return user_id;
	}
	
	
	// DB 연결 자원 해제
	private void removeAll() {
		try {
			if(rs != null) 
				rs.close();	
			if(stmt != null)
				stmt.close();
			if(pstmt != null)
				pstmt.close();
			if(conn != null)
				conn.close();
					
		}catch(Exception e) {
			e.printStackTrace();
		}
	}
	
}
