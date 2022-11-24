package model.msgsObservers;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import com.jfoenix.controls.datamodels.treetable.RecursiveTreeObject;

import model.classes.Customer;

public class Message extends RecursiveTreeObject<Message> {
	private Customer cust;
	private String messageText;
	private String msgSubject;
	private LocalDateTime dateTime;
	
	public Message(iCustomerObserver cust, String messageText, String msgSubject, LocalDateTime dateTime) {
		this.cust = (Customer)cust;
		this.messageText = messageText;
		this.msgSubject = msgSubject;
		this.dateTime = dateTime;
	}

	public Customer getCust() {
		return cust;
	}

	public String getMessageText() {
		return messageText;
	}


	public String getMsgSubject() {
		return msgSubject;
	}

	public LocalDateTime getDateTime() {
		return dateTime;
	}
	
	public String getDateTimeString() {
		return dateTime.format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm"));
	}

}