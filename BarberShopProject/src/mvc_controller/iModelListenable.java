package mvc_controller;

public interface iModelListenable {
	//listener to the model
	//if the model will notify a change  ---> get a message 

	public void notifyErrorMsg(String msg);
	public void appointmentCanceled(String msg);
	public void customerAppointmentChanged();
	public void forceLogout(String msg);
}
