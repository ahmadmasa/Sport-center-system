package sport.center.terminal.forms.session;

import java.net.URL;
import java.util.ResourceBundle;

import org.controlsfx.control.PopOver;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.util.Duration;
import sport.center.terminal.constants.Dialogs;
import sport.center.terminal.data.model.SessionDataModel;
import sport.center.terminal.jpa.entities.SessionEntity;
import tray.notification.NotificationType;

/**
 *
 * @author Asendar
 */
public class UpdateSessionFormController implements Initializable {

	/**
	 * 
	 */
	private SessionEntity session;

	/**
	 * 
	 */
	@FXML
	private TextField txtMax, txtName, txtInstructor;

	/**
	 * 
	 */
	@FXML
	private Button btnUpdate;

	/**
	 * @param session
	 */
	public void init(SessionEntity session) {
		this.session = session;
		
		fillData();
		addListeners(txtMax, txtName, txtInstructor);
	}

	/**
	 * 
	 */
	private void fillData() {
		txtName.setText(session.getName());
		txtInstructor.setText(session.getInstructorName());
		txtMax.setText(session.getSessionMaxCapacity() + "");
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

				if (!isEmpty(txtMax, txtName, txtInstructor)) {
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
			if (Integer.parseInt(text) <= 500) {
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
	@FXML 
	private void updateAction() {

		if (Integer.parseInt(txtMax.getText()) < SessionDataModel.instance.getSessionCurrentNumber(session)) {
			Dialogs.getNotificationTray("Cant Update Session " + session.getName(),
					"This session currently has " + SessionDataModel.instance.getSessionCurrentNumber(session) + " members!",
					NotificationType.ERROR).showAndDismiss(Duration.seconds(8));
			return;

		}

		session.setName(txtName.getText());
		session.setInstructorName(txtInstructor.getText());
		session.setSessionMaxCapacity(Integer.parseInt(txtMax.getText()));

		SessionDataModel.instance.update(session);
		((PopOver) txtName.getScene().getWindow()).hide();
	}

	/* (non-Javadoc)
	 * @see javafx.fxml.Initializable#initialize(java.net.URL, java.util.ResourceBundle)
	 */
	@Override
	public void initialize(URL url, ResourceBundle rb) {
		// TODO
	}

}
