package model.msgsObservers;

import model.classes.IsraelDayOfWeek;

public interface iCustomerObserver {
	public void updateActivityHoursChanged(String msg);
	public void updateAppointmentCanceled(IsraelDayOfWeek dayOfWeek, String msg);
}
