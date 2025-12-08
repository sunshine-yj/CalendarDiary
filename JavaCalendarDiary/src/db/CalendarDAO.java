package db;

import java.sql.*;
import java.util.ArrayList;


public class CalendarDAO {
	
	DBConnect dbc = new DBConnect();
	private Connection conn = null;
	private Statement stmt = null;
	private PreparedStatement pstmt= null;
	private ResultSet rs = null;
	
	
	// 메모 입력 메소드
	public void getCalendarMemo(String _memo, Date _date, String _uid) {
		conn = dbc.getConnection();
		String query = "insert into calendar_t16(user_id, memo_date, text) values(?,?,?)";
		
		try {
			pstmt = conn.prepareStatement(query);
			
			pstmt.setString(1, _uid);
			pstmt.setDate(2, _date);
			pstmt.setString(3, _memo);
			
			int updated = pstmt.executeUpdate();
			if(updated == 1) {
				System.out.println();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		removeAll();
	}
	
	
	// 입력된 메모를 불러오는 메소드
	public ArrayList<String> viewCalendarMemo(String _uid, Date _date) {
		String memo = null;
		ArrayList<String> list = new ArrayList<>();
		conn = dbc.getConnection();
		String query = "select text from calendar_t16 where user_id = ? and memo_date = ?";
		
		try {
			pstmt = conn.prepareStatement(query);
			pstmt.setString(1, _uid);
			pstmt.setDate(2, _date);
			rs = pstmt.executeQuery();
			
			while(rs.next()) {
				// 해당 메모 출력
				list.add(rs.getString(1));
			}
		} catch(Exception e) {
			e.printStackTrace();
		} finally {
			removeAll();
		}
		
		return list;
	}
	
		
	
	// 메모 삭제 메소드
	public void deleteCalendarMemo(String _uid, Date _date, String _text) {
		conn = dbc.getConnection();
		String query = "delete from calendar_t16 where user_id = ? and memo_date = ? and text = ? ";
		
		try {
			pstmt = conn.prepareStatement(query);
			pstmt.setString(1, _uid);
			pstmt.setDate(2, _date);
			pstmt.setString(3, _text);
			pstmt.executeUpdate();
			
		} catch(Exception e) {
			e.printStackTrace();
		}
		
		removeAll();
	}
	
	
	// 메모 수정 메소드
	public void updateCalendarMemo(String _uid, Date _date, String _memo, String _oldmemo) {
		conn = dbc.getConnection();
		String query = "update calendar_t16 set text = ? where user_id = ? and memo_date = ? and text = ?";
		try {
			pstmt = conn.prepareStatement(query);
			pstmt.setString(1, _memo);
			pstmt.setString(2, _uid);
			pstmt.setDate(3, _date);
			pstmt.setString(4, _oldmemo);
			pstmt.executeUpdate();
			
		} catch(Exception e) {
			e.printStackTrace();
		}
		
		removeAll();
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
