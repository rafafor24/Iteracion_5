package other;

import java.io.IOException;

import javax.annotation.Resource;
import javax.annotation.Resources;
import javax.jms.BytesMessage;
import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.DeliveryMode;
import javax.jms.ExceptionListener;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.Queue;
import javax.jms.QueueConnection;
import javax.jms.QueueSender;
import javax.jms.QueueSession;
import javax.jms.Session;
import javax.jms.TextMessage;
import javax.jms.Topic;
import javax.jms.TopicConnection;
import javax.jms.TopicPublisher;
import javax.jms.TopicSession;
import javax.jms.TopicSubscriber;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;

import com.rabbitmq.jms.admin.RMQConnectionFactory;
import com.rabbitmq.jms.admin.RMQDestination;

import vos.ExchangeMsg;



@WebListener
public class ExampleContextListener implements ServletContextListener {

//	@Resource(lookup="java:global/RMQClient")
//	public ConnectionFactory fac;
	
	@Override
	public void contextDestroyed(ServletContextEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void contextInitialized(ServletContextEvent arg0) {
		try {
			InitialContext ctx = new InitialContext();
//			RMQObjectFacory
		    final RMQConnectionFactory fac = (RMQConnectionFactory) ctx.lookup("java:global/RMQClient");
		    TopicConnection c = fac.createTopicConnection();
		    final TopicSession topicSession = c.createTopicSession(false, Session.AUTO_ACKNOWLEDGE);
            final RMQDestination topic = (RMQDestination) ctx.lookup("java:global/RMQTopicVideos"); 

		    TopicSubscriber topicSubscriber =  topicSession.createSubscriber(topic);
		    topicSubscriber.setMessageListener(new MessageListener() {
				
				@Override
				public void onMessage(Message message) {
					System.out.println(message);
//					BytesMessage bytes = (BytesMessage) message;
					TextMessage txt = (TextMessage) message;
					try {
//						byte[] byteArr = new byte[(int)bytes.getBodyLength()];
//						bytes.readBytes(byteArr); 
//						String body = new String(byteArr);
						String body = txt.getText();
						ObjectMapper mapper = new ObjectMapper();
						ExchangeMsg ex = mapper.readValue(body, ExchangeMsg.class);
						if(!ex.getQueue().equals(topic.getAmqpQueueName()))
						{
							System.out.println(body);
							Topic t = new RMQDestination(null, "videos.list", ex.getQueue(), topic.getAmqpQueueName());
							TopicPublisher topicPublisher = topicSession.createPublisher(t);
							topicPublisher.setDeliveryMode(DeliveryMode.PERSISTENT);
							TextMessage msg = topicSession.createTextMessage();
							msg.setJMSType("TextMessage");
						    ExchangeMsg ex1 = new ExchangeMsg("#.java", "Hi!", "SOME_ANSWER");
							msg.setText(mapper.writeValueAsString(ex1));
							topicPublisher.publish(msg);
						}
					} catch (JMSException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (JsonParseException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (JsonMappingException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					
				}
			});
		    c.setExceptionListener(new ExceptionListener() {
				
				@Override
				public void onException(JMSException exception) {
					System.out.println(exception);
					
				}
			});
		    c.start();
		} catch (NamingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (JMSException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
	}

}
