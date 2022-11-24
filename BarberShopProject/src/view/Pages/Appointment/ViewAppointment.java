package view.Pages.Appointment;

import java.sql.SQLException;
import java.time.*;
import java.time.temporal.TemporalAdjusters;
import java.util.List;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import model.classes.Appointment;
import model.classes.Customer;
import model.classes.IsraelDayOfWeek;
import model.haircutDecorator.*;
import model.singletons.ActivityHoursSingleton;
import model.singletons.AppointmentScheduleSingleton;
import model.singletons.CatalogStylesSingleton;
import view.Pages.iCustomerViewChild;
import view.Pages.RootController.ViewRoot;

public class ViewAppointment implements iCustomerViewChild {

	private ViewRoot parentController;
	private ActivityHoursSingleton activityMng;
	private CatalogStylesSingleton catalogMng;
	private AppointmentScheduleSingleton scheduleMng;
	private boolean isNewAppointment;
	private Appointment appointment;
	private iHaircutStyle hairStyle;
	private Customer cust;
	
	@FXML
    private AnchorPane apNewAppointment;
    @FXML
    private JFXButton btnBack;
    @FXML
    private JFXButton btnConfirm;
    @FXML
    private JFXComboBox<IsraelDayOfWeek> cmbDay;
    @FXML
    private JFXComboBox<String> cmbHairStyle;
    @FXML
    private JFXComboBox<LocalTime> cmbHour;
    @FXML
    private GridPane gpTotal;
    @FXML
    private Label lblAddEditApp;
    @FXML
    private Label lblLenght;
    @FXML
    private Label lblPrice;
    
    @Override
    public void initializePage() {
    	cust 		= parentController.getUserCustomer();
    	activityMng = parentController.getMvcController().getActivityHoursMng();
    	catalogMng 	= parentController.getMvcController().getCatalogMng();
    	scheduleMng	= parentController.getMvcController().getScheduleMng();
    	if(cust == null || activityMng == null || catalogMng == null || scheduleMng == null) {
			parentController.moveToUserHomePage();
			return;
    	}
    		
    	initUserAppointment();
    	initHeadlineLabel();
    	setLengthPriceLabels();
		initComboBoxes();
		btnConfirm.setFocusTraversable(false);
		btnBack.setFocusTraversable(false);
    }
    
    @Override
    public void setParentController(ViewRoot parent) {
    	parentController = parent;		
    }
    
    private void initUserAppointment() {
    	if(!cust.isNextAppointment()) {
    		isNewAppointment = true;
    		return;
    	}
    	
    	isNewAppointment = false;
    	appointment = cust.getNextAppointment();
    	hairStyle = appointment.getChoosenStyle();
    }
    
    private void initHeadlineLabel() {
		if(isNewAppointment)
			lblAddEditApp.setText("Schedule new appointment");
		else
			lblAddEditApp.setText("Edit next appointment");
    }
    
    private void initComboBoxes() {
    	initCmbStyle();

    	if(isNewAppointment) {    		
    		cmbDay.setVisible(false);
    		cmbHour.setVisible(false);
    	}
    	else {
    		initCmbDays();
    		initCmbHour();
    	}
    }
        
    private void initCmbStyle() {
    	cmbHairStyle.getItems().clear();
    	List<String> styleOptions;
		styleOptions = catalogMng.getAllStylesOptionsStringList();
		styleOptions.stream().forEachOrdered(style -> cmbHairStyle.getItems().add(style));
		if(!isNewAppointment)
			cmbHairStyle.getSelectionModel().select(appointment.getChoosenStyle().getAllStylesStrings());
    	cmbHairStyle.setVisible(true);
		cmbHairStyle.getSelectionModel().selectedItemProperty().addListener((option, oldValue, newValue) -> {
			if(newValue==null && oldValue.equals(newValue))
			{

				cmbDay.setVisible(false);
				return;
			}
			try {
				hairStyle = HairStyleFactory.getDecoratedHairStyle(cmbHairStyle.getValue());
				setLengthPriceLabels();
				initCmbDays();
			} catch (SQLException e) {
				parentController.errorMsg(e.getMessage());
				parentController.moveToStartPage();
			}
		});
    }
    
    private void initCmbDays() {
    	cmbDay.getItems().clear();

    	List<IsraelDayOfWeek> days;

    	days = activityMng.getAvailableOptionsDay();

		days.stream().forEachOrdered(day -> cmbDay.getItems().add(day));

		if(!isNewAppointment) {
			IsraelDayOfWeek day = appointment.getDayOfWeek();
			if(activityMng.getBussinessDay(day).isAvailable())
				cmbDay.getSelectionModel().select(appointment.getDayOfWeek());
		}

		cmbDay.setVisible(true);
		
		cmbDay.getSelectionModel().selectedItemProperty().addListener((option, oldValue, newValue) -> {
			if((newValue==null && oldValue.equals(newValue)) || cmbHairStyle.getSelectionModel().isEmpty()) {
				cmbHour.setVisible(false);
				return;
			}
				if(!cmbDay.getSelectionModel().isEmpty())
					initCmbHour();
		});
		
    }
    
    private void initCmbHour() {
    	cmbHour.getItems().clear();
    	List<LocalTime> hours;
		try {
			hours = scheduleMng.getAvailableOptionsTime(cmbDay.getValue(), hairStyle.getTotalLenght(), cust);
			hours.stream().forEachOrdered(hourOption -> cmbHour.getItems().add(hourOption));
		} catch (SQLException e) {
			parentController.errorMsg(e.getMessage());
		}
		
		if(!isNewAppointment)
			cmbHour.getSelectionModel().select(appointment.getTimeOfAppointment().toLocalTime());
		cmbHour.setVisible(true);
    }

    private void setLengthPriceLabels() {
    	if(hairStyle == null) {
    		gpTotal.setVisible(false);
    		return;
    	}
    	gpTotal.setVisible(true);
    	lblLenght.setText(hairStyle.getTotalLenght()+ " minutes");
    	lblPrice.setText(hairStyle.getTotalPrice()+ " ¤");
    }
    
    @FXML
    void confirmCreateAppointment () {
    	DayOfWeek day = cmbDay.getValue().getDayOfWeek();
    	LocalDate date;
    	if(!day.equals(LocalDate.now().getDayOfWeek()))
    		date = LocalDate.now().with(TemporalAdjusters.next(day)); 	
    	else
   		 	date = LocalDate.now(); 	

    	LocalDateTime timeOfApp= LocalDateTime.of(date,cmbHour.getValue());
    	appointment = new Appointment(timeOfApp, hairStyle);
		try {
			cust.setNextAppointment(appointment);
			parentController.moveToUserHomePage();
		} catch (SQLException e) {
			parentController.errorMsg(e.getMessage());
		}
    }
    
    @FXML
    void btnActionBack(ActionEvent event) {
    	parentController.moveToUserHomePage();
    }

}
