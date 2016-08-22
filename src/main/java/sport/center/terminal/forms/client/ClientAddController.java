/**
 * 
 */
package sport.center.terminal.forms.client;

import java.net.URL;
import java.sql.Date;
import java.util.ResourceBundle;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javafx.util.Duration;
import sport.center.terminal.constants.Dialogs;
import sport.center.terminal.constants.EventListenSupport;
import sport.center.terminal.data.model.ClientDataModel;
import sport.center.terminal.data.model.SessionDataModel;
import sport.center.terminal.data.model.SportDataModel;
import sport.center.terminal.fxml.manager.FXMLConstants;
import sport.center.terminal.fxml.manager.FXMLManager;
import sport.center.terminal.jpa.entities.ClientEntity;
import sport.center.terminal.jpa.entities.SessionEntity;
import sport.center.terminal.jpa.entities.SportEntity;
import sport.center.terminal.production.support.Constants;
import sport.center.terminal.support.Messages;
import sport.center.terminal.support.SportChangeFilter;
import tray.notification.NotificationType;

/**
 * @author Asendar
 *
 */
public class ClientAddController implements Initializable {

	/**
	 * 
	 */
	@FXML
	private TextField txtName, txtAddress, txtPhone1, txtPhone2, txtEmergenceyPhone, txtCollage, txtStudyLevel,
			txtHelth, txtHieght, txtWeight, txtFatherWorkInfo, txtMotherWorkInfo, txtEmail, txtFBName,
			txtMonthlyPayment, txtBelt;

	/**
	 * 
	 */
	@FXML
	private DatePicker birthdate, monthlyPaymentDate, signDate;

	/**
	 * 
	 */
	@FXML
	private Button btnAdd;

	/**
	 * 
	 */
	@FXML
	private ComboBox<ClientEntity.Gender> cmGender;

	/**
	 * 
	 */
	@FXML
	private ComboBox<SportEntity> cmSport;

	/**
	 * 
	 */
	@FXML
	private ComboBox<SessionEntity> cmSession;

	/**
	 * 
	 */
	@FXML
	private Label lblClientsCount;

	/**
	 * 
	 */
	@FXML
	private CheckBox checkTrans;

	/**
	 * 
	 */
	public void init() {

		initComboBoxes();

		cmGender.getItems().addAll(ClientEntity.Gender.values());
		cmGender.getSelectionModel().selectFirst();

		addtTextListeners(txtName, txtPhone1, txtMonthlyPayment, txtEmergenceyPhone, txtAddress, txtHelth, txtHieght,
				txtWeight, txtBelt);

		btnAdd.setDisable(true);

	}

	/**
	 * 
	 */
	private void initComboBoxes() {
		cmSport.getItems().addAll(SportDataModel.instance.getAllActiveSports());

		if (SportChangeFilter.instance.getSportFilter() == null)
			cmSport.getSelectionModel().selectFirst();
		else
			cmSport.getSelectionModel().select(SportChangeFilter.instance.getSportFilter());

		cmSport.valueProperty().addListener((ov, o1, o2) -> {

			fillSessionCombo(cmSport.getSelectionModel().getSelectedItem());

		});

		fillSessionCombo(cmSport.getSelectionModel().getSelectedItem());
	}

	/**
	 * @param sport
	 */
	private void fillSessionCombo(SportEntity sport) {

		cmSession.getItems().clear();

		SessionEntity blankSession = SessionDataModel.instance.getBlankSession();
		cmSession.getItems().add(blankSession);

		cmSession.getSelectionModel().selectFirst();

		if (sport == null)
			return;

		cmSession.getItems().addAll(SessionDataModel.instance.getSessions(sport));
	}

	/**
	 * @param txtFields
	 */
	private void addtTextListeners(TextField... txtFields) {

		for (TextField txtField : txtFields) {
			txtField.textProperty().addListener((observable, oldValue, newValue) -> {
				if (newValue.replace(" ", "").equals("") //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
						|| isEmpty(txtName, txtAddress, txtPhone1, txtEmergenceyPhone, txtHelth, txtHieght, txtWeight,
								txtMonthlyPayment)) {
					btnAdd.setDisable(true);
				} else {
					btnAdd.setDisable(false);
				}

			});
		}
	}

	/**
	 * @param txtFields
	 * @return
	 */
	private boolean isEmpty(TextField... txtFields) {
		for (TextField txtField : txtFields) {
			if (txtField.getText().equals("")) { //$NON-NLS-1$
				return true;
			}
		}
		return false;
	}

