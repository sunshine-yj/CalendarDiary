package UI;

import javax.swing.*;
import javax.swing.event.*;
import java.awt.*;
import java.awt.event.*;
import db.UsersDAO;
import java.sql.*;

public class LoginFrame extends JFrame {
	private JPanel panel = new JPanel(new FlowLayout());
    private JTextField userIdField;  // 사용자 아이디 입력 필드
    private JPasswordField passwordField;  // 비밀번호 입력 필드
    private JButton loginButton;  // 로그인 버튼
    private JButton registerButton;  // 회원가입 버튼
    RunEx runEx;
    static String _uid;

    public LoginFrame(RunEx _r) {
    	runEx = _r;
    	setTitle("로그인");
    	setSize(400, 150);
    	setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
        // 아이디 입력 레이블 및 필드
        JLabel idLabel = new JLabel("아이디:");
        idLabel.setPreferredSize(new Dimension(70, 30));
        panel.add(idLabel);

        userIdField = new JTextField();
        userIdField.setPreferredSize(new Dimension(300, 30));
        panel.add(userIdField);

        // 비밀번호 입력 레이블 및 필드
        JLabel passLabel = new JLabel("비밀번호:");
        passLabel.setPreferredSize(new Dimension(70, 30));
        panel.add(passLabel);

        passwordField = new JPasswordField();
        passwordField.setPreferredSize(new Dimension(300, 30));
        panel.add(passwordField);

        // 로그인 버튼
        loginButton = new JButton("로그인");
        loginButton.setPreferredSize(new Dimension(185, 30));
        panel.add(loginButton);

        // 회원가입 버튼
        registerButton = new JButton("회원가입");
        registerButton.setPreferredSize(new Dimension(185, 30));
        panel.add(registerButton);
        
        add(panel);
        setVisible(true);

        // 로그인 버튼 클릭 시 처리
        loginButton.addActionListener(e -> {
            UsersDAO userDB = new UsersDAO();
        	try {
                String inputId = userIdField.getText().trim();  // 사용자가 입력한 ID
                String password = new String(passwordField.getPassword()).trim(); // 사용자가 입력한 비밀번호

                if (inputId.isEmpty() || password.isEmpty()) {
                    JOptionPane.showMessageDialog(this, "아이디와 비밀번호를 모두 입력해주세요.");
                    return;
                }

                // 데이터베이스에서 로그인 인증
                int userId = userDB.loginUser(inputId, password);// 수정함
                if (userId != -1) {
                	_uid = inputId;
                    // 화면 전환
                	runEx.calendarapp = new CalendarApp(runEx, _uid);
                	runEx.calendarapp.setVisible(true);
                	dispose();
                } else {
                    // 로그인 실패
                    JOptionPane.showMessageDialog(this, "로그인 실패. 아이디 또는 비밀번호를 확인해주세요.");
                }
            } catch (Exception ex) {
                // 예기치 않은 오류 처리
                ex.printStackTrace();
                JOptionPane.showMessageDialog(this, "로그인 중 오류가 발생했습니다. 다시 시도해주세요.");
            }
        });

        // 회원가입 버튼 클릭 시 처리
        registerButton.addActionListener(e -> {
        	runEx.registerFrame.setVisible(true);
        });
    }
    
}
