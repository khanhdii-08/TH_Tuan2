package app;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;

public class MyApp extends JFrame implements ActionListener {
	
	JPanel panel;
	JButton button, btnConnect;
	JTextField textField, txtTen, txtUser, txtPass, txtTenNhan;
	JTextArea textArea;
	String ten;
	String user; 
	String pass; 
	String tenNhan;
	
	public MyApp() {
		
		panel = new JPanel();
		textField = new JTextField();
		textArea = new JTextArea();
		txtTen = new JTextField();
		txtUser = new JTextField();
		txtPass = new JTextField();
		txtTenNhan = new JTextField();
		
		JLabel lblTen = new JLabel("Tên");
		JLabel lblUser= new JLabel("User");
		JLabel lblPass = new JLabel("Pass");
		JLabel lblTenNhan = new JLabel("Tên Nhận");
		
		
		
		textArea = new JTextArea();

		button = new JButton("Send");
		btnConnect = new JButton("Connect");
		
		

		this.setSize(500, 600);
		this.setDefaultCloseOperation(EXIT_ON_CLOSE);
		this.setVisible(true);
		this.setTitle("MyApp");
		
		this.setResizable(false);

		panel.setLayout(null);
		this.add(panel);
		
		lblTen.setBounds(20, 40, 70, 20);
		panel.add(lblTen);
		
		txtTen.setBounds(90, 40, 120, 20);
		panel.add(txtTen);
		
		lblUser.setBounds(230, 40, 70, 20);
		panel.add(lblUser);
		
		txtUser.setBounds(270, 40, 120, 20);
		panel.add(txtUser);
		
		lblPass.setBounds(230, 80, 50, 20);
		panel.add(lblPass);
		
		txtPass.setBounds(270, 80, 120, 20);
		panel.add(txtPass);
		
		lblTenNhan.setBounds(20, 80, 70, 20);
		panel.add(lblTenNhan);
		
		
		btnConnect.setBounds(400, 50, 80, 40);
		panel.add(btnConnect);
		
		
		txtTenNhan.setBounds(90, 80, 120, 20);
		panel.add(txtTenNhan);
		
		textArea.setBounds(20, 120, 450, 360);
		panel.add(textArea);
		
		textField.setBounds(20, 500, 340, 30);
		panel.add(textField);
		
		button.setBounds(375, 500, 95, 30);
		panel.add(button);
		
		button.addActionListener(this);
		
		btnConnect.addActionListener(this);
		
	}
	
	public static void main(String[] args) {
		new MyApp();
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		Object obj = e.getSource();

		String input = textField.getText();
		
		user = txtUser.getText().toString().trim();
		pass = txtPass.getText().toString().trim();
		ten = txtTen.getText().toString().trim();
		tenNhan = txtTenNhan.getText().toString().trim();

		if (obj.equals(button) && !input.equals("")) {
			try {
				new Thread(new Sender(textArea, textField, input, ten, user, pass)).start();
			} catch (Exception e1) {
				e1.printStackTrace();
			}
		}else if(obj.equals(btnConnect)){
			try {
				new Thread(new Receiver(textArea, user, pass, tenNhan)).start();
			} catch (Exception e1) {
				e1.printStackTrace();
			}
		}
	}

}
