package gui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.time.*;
import db.CalendarDAO;
import java.sql.*;


public class MainCalendarFrame extends JFrame{
	String _uid;
	JTextArea viewMemo;
	
	MainCalendarFrame() {
		setSize(500, 600);
		setTitle("CalendarMain");
		// 메인 화면 호출
		setCalendarMain();
		// 윈도우창 크기 조정 가능 여부		
		setResizable(true);
		// 윈도우창의 X 버튼을 눌렀을 때 프로그램 종료 처리
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		// 윈도우창을 화면 중앙으로 배치
		setLocationRelativeTo(null);
		setVisible(true);
	}
	
	
	// 캘린더 메모 메인 프레임 배정
	private void setCalendarMain() {
		JPanel calendarMemoPanel = new JPanel();
		calendarMemoPanel.setLayout(new BorderLayout());
		getMemoPanel(calendarMemoPanel);
		
		// 캘린더 일정 출력 패널 추가
		add(calendarMemoPanel);
		this.setVisible(true);
	}
	
	
	// 캘린더 프레임
	private void calendarFrame() {
		
	}
	
	
	// 캘린더 일정 출력 패널
	private void getMemoPanel(JPanel _calendarMemoPanel) {
		JPanel memoPanel = new JPanel();
		
		_calendarMemoPanel.add(memoPanel, BorderLayout.SOUTH);
		viewMemo = new JTextArea(6, 40);
		viewMemo.setEditable(false);
		
		getMemoDB();
		memoPanel.add(viewMemo);
	}
	
	
	// 캘린더 메모 출력
	void getMemoDB() {
		_uid = "id01";
		LocalDate today = java.time.LocalDate.now();
		Date dateToday = Date.valueOf(today);
		CalendarDAO calendarDB = new CalendarDAO();
		
		String memo = calendarDB.viewCalendarMemo(_uid, dateToday);
		
		if(memo == null) {
			viewMemo.setText("오늘 일정이 없습니다.");
		} else {
			viewMemo.setText(memo);
		}
		
	}
	
	
}
