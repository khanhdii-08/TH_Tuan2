package main;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Properties;

import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageConsumer;
import javax.jms.MessageListener;
import javax.jms.MessageProducer;
import javax.jms.ObjectMessage;
import javax.jms.Session;
import javax.jms.TextMessage;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import org.apache.log4j.BasicConfigurator;

public class ChatGUI extends JFrame implements ActionListener {

	JPanel panel;
	JTextField textField;
	JTextArea textArea;
	JButton button;

	private Session session;
	private MessageProducer producer;
	private Message msg;
	private Connection con;
	private Destination destination;

	public ChatGUI() throws JMSException, NamingException {

		panel = new JPanel();
		textField = new JTextField();
		textArea = new JTextArea();
		button = new JButton("Send");
		this.setSize(500, 500);
		this.setVisible(true);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		panel.setLayout(null);
		this.add(panel);
		textArea.setBounds(20, 20, 450, 360);
		panel.add(textArea);
		textField.setBounds(20, 400, 340, 30);
		panel.add(textField);
		button.setBounds(375, 400, 95, 30);
		panel.add(button);
		button.addActionListener(this);
		

			//thiết lập môi trường cho JMS
			BasicConfigurator.configure();
	//thiết lập môi trường cho JJNDI
			Properties settings = new Properties();
			settings.setProperty(Context.INITIAL_CONTEXT_FACTORY, "org.apache.activemq.jndi.ActiveMQInitialContextFactory");
			settings.setProperty(Context.PROVIDER_URL, "tcp://localhost:61616");
	//tạo context
			Context ctx = new InitialContext(settings);
	//lookup JMS connection factory
			Object obj = ctx.lookup("ConnectionFactory");
			ConnectionFactory factory = (ConnectionFactory) obj;
	//lookup destination
			destination = (Destination) ctx.lookup("dynamicQueues/thanthidet");
	//tạo connection
			con = factory.createConnection("admin", "admin");
	//nối đến MOM
			con.start();
			

			// tạo session
			session = con.createSession(/* transaction */false, /* ACK */Session.AUTO_ACKNOWLEDGE);
			// tạo consumer
			MessageConsumer receiver = session.createConsumer(destination);

			
			receiver.setMessageListener(new MessageListener() {
				// có message đến queue, phương thức này được thực thi
				public void onMessage(Message msg) {// msg là message nhận được
					try {
						if (msg instanceof TextMessage) {
							TextMessage tm = (TextMessage) msg;
							String txt = tm.getText();
							textArea.append("Nhận được " + txt + "\n");
							msg.acknowledge();// gửi tín hiệu ack
						} else if (msg instanceof ObjectMessage) {
							ObjectMessage om = (ObjectMessage) msg;
							System.out.println(om);
						}
						// others message type....
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			});
			

	}

	public static void main(String[] args) throws JMSException, NamingException {
		new ChatGUI();
	}

	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		Object obj = e.getSource();

		if (obj == button && !textField.getText().equalsIgnoreCase("")) {
			try {

				// create session

				// create producer
				MessageProducer producer = session.createProducer(destination);
				// create text message

				String input = textField.getText().toString().trim();

				msg = session.createTextMessage(input);
				producer.send(msg);

				textArea.append("Gui di: " + input + "\n");

				textField.setText("");
			} catch (Exception e2) {
				e2.printStackTrace();
			}

		}

	}
}
