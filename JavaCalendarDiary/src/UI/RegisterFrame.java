package UI;

import javax.swing.*;
import javax.swing.event.*;
import java.awt.*;
import java.awt.event.*;
import db.UsersDAO;
import java.sql.*;

public class RegisterFrame extends JFrame {
	private JPanel panel = new JPanel(new FlowLayout());
    private JTextField userIdField;  // 사용자 아이디 입력 필드
    private JPasswordField passwordField;  // 비밀번호 입력 필드
    private JButton canelButton;  // 입력 데이터 취소 버튼
    private JButton registerButton;  // 회원가입 버튼
    RunEx runEx;

    public RegisterFrame(RunEx _r) {
    	runEx = _r;
    	
    	setTitle("회원가입");
    	setLocationRelativeTo(null);
    	setSize(400, 150);
    	
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

        // 회원가입 버튼
        registerButton = new JButton("Enter");
        registerButton.setPreferredSize(new Dimension(185, 30));
        panel.add(registerButton);

        // 입력데이터 삭제 버튼
        canelButton = new JButton("Cancel");
        canelButton.setPreferredSize(new Dimension(185, 30));
        panel.add(canelButton);
        
        add(panel);
        setVisible(false);

        // 회원가입 버튼 클릭 시 처리
        registerButton.addActionListener(e -> {
            UsersDAO userDB = new UsersDAO();
        	try {
                String inputId = userIdField.getText().trim();  // 사용자가 입력한 ID
                String password = new String(passwordField.getPassword()).trim(); // 사용자가 입력한 비밀번호

                if (inputId.isEmpty() || password.isEmpty()) {
                    JOptionPane.showMessageDialog(this, "아이디와 비밀번호를 모두 입력해주세요.");
                    return;
                }
                // 데이터베이스에서 회원가입
                userDB.insertUser(inputId, password);
                dispose();
            } catch (Exception ex) {
                // 예기치 않은 오류 처리
                ex.printStackTrace();
                JOptionPane.showMessageDialog(this, "회원가입 중 오류가 발생했습니다. 다시 시도해주세요.");
            }
        });
        
        canelButton.addActionListener(e ->{
        	userIdField.setText("");
        	passwordField.setText("");
        });
    }
    
}
