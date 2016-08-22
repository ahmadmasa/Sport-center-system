/**
 * 
 */
package sport.center.terminal.forms.session;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.net.URL;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;

import javax.annotation.PostConstruct;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Predicate;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.TabPane;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import javafx.util.Duration;
import lombok.Getter;
import lombok.Setter;
import sport.center.terminal.constants.Dialogs;
import sport.center.terminal.data.model.ClientDataModel;
import sport.center.terminal.data.model.SessionDataModel;
import sport.center.terminal.data.model.SessionMissDataModel;
import sport.center.terminal.forms.client.ClientChooserController;
import sport.center.terminal.fxml.manager.FXMLConstants;
import sport.center.terminal.fxml.manager.FXMLManager;
import sport.center.terminal.jpa.entities.ClientEntity;
import sport.center.terminal.jpa.entities.SessionEntity;
import sport.center.terminal.jpa.entities.SessionMissEntity;
import sport.center.terminal.tables.ClientTable;
import tray.notification.NotificationType;

/**
 * @author Asendar
 *
 */
public class SessionViewController implements Initializable{
	/**
	 * @param selectedSession
	 */
	@Setter @Getter SessionEntity selectedSession;

	/**
	 * 
	 */
	@FXML
	private Button btnAddMember, btnDeleteSessionMembers, btnSessionMiss, btnDeleteSession, btnExportSheet,
			btnDeactivateMembers, btnActivateMembers;

	/**
	 * 
	 */
	@FXML
	private BorderPane sessionsInactiveMembers, sessionsMembers;

	/**
	 * 
	 */
	@FXML
	private TabPane clientsTabPane;

	/**
	 * 
	 */
	@FXML
	private TextField txtFilter;
	
	/**
	 * 
	 */
	final ClientTable activeClientTable = new ClientTable();
	/**
	 * 
	 */
	final ClientTable inactiveClientTable = new ClientTable();
	
	/**
	 * @param selectedSession
	 */
	@PostConstruct
	public void init(SessionEntity selectedSession){
		this.selectedSession=selectedSession;

		activeClientTable.fillTable(ClientDataModel.instance.getActiveClients(selectedSession));

		sessionsMembers.setCenter(activeClientTable);
		activeClientTable.addChangeListener(new PropertyChangeListener() {

			@Override
			public void propertyChange(PropertyChangeEvent evt) {
				activeClientTable.fillTable(ClientDataModel.instance.getActiveClients(selectedSession));

			}
		});

		inactiveClientTable.fillTable(ClientDataModel.instance.getInactiveClients(selectedSession));

		sessionsInactiveMembers.setCenter(inactiveClientTable);

		inactiveClientTable.addChangeListener(new PropertyChangeListener() {

			@Override
			public void propertyChange(PropertyChangeEvent evt) {
				inactiveClientTable.fillTable(ClientDataModel.instance.getInactiveClients(selectedSession));

			}
		});
		
		txtFilter.textProperty().addListener((observable, oldValue, newValue) -> {
			doFiltering();
		});
		
		
		addMainActions();
		

	}
	
	/**
	 * 
	 */
	private void doFiltering(){
		String filterText = txtFilter.getText().replace(" ", "");
		
		List<ClientEntity> activeClients = new ArrayList<>(ClientDataModel.instance.getActiveClients(selectedSession));
		List<ClientEntity> inactiveClients = new ArrayList<>(ClientDataModel.instance.getInactiveClients(selectedSession));
		
		CollectionUtils.filter(activeClients, new Predicate() {
			
			@Override
			public boolean evaluate(Object object) {
				ClientEntity client = (ClientEntity) object;
				return client.contains(filterText);
			}
		});
		
		activeClientTable.getItems().clear();
		activeClientTable.getItems().addAll(activeClients);
		
		CollectionUtils.filter(inactiveClients, new Predicate() {

			@Override
			public boolean evaluate(Object object) {
				ClientEntity client = (ClientEntity) object;
				return client.contains(filterText);
			}
		});
		
		inactiveClientTable.getItems().clear();
		inactiveClientTable.getItems().addAll(inactiveClients);
		
	}
	
