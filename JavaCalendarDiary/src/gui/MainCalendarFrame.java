package gui;


import db.CalendarDAO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.time.*;
import java.sql.*;


public class MainCalendarFrame extends JFrame implements ActionListener{
	String _uid;
	LocalDate date;
	JTextArea viewMemo;
	JTextField calendarY, calendarM;
	JButton calendarButton, calendarMonthUp, calendarMonthDown;
	JPanel calendarPanel, calendarSwitchPanel;
	
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
		calendarFrame(calendarMemoPanel);
		
		// 캘린더 일정 출력 패널 추가
		add(calendarMemoPanel);
		this.setVisible(true);
	}
	
	
	// 캘린더 프레임
	private void calendarFrame(JPanel _calendarMemoPanel) {
		// 달력 배치
		calendarPanel = new JPanel(new BorderLayout());
		_calendarMemoPanel.add(calendarPanel, BorderLayout.CENTER);
		
		
		// 캘린더 상단바 월 출력 및 변경 버튼(North)
		calendarSwitchPanel = new JPanel();
		// 캘린더 버튼 (아직 이벤트 처리 안함)
		calendarMonthDown  = new JButton("<");
		calendarMonthUp = new JButton(">");
		calendarMonthDown.addActionListener(null);
		calendarMonthUp.addActionListener(null);
		// 캘린더 년,월 출력 패널
		date = LocalDate.now();
		calendarY = new JTextField(String.valueOf(date.getYear()));
		calendarM = new JTextField(String.valueOf(date.getMonth()));
		// 캘린더 상단바 배치
		calendarSwitchPanel.add(calendarMonthDown);
		calendarSwitchPanel.add(calendarY);
		calendarSwitchPanel.add(calendarM);
		calendarSwitchPanel.add(calendarMonthUp);
		calendarPanel.add(calendarSwitchPanel, BorderLayout.NORTH);
		
		
		// 캘린더 메인 버튼(Center)
		
		
	}
	
	// 캘린더 상단바 월 변경 버튼 이벤트 처리
	@Override
	public void actionPerformed(ActionEvent e) {
		if(e.getSource()==calendarMonthDown) {
			
		}
		if(e.getSource()==calendarMonthUp) {
			
		}
	}
	
	
	// 캘린더 금일 일정 출력 패널
	private void getMemoPanel(JPanel _calendarMemoPanel) {
		JPanel memoPanel = new JPanel();
		// 메모 출력 패널
		_calendarMemoPanel.add(memoPanel, BorderLayout.SOUTH);
		viewMemo = new JTextArea(6, 40);
		viewMemo.setEditable(false); // 메모 수정 불가
		// 캘린더 메모 호출
		getMemoDB();
		memoPanel.add(viewMemo);
	}
	
	
	// 캘린더 메모 호출(호출되는거 확인함)
	void getMemoDB() {
		CalendarDAO calendarDB = new CalendarDAO();
//		_uid = "id01"; // 회원 아이디를 로그인할때 따로 저장해야 함
		
		// 금일 날짜 
		LocalDate today = LocalDate.now();
		Date dateToday = Date.valueOf(today);
		// DB에서 금일 메모 호출
		String memo = calendarDB.viewCalendarMemo(_uid, dateToday);

		if(memo == null) {
			viewMemo.setText("오늘 일정이 없습니다.");
		} else {
			viewMemo.setText(memo);
		}
	}
	
	
}
