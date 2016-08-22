/**
 * 
 */
package sport.center.terminal.forms.client;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import javax.annotation.PostConstruct;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Predicate;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.util.Duration;
import sport.center.terminal.constants.Dialogs;
import sport.center.terminal.data.model.ClientDataModel;
import sport.center.terminal.data.model.SessionDataModel;
import sport.center.terminal.data.model.SportDataModel;
import sport.center.terminal.data.model.SportEventClientDataModel;
import sport.center.terminal.data.model.SportEventDataModel;
import sport.center.terminal.jpa.entities.ClientEntity;
import sport.center.terminal.jpa.entities.SessionEntity;
import sport.center.terminal.jpa.entities.SportEntity;
import sport.center.terminal.jpa.entities.SportEventClientEntity;
import sport.center.terminal.jpa.entities.SportEventEntity;
import sport.center.terminal.tables.ClientTable;
import tray.notification.NotificationType;

/**
 * @author Asendar
 *
 */
public class ClientChooserController implements Initializable{

	/**
	 * 
	 */
	@FXML
	private TextField txtFilter;

	/**
	 * 
	 */
	@FXML
	private BorderPane clientsPanel;

	/**
	 * 
	 */
	@FXML
	private Label lblClientsCount;

	/**
	 * 
	 */
	@FXML
	private Button btnAdd;
	
	/**
	 * 
	 */
	private final ClientTable clientTable = new ClientTable();

	
	/**
	 * @param session
	 */
	@PostConstruct
	public void init(SessionEntity session){
		clientsPanel.setCenter(clientTable);
		
		SportEntity sport = SportDataModel.instance.getSport(session.getSportId());
		
		List<ClientEntity> allClients = ClientDataModel.instance.getActiveClients(sport);
		
		clientTable.fillTable(ClientDataModel.instance.getActiveClients(sport));
		clientTable.addChangeListener(new PropertyChangeListener() {
			
			@Override
			public void propertyChange(PropertyChangeEvent evt) {
				doFiltering(allClients);
			}
		});
		
		txtFilter.textProperty().addListener((observable, oldValue, newValue) -> {
			doFiltering(allClients);
			lblClientsCount.setText(clientTable.getItems().size() +" Members");
		});
		
		lblClientsCount.setText(clientTable.getItems().size() +" Members");
		
		btnAdd.setOnAction((e)->{
			if(clientTable.getSelectionModel().getSelectedItems().isEmpty())
				return;
			
			int current = SessionDataModel.instance.getSessionCurrentNumber(session)
					+clientTable.getSelectionModel().getSelectedItems().size();
			int max = session.getSessionMaxCapacity();
			
			if(max<current){
				Dialogs.getNotificationTray("Cant Add More",
						"Session "+session.getName()+" is full !", NotificationType.ERROR)
						.showAndDismiss(Duration.seconds(8));
				
				return;
			}
			
			
			for(ClientEntity client : clientTable.getSelectionModel().getSelectedItems()){
				client.setSession(session.getId());
				
				Platform.runLater(() -> {
					ClientDataModel.instance.update(client);
				});
			}
			
			SessionDataModel.instance.fireDataModelChanged();
		});
		
		
	}
	
	/**
	 * @param SportEvent
	 */
	@PostConstruct
	public void init(SportEventEntity SportEvent){
		clientsPanel.setCenter(clientTable);
		
		
		List<ClientEntity> allClients = new ArrayList<>();

		if (SportEvent.getSportId() != -1){
			SportEntity sport = SportDataModel.instance.getSport(SportEvent.getSportId());
			allClients.addAll(ClientDataModel.instance.getActiveClients(sport));
			clientTable.fillTable(allClients);
		}else{
			allClients.addAll(ClientDataModel.instance.getActiveClients());
			clientTable.fillTable(allClients);
		}
		
		
		
		clientTable.addSportEventChangeListener(new PropertyChangeListener() {
			
			@Override
			public void propertyChange(PropertyChangeEvent evt) {
				doFiltering(allClients);
			}
		});
		
		txtFilter.textProperty().addListener((observable, oldValue, newValue) -> {
			doFiltering(allClients);
			lblClientsCount.setText(clientTable.getItems().size() +" Members");
		});
		
		lblClientsCount.setText(clientTable.getItems().size() +" Members");
		
		btnAdd.setOnAction((e)->{
			if(clientTable.getSelectionModel().getSelectedItems().isEmpty())
				return;
			
			int current = SportEventDataModel.instance.getSportEventCurrentNumber(SportEvent)
					+clientTable.getSelectionModel().getSelectedItems().size();
			int max = SportEvent.getEventMaxCapacity();
			
			if(max<current){
				Dialogs.getNotificationTray("Cant Add More",
						"Event "+SportEvent.getName()+" is full !", NotificationType.ERROR)
						.showAndDismiss(Duration.seconds(8));
				
				return;
			}
			
			
			for(ClientEntity client : clientTable.getSelectionModel().getSelectedItems()){
				Platform.runLater(()->{
					SportEventClientDataModel.instance.add(new SportEventClientEntity(0, SportEvent.getId(), client.getId(),
							""));
				});
			}
			SportEventClientDataModel.instance.fireDataModelChanged();
			SportEventDataModel.instance.fireDataModelChanged();
			
			
		});
		
		
	}
	
	/**
	 * @param allClients
	 */
	private void doFiltering(List<ClientEntity> allClients ){
		String filterText = txtFilter.getText().replace(" ", "");
		
		List<ClientEntity> filteredClients = new ArrayList<>(allClients);
		
		
		CollectionUtils.filter(filteredClients, new Predicate() {
			
			@Override
			public boolean evaluate(Object object) {
				ClientEntity client = (ClientEntity) object;
				return client.contains(filterText);
			}
		});
		
		clientTable.getItems().clear();
		clientTable.getItems().addAll(filteredClients);
	}
	
	
	/* (non-Javadoc)
	 * @see javafx.fxml.Initializable#initialize(java.net.URL, java.util.ResourceBundle)
	 */
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		// TODO Auto-generated method stub
		
	}

}
