package dtm;

import java.io.IOException;

import javax.jms.JMSException;
import javax.jms.TopicConnectionFactory;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.servlet.ServletContext;
import javax.ws.rs.core.Context;

import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.map.JsonMappingException;

import com.rabbitmq.jms.admin.RMQConnectionFactory;

import jms.AllVideosMDB;
import jms.NonReplyException;
import tm.VideoAndesMaster;
import vos.ListaVideos;

public class VideoAndesDistributed 
{
	private final static String MQ_CONNECTION_NAME = "java:global/RMQClient";
	
	private static VideoAndesDistributed instance;
	
	private VideoAndesMaster tm;
	
	private TopicConnectionFactory factory;
	
	private AllVideosMDB allVideosMQ;
	
	private static String path;


	private VideoAndesDistributed() throws NamingException, JMSException
	{
		InitialContext ctx = new InitialContext();
		factory = (RMQConnectionFactory) ctx.lookup(MQ_CONNECTION_NAME);
		allVideosMQ = new AllVideosMDB(factory, ctx);
		allVideosMQ.start();
		
	}
	
	public void stop() throws JMSException
	{
		allVideosMQ.close();
	}
	
	/**
	 * Método que retorna el path de la carpeta WEB-INF/ConnectionData en el deploy actual dentro del servidor.
	 * @return path de la carpeta WEB-INF/ConnectionData en el deploy actual.
	 */
	public static void setPath(String p) {
		path = p;
	}
	
	public void setUpTransactionManager(VideoAndesMaster tm)
	{
	   this.tm = tm;
	}
	
	private static VideoAndesDistributed getInst()
	{
		return instance;
	}
	
	public static VideoAndesDistributed getInstance(VideoAndesMaster tm)
	{
		if(instance == null)
		{
			try {
				instance = new VideoAndesDistributed();
			} catch (NamingException e) {
				e.printStackTrace();
			} catch (JMSException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		instance.setUpTransactionManager(tm);
		return instance;
	}
	
	public static VideoAndesDistributed getInstance()
	{
		if(instance == null)
		{
			VideoAndesMaster tm = new VideoAndesMaster(path);
			return getInstance(tm);
		}
		if(instance.tm != null)
		{
			return instance;
		}
		VideoAndesMaster tm = new VideoAndesMaster(path);
		return getInstance(tm);
	}
	
	public ListaVideos getLocalVideos() throws Exception
	{
		return tm.darVideos();
	}
	
	public ListaVideos getRemoteVideos() throws JsonGenerationException, JsonMappingException, JMSException, IOException, NonReplyException, InterruptedException
	{
		return allVideosMQ.getRemoteVideos();
	}
}
