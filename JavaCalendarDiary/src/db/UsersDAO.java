package db;

import java.sql.*;


public class UsersDAO {
	
	DBConnect dbc = new DBConnect();
	private Connection conn = null;
	private Statement stmt = null;
	private PreparedStatement pstmt= null;
	private ResultSet rs = null;
	
	
	// 회원가입 메소드 
	public void insertUser(String _uid, String _pwd) {
		conn = dbc.getConnection();
		String query = "insert into users_t16 values(?,?)";
		
		try {
			
			pstmt = conn.prepareStatement(query);
			
			pstmt.setString(1, _uid);
			pstmt.setString(2, _pwd);
			
			int updated = pstmt.executeUpdate();
			if(updated == 1) {
				System.out.println("-> " + _uid + " 회원가입에 성공함");
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			removeAll();
		}
	}
	
	
	// 로그인 메소드 
	public int loginUser(String _uid, String _pwd) { // 로그인에 맞게 수정
		int result = -1;
		conn = dbc.getConnection();
		String query = "select * from users_t16 where user_id = ? and pwd = ?";
		
		try {
			
			pstmt = conn.prepareStatement(query);
			
			pstmt.setString(1, _uid);
			pstmt.setString(2, _pwd);
			rs = pstmt.executeQuery();
		
			while(rs.next()) {
				System.out.println("-> " + _uid + " 로그인에 성공함");
				result = 1;
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			removeAll();
		}
		
		return result;
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
