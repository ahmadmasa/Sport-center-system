/**
 * 
 */
package sport.center.terminal.forms.client;

import java.net.URL;
import java.sql.Date;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;

import javax.annotation.PostConstruct;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TabPane;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import javafx.util.Duration;
import sport.center.terminal.constants.Dialogs;
import sport.center.terminal.constants.NodePrinter;
import sport.center.terminal.data.model.ClientDataModel;
import sport.center.terminal.data.model.NoteDataModel;
import sport.center.terminal.data.model.SessionDataModel;
import sport.center.terminal.data.model.SportEventDataModel;
import sport.center.terminal.forms.payment.AddReceivableFormController;
import sport.center.terminal.forms.payment.ClientPaymentFormController;
import sport.center.terminal.fxml.manager.FXMLConstants;
import sport.center.terminal.fxml.manager.FXMLManager;
import sport.center.terminal.jpa.entities.ClientEntity;
import sport.center.terminal.jpa.entities.NoteEntity;
import sport.center.terminal.jpa.entities.SessionEntity;
import sport.center.terminal.jpa.entities.SportEventEntity;
import sport.center.terminal.note.AddUpdateNoteController;
import sport.center.terminal.support.Messages;
import sport.center.terminal.tables.NotesTable;
import sport.center.terminal.tables.PaymentTable;
import sport.center.terminal.tables.ReceivableTable;
import sport.center.terminal.tables.SessionMissTable;
import sport.center.terminal.tables.SportEventsTable;
import tray.notification.NotificationType;

/**
 * @author Asendar
 *
 */
public class ClientViewController implements Initializable{

	
	// main controls
	/**
	 * 
	 */
	@FXML
	private Button btnPrintCard, btnAddNote, btnDeleteNote, btnSave, btnAddReceivable;

	/**
	 * 
	 */
	@FXML
	private MenuItem menuAddMonthlyPayment, menuAddRegularPayment;

	// client details
	
	/**
	 * 
	 */
	@FXML
	private TextField txtName, txtPhone1, txtPhone2, txtEmergenceyPhone, txtHieght, txtWeight, txtHelth, txtAddress,
			txtFatherWorkInfo, txtMotherWorkInfo, txtCollage, txtStudyLevel, txtEmail, txtFBName, txtMonthlyPayment,
			txtBelt;

	/**
	 * 
	 */
	@FXML
	private TabPane clientTabPane;


	/**
	 * 
	 */
	@FXML
	private DatePicker birthdate, signDate, monthlyPaymentDate;

	/**
	 * 
	 */
	@FXML
	private ComboBox<ClientEntity.Gender> cmGender;

	/**
	 * 
	 */
	@FXML
	private ComboBox<SessionEntity> cmSession;

	/**
	 * 
	 */
	@FXML
	private CheckBox checkTrans;

	/**
	 * 
	 */
	@FXML
	private BorderPane missesPanel, paymentsPanel, monthlyPaymentsPanel, receivablesPanel, notesPanel, eventsPanel;

	/**
	 * 
	 */
	private ClientEntity client;


	/**
	 * 
	 */
	private SessionMissTable sessionMissesTable;
	/**
	 * 
	 */
	private PaymentTable regularPaymentTable;
	/**
	 * 
	 */
	private PaymentTable monthlyPaymentTable;
	/**
	 * 
	 */
	private ReceivableTable receivableTable ;
	/**
	 * 
	 */
	private NotesTable notesTable;
	/**
	 * 
	 */
	private SportEventsTable sportEventsTable;
	
	/**
	 * @param client
	 */
	@PostConstruct
	public void init(ClientEntity client){
		this.client = client;
		
		sessionMissesTable = new SessionMissTable(this.client);
		regularPaymentTable = new PaymentTable(PaymentTable.TableType.REGULAR, this.client);
		monthlyPaymentTable = new PaymentTable(PaymentTable.TableType.MONTHLY, this.client);
		receivableTable = new ReceivableTable(ReceivableTable.TableType.VIEW, this.client);
		notesTable = new NotesTable(client);
		sportEventsTable = new SportEventsTable();
		
		
		final List<SportEventEntity> events = new ArrayList<>(SportEventDataModel.instance.getSportEvents(client));
		sportEventsTable.fillTable(events);
		sportEventsTable.addChangeListener((evt)->{
			events.clear();
			events.addAll(SportEventDataModel.instance.getSportEvents(client));
			sportEventsTable.fillTable(events);
		});
		
		
		
		missesPanel.setCenter(sessionMissesTable);
		paymentsPanel.setCenter(regularPaymentTable);
		monthlyPaymentsPanel.setCenter(monthlyPaymentTable);
		receivablesPanel.setCenter(receivableTable);
		notesPanel.setCenter(notesTable);
		eventsPanel.setCenter(sportEventsTable);
		
		fillComboBoxes();
		
		fillClientDetails();

		addSaveAction();
		
		addMainActons();
		
		
		
		addReceivablesDragAndDropActions();
		
		addPaymentsDragAndDropActions();
		
	}
	
