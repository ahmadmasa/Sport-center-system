package sport.center.terminal.forms.sport;

import java.net.URL;
import java.util.ResourceBundle;
import javax.annotation.PostConstruct;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import sport.center.terminal.constants.Dialogs;
import sport.center.terminal.data.model.ClientDataModel;
import sport.center.terminal.data.model.SessionDataModel;
import sport.center.terminal.data.model.SessionMissDataModel;
import sport.center.terminal.data.model.SportDataModel;
import sport.center.terminal.data.model.SportEventClientDataModel;
import sport.center.terminal.data.model.SportEventDataModel;
import sport.center.terminal.fxml.manager.FXMLConstants;
import sport.center.terminal.fxml.manager.FXMLManager;
import sport.center.terminal.jpa.entities.SportEntity;
import sport.center.terminal.tables.SportTable;

/**
 * FXML Controller class
 *
 * @author Asendar
 */
public class SportManagerController implements Initializable {

	/**
	 * 
	 */
	@FXML
	private BorderPane sportsPanel;

	/**
	 * 
	 */
	@FXML
	private Button btnAdd, btnActivate, btnDeactivate, btnDelete;
	
	/**
	 * 
	 */
	private final SportTable sportTable = new SportTable();
	
	
	/**
	 * 
	 */
	@PostConstruct
	public void init(){
		sportsPanel.setCenter(sportTable);
		addMainActions();
	}
	
	/**
	 * 
	 */
	private void addMainActions(){
		btnAdd.setOnAction((e) -> {

			FXMLManager manager = new FXMLManager(FXMLConstants.ADD_SPORT_FORM);
			manager.getController(AddSportFormController.class).init();
			
			Dialogs.showPopOver("Add Sport", manager.getRoot(), btnAdd);
			
			 fireSportEvents();

		});

		btnActivate.setOnAction((e) -> {
			SportEntity sport = sportTable.getSelectionModel().getSelectedItem();
			
			if(sport==null){
				Dialogs.showPopOver("Can't deactivate",new Label("      Select a sport to activate     "), btnActivate);
				return;
			}
			
			sport.setActive(true);
			SportDataModel.instance.update(sport);
			
			 fireSportEvents();
		});

		btnDeactivate.setOnAction((e) -> {

			SportEntity sport = sportTable.getSelectionModel().getSelectedItem();

			if (sport == null) {
				Dialogs.showPopOver("Can't deactivate", new Label("      Select a sport to deactivate     "),
						btnDeactivate);
				return;
			}

			sport.setActive(false);
			SportDataModel.instance.update(sport);
			
			 fireSportEvents();

		});

		btnDelete.setOnAction((e) -> {

		});
		btnDelete.setVisible(false);
	}
	
	/**
	 * 
	 */
	private void fireSportEvents(){
		SessionDataModel.instance.fireDataModelChanged();
		SessionMissDataModel.instance.fireDataModelChanged();
		ClientDataModel.instance.fireDataModelChanged();
		SportEventDataModel.instance.fireDataModelChanged();
		SportEventClientDataModel.instance.fireDataModelChanged();
	}
	
	/* (non-Javadoc)
	 * @see javafx.fxml.Initializable#initialize(java.net.URL, java.util.ResourceBundle)
	 */
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		
	}

}