	/**
	 * 
	 */
	private void addMainActions(){
		FXMLManager manager = new FXMLManager(FXMLConstants.CLIENT_CHOOSER);
		Stage stage = manager.getStage("Add Members");

		btnAddMember.setOnAction((e) -> {

			ClientChooserController controller = manager.getController(ClientChooserController.class);
			controller.init(selectedSession);
			stage.show();
		});
		
		btnDeleteSessionMembers.setOnAction((e)->{
			int index = clientsTabPane.getSelectionModel().getSelectedIndex();
			
			if(index==0){
				List<ClientEntity> clients = activeClientTable.getSelectionModel().getSelectedItems();
				
				if(clients == null || clients.isEmpty()){
					Dialogs.showPopOver("Cant Delete", new Label("     Select members to delete      "), btnDeleteSessionMembers);
					return;
				}
				
				for(ClientEntity client : clients){
					client.setSession(0);
					Platform.runLater(() -> {
						ClientDataModel.instance.update(client);
					});
				}

			}
			
			
			if(index==1){
				List<ClientEntity> clients = inactiveClientTable.getSelectionModel().getSelectedItems();

				if(clients == null || clients.isEmpty()){
					Dialogs.showPopOver("Cant Delete", new Label("     Select members to delete      "), btnDeleteSessionMembers);
					return;
				}

				for (ClientEntity client : clients) {
					client.setSession(0);
					Platform.runLater(()->{
						ClientDataModel.instance.update(client);
					});
				}	
			}
			
			SessionDataModel.instance.fireDataModelChanged();
			
		});
		
		
		btnDeleteSession.setOnAction((e)->{
			
			Alert alert = Dialogs.getConfirmDialog("Delete Session", "", "Can't be undone ! continue ?");
			Optional<ButtonType> result = alert.showAndWait();

			if (result.get() == ButtonType.OK) {
				SessionDataModel.instance.delete(selectedSession);
				((Stage)btnDeleteSession.getScene().getWindow()).close();
			}

		});
		
		btnSessionMiss.setOnAction((e) -> {

			List<ClientEntity> clients = activeClientTable.getSelectionModel().getSelectedItems();

			if (clients == null || clients.isEmpty()) {
				Dialogs.showPopOver("Error", new Label("     Select members to add session misses      "),
						btnSessionMiss);
				
				clientsTabPane.getSelectionModel().select(0);
				
				return;
			}
			
			Alert alert = Dialogs.getConfirmDialog("Adding Session Misses", "", clients.size() +" Member(s) selected, Add session misses?");
			Optional<ButtonType> result = alert.showAndWait();

			if (result.get() == ButtonType.OK) {
				for (ClientEntity client : clients) {
					Platform.runLater(() -> {
						SessionMissDataModel.instance.add(new SessionMissEntity(0, 
								client.getId(), java.sql.Date.valueOf(LocalDate.now()),
								"", true, client.getSportId()));
					});
				}

				Dialogs.getNotificationTray("Success", "Session Misses Added", NotificationType.SUCCESS).showAndDismiss(Duration.seconds(4));
				
//				clientsTabPane.getSelectionModel().select(1);
				SessionDataModel.instance.fireDataModelChanged();
			}

		});
		
		
		btnDeactivateMembers.setOnAction((e) -> {

			List<ClientEntity> clients = activeClientTable.getSelectionModel().getSelectedItems();

			if (clients == null || clients.isEmpty()) {
				Dialogs.showPopOver("Cant Deactivate", new Label("     Select members to deactivate      "),
						btnDeactivateMembers);
				
				clientsTabPane.getSelectionModel().select(0);
				
				return;
			}

			for (ClientEntity client : clients) {
				client.setActive(false);
				Platform.runLater(() -> {
					ClientDataModel.instance.update(client);
				});
			}

			clientsTabPane.getSelectionModel().select(1);
			SessionDataModel.instance.fireDataModelChanged();

		});
		
		btnActivateMembers.setOnAction((e) -> {

			List<ClientEntity> clients = inactiveClientTable.getSelectionModel().getSelectedItems();

			if (clients == null || clients.isEmpty()) {
				Dialogs.showPopOver("Cant Activate", new Label("     Select members to activate      "),
						btnActivateMembers);
				
				clientsTabPane.getSelectionModel().select(1);
				
				return;
			}

			for (ClientEntity client : clients) {
				client.setActive(true);
				Platform.runLater(() -> {
					ClientDataModel.instance.update(client);
				});
			}

			clientsTabPane.getSelectionModel().select(0);
			SessionDataModel.instance.fireDataModelChanged();

		});
		
		
		btnExportSheet.setOnAction((e)->{
			if(clientsTabPane.getSelectionModel().getSelectedIndex()==0){
				activeClientTable.exportToXLS();
			}
			
			if(clientsTabPane.getSelectionModel().getSelectedIndex()==1){
				inactiveClientTable.exportToXLS();
			}
		});
	}
	
	
	/* (non-Javadoc)
	 * @see javafx.fxml.Initializable#initialize(java.net.URL, java.util.ResourceBundle)
	 */
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		// TODO Auto-generated method stub
		
	}

}
