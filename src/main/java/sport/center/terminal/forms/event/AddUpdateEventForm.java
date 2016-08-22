package sport.center.terminal.forms.event;

import java.net.URL;
import java.util.ResourceBundle;

import org.controlsfx.control.PopOver;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import javafx.util.Duration;
import sport.center.terminal.constants.Dialogs;
import sport.center.terminal.data.model.SportDataModel;
import sport.center.terminal.data.model.SportEventDataModel;
import sport.center.terminal.jpa.entities.SportEntity;
import sport.center.terminal.jpa.entities.SportEventEntity;
import tray.notification.NotificationType;

public class AddUpdateEventForm implements Initializable {

	/**
	 * 
	 */
	SportEventEntity event;

	/**
	 * 
	 */
	@FXML
	private TextField txtName, txtComment, txtMax;
	/**
	 * 
	 */
	@FXML
	private ComboBox<SportEntity> cmSport;
	/**
	 * 
	 */
	/**
	 * 
	 */
	@FXML
	private DatePicker dateStart, dateEnd;

	/**
	 * 
	 */
	@FXML
	private Button btnUpdate;

	/**
	 * @param event
	 */
	public void init(SportEventEntity event) {
		this.event = event;

		setUpDatePickersAndComboBoxes();

		fillData();
		addListeners(txtMax, txtName, txtComment);

		btnUpdate.setText("تعديل");

		btnUpdate.setOnAction((e) -> {

			updateAction();
		});

	}

	/**
	 * 
	 */
	public void init() {
		this.event = new SportEventEntity();
		btnUpdate.setDisable(true);
		addListeners(txtMax, txtName, txtComment);

		btnUpdate.setText("اضافه");

		btnUpdate.setOnAction((e) -> {
			addAction();

		});

		setUpDatePickersAndComboBoxes();

		cmSport.getSelectionModel().select(0);

	}

	/**
	 * 
	 */
	private void setUpDatePickersAndComboBoxes() {
		dateStart.valueProperty().addListener((L) -> {
			try {
				if (dateStart.getValue().isAfter(dateEnd.getValue()))
					dateEnd.setValue(dateStart.getValue());
			} catch (Exception e) {
				System.out.println("Empty date");

			}
		});

		dateEnd.valueProperty().addListener((L) -> {
			try {
				if (dateStart.getValue().isAfter(dateEnd.getValue()))
					dateEnd.setValue(dateStart.getValue());
			} catch (Exception e) {
				System.out.println("Empty date");

			}
		});

		SportEntity allSports = new SportEntity();
		allSports.setId(-1);
		allSports.setSportName("جميع الرياضات");
		cmSport.getItems().add(allSports);
		for (SportEntity sport : SportDataModel.instance.getAllActiveSports())
			cmSport.getItems().add(sport);
	}

	/**
	 * 
	 */
	private void fillData() {
		txtName.setText(event.getName());
		txtComment.setText(event.getComment());
		txtMax.setText(event.getEventMaxCapacity() + "");

		dateStart.setValue(event.getStartDate().toLocalDate());
		dateEnd.setValue(event.getEndDate().toLocalDate());

		cmSport.getSelectionModel().select(0);
		int index = 0;
		if (event.getSportId()!=0)
			for (SportEntity sport : cmSport.getItems()) {
				if (sport.getId() == event.getSportId()) {
					cmSport.getSelectionModel().select(index);
					break;
				}
				index++;
			}

	}

	/**
	 * @param txtFields
	 */
	private void addListeners(TextField... txtFields) {
		for (TextField txtField : txtFields) {
			txtField.textProperty().addListener((observable, oldValue, newValue) -> {
				if (txtField.getText().equals(" ")) {
					txtField.setText("");
				}

				if (txtField == txtMax) {
					if (!isInteger(newValue)) {
						txtField.setText(oldValue);
					}
				}

				if (!isEmpty(txtMax, txtName, txtComment)) {
					btnUpdate.setDisable(false);
				} else {
					btnUpdate.setDisable(true);
				}

			});
		}
	}

	/**
	 * @param text
	 * @return
	 */
	private boolean isInteger(String text) {
		try {
			if (text.equals("")) {
				return true;
			}
			if (Integer.parseInt(text) <= 1000000) {
				return true;
			}
			return false;

		} catch (Exception e) {

			return false;
		}

	}

	/**
	 * @param txtFields
	 * @return
	 */
	private boolean isEmpty(TextField... txtFields) {
		for (TextField txtField : txtFields) {
			if (txtField.getText().equals("")) {
				return true;
			}
		}
		return false;
	}

	/**
	 * 
	 */
	private void updateAction() {

		if (Integer.parseInt(txtMax.getText()) < SportEventDataModel.instance.getSportEventCurrentNumber(this.event)) {
			Dialogs.getNotificationTray("لا يمكن تعديل النشاط  " + event.getName(),
					"هذا النشاط يحتوي حاليا على  " + SportEventDataModel.instance.getSportEventCurrentNumber(this.event) + " مشترك!", NotificationType.ERROR)
					.showAndDismiss(Duration.seconds(8));
			return;

		}
		prepareEventObject();

		SportEventDataModel.instance.update(event);
		
		

		((PopOver) txtName.getScene().getWindow()).hide();
	}

	/**
	 * 
	 */
	private void addAction() {
		prepareEventObject();
		SportEventDataModel.instance.add(event);

		((PopOver) txtName.getScene().getWindow()).hide();
	}

	/**
	 * 
	 */
	private void prepareEventObject() {
		event.setName(txtName.getText());
		event.setComment(txtComment.getText());
		event.setEventMaxCapacity(Integer.parseInt(txtMax.getText()));

		event.setEndDate(java.sql.Date.valueOf(dateEnd.getValue()));
		event.setStartDate(java.sql.Date.valueOf(dateStart.getValue()));

		event.setSportId(cmSport.getSelectionModel().getSelectedItem().getId());

		event.setActive(true);
	}

	/* (non-Javadoc)
	 * @see javafx.fxml.Initializable#initialize(java.net.URL, java.util.ResourceBundle)
	 */
	@Override
	public void initialize(URL url, ResourceBundle rb) {
		// TODO
	}

}
