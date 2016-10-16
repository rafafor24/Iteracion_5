package vos;

import org.codehaus.jackson.annotate.JsonProperty;

public class ExchangeMsg 
{
	@JsonProperty(value="routingkey")
	private String routingKey;
	
	@JsonProperty(value="sender")
	private String sender;
	
	@JsonProperty(value="payload")
	private String payload;
	
	@JsonProperty(value="status")
	private String status;
	
	public ExchangeMsg(@JsonProperty(value="routingkey") String queue, @JsonProperty(value="sender") String sender, @JsonProperty(value="payload") String payload, 
						@JsonProperty(value="status") String status) 
	{
		this.routingKey = queue;
		this.sender = sender;
		this.payload = payload;
		this.status = status;
	}
	

	public String getPayload() {
		return payload;
	}

	public void setPayload(String payload) {
		this.payload = payload;
	}

	public String getRoutingKey() {
		return routingKey;
	}

	public void setRoutingKey(String queue) {
		this.routingKey = queue;
	}


	public String getStatus() {
		return status;
	}


	public void setStatus(String status) {
		this.status = status;
	}


	public String getSender() {
		return sender;
	}


	public void setSender(String sender) {
		this.sender = sender;
	}
	
	

}
