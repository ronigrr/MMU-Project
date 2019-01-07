package com.hit.server;

import java.io.Serializable;
import java.util.Map;

@SuppressWarnings("serial")
public class Request<T> implements Serializable {
	private Map<String, String> headers;
	private T body;

	public Request(Map<String, String> headers, T body) {
		this.body = body;
		this.headers = headers;
	}

	public T getBody() {
		return body;
	}

	public Map<String, String> getHeaders() {
		return headers;
	}

	public void setBody(T body) {
		this.body = body;
	}

	public void setHeaders(Map<String, String> headers) {
		this.headers = headers;
	}

    @Override
    public String toString(){
    	String string = new String("action");
       return "Action:" + headers.get(string) + "content:" + body.toString();
    }
}