	/**
	 * 
	 */
	private void addMainActons() {
		menuAddRegularPayment.setOnAction((e) -> {

			FXMLManager manager = new FXMLManager(FXMLConstants.CLIENT_PAYMENT_FORM);
			Stage stage = manager.getStage("Add Payment");
			manager.getController(ClientPaymentFormController.class).init(client, null, false);
			stage.show();
			
			clientTabPane.getSelectionModel().select(2);
			
		});
		
		menuAddMonthlyPayment.setOnAction((e) -> {

			FXMLManager manager = new FXMLManager(FXMLConstants.CLIENT_PAYMENT_FORM);
			Stage stage = manager.getStage("Add Payment");
			manager.getController(ClientPaymentFormController.class).init(client, null, true);
			stage.show();
			
			clientTabPane.getSelectionModel().select(3);
			
		});
		
		btnAddReceivable.setOnAction((e) -> {

			FXMLManager manager = new FXMLManager(FXMLConstants.ADD_RECEIVABLE_FORM);
			manager.getController(AddReceivableFormController.class).init(client, null);
			
			Dialogs.showPopOver("Add Receivable", manager.getRoot(), btnAddReceivable);
			
			clientTabPane.getSelectionModel().select(4);
		});	
		
		
		btnAddNote.setOnAction((e) -> {
			
			FXMLManager manager = new FXMLManager(FXMLConstants.ADD_UPDATE_NOTE);
			manager.getController(AddUpdateNoteController.class).init(client);
			
			Dialogs.showPopOver("Add Note", manager.getRoot(), btnAddNote);
			
			clientTabPane.getSelectionModel().select(5);
			
		});
		
		btnDeleteNote.setOnAction((e) -> {
			
			NoteEntity note = notesTable.getSelectionModel().getSelectedItem();
			
			clientTabPane.getSelectionModel().select(5);
			
			if(note ==null){
				Dialogs.showPopOver("Can't Delete", new Label("     Select a note to delete     "), btnDeleteNote);
				
				return;
			}
			
//			
			Alert alert = Dialogs.getConfirmDialog(Messages.getString("MainTabController.DELETE"), "",
					Messages.getString("MainTabController.DELETE_BODY"));
			Optional<ButtonType> result = alert.showAndWait();
			
			if (result.get() == ButtonType.OK) {
				NoteDataModel.instance.delete(note);
			}
			
		});
		
		
		btnPrintCard.setOnAction((e)->{
			FXMLManager manager = new FXMLManager(FXMLConstants.CLIENT_CARD);
			ClientCardController clientCardController = manager.getController(ClientCardController.class);
			clientCardController.init(client);
			AnchorPane printPane = clientCardController.getPrintPane();
			NodePrinter.printForm(printPane, (Stage)btnPrintCard.getScene().getWindow(),NodePrinter.getCardPaper());
		});
	}
	
	
	
	/**
	 * 
	 */
	private void fillComboBoxes() {
		this.cmGender.getItems().addAll(ClientEntity.Gender.values());
		this.cmSession.getItems().addAll(SessionDataModel.instance.getSessions());
	}
	
	/**
	 * 
	 */
	private void fillClientDetails(){
		if (client == null) {
			return;
		}
		txtName.setText(client.getName());
		txtPhone1.setText(client.getPhone1() + ""); 
		if (client.getPhone2() != null) {
			txtPhone2.setText(client.getPhone2() + ""); 
		}
		txtEmergenceyPhone.setText(client.getEmergencyPhone());

		if (client.getCollage() != null) {
			txtCollage.setText(client.getCollage());
		}
		if (client.getStudyLevel() != null) {
			txtStudyLevel.setText(client.getStudyLevel());
		}
		if (client.getHelth() != null) {
			txtHelth.setText(client.getHelth());
		}

		txtHieght.setText(client.getHight() + ""); 
		txtWeight.setText(client.getWieght() + ""); 

		if (client.getFatherWorkInfo() != null) {
			txtFatherWorkInfo.setText(client.getFatherWorkInfo());
		}

		if (client.getMotherWorkInfo() != null) {
			txtMotherWorkInfo.setText(client.getMotherWorkInfo());
		}
		if (client.getEmail() != null) {
			txtEmail.setText(client.getEmail());
		}
		if (client.getFbName() != null) {
			txtFBName.setText(client.getFbName());
		}

		cmSession.getSelectionModel().select(SessionDataModel.instance.getSessionById(client.getSession()));
		cmGender.getSelectionModel().select(client.getGender());
		birthdate.setValue(client.getBirthdate().toLocalDate());
		monthlyPaymentDate.setValue(client.getMonthlyPaymentDate().toLocalDate());
		signDate.setValue(client.getSignDate().toLocalDate());
		txtMonthlyPayment.setText(client.getMonthlyPayment() + ""); 

		txtAddress.setText(client.getAddress());

		if (client.getBelt() != null) {
			txtBelt.setText(client.getBelt());
		}

		checkTrans.setSelected(client.isTransportationParticipated());
	}

