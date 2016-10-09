package jms;

import javax.jms.Message;
import javax.jms.MessageListener;



public class TestMDB implements MessageListener {
	

	//@Resource(lookup = "java:global/RMQClient")
	//private ConnectionFactory fact;
	
	
	@Override
	public void onMessage(Message arg0) {
		System.out.println(arg0);
		// TODO Auto-generated method stub
		
	}

}
