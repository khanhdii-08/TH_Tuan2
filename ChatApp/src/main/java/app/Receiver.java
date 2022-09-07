package app;

import javax.swing.JTextArea;

import java.util.Properties;
import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.Destination;
import javax.jms.Message;
import javax.jms.MessageConsumer;
import javax.jms.MessageListener;
import javax.jms.ObjectMessage;
import javax.jms.Session;
import javax.jms.TextMessage;
import javax.naming.Context;
import javax.naming.InitialContext;
import org.apache.log4j.BasicConfigurator;

public class Receiver implements Runnable{
	
	private JTextArea textArea;

	private String user; 
	private String pass;
	private String tenNhan;
	
	public Receiver(JTextArea textArea, String user, String pass, String tenNhan) {
		this.textArea = textArea;
		this.tenNhan = tenNhan;
		this.user = user;
		this.pass = pass;
	}

	@Override
	public void run(){
		
		try {
			//thiết lập môi trường cho JMS
			BasicConfigurator.configure();
			//thiết lập môi trường cho JJNDI
			Properties settings=new Properties();
			settings.setProperty(Context.INITIAL_CONTEXT_FACTORY, 
			"org.apache.activemq.jndi.ActiveMQInitialContextFactory");
			settings.setProperty(Context.PROVIDER_URL, "tcp://localhost:61616");
			//tạo context
			Context ctx=new InitialContext(settings);
			//lookup JMS connection factory
			Object obj=ctx.lookup("ConnectionFactory");
			ConnectionFactory factory=(ConnectionFactory)obj;
			//lookup destination
			Destination destination=(Destination) ctx.lookup("dynamicQueues/"+tenNhan);
			//tạo connection
			Connection con=factory.createConnection(user,pass);
			//nối đến MOM
			con.start();
			//tạo session
			Session session=con.createSession(
			/*transaction*/false,
			/*ACK*/Session.CLIENT_ACKNOWLEDGE
			);
			//tạo consumer
			MessageConsumer receiver = session.createConsumer(destination);
			
			System.out.println("Tý was listened on queue...");

			
			receiver.setMessageListener(new MessageListener() {
				
				@Override
				public void onMessage(Message message) {
					try {
						
						if(message instanceof TextMessage) {
							TextMessage textMessage = (TextMessage) message;
							String output = textMessage.getText();
							textArea.append(tenNhan+" : "+output + "\n");	
							System.out.println("output : " + output);
							message.acknowledge();
						}else if(message instanceof ObjectMessage) {
							ObjectMessage objMessage = (ObjectMessage) message;
							System.out.println(objMessage);
						}
					} catch (Exception e) {
						e.printStackTrace();
					}
					
				}
			});
			
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

}
