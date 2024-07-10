package ar.com.poker.dispatcher;

import net.jcip.annotations.NotThreadSafe;

@NotThreadSafe
public class GameEvent<E extends Enum> {
	
	/**
	 *Permite transmitir informacion de los eventos producidos.*/
	
	private  E type;
	private String source;
	private Object payload;

	
	public GameEvent(E type, String source){
		this(type, source, null);
	}
	
	public GameEvent(E type, String source, Object payload){
			this.source = source;
			this.type = type;
			this.payload = payload;
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

	public E getType() {
		return type;
	}

	public void setType(E type) {
		this.type = type;
	}

	
	

}
