package model.classes;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAdjusters;

public class IsraelDayOfWeek implements Comparable<IsraelDayOfWeek> {

	public static IsraelDayOfWeek SUNDAY 	= new IsraelDayOfWeek("SUNDAY", 1, "Sunday");
	public static IsraelDayOfWeek MONDAY 	= new IsraelDayOfWeek("MONDAY", 2, "Monday");
	public static IsraelDayOfWeek TUESDAY 	= new IsraelDayOfWeek("TUESDAY", 3, "Tuesday");
	public static IsraelDayOfWeek WEDNESDAY = new IsraelDayOfWeek("WEDNESDAY", 4, "Wednesday");
	public static IsraelDayOfWeek THURSDAY 	= new IsraelDayOfWeek("THURSDAY", 5, "Thursday");
	public static IsraelDayOfWeek FRIDAY 	= new IsraelDayOfWeek("FRIDAY", 6, "Friday");
	public static IsraelDayOfWeek SATURDAY 	= new IsraelDayOfWeek("SATURDAY", 7, "Saturday");
	private static IsraelDayOfWeek[] weekDays = {SUNDAY, MONDAY, TUESDAY, WEDNESDAY, THURSDAY, FRIDAY, SATURDAY};

	private int dayNum;
	private String tagName;
	private String dayDisplayName;

	private IsraelDayOfWeek(String tagName, int dayNum, String dayDisplayName) {
		this.tagName = tagName;
		this.dayNum = dayNum;
		this.dayDisplayName = dayDisplayName;
	}

	public int getDayNum() {
		return dayNum;
	}

	public String getDisplayName() {
		return dayDisplayName;
	}
	
	public String getTagName() {
		return tagName;
	}
	
	public DayOfWeek getDayOfWeek() {
		return DayOfWeek.valueOf(this.getTagName());
	}
	
	public LocalDate getNextDayOccurnceDate() {
		return LocalDate.now().with(TemporalAdjusters.next(getDayOfWeek()));
	}
	
	public static IsraelDayOfWeek fromJavaDayOfWeek(DayOfWeek day) {
		IsraelDayOfWeek theDay = null;
		try {
			theDay = (IsraelDayOfWeek) IsraelDayOfWeek.class.getField(day.toString()).get(null);
		} catch (IllegalArgumentException | IllegalAccessException | NoSuchFieldException | SecurityException e) {
		} 
		return theDay;
	}
	
	public static DayOfWeek toJavaDayOfWeek(IsraelDayOfWeek day) {
		return DayOfWeek.valueOf(day.getTagName());
	}
	
	public static IsraelDayOfWeek getToday() {
		return IsraelDayOfWeek.fromJavaDayOfWeek(LocalDate.now().getDayOfWeek());
	}
	
	public static IsraelDayOfWeek getTomorrow() {
		if(getToday().equals(SUNDAY))
			return IsraelDayOfWeek.MONDAY;
		
		return IsraelDayOfWeek.weekDays[IsraelDayOfWeek.getToday().getDayNum()];
	}
	
	@Override
	public String toString() {
		LocalDate date = LocalDate.now();
		if(!getDayOfWeek().equals(date.getDayOfWeek()))
			date = getNextDayOccurnceDate();
		
		return dayDisplayName + (this.getTagName().equals(WEDNESDAY.getTagName()) ? "\t" : "\t\t") + date.format(DateTimeFormatter.ofPattern("dd/MM"));
	}
	
	@Override
	public boolean equals(Object obj) {
		if(!(obj instanceof IsraelDayOfWeek))
			return false;
				
		IsraelDayOfWeek temp = (IsraelDayOfWeek)obj;
		return this.getTagName().equals(temp.getTagName());
	}

	@Override
	public int compareTo(IsraelDayOfWeek day) {
		return dayNum - day.dayNum;
	}
	
}
