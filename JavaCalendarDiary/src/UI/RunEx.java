package UI;

public class RunEx {
	public LoginFrame loginPanel;
	public CalendarApp calendarapp;
	public RegisterFrame registerFrame;
	
	public static void main(String[] args) {
		RunEx runEx = new RunEx();
		runEx.loginPanel = new LoginFrame(runEx);
		runEx.registerFrame = new RegisterFrame(runEx);
	}

}
