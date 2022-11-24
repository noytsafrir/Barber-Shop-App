package model.classes;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

import com.jfoenix.controls.datamodels.treetable.RecursiveTreeObject;

public class BusinessDay extends RecursiveTreeObject<BusinessDay> implements Comparable<BusinessDay>  {

	private IsraelDayOfWeek theDay;
	private LocalTime openHour;
	private LocalTime closeHour;
	public static final String timeFormat = "HH:mm";	
	
	public BusinessDay(IsraelDayOfWeek theDay) {
		this.theDay = theDay;
		this.openHour =LocalTime.of(0, 0);
		this.closeHour =LocalTime.of(0, 0);
	}

	public IsraelDayOfWeek getTheDay() {
		return theDay;
	}

	public LocalTime getOpenHour() {
		return openHour;
	}

	public void setOpenHour(LocalTime openHour) {
		this.openHour = openHour;
	}

	public LocalTime getCloseHour() {
		return closeHour;
	}

	public void setCloseHour(LocalTime closeHour) {
		this.closeHour = closeHour;
	}

	@Override
	public String toString() {
		return theDay.getDisplayName();
	}	
	
	public String toStringDetails() {
		StringBuffer str = new StringBuffer();
		str.append(theDay.getDisplayName() + ": ");
		if(!isAvailable())
			str.append("Closed");
		else
			str.append(openHour.format(DateTimeFormatter.ofPattern(timeFormat)) + " - " + closeHour.format(DateTimeFormatter.ofPattern(timeFormat)));
		
		return str.toString();
	}	
	
	public String toStringOpenHour() {
		return openHour.format(DateTimeFormatter.ofPattern(timeFormat));
	}	
	
	public String toStringCloseHour() {
		return closeHour.format(DateTimeFormatter.ofPattern(timeFormat));
	}	
	
	public Boolean isAvailable() {
		if(getOpenHour().equals(closeHour))
			return false;
		return true;
	}

	@Override
	public int compareTo(BusinessDay day) {
		return theDay.compareTo(day.getTheDay());
	}

}