	/**
	 * @param e
	 */
	@FXML
	private void doAdd(ActionEvent e) {

		if (!canDoAction()) {
			Constants.instance.showNotificationTry(Messages.getString("Terminal.DEMO_ADD_CLIENT"));
			return;
		}

		SessionEntity selectedSession = cmSession.getSelectionModel().getSelectedItem();

		if (selectedSession.getId() != 0) {
			int currentNum = SessionDataModel.instance.getSessionCurrentNumber(selectedSession);

			if (selectedSession.getSessionMaxCapacity() < currentNum + 1) {
				Dialogs.getNotificationTray("Error", "Session " + selectedSession.getName() + " is full !",
						NotificationType.ERROR).showAndDismiss(Duration.seconds(8));
				return;
			}

		}

		ClientEntity client = null;
		try {

			client = new ClientEntity();
			client.setName(txtName.getText());
			client.setBirthdate(Date.valueOf(birthdate.getValue()));
			client.setMonthlyPaymentDate(Date.valueOf(monthlyPaymentDate.getValue()));
			client.setSignDate(Date.valueOf(signDate.getValue()));

			client.setGender(cmGender.getSelectionModel().getSelectedItem());
			client.setPhone1(txtPhone1.getText());
			client.setPhone2(txtPhone2.getText());
			client.setHelth(txtHelth.getText());
			client.setCollage(txtCollage.getText());
			client.setStudyLevel(txtStudyLevel.getText());
			client.setHight(Double.parseDouble(txtHieght.getText()));
			client.setWieght(Double.parseDouble(txtWeight.getText()));
			client.setFatherWorkInfo(txtFatherWorkInfo.getText());
			client.setMotherWorkInfo(txtMotherWorkInfo.getText());
			client.setEmergencyPhone(txtEmergenceyPhone.getText());
			client.setEmail(txtEmail.getText());
			client.setFbName(txtFBName.getText());
			client.setTransportationParticipated(checkTrans.isSelected());
			client.setAddress(txtAddress.getText());
			client.setBelt(txtBelt.getText());
			client.setActive(true);
			client.setMonthlyPayment(Double.parseDouble(txtMonthlyPayment.getText()));
			client.setSportId(cmSport.getSelectionModel().getSelectedItem().getId());
			client.setSession(cmSession.getSelectionModel().getSelectedItem().getId());

		} catch (Exception ex) {
			Dialogs.getNotificationTray(Messages.getString("ClientAddFormController.ERROR"),
					Messages.getString("ClientAddFormController.ENTER_VALID_DATE"), NotificationType.ERROR)
					.showAndDismiss(Duration.seconds(8));
			return;
		}

		ClientEntity addedClient = ClientDataModel.instance.addClient(client);

		emptyFields(txtName, txtPhone1, txtPhone2, txtMonthlyPayment, txtEmergenceyPhone, txtAddress, txtFBName,
				txtEmail, txtHelth, txtCollage, txtStudyLevel, txtHieght, txtWeight, txtFatherWorkInfo,
				txtMotherWorkInfo, txtBelt);
		emptyPickers(birthdate, monthlyPaymentDate);

		cmGender.getSelectionModel().selectFirst();

		checkTrans.setSelected(false);

		txtName.requestFocus();

		btnAdd.setDisable(true);

		Dialogs.getNotificationTray(Messages.getString("ClientAddFormController.MEMBER_ADDED"),
				Messages.getString("ClientAddFormController.MEMBER_ADDED_ID") + addedClient.getHumanReadableId(),
				NotificationType.SUCCESS).showAndDismiss(Duration.seconds(8));

		((Stage) btnAdd.getScene().getWindow()).hide();

		EventListenSupport.instance.firePropertyChange(EventListenSupport.EVN_CLIENT_CHANGED);
		SessionDataModel.instance.fireDataModelChanged();

		FXMLManager manager = new FXMLManager(FXMLConstants.CLIENT_VIEW);
		manager.getController(ClientViewController.class).init(addedClient);
		manager.getStage(addedClient.getName()).show();

	}

	/**
	 * @param txtFields
	 */
	private void emptyFields(TextField... txtFields) {
		for (TextField txtField : txtFields) {
			txtField.setText("");
		}
	}

	/**
	 * @param datePickers
	 */
	private void emptyPickers(DatePicker... datePickers) {
		for (DatePicker datePicker : datePickers) {
			datePicker.setValue(null);
		}
	}

	/* (non-Javadoc)
	 * @see javafx.fxml.Initializable#initialize(java.net.URL, java.util.ResourceBundle)
	 */
	@Override
	public void initialize(URL url, ResourceBundle rb) {
		// TODO
	}

	/**
	 * @return
	 */
	private boolean canDoAction() {
		if (!Constants.instance.isDemo())
			return true;

		return true;
	}

}