package app;

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
import javax.swing.JTextArea;
import javax.swing.JTextField;

import org.apache.log4j.BasicConfigurator;

public class Sender implements Runnable{
		
	private String input;
	private JTextArea textArea;
	private JTextField textField;
	private String ten, user, pass, tenNhan;
	
	public Sender(JTextArea textArea, JTextField textField, String input, String ten, String user, String pass) {
		this.textArea = textArea;
		this.textField = textField;
		this.input = input;		
		this.ten = ten;
		this.user = user;
		this.pass = pass;
	}

	@Override
	public void run() {
		try {
			//config environment for JMS
			BasicConfigurator.configure();
			//config environment for JNDI
			Properties settings=new Properties();
			settings.setProperty(Context.INITIAL_CONTEXT_FACTORY, 
			"org.apache.activemq.jndi.ActiveMQInitialContextFactory");
			settings.setProperty(Context.PROVIDER_URL, "tcp://localhost:61616");
			//create context
			Context ctx=new InitialContext(settings);
			//lookup JMS connection factory
			ConnectionFactory factory=
			(ConnectionFactory)ctx.lookup("ConnectionFactory");
			//lookup destination. (If not exist-->ActiveMQ create once)
			Destination destination=
			(Destination) ctx.lookup("dynamicQueues/"+ten);
			//get connection using credential
			Connection con=factory.createConnection(user,pass);
			//connect to MOM
			con.start();
			//create session
			Session session=con.createSession(
			/*transaction*/false,
			/*ACK*/Session.AUTO_ACKNOWLEDGE
			);
			//create producer
			MessageProducer producer = session.createProducer(destination);
			//create text message
			Message msg=session.createTextMessage(input);
			producer.send(msg);

			//shutdown connection
			session.close();
			con.close();
			
			textArea.append(ten+" : "+input +"\n");
			textField.setText("");

			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
