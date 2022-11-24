package mvc_controller;

import model.singletons.ActivityHoursSingleton;

public interface iViewManagerListenable {
	//listener to the manager view
	//if the view of the manager will notify a change  ---> get a message 
	
	public void setManagerViewActiveStatus(Boolean status);
	public void activityHoursChanged(String msg);
	public ActivityHoursSingleton getActivityHoursMng();

}
