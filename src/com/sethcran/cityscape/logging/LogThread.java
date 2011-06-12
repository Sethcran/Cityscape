package com.sethcran.cityscape.logging;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.concurrent.ConcurrentLinkedQueue;

import com.sethcran.cityscape.Cityscape;
import com.sethcran.cityscape.Settings;

public class LogThread implements Runnable {
	
	private ConcurrentLinkedQueue<CityChatLogEntry> chatQueue = null;
	private ConcurrentLinkedQueue<LogEntry> logQueue = null;
	private Connection con = null;
	private Settings settings = null;
	
	public LogThread(Cityscape plugin) {
		chatQueue = new ConcurrentLinkedQueue<CityChatLogEntry>();
		logQueue = new ConcurrentLinkedQueue<LogEntry>();
		settings = plugin.getSettings();
		
		try {
			Class.forName(settings.databaseDriver);
			con = DriverManager.getConnection(settings.databaseUrl, 
					settings.databaseUsername, settings.databasePassword);
		} catch(Exception e) {
			if(settings.debug)
				e.printStackTrace();
		}
	}
	
	public synchronized void addLogEntry(String header, String message) {
		logQueue.offer(new LogEntry(header, message));
	}
	
	public synchronized void addCityChatLogEntry(String city, String player, String message) {
		chatQueue.offer(new CityChatLogEntry(city, player, message));
	}

	@Override
	public void run() {
		String logsql = 	"INSERT INTO cslogs VALUES";
		String chatsql = 	"INSERT INTO cscitychatlogs VALUES";
		int logCount = 0;
		int chatCount = 0;
		int logCurrent = 1;
		int chatCurrent = 1;
		
		for(int i = 0; i < logQueue.size(); i++) {
			if(logCount == 0)
				logsql += "(NOW(), ?, ?)";
			else
				logsql += ", (NOW(), ?, ?)";
			logCount++;
		}
		logsql += ";";
		
		for(int i = 0; i < chatQueue.size(); i++) {
			if(chatCount == 0)
				chatsql += "(NOW(), ?, ?, ?)";
			else
				chatsql += ", (NOW(), ?, ?, ?)";
			chatCount++;
		}
		chatsql += ";";
		
		try {
			PreparedStatement logstmt = con.prepareStatement(logsql);
			PreparedStatement chatstmt = con.prepareStatement(chatsql);
			
			for(int i = 0; i < logCount; i++) {
				LogEntry entry = logQueue.poll();
				logstmt.setString(logCurrent++, entry.getHeader());
				logstmt.setString(logCurrent++, entry.getMessage());
			}
			
			for(int i = 0; i < chatCount; i++) {
				CityChatLogEntry entry = chatQueue.poll();
				chatstmt.setString(chatCurrent++, entry.getCity());
				chatstmt.setString(chatCurrent++, entry.getPlayer());
				chatstmt.setString(chatCurrent++, entry.getMessage());
			}
			
			if(logCount > 0)
				logstmt.executeUpdate();
			if(chatCount > 0)
				chatstmt.executeUpdate();
		} catch(SQLException e) {
			if(settings.debug)
				e.printStackTrace();
		}
	}

}
