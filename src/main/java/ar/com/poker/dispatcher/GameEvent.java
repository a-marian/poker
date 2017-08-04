package ar.com.poker.dispatcher;

public class GameEvent {
	
	/**
	 *Permite transmitir informacion de los eventos producidos.*/
	
	private String type;
	private String source;
	private Object payload;
	
	private GameEvent(){
		
	}
	
	private GameEvent(String type, String source){
			this.source = source;
			this.type = type;
		}
	
	public String getType() {
		return type;
	}
	
	public void setType(String type) {
		this.type = type;
	}
	
	public String getSource() {
		return source;
	}
	
	public void setSource(String source) {
		this.source = source;
	}
	
	public Object getPayload() {
		return payload;
	}
	
	public void setPayload(Object payload) {
		this.payload = payload;
	}


	

}
