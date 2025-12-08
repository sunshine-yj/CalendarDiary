package UI;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.time.LocalDate;
import java.time.YearMonth;
import java.util.ArrayList;
import java.util.HashMap;
import java.sql.*;
import db.*;

public class CalendarApp extends JFrame {
	
	RunEx runEx;
	String _uid;
    CardLayout cardLayout = new CardLayout();
    JPanel mainPanel = new JPanel(cardLayout);

    CalendarView calendarView;
    ScheduleView scheduleView;
    TodayScheduleView todayScheduleView;

    HashMap<LocalDate, ArrayList<String>> scheduleMap = new HashMap<>();

    public CalendarApp(RunEx _r, String _uid) {
    	this.runEx = _r;
    	this._uid = _uid;
        // ---------------------- ★ 한글 폰트 강제 적용 ----------------------
        Font korFont = new Font("Malgun Gothic", Font.PLAIN, 14);
        UIManager.put("Label.font", korFont);
        UIManager.put("Button.font", korFont);
        UIManager.put("List.font", korFont);
        UIManager.put("TextArea.font", korFont);
        UIManager.put("TextField.font", korFont);
        UIManager.put("ComboBox.font", korFont);
        UIManager.put("Panel.font", korFont);

        setTitle("Beautiful Calendar App");
        setSize(750, 700);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        calendarView = new CalendarView(this);
        scheduleView = new ScheduleView(this, runEx, _uid);
        todayScheduleView = new TodayScheduleView(this, runEx, _uid);
        
        JPanel calendarViewMain = new JPanel(new BorderLayout());
        calendarViewMain.add(calendarView, BorderLayout.CENTER);
        calendarViewMain.add(todayScheduleView, BorderLayout.SOUTH);

        
        mainPanel.add(calendarViewMain, "calendar");
        mainPanel.add(scheduleView, "schedule");
        

        add(mainPanel);

        setVisible(false);
    }

    // 화면 이동
    public void showCalendar() {
        calendarView.updateCalendar();
        cardLayout.show(mainPanel, "calendar");
    }

    public void showSchedule(LocalDate date) {
        scheduleView.setDate(date);
        scheduleView.updateScheduleList();
        cardLayout.show(mainPanel, "schedule");
    }

    // 일정 함수들
    public ArrayList<String> getSchedule(LocalDate date) {
        return scheduleMap.getOrDefault(date, new ArrayList<>());
    }

    public void addSchedule(LocalDate date, String text) {
        scheduleMap.putIfAbsent(date, new ArrayList<>());
        scheduleMap.get(date).add(text);
    }

    public void updateSchedule(LocalDate date, int index, String newText) {
        scheduleMap.get(date).set(index, newText);
    }

    public void deleteSchedule(LocalDate date, int index) {
        scheduleMap.get(date).remove(index);
    }

}


// ============================================================
// 캘린더 화면
// ============================================================
class CalendarView extends JPanel {
	
    CalendarApp app;
    YearMonth currentYM;
    JPanel dayPanel;

    public CalendarView(CalendarApp app) {
        this.app = app;
        this.currentYM = YearMonth.now();
        setLayout(new BorderLayout());

        // ---------- 상단 헤더 ----------
        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(new Color(240, 245, 255));
        header.setBorder(BorderFactory.createEmptyBorder(15, 20, 15, 20));

        JButton prev = new JButton("◀");
        JButton next = new JButton("▶");

        styleButton(prev);
        styleButton(next);

        JLabel title = new JLabel("", SwingConstants.CENTER);
        title.setFont(new Font("Malgun Gothic", Font.BOLD, 24));

        header.add(prev, BorderLayout.WEST);
        header.add(title, BorderLayout.CENTER);
        header.add(next, BorderLayout.EAST);

        prev.addActionListener(e -> {
            currentYM = currentYM.minusMonths(1);
            updateCalendar();
        });
        next.addActionListener(e -> {
            currentYM = currentYM.plusMonths(1);
            updateCalendar();
        });

        add(header, BorderLayout.NORTH);

        // ---------- 날짜 영역 ----------
        dayPanel = new JPanel();
        dayPanel.setLayout(new GridLayout(0, 7, 8, 8));
        dayPanel.setBorder(BorderFactory.createEmptyBorder(15, 20, 20, 20));

        add(dayPanel, BorderLayout.CENTER);

        updateCalendar();
    }

    private void styleButton(JButton b) {
        b.setFocusPainted(false);
        b.setBackground(new Color(220, 230, 255));
        b.setBorder(BorderFactory.createLineBorder(new Color(150, 160, 200)));
    }

    // 캘린더 업데이트
    public void updateCalendar() {
        dayPanel.removeAll();

        JPanel header = (JPanel) getComponent(0);
        JLabel title = (JLabel) ((BorderLayout) header.getLayout())
                .getLayoutComponent(BorderLayout.CENTER);

        title.setText(currentYM.getYear() + "년 " + currentYM.getMonthValue() + "월");

        // 요일
        String[] days = {"일", "월", "화", "수", "목", "금", "토"};
        for (String d : days) {
            JLabel lbl = new JLabel(d, SwingConstants.CENTER);
            lbl.setFont(new Font("Malgun Gothic", Font.BOLD, 14));
            lbl.setForeground(
                    d.equals("일") ? Color.RED :
                    d.equals("토") ? Color.BLUE : Color.DARK_GRAY
            );
            dayPanel.add(lbl);
        }

        LocalDate first = currentYM.atDay(1);
        int offset = first.getDayOfWeek().getValue() % 7;

        for (int i = 0; i < offset; i++)
            dayPanel.add(new JLabel(""));

        for (int i = 1; i <= currentYM.lengthOfMonth(); i++) {
            LocalDate date = currentYM.atDay(i);

            JButton btn = new JButton(String.valueOf(i));
            btn.setFocusPainted(false);
            btn.setBackground(Color.WHITE);

            if (app.getSchedule(date).size() > 0)
                btn.setBackground(new Color(230, 240, 255));

            btn.addActionListener(e -> app.showSchedule(date));

            dayPanel.add(btn);
        }

        dayPanel.revalidate();
        dayPanel.repaint();
    }
}

