import javax.swing.*;

public class ServerStart {
	public static void main(String[] args) {
		Server start = new Server();
		start.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		start.startRunning();
		start.setResizable(false);
	}
}
