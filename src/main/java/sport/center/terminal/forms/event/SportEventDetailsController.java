/**
 * 
 */
package sport.center.terminal.forms.event;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.net.URL;
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
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import sport.center.terminal.constants.Dialogs;
import sport.center.terminal.data.model.ClientDataModel;
import sport.center.terminal.data.model.SportEventClientDataModel;
import sport.center.terminal.data.model.SportEventDataModel;
import sport.center.terminal.forms.client.ClientChooserController;
import sport.center.terminal.fxml.manager.FXMLConstants;
import sport.center.terminal.fxml.manager.FXMLManager;
import sport.center.terminal.jpa.entities.ClientEntity;
import sport.center.terminal.jpa.entities.SportEventClientEntity;
import sport.center.terminal.jpa.entities.SportEventEntity;
import sport.center.terminal.support.Messages;
import sport.center.terminal.tables.ClientTable;

/**
 * @author Asendar
 *
 */
public class SportEventDetailsController implements Initializable{

	/**
	 * 
	 */
	@FXML
	private TextField txtFilter;

	/**
	 * 
	 */
	@FXML
	private BorderPane sportEventsPanel;

	/**
	 * 
	 */
	@FXML
	private Label lblClientsCount;

	/**
	 * 
	 */
	@FXML
	private Button btnAddMember, btnDeleteMembers, btnDeleteEvent, btnExportSheet;
	
	/**
	 * 
	 */
	private ClientTable clientTable = new ClientTable();
	
	/**
	 * 
	 */
	private SportEventEntity sportEvent;
	
	/**
	 * @param sportEvent
	 */
	@PostConstruct
	public void init(SportEventEntity sportEvent){
		this.sportEvent=sportEvent;
		
		
		sportEventsPanel.setCenter(clientTable);
		
		
		clientTable.fillTable(ClientDataModel.instance.getAllClients(sportEvent));
		clientTable.addSportEventChangeListener(new PropertyChangeListener() {
			
			@Override
			public void propertyChange(PropertyChangeEvent evt) {
				doFiltering();
			}
		});
		
		txtFilter.textProperty().addListener((observable, oldValue, newValue) -> {
			doFiltering();
			lblClientsCount.setText(clientTable.getItems().size() +" Members");
		});
		
		lblClientsCount.setText(clientTable.getItems().size() +" Members");
		
		
		initMainActions();
		
	}
	
	/**
	 * 
	 */
	private void initMainActions(){	
		btnAddMember.setOnAction((e)->{
			FXMLManager manager = new FXMLManager(FXMLConstants.CLIENT_CHOOSER);
			Stage stage = manager.getStage("Add member to event");
			ClientChooserController controller =  manager.getController(ClientChooserController.class);
			controller.init(this.sportEvent);
			
			stage.show();
		});
		
		btnDeleteMembers.setOnAction((e)->{
			List<ClientEntity> selectedClients = clientTable.getSelectionModel().getSelectedItems();
			
			if(selectedClients==null || selectedClients.isEmpty()){
				Dialogs.showPopOver("Error", new Label("        Select members from table to delete       "), btnDeleteMembers);
				return;
			}
			
			for (ClientEntity client : selectedClients) {
				Platform.runLater(()->{
					SportEventClientEntity sec = SportEventClientDataModel.instance.getSportEventClient(client, sportEvent);
					
					SportEventClientDataModel.instance.delete(sec);
				});
				
			}
			SportEventDataModel.instance.fireDataModelChanged();
			
		});
		
		btnDeleteEvent.setOnAction((e)->{
			
			Alert alert = Dialogs.getConfirmDialog(Messages.getString("MainTabController.DELETE"), "",
					Messages.getString("MainTabController.DELETE_BODY"));
			Optional<ButtonType> result = alert.showAndWait();
			
			if (result.get() == ButtonType.OK) {
				SportEventDataModel.instance.delete(sportEvent);
			}
			
			((Stage)btnDeleteEvent.getScene().getWindow()).close();
		});
		
		
		btnExportSheet.setOnAction((e)->{
			clientTable.exportToXLS();
		});
		
	}
	
	/**
	 * 
	 */
	private void doFiltering(){
		String filterText = txtFilter.getText().replace(" ", "");
		
		List<ClientEntity> clients = new ArrayList<>(ClientDataModel.instance.getAllClients(sportEvent));
		
		CollectionUtils.filter(clients, new Predicate() {
			
			@Override
			public boolean evaluate(Object object) {
				ClientEntity client = (ClientEntity) object;
				return client.contains(filterText);
			}
		});
		
		clientTable.getItems().clear();
		clientTable.getItems().addAll(clients);
	}
	
	
	/* (non-Javadoc)
	 * @see javafx.fxml.Initializable#initialize(java.net.URL, java.util.ResourceBundle)
	 */
	@Override
	public void initialize(URL location, ResourceBundle resources){}

}
