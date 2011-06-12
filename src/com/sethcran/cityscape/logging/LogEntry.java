package com.sethcran.cityscape.logging;

public class LogEntry {
	public String header = null;
	public String message = null;
	
	public LogEntry(String header, String message) {
		this.header = header;
		this.message = message;
	}
	
	public String getHeader() {
		return header;
	}
	
	public String getMessage() {
		return message;
	}
}
