package vos;

import org.codehaus.jackson.annotate.JsonProperty;

public class ExchangeMsg 
{
	@JsonProperty(value="queue")
	private String queue;
	
	@JsonProperty(value="payload")
	private String payload;
	
	@JsonProperty(value="status")
	private String status;
	
	public ExchangeMsg(@JsonProperty(value="queue") String queue, @JsonProperty(value="payload") String payload, 
						@JsonProperty(value="status") String status) 
	{
		this.queue = queue;
		this.payload = payload;
		this.status = status;
	}
	

	public String getPayload() {
		return payload;
	}

	public void setPayload(String payload) {
		this.payload = payload;
	}

	public String getQueue() {
		return queue;
	}

	public void setQueue(String queue) {
		this.queue = queue;
	}


	public String getStatus() {
		return status;
	}


	public void setStatus(String status) {
		this.status = status;
	}
	
	

}