	/**
	 * 
	 */
	private void addSaveAction(){
		this.btnSave.setOnAction((e)->{
			saveAction();
		});
	}
	
	/**
	 * 
	 */
	private void saveAction() {
		ClientEntity updClient = new ClientEntity();

		SessionEntity selectedSession = cmSession.getSelectionModel().getSelectedItem();

		if (selectedSession != null) {
			int currentNum = SessionDataModel.instance.getSessionCurrentNumber(selectedSession);

			if (selectedSession.getId() != 0 && selectedSession.getId() != client.getSession()) {
				if (selectedSession.getSessionMaxCapacity() < currentNum + 1) {
					Dialogs.getNotificationTray("Error", "Session " + selectedSession.getName() + " is full !",
							NotificationType.ERROR).showAndDismiss(Duration.seconds(8));
					return;
				}
			}
			updClient.setSession(selectedSession.getId());
			
			
		}else{
			updClient.setSession(0);
		}

		try {

			updClient.setId(this.client.getId());
			updClient.setName(txtName.getText());
			updClient.setBirthdate(Date.valueOf(birthdate.getValue()));
			updClient.setMonthlyPaymentDate(Date.valueOf(monthlyPaymentDate.getValue()));
			updClient.setSignDate(Date.valueOf(signDate.getValue()));

			ClientEntity.Gender gender = cmGender.getSelectionModel().getSelectedItem();

			updClient.setGender(gender);
			updClient.setPhone1(txtPhone1.getText());
			updClient.setPhone2(txtPhone2.getText());
			updClient.setHelth(txtHelth.getText());
			updClient.setCollage(txtCollage.getText());
			updClient.setStudyLevel(txtStudyLevel.getText());
			updClient.setHight(Double.parseDouble(txtHieght.getText()));
			updClient.setWieght(Double.parseDouble(txtWeight.getText()));
			updClient.setFatherWorkInfo(txtFatherWorkInfo.getText());
			updClient.setMotherWorkInfo(txtMotherWorkInfo.getText());
			updClient.setEmergencyPhone(txtEmergenceyPhone.getText());
			updClient.setEmail(txtEmail.getText());
			updClient.setFbName(txtFBName.getText());
			updClient.setTransportationParticipated(checkTrans.isSelected());
			updClient.setAddress(txtAddress.getText());
			updClient.setBelt(txtBelt.getText());
			updClient.setActive(true);
			updClient.setMonthlyPayment(Double.parseDouble(txtMonthlyPayment.getText()));
			updClient.setSportId(client.getSportId());

		} catch (Exception ex) {
			Dialogs.getNotificationTray(Messages.getString("ClientDetailsFormController.ERROR"),
					Messages.getString("ClientDetailsFormController.ERROR_DATE"), NotificationType.ERROR)
					.showAndDismiss(Duration.seconds(8));
			return;
		}

		ClientDataModel.instance.update(updClient);

		SessionDataModel.instance.fireDataModelChanged();
		
		
		Dialogs.getNotificationTray(Messages.getString("ClientDetailsFormController.DONE"),
				Messages.getString("ClientDetailsFormController.DONE_BODY"), NotificationType.SUCCESS)
				.showAndDismiss(Duration.seconds(5));

		txtName.requestFocus();
	}

	
	/**
	 * 
	 */
	private void addPaymentsDragAndDropActions(){
		
		ImageView imgRubbishRegular = new ImageView();
		ImageView imgRubbishMonthly = new ImageView();
		
		paymentsPanel.setBottom(imgRubbishRegular);
		monthlyPaymentsPanel.setBottom(imgRubbishMonthly);
		
		BorderPane.setAlignment(imgRubbishMonthly, Pos.BASELINE_CENTER);
		BorderPane.setAlignment(imgRubbishRegular, Pos.BASELINE_CENTER);
		
		imgRubbishRegular.setVisible(false);
		imgRubbishRegular.setImage(new Image("/icons/rubish-close.png"));
		
		imgRubbishMonthly.setVisible(false);
		imgRubbishMonthly.setImage(new Image("/icons/rubish-close.png"));
		
		regularPaymentTable.addDragAndDropAction(imgRubbishRegular);
		monthlyPaymentTable.addDragAndDropAction(imgRubbishMonthly);
	}
	
	
	/**
	 * 
	 */
	private void addReceivablesDragAndDropActions(){
		
		ImageView imgRubbishReceivable = new ImageView();
		
		imgRubbishReceivable.setVisible(false);
		imgRubbishReceivable.setImage(new Image("/icons/rubish-close.png"));
		
		
		receivablesPanel.setBottom(imgRubbishReceivable);
		BorderPane.setAlignment(imgRubbishReceivable, Pos.BASELINE_CENTER);
		
		
		
		receivableTable.addDragAndDropAction(imgRubbishReceivable);
	}
	
	
	/* (non-Javadoc)
	 * @see javafx.fxml.Initializable#initialize(java.net.URL, java.util.ResourceBundle)
	 */
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		// TODO Auto-generated method stub
		
	}

}