//============================================================
//금일 일정 화면
//============================================================
class TodayScheduleView extends JPanel {
	CalendarDAO cDao = new CalendarDAO();
	CalendarApp app;
	RunEx runEx;
	String _uid;
	JPanel todaySVeiw = new JPanel();
	
	Date today = Date.valueOf(LocalDate.now());
	
	DefaultListModel<String> listModel = new DefaultListModel<>();
    JList<String> list = new JList<>(listModel);
	
	public TodayScheduleView(CalendarApp app, RunEx _r, String _uid) {
		this.runEx = _r;
		this._uid = _uid;
		
		setLayout(new BorderLayout());
        list.setFont(new Font("Malgun Gothic", Font.PLAIN, 14));
        list.setPreferredSize(new Dimension(600, 150));
        add(new JScrollPane(list), BorderLayout.CENTER);

		
		refresh();
	}
	
	// 일정 최신화
	public void refresh() {
		listModel.clear();
		
		ArrayList<String> dbList = cDao.viewCalendarMemo(_uid, today);
		System.out.println(_uid);
		if(dbList == null) {
			listModel.addElement("오늘 일정은 없습니다.");
		}
		for(String memo : dbList) {
			listModel.addElement(memo);
		}
		
		revalidate();
		
	}
	
}

// ============================================================
// 일정 화면
// ============================================================
class ScheduleView extends JPanel {
	CalendarDAO cDao = new CalendarDAO();
	RunEx runEx;
	String _uid;
    CalendarApp app;
    LocalDate date;
    DefaultListModel<String> listModel = new DefaultListModel<>();
    JList<String> list;

    public ScheduleView(CalendarApp app, RunEx _r, String _uid) {
    	this.app = app;
        this.runEx = _r;
        this._uid = _uid;
        
        setLayout(new BorderLayout());

        // ---------- 상단 ----------
        JPanel top = new JPanel(new BorderLayout());
        top.setBackground(new Color(255, 245, 245));
        top.setBorder(BorderFactory.createEmptyBorder(15, 20, 15, 20));

        JButton back = new JButton("⟵ 뒤로가기");
        back.setFocusPainted(false);
        back.addActionListener(e -> app.showCalendar());

        JLabel title = new JLabel("일정 관리", SwingConstants.CENTER);
        title.setFont(new Font("Malgun Gothic", Font.BOLD, 22));

        top.add(back, BorderLayout.WEST);
        top.add(title);

        add(top, BorderLayout.NORTH);

        // ---------- 리스트 ----------
        list = new JList<>(listModel);
        list.setFont(new Font("Malgun Gothic", Font.PLAIN, 15));
        list.setPreferredSize(new Dimension(600, 500));
        add(new JScrollPane(list), BorderLayout.CENTER);

        // ---------- 하단 버튼 ----------
        JPanel bottom = new JPanel(new FlowLayout());

        JButton addBtn = new JButton("추가");
        JButton editBtn = new JButton("수정");
        JButton delBtn = new JButton("삭제");
        addBtn.setPreferredSize(new Dimension(185, 30));
        editBtn.setPreferredSize(new Dimension(185, 30));
        delBtn.setPreferredSize(new Dimension(185, 30));
        bottom.add(addBtn);
        bottom.add(editBtn);
        bottom.add(delBtn);

        add(bottom, BorderLayout.SOUTH);

        // 일정 추가
        addBtn.addActionListener(e -> {
            String text = JOptionPane.showInputDialog("일정 입력:");
            if (text != null && !text.trim().isEmpty()) {
                app.addSchedule(date, text);
                Date insertDay = Date.valueOf(date);
                cDao.getCalendarMemo(text, insertDay, _uid);
                updateScheduleList();
                app.todayScheduleView.refresh();
            }
        });

        // 일정 수정
        editBtn.addActionListener(e -> {
            int idx = list.getSelectedIndex();
            if (idx < 0) return;

            String old = listModel.get(idx);
            String text = JOptionPane.showInputDialog("수정:", old);

            if (text != null && !text.trim().isEmpty()) {
                app.updateSchedule(date, idx, text);
                Date insertDay = Date.valueOf(date);
                cDao.updateCalendarMemo(_uid, insertDay, text, old);
                updateScheduleList();
                app.todayScheduleView.refresh();
            }
        });

        // 일정 삭제
        delBtn.addActionListener(e -> {
            int idx = list.getSelectedIndex();
            if (idx < 0) return;
            
            String deleteMemo = listModel.get(idx);
            Date insertDay = Date.valueOf(date);
            cDao.deleteCalendarMemo(_uid, insertDay, deleteMemo);
            updateScheduleList();
            app.todayScheduleView.refresh();
        });
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public void updateScheduleList() {
        listModel.clear();
        
        ArrayList<String> dbList = cDao.viewCalendarMemo(_uid, Date.valueOf(date));
        app.scheduleMap.put(date, new ArrayList<>(dbList));
        for (String s : dbList)
            listModel.addElement(s);
    }
}
