package com.funongo.cms.service;

import java.util.Properties;

import javax.mail.Message;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import org.springframework.stereotype.Service;

import com.funongo.cms.bo.EmailBO;

@Service
public class SendEmailService {

	public static void sendEmail(EmailBO emailBO, final String username, final String password){
		  
	     String from = emailBO.getFrom();  
	     String host = "mail.funongo.com";//or IP address  
	     
	     //Get the session object  
	     Properties properties = System.getProperties(); 
	     String[] tos = emailBO.getTo();
	     properties.setProperty("mail.smtp.host", host);
	     properties.setProperty("mail.smtp.auth", "true");
	     Session session = Session.getDefaultInstance(properties, new javax.mail.Authenticator() {
	            protected PasswordAuthentication getPasswordAuthentication() {
	                return new PasswordAuthentication(username, password);
	             }
	          });  
	     
	     try{
	    	 MimeMessage message = new MimeMessage(session);  
	    	
	         message.setFrom(new InternetAddress(from)); 
	         for(String to : tos){
	        	 message.addRecipient(Message.RecipientType.TO,new InternetAddress(to));  
	         }
	         message.addRecipient(Message.RecipientType.BCC, new InternetAddress("shweta.m@funongo.com"));
	         
	         message.setSubject(emailBO.getSubject());  
	         message.setContent(emailBO.getMessage(), "text/html");  
	  
	         // Send message  
	         Transport.send(message);  
	         System.out.println("message sent successfully....");  
	     }
	     catch(Exception e){
	    	 e.printStackTrace();
	     }
	}
}
